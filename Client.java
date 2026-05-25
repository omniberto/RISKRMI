import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void onMessageReceived(int action, String sender) throws RemoteException;
    void feedback(String message) throws RemoteException;
    String getName() throws RemoteException;
    String menu() throws RemoteException;
}
