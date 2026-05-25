package game_assets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
// Tested and Working as Intended
public class army implements Serializable{
    private static ArrayList <String> types = new ArrayList<String>(Arrays.asList("Infantaria", "Cavalaria", "Artilharia"));
    private int value;
    private String type;

    public army(){
        this.value = 1;
        this.type = types.get(0);
    }
    public army(int value){
        this.value = value;
        this.type = types.get(value/5);
    }

    public int value(){
        return this.value;
    }

    public String type(){
        return this.type;
    }

    public void upgrade(){
        if(this.value == 1){
            this.value = 5;
            this.type = types.get(1);
        }
        else if (this.value == 5){
            this.value = 10;
            this.type = types.get(2);
        }
    }

    public void downgrade(){
        if(this.value == 5){
            this.value = 1;
            this.type = types.get(0);
        }
        else if (this.value == 10){
            this.value = 5;
            this.type = types.get(1);
        }
    }
    
}
