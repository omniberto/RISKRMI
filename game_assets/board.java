package game_assets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class board implements Serializable{
    private ArrayList<territory> territories = new ArrayList<territory>();

    public board(){
        territory t1 =  new territory("Nação do Fogo");
        territory t2 =  new territory("Terra Doce");
        territory t3 =  new territory("Reino Gelado");
        territory t4 =  new territory("A Grande Gosma");
        territory t5 =  new territory("Lago Lacrimosa", t3);
        territory t6 =  new territory("Muspelheim", t1);
        territory t7 =  new territory("Delmarva", t4);
        territory t8 =  new territory("Morioh", t7, t2);
        territory t9 =  new territory("Campos Elísios", t8, t5);
        territory t10 = new territory("A Torre", t9);
        territory t11 = new territory("Os Jardins Suspensos", t10, t9, t4);
        territory t12 = new territory("A Terra Chamuscada", t6, t1);
        territory t13 = new territory("A Ilha das Bençãos", t3, t5);
        territory t14 = new territory("O Ferro-Velho", t13, t10, t8, t7, t4);
        territory t15 = new territory("O Cemitério", t14, t8, t6, t5);
        territory t16 = new territory("Oculus Venti", t11, t10, t7);
        territory t17 = new territory("A Caverna dos Cristais", t13, t5, t2);
        territory t18 = new territory("Os Morros Flutuantes", t16, t14, t11);
        territory t19 = new territory("O Domo do Trovão", t1, t4, t6, t12);
        territory t20 = new territory("O Contra-Tempo", t15, t13, t10, t9, t8);
        this.territories.addAll(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    public void showTerritories(){
        for(territory Territory: this.territories){
            System.out.println(Territory.name() + " - " + Territory.neighbors());
        }
    }

    public ArrayList<territory> getTerritories(){
        return this.territories;
    }
}
