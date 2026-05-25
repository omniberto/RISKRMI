package game_assets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class player implements Serializable{
    private static Scanner player_Val = new Scanner(System.in);
    private static Random rand = new Random();
    private static int ID = 0;

    private ArrayList <territory> territories = new ArrayList<territory>();
    private String player_name;
    private int player_id;
    private deck cards;
    private int troops;
    private int continent_bonus = 0;
    
    public player(String name, ArrayList<territory> territories, deck hand){
        this.player_id = ID++;
        this.player_name = name;
        this.territories.addAll(territories);
        for(territory Territory: territories){
            if(Territory.player() == null || !Territory.player().equals(this)) Territory.change_player(this);
        }
        this.troops = 40;
        this.initTerritories();
        this.randomAssignTroops();
        this.cards = hand;
    }

    public String name(){
        return this.player_name;
    }

    public int troops(){
        return this.troops;
    }

    public void increaseContinentBonus(){
        this.continent_bonus++;
    }

    public void setContinentBonus(int amount){
        this.continent_bonus = amount;
    }

    public int continentBonus(){
        return this.continent_bonus;
    }

    public void setName(String new_name){
        this.player_name = new_name;
    }

    public ArrayList <territory> territories(){
        return this.territories;
    }
    
    public int id(){
        return this.player_id;
    }

    public deck hand(){
        return this.cards;
    }

    public int increment_troops(int amount){
        this.troops += amount;
        return this.troops;
    }

    public int decrement_troops(int amount){
        if(amount > this.troops) return 0;
        this.troops -= amount;
        return amount;
    }

    public int increment_troops(){
        this.troops += this.territories.size() > 9? this.territories.size()/3: 3;
        return this.troops;
    }

    public void addTerritory(territory Territory){
        this.territories.add(Territory);
        if(!Territory.player().equals(this)) Territory.change_player(this);
    }
    
    public boolean increment_territory(territory Territory){
        if (this.territories.contains(Territory)){Territory.increment_player_troops(2);}
        return this.territories.contains(Territory);
    }

    public boolean increment_territory(territory Territory, int amount){
        if (this.territories.contains(Territory)){Territory.increment_player_troops(amount);}
        return this.territories.contains(Territory);
    }

    public void seeHand(){
        System.out.println(this.cards.show_deck());
    }

    public boolean setDown(int... indexes){
        Arrays.sort(indexes);
        if (this.cards.validate(indexes[0], indexes[1], indexes[2])){
            this.increment_territory(this.cards.getCard(indexes[2]).getTerritory());
            this.increment_territory(this.cards.getCard(indexes[1]).getTerritory());
            this.increment_territory(this.cards.getCard(indexes[0]).getTerritory());
            return true;
        }
        return false;
    }

    public String showMyTerritories(){
        String root = "Seus Territórios:\n";
        int i = 0;
        for(territory Territory: this.territories){
            root += "| "+ i + " - " + Territory.name() + "\t" + 
            (Territory.name().length() < 10? "\t": "") + 
            (Territory.name().length() < 17? "\t": "") + 
            "| Tropas: " + Territory.player_troops() + 
            " | Vizinhos: " + Territory.neighbors() + " |\n";
            i++;
        }
        return root;
    }

    public String changeTroop(territory Territory, army new_army){
        int cost;
        if(new_army.value() == 10){
            player_Val.reset();
            System.out.println("Como deseja dividir as peças:\n1 - 1 Cavalaria + 5 Infantaria\n2 - 2 Cavalarias");
            cost = player_Val.nextInt();
        }
        else cost = 2;
        if (this.territories.contains(Territory)){
            if(Territory.upgrade_troop(new_army, cost)) return "Valor é menor que a quantidade de tropas no território.";
        }
        return Territory.list_troops();
    }

    public boolean validate_attack(territory Origin, territory Destiny, int amount){
        return (Origin.player().id() == this.player_id) && 
        (Origin.getAttackNeighbors().contains(Destiny)) && 
        (Destiny.player().id() != this.player_id) && 
        (Origin.player_troops() >= 2) && 
        (Origin.player_troops() > amount) && 
        (amount >= 1 && amount <= 3);
    }

    public void initTerritories(){
        for(territory T: this.territories){
            T.increment_player_troops(1);
            this.troops--;
        }
    }
    public void randomAssignTroops(){
        while (this.troops > 0) {
            while(this.troops > this.territories.size()){
                for(territory T: this.territories){
                    int amount = rand.nextInt((this.troops/3));
                    T.increment_player_troops(amount);
                    this.decrement_troops(amount);
                }
            for(territory T: this.territories){
                T.increment_player_troops(1);
                this.decrement_troops(1);
            }
    }
    }
    }
}
