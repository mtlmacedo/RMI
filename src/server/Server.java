package server;

import java.rmi.*;
import java.rmi.registry.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.*;
import java.util.*;

import interfaces.IReceiver;

public class Server extends java.rmi.server.UnicastRemoteObject implements IReceiver {
    
	private static final long serialVersionUID = 1L;
	private int id, thisPort;
    private String thisAddress;
    Registry registry;
    private String database = "worker1";
    Conn conn = new Conn();
    ArrayList<Server> servers = new ArrayList<Server>();

    public int receiveMessage(String query) throws RemoteException {
        
        int status = this.sendCommand(query); 
        
        if (status != 200) {
            System.out.println("Erro maquina principal");
            System.out.println();
            return status;
        }
                
        for (Server server : servers) {
        
            status = server.sendCommand(query);
            if (status == 200)
                continue;
            else {
                System.out.printf("Erro na maquina: %d", server.id);
                System.out.println();
                servers.remove(server);
            }
        
            System.out.printf("Maquina: %d", server.id);
            System.out.println();
        }
        return 200;
    }

    private int sendCommand(String query) {
        try (Connection con = conn.Connect("jdbc:sqlserver://localhost:1433", "sa", "123456789", database)) {
            Statement stmt;
            try {
                //con.setAutoCommit(false);
                stmt = con.createStatement();
                stmt.execute(query);
                con.close();
                return 200;
            } catch (SQLException e) {
                System.out.printf("erro na maquina %d", this.id);
                System.out.println();
                return e.getErrorCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 400;
        }
    }

    public Server(Integer porta, int id, String dataBase) throws RemoteException {
        try {
            this.thisAddress = (InetAddress.getLocalHost()).toString();
            this.id = id;
            this.database = dataBase;
        } catch (Exception e) {
            throw new RemoteException("Não foi possível encontrar o endereço.");
        }
        this.thisPort = porta;
        System.out.println("Conectado address=" + this.thisAddress + "- port=" + this.thisPort);

        try {
            registry = LocateRegistry.createRegistry(thisPort);
            registry.rebind("rmiServer", this);
        } catch (RemoteException e) {
            throw e;
        }
    }

    public void addNewMember(Server server) {
        this.servers.add(server);
    }

    public ArrayList<Server> getServers() {
        return servers;
    }

    public int getId() {
        return id;
    }

    public int getThisPort() {
        return thisPort;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBiggerGroup() {
        Server server = null;

        for (int i = 0; i < servers.size(); i++) {
            if (server == null) {
                server = servers.get(i);
            } else if (servers.get(i).id > server.id) {
                server = servers.get(i);
            }
        }
        this.id = 0;
        return server.thisPort;
    }

}