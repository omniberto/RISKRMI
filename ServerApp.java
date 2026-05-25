import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerApp {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Risk", new ServerImpl());
            System.out.println("Servidor RMI pronto.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
