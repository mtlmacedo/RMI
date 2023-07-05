package client;

import java.rmi.*;
import java.rmi.registry.*;
import java.util.Scanner;

import interfaces.IReceiver;


public class Client {
    private IReceiver server;
    private Registry registry;
    private String serverAddress;
    private int serverPort;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void RmiConect() {
        try {
            this.registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            this.server = (IReceiver) (registry.lookup("server"));
        } catch (NumberFormatException | RemoteException | NotBoundException e) {
            e.printStackTrace();
            System.out.println("Erro ao conectar o client");
        }
    }

    private void RmiSendMessage(String sql) {
        try {
            int status = this.server.receiveMessage(sql);
            if (status == 200) {
                System.out.println("Comando executado");
            } else if (status == 2627) {
                System.out.println("Comando invalido");
            } else if (status == 400) {
                System.out.println("Trocando de server");

                int newPort = this.server.getBiggerGroup();

                this.serverPort = newPort;
                this.RmiConect();
                this.RmiSendMessage(sql);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void RmiSendMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Informe o comando SQL: ");
        String sql = scanner.nextLine();
        scanner.close();

        try {
            int status = this.server.receiveMessage(sql);
            if (status == 200) {
                System.out.println("Comando executado");
            } else if (status == 2627) {
                System.out.println("Comando invalido");
            } else if (status == 400) {
                System.out.println("Trocando de server");

                int newPort = this.server.getBiggerGroup();

                this.serverPort = newPort;
                this.RmiConect();
                this.RmiSendMessage(sql);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}