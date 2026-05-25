package game_assets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
public class territory implements Serializable{
    private static int ID = 0;
    protected static army infantary = new army();
    protected static army cavalry = new army(5);
    protected static army artillery = new army(10);

    private ArrayList <territory> neighbors = new ArrayList<territory>();
    private player Player;
    private String name;
    private int id;
    private int player_troops;
    Map <army, Integer> troop_list = new HashMap<army, Integer>();

    public territory(String name, player Player, int player_troops, territory... neighbors) {
        this.name = name;
        this.Player = Player;
        if(!Player.territories().contains(this)) Player.increment_territory(this);
        this.player_troops = player_troops;
        this.neighbors.addAll(Arrays.asList(neighbors));
        for(territory neighbor: neighbors){
            neighbor.add_neighbor(this);
        }
        this.id = ID++;
        this.troop_list.put(artillery, 0);
        this.troop_list.put(cavalry, 0);
        this.troop_list.put(infantary, player_troops);
    }
    public territory(String name, player Player, int player_troops) {
        this.name = name;
        this.Player = Player;
        if(!Player.territories().contains(this)) Player.increment_territory(this);
        this.player_troops = player_troops;
        this.id = ID++;
        this.troop_list.put(artillery, 0);
        this.troop_list.put(cavalry, 0);
        this.troop_list.put(infantary, player_troops);
    }
    public territory (String name){
        this.name = name;
        this.Player = null;
        this.id = ID++;
        this.troop_list.put(artillery, 0);
        this.troop_list.put(cavalry, 0);
        this.troop_list.put(infantary, 0);
    }
    public territory (String name, territory... neighbors){
        this.name = name;
        this.Player = null;
        this.id = ID++;
        this.neighbors.addAll(Arrays.asList(neighbors));
        for(territory neighbor: neighbors){
            neighbor.add_neighbor(this);
        }
        this.troop_list.put(artillery, 0);
        this.troop_list.put(cavalry, 0);
        this.troop_list.put(infantary, 0);
    }

    // getters
    public String name() { return this.name; }
    public player player() { return this.Player;}
    public String neighbors() {
        String neighborList = "[";
        int counter = 0;
        for(territory neighbor: this.neighbors) {
            neighborList += counter + " - " + neighbor.name() +  ", ";
            counter++;
        }
        if (neighborList.length() > 1){
            String nova = neighborList.substring(0, neighborList.length() - 2) + "]";
            return nova;
        }
        neighborList += ']';
        return neighborList;
    }
    public String attackNeighbors(){
        String neighborList = "[";
        int counter = 0;
        for(territory neighbor: this.neighbors) {
            if(!neighbor.Player.equals(this.Player)){
                neighborList += counter + " - " + neighbor.name() +  ", ";
                counter++;
            }
        }
        if (neighborList.length() > 1){
            String nova = neighborList.substring(0, neighborList.length() - 2) + "]";
            return nova;
        }
        neighborList += ']';
        return neighborList;
    }
    public ArrayList<territory> getNeighbors() { return this.neighbors; }
    public ArrayList<territory> getAttackNeighbors() { 
        ArrayList<territory> nova = new ArrayList<territory>();
        for(territory neighbor: this.neighbors){
            if(!neighbor.player().equals(this.Player)){
                nova.add(neighbor);
            }
        }
        return nova; }
    public int id() { return this.id; }
    public int player_troops() { return this.player_troops; }
    public String list_troops(){
        String count = "Contagem de tropas: ";
        for (Map.Entry<army, Integer> mapElement: this.troop_list.entrySet()){
            army key = (army)mapElement.getKey();
            count += key.type() + ": " + mapElement.getValue() + " ";
        }
        return count;
    }
    public Map <army, Integer> getTroops(){
        return this.troop_list;
    }

    // setters
    public void change_player(player Player) { this.Player = Player; }
    public void change_player_and_troops(player Player, int new_player_troops) { 
        this.Player = Player;
        if(!Player.territories().contains(this)) Player.addTerritory(this);
        this.player_troops = new_player_troops;
    }
    public void change_player_troops(int new_player_troops_amount) { this.player_troops = new_player_troops_amount; }
    public void decrement_player_troops(int amount) { 
        int remove_troops = amount;
            while(remove_troops >= 10 && this.troop_list.get(artillery) >= 1){
                this.troop_list.replace(artillery, this.troop_list.get(artillery) - 10);
                remove_troops -= 10;
            }
            while(remove_troops >= 5 && this.troop_list.get(cavalry) >= 1){
                this.troop_list.replace(cavalry, this.troop_list.get(cavalry) - 5);
                remove_troops -= 5;
            }
            while(remove_troops > 0 && this.troop_list.get(infantary) >= 1){
                this.troop_list.replace(cavalry, this.troop_list.get(cavalry) - 5);
                remove_troops--;
            }
    
        this.player_troops -= amount; }
    public boolean upgrade_troop(army newArmy, int cost){
        if(this.player_troops >= newArmy.value()){
            if (cost == 1) {
                if (newArmy.value() == 5){
                    this.troop_list.replace(infantary, this.troop_list.get(infantary) - 5);
                    this.troop_list.replace(newArmy, this.troop_list.get(newArmy) + 1);
                }
                else if (player_troops >= 10){
                    this.troop_list.replace(cavalry, this.troop_list.get(cavalry) - 1);
                    this.troop_list.replace(infantary, this.troop_list.get(infantary) - 5);
                    this.troop_list.replace(newArmy, this.troop_list.get(newArmy) + 1);
                }
            }
            if (cost == 2) {
                this.troop_list.replace(cavalry, this.troop_list.get(cavalry) - 2);
                this.troop_list.replace(newArmy, this.troop_list.get(newArmy) + 1);
            }
        }
        return (this.player_troops >= newArmy.value());
    }
    public void increment_player_troops(int amount) {
        this.player_troops += amount;
        this.troop_list.replace(infantary, this.troop_list.get(infantary) + amount);
    }

    public void add_neighbor(territory neighbor) { 
        if(!neighbors.contains(neighbor)){
            this.neighbors.add(neighbor); 
            neighbor.add_neighbor(this);
        }
    }

    public void add_neighbors(territory... neighbors) { 
        for(territory neighbor: neighbors) {
            add_neighbor(neighbor);
        }
    }

    public void remove_neighbor(territory neighbor) { 
        if (this.neighbors.contains(neighbor)) 
            this.neighbors.remove(neighbor); 
    }

}
