import java.rmi.RemoteException;
import java.util.Scanner;

import client.Client;
import server.Server;

public class App {
    public static void main(String[] args) {
        try {
            Server server1 = new Server(3000, 0, "1");
            server1.addNewMember(new Server(3001, 1, "2"));
            server1.addNewMember(new Server(3002, 2, "3"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Client client1 = new Client("localhost", 3232);
        client1.RmiConect();

        int condicion = -1;
        while (condicion != 0) {
            System.out.println("----------- BEM VINDO AO SISTEMA -----------");
            System.out.println("1- Executar comando SQL");
            System.out.println("0- Sair");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Informe a opção: ");
            condicion = scanner.nextInt();

            if (condicion == 1) {
                client1.RmiSendMessage();
            } else {
            	System.out.println("SISTEMA ENCERRADO!!");
            	break;            	
            }
            scanner.close();
        }
    }
} 
