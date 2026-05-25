import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    boolean registerClient(Client client) throws RemoteException;
    void broadcast(int action, Client sender) throws RemoteException;
    void broadcast(int action, int territory_id, Client sender) throws RemoteException;
    void broadcast(int action, int territory_id, int amount, Client sender) throws RemoteException;
    void broadcast(int action, int territory_id, int territory_id2, int amount, Client sender) throws RemoteException;
    void broadcast(int action, Client sender, int... indexes) throws RemoteException;
    void handleDesconnection(RemoteException e) throws RemoteException; 
    void handleDesconnection(Client callback) throws RemoteException; 
    void handleEndGame() throws RemoteException;
    boolean gameOn() throws RemoteException;
    String turnTitle() throws RemoteException;
    int getTurn() throws RemoteException;
    boolean minP() throws RemoteException;
    int getIndex(Client client) throws RemoteException;
    int troopsTotal(Client client) throws RemoteException;
    int troopsTerritory(Client client, int territory_id) throws RemoteException;
}
