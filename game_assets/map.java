package game_assets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;
import java.util.List;

public class map implements Serializable{
    Random rand = new Random();
    private ArrayList<territory> territories =  new ArrayList<territory>();
    private int size;

    public map(territory... territories){
        for(territory Territory: territories){
            this.territories.add(Territory);
        }
        this.size = this.territories.size();
    }

    public map(ArrayList<territory> territories){
        this.territories.addAll(territories);
        this.size = this.territories.size();
    }

    public ArrayList<territory> getRandomTerritories(int amount){
        ArrayList <territory> limit = new ArrayList<territory>();
        for(int i = 0; i < amount; i++){
            territory provisory = this.getRandomTerritory();
            while (limit.contains(provisory)) {
                provisory = this.getRandomTerritory();
            }
            limit.add(provisory);
        }
        return limit;
    }

    public boolean contains(territory Territory){
        return this.territories.contains(Territory);
    }

    public void addTerritory(territory newTerritory){
        this.territories.add(newTerritory);
        this.size++;
    }

    public void addTerritories(territory... territories){
        for(territory Territory:territories){
            this.addTerritory(Territory);
        }
    }

    public territory getRandomTerritory(){
        return this.territories.get(rand.nextInt(size));
    }

    public int size(){
        return this.size;
    }

    public ArrayList<territory> territories(){
        return this.territories;
    }

    public String showTerritories(){
        String root = "Territórios do Mapa:\n";
        int i = 0;
        for(territory Territory: this.territories){
            root += "| "+ i + " - " + Territory.name() + "\t" + 
            (Territory.name().length() < 10? "\t": "") + 
            (Territory.name().length() < 17? "\t": "") + 
            "| Tropas: " + Territory.player_troops() + 
            "\t| Jogador " + Territory.player().id() + 
            " | Vizinhos: " + Territory.neighbors() + " |\n";
            i++;
        }
        return root;
    }

    public String showMyTerritories(player Player){
        String root = "Seus Territórios:\n";
        int i = 0;
        for(territory Territory: this.territories){
            if(Territory.player().equals(Player))
                root += "| "+ i + " - " + Territory.name() + "\t" + 
                (Territory.name().length() < 10? "\t": "") + 
                (Territory.name().length() < 17? "\t": "") + 
                "| Tropas: " + Territory.player_troops() + 
                " | Vizinhos: " + Territory.neighbors() + " |\n";
            i++;
        }
        return root;
    }

    public String showEnemyTerritories(player Player){
        String root = "Territórios do Mapa:\n";
        int i = 0;
        for(territory Territory: this.territories){
            if(!Territory.player().equals(Player))
                root += "| "+ i + " - " + Territory.name() + "\t" + 
                (Territory.name().length() < 10? "\t": "") + 
                (Territory.name().length() < 17? "\t": "") + 
                "| Tropas: " + Territory.player_troops() + 
                " | Vizinhos: " + Territory.neighbors() + " |\n";
            i++;
        }
        return root;
    }

    public void showTerritoriesN(){
        for(territory Territory: this.territories){
            System.out.println(Territory.name() + " - " + Territory.neighbors());
        }
    }

    public ArrayList<ArrayList<territory>> split(){
        ArrayList<ArrayList<territory>> splited = new ArrayList<ArrayList<territory>>();
        splited.add(new ArrayList<territory>());
        splited.add(new ArrayList<territory>());
        splited.get(0).addAll(this.getRandomTerritories(size/2));
        for(territory Territory: this.territories){
            if(!splited.get(0).contains(Territory)){
                splited.get(1).add(Territory);
            }
        }
        return splited;
    }

    public ArrayList<ArrayList<territory>> split(int amount){
        ArrayList<ArrayList<territory>> splited = new ArrayList<ArrayList<territory>>();
        amount = amount > this.territories.size()/2? this.territories.size()/2: amount;
        for(int i = 0; i < amount; i++) {
            splited.add(new ArrayList<territory>());
        }
        Integer indexes[] = new Integer[this.territories.size()];
        for(int i = 0; i < this.territories.size(); i++) indexes[i] = i;
        List<Integer> shuffled = Arrays.asList(indexes);
        Collections.shuffle(shuffled);
        for(int i = 0, j = 0; j < shuffled.size(); i++, j++){
            i %= amount;
            splited.get(i).add(this.territories.get(j));
        }
        return splited;
    }
}