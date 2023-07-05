package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import server.Server;

public interface IReceiver extends Remote {
	int receiveMessage(String query) throws RemoteException;

	int getBiggerGroup() throws RemoteException;

	ArrayList<Server> getServers() throws RemoteException;

	// void commitChanges() throws RemoteException;

	// void rollbackChanges() throws RemoteException;
}