import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import game_assets.game;
import game_assets.player;

public class ServerImpl extends UnicastRemoteObject implements Server {
    private List<Client> clients = new ArrayList<>();
    private game Game = new game();
    private String feedback[] =  new String[2];
    protected ServerImpl() throws RemoteException { super(); }

    @Override
    public synchronized boolean registerClient(Client client) throws RemoteException {
        if(clients.size() < 2) {
            clients.add(client);
            Game.changePlayerName(clients.indexOf(client), client.getName());
            System.out.println("Novo cliente registrado!");
            return true;
        }
        else {
            System.out.println("Número máximo de jogadores!");
            client.feedback("Número máximo de jogadores atingido!");
            return false;
        }
    }
    @Override
    public synchronized boolean gameOn() throws RemoteException{
        return Game.game_on();
    }
    @Override
    public synchronized void broadcast(int action, Client sender) throws RemoteException {
        System.out.println(sender.getName() + " solicitou " + action);
        try {
            if(clients.lastIndexOf(sender) == Game.get_turn()) {
                if(action == 13) {
                    feedback = Game.handle(clients.indexOf(sender), action);
                    for(Client client: clients) {
                        client.feedback(feedback[clients.lastIndexOf(client)]);
                    }
                    feedback = Game.handle((clients.indexOf(sender) + 1) % 2 , 0);
                }
                else{ feedback = Game.handle(clients.indexOf(sender), action); }
                for(Client client: clients) {
                    client.feedback(feedback[clients.lastIndexOf(client)]);
                }
            }
            else {
                sender.feedback("Não é sua Vez!");
            }
        } catch (RemoteException e) {
            handleDesconnection(e);
        }
    }
    @Override
    public synchronized void broadcast(int action, int territory_id, Client sender) throws RemoteException {
        System.out.println(sender.getName() + " solicitou " + action);
        try {
            if(clients.lastIndexOf(sender) == Game.get_turn()){
                feedback = Game.handle(clients.indexOf(sender), action, territory_id);
                for(Client client: clients){
                    client.feedback(feedback[clients.lastIndexOf(client)]);
                }
            }
            else {
                sender.feedback("Não é sua Vez!");
            }
        } catch (RemoteException e) {
               handleDesconnection(e);
        }
    }
    @Override
    public synchronized void broadcast(int action, int territory_id, int amount, Client sender) throws RemoteException {
        System.out.println(sender.getName() + " solicitou " + action);
        try {
            if(clients.lastIndexOf(sender) == Game.get_turn()){
                feedback = Game.handle(clients.indexOf(sender), action, territory_id, amount);
                for(Client client: clients){
                    client.feedback(feedback[clients.lastIndexOf(client)]);
                }
            }
            else {
                sender.feedback("Não é sua Vez!");
            }
        } catch (RemoteException e) {
               handleDesconnection(e);
        }
    }
    @Override
    public synchronized void broadcast(int action, int territory_id, int territory_id2, int amount, Client sender) throws RemoteException {
        System.out.println(sender.getName() + " solicitou " + action);
        try {
            if(clients.lastIndexOf(sender) == Game.get_turn()){
                feedback = Game.handle(clients.indexOf(sender), action, territory_id, territory_id2, amount);
                for(Client client: clients){
                    client.feedback(feedback[clients.lastIndexOf(client)]);
                }
            }
            else {
                sender.feedback("Não é sua Vez!");
            }
        } catch (RemoteException e) {
                handleDesconnection(e);
        }
    }
    @Override
    public synchronized void broadcast(int action, Client sender, int... indexes) throws RemoteException {
        System.out.println(sender.getName() + " solicitou " + action);
        try {
            if(clients.lastIndexOf(sender) == Game.get_turn()){
                feedback = Game.handDown(clients.indexOf(sender), indexes);
                for(Client client: clients){
                    client.feedback(feedback[clients.lastIndexOf(client)]);
                }
            }
            else {
                sender.feedback("Não é sua Vez!");
            }
        } catch (RemoteException e) {
                handleDesconnection(e);
        }
    }
    @Override
    public String turnTitle() throws RemoteException{
        return clients.get(getTurn()).getName();
    }
    @Override
    public int getTurn() throws RemoteException {
        return this.Game.get_turn();
    }
    @Override
    public int getIndex(Client client) throws RemoteException {
        return this.clients.lastIndexOf(client);
    }
    @Override
    public int troopsTotal(Client client) throws RemoteException {
        player P = this.Game.getPlayer(this.clients.lastIndexOf(client));
        return P.troops();
    }
    @Override
    public int troopsTerritory(Client client, int territory_id) throws RemoteException {
        return (Game.territoryTroops(this.clients.lastIndexOf(client), territory_id) - 1);
    }
    @Override
    public synchronized boolean minP() throws RemoteException {
        return this.clients.size() == 2;
    }
    @Override
    public synchronized void handleDesconnection(RemoteException e) throws RemoteException {
        e.printStackTrace();
        for(Client client: clients){
                client.feedback("Um jogador se desconectou.\nO jogo será encerrado");
        }
        Game.force_finish();
        handleEndGame();
    }
    @Override
    public synchronized void handleDesconnection(Client callback) throws RemoteException {
        for(Client client: clients) {
                client.feedback("Um jogador se desconectou.\nO jogo será encerrado");
        }
        Game.force_finish();
        handleEndGame();
    }
    @Override
    public synchronized void handleEndGame() throws RemoteException {
        this.Game = new game();
        clients = new ArrayList<Client>();
        System.out.println(clients.size());
    }
}
