package game_assets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class deck implements Serializable{
    private static Random rand = new Random();
    private ArrayList<card> cards = new ArrayList<card>();
    private int size;


    public deck(){
        this.size = this.cards.size();
    }

    public deck(territory... territories){
            for(territory Territory : territories){
                this.cards.add(new card(Territory, new army()));
                this.cards.add(new card(Territory, new army(5)));
                this.cards.add(new card(Territory, new army(10)));
        }
        this.cards.add(new card());
        this.size = this.cards.size();
    }
    public deck(ArrayList <territory> territories, boolean territory_mode){
            for(territory Territory : territories){
                this.cards.add(new card(Territory, new army()));
                this.cards.add(new card(Territory, new army(5)));
                this.cards.add(new card(Territory, new army(10)));
        }
        this.cards.add(new card());
        this.size = this.cards.size();
    }

    public deck(card... cards){
        for(card Card:cards){
            this.cards.add(Card);
        }
        this.size = this.cards.size();
    }
    
    public deck(ArrayList <card> cards){
        this.cards.addAll(cards);
        this.size = this.cards.size();
    }

    public deck(deck cards, int hand_size){
        for (int i = 0; i < hand_size; i++){
                this.cards.add(cards.getCard());
        }
        this.size = this.cards.size();
    }

    public ArrayList <card> getCards(int amount){
            ArrayList <card> return_deck = new ArrayList<card>();
            for(int i = 0; i < amount%this.size; i++){
                return_deck.add(this.cards.remove(rand.nextInt(this.size)));
                this.size--;
            }
            return return_deck;
    }

    public card getCard(){
        this.size--;
        return this.cards.remove(rand.nextInt(this.cards.size()));
    }

    public card getCard(int index){
        this.size--;
        return this.cards.remove(index);
    }

    private card getCardP(int index){
        return this.cards.get(index);
    }

    public void addCard(card Card){
        this.cards.add(Card);
    }

    public void addCards(ArrayList<card> cards){
        this.cards.addAll(cards);
    }

    public void addCards(card... cards){
        for(card Card: cards){
            this.cards.add(Card);
        }
    }

    public void draw(deck main){
        this.addCard(main.getCard());
    }

    public void draw(deck main, int amount){
        this.addCards(main.getCards(amount%6));
    }

    public String show_deck(){
        String return_text = "Cartas: \n";
        int counter = 0;
        for(card Carta: this.cards){
            return_text += counter + " - " + Carta.show("") + "\n";
            counter++;
        }
        return return_text;
    }

    public boolean validate(int index_1, int index_2, int index_3){
        if(this.cards.size() < 3 || index_3 > this.cards.size()){return false;}
        card card_1 = this.getCardP(index_1);
        card card_2 = this.getCardP(index_2);
        card card_3 = this.getCardP(index_3);
        return (card_1.value() != card_2.value()) && (card_1.value() != card_3.value()) && (card_2.value() != card_3.value()) ||
        (card_1.value() == card_2.value() && card_2.value() == card_3.value() && card_3.value() > 0);   
    }

    public card drop(card cart){
        if (this.cards.remove(cart)){
            System.out.println(cart.show("\n"));
            return cart;
        }
        return null;
    }
}
