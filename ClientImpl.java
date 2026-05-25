import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements Client {
    protected static Scanner scanner = new Scanner(System.in);

    private String name;

    protected ClientImpl() throws RemoteException { super(); 
        scanner.reset();
        System.out.println("Digite seu nome:");
        this.name = scanner.next();
    }

    @Override
    public void onMessageReceived(int action, String sender) throws RemoteException {
        System.out.print("\r" + sender + ": " + action + "\n" + this.name + ": ");
    }
    @Override
    public void feedback(String message){
        System.out.println(message);
    }
    @Override
    public String getName() throws RemoteException{
        return this.name;
    }
    @Override
    public String menu() throws RemoteException{
        String menu = "1 - Ver Mapa;\n2 - Ver quantidade de tropas;\n3 - Ver mão;\n4 - Posicionar tropas;\n5 - Atacar;\n6 - Passar o turno.";
        return menu;
    }

}
