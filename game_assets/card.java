package game_assets;

import java.io.Serializable;

//Tested and Working as Intended
public class card implements Serializable{
    private territory Territory;
    private army Army;
    private boolean wild;

    public card(territory Territory, army Army){
        this.Army = Army;
        this.Territory = Territory;
        this.wild = false;
    }

    public card(){
        this.wild = true;
    }

    public boolean wild(){
        return this.wild;
    }

    public int value(){
        if(this.wild){
            return 0;
        }
        return this.Army.value();
    }
     
    public String territory(){
        if(this.wild){
            return this.show("\n");
        }
        return this.Territory.name();
    }

    public territory getTerritory(){
        if(this.wild){
            return null;
        }
        return this.Territory;
    }

    public String army(){
        if(this.wild){
            return this.show("\n");
        }
        return this.Army.type();
    }

    public army getArmy(){
        if(this.wild){
            return null;
        }
        return this.Army;
    }

    public String show(String end){
        if(this.wild){
            return "Carta coringa> " + new army().type() + ", " + new army(5).type() + ", " + new army(10).type() + end;
        }
        return "Carta comum> Território: " + this.Territory.name() + ", Tropa: " + this.Army.type() + end;
    }
}
