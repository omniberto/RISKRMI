package game_assets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class game implements Serializable {

    private static Random dice = new Random();
    private boolean on = true;
    private int hands_downed = 0;
    private int turn = 0;
    private board Board = new board();
    private map Mapa = new map(Board.getTerritories());
    private ArrayList<player> players = new ArrayList<player>();
    private deck cards = new deck(this.Mapa.territories(), true);
    private ArrayList<ArrayList<territory>> Continents = this.Mapa.split(4);

    public game() {
        ArrayList<ArrayList<territory>> split = this.Mapa.split();
        this.players.add(new player(null, split.get(0), new deck(cards, 1)));
        this.players.add(new player(null, split.get(1), new deck(cards, 1)));
        this.players.get(0).setContinentBonus(CalculateContinentBonus(this.players.get(0)));
        this.players.get(1).setContinentBonus(CalculateContinentBonus(this.players.get(1)));
    }
    private int CalculateContinentBonus(player Player){
        int quantity = 0;
        for(ArrayList<territory> Continent: Continents){
            boolean complete = true;
            for(territory Territory: Continent){
                complete = complete && Player.territories().contains(Territory);
                if(!complete) break;
            }
            if(complete) quantity++;
        }
        return quantity;
    }
    public int get_turn() {
        return turn;
    }
    public int change_turn() {
        turn = (turn + 1) % 2;
        return turn;
    }
    public boolean game_on(){
        return this.on;
    }
    public void force_finish(){
        this.on = false;
    }
    public player getPlayer(int player_id) {
        return this.players.get(player_id);
    }
    public int hands() {
        return hands_downed;
    }
    public int territoryTroops(int player_id, int Territory_id){
        return players.get(player_id).territories().get(Territory_id).player_troops();
    }
    public String getContinents() {
        String Continent_List = "Lista de Continentes:\n";
        int counter = 0;
        for(ArrayList<territory> Continent: Continents){
            Continent_List += counter + " - [";
            for(territory Territory: Continent){
                Continent_List += Territory.name() + ", ";
            }
            if (Continent_List.length() > 1){
                Continent_List = Continent_List.substring(0, Continent_List.length() - 2) + "]\n";
            }
            counter++;
        }
        return Continent_List;
    }
    public String[] handle(int player_id, int action) {
        player Player = this.players.get(player_id);
        String [] feedback = new String[2];
        switch (action) {
            case 0:
                int amount = this.updatePlayerTroops(Player);
                feedback[player_id] = "Você recebeu " + amount + " tropas!\n";
                feedback[(player_id + 1) % 2] = "O outro jogador recebeu tropas!\n";
                break;
            case 1:
                feedback[player_id] = showMap();
                feedback[(player_id + 1) % 2] = "O outro jogador viu o mapa completo.\n";
                break;
            case 2:
                feedback[player_id] = Player.showMyTerritories();
                feedback[(player_id + 1) % 2] = "O outro jogador viu os territórios dele.\n";
                break;
            case 3:
                feedback[player_id] = this.getPlayer((player_id + 1) % 2).showMyTerritories();
                feedback[(player_id + 1) % 2] = "O outro jogador viu os seus territórios.\n";
                break;
            case 4:
                feedback[player_id] = getContinents();
                feedback[(player_id + 1) % 2] = "O outro jogador viu os continentes.\n";
                break;
            case 5:
                feedback[player_id] = "Você tem " + Player.troops() + " tropas (+ " + (Player.continentBonus()*2 + Player.territories().size()/3) + " por turno)";
                feedback[(player_id + 1) % 2] = "O outro jogador viu a quantidade de tropas dele.\n";
                break;
            case 6:
                feedback[player_id] = Player.hand().show_deck();
                feedback[(player_id + 1) % 2] = "O outro jogador está vendo a mão dele\n";
                break;
            case 13:
                this.change_turn();
                feedback[player_id] = "Você passou seu turno!\n";
                feedback[(player_id + 1) % 2] = "O outro jogador passou o turno\n";
                break;
            default:
                feedback[player_id] = "Chamada errada de função!";
                feedback[(player_id + 1) % 2] = "Chamada errada de função!\n";
                break;
        }
        return feedback;
    }
    public String[] handle(int player_id, int action, int territory_id) {
        player Player = this.players.get(player_id);
        territory Territory = Player.territories().get(territory_id);
        String [] feedback = new String[2];
        switch (action) {
            case 9:
                feedback[player_id] = Territory.neighbors();
                feedback[(player_id + 1) % 2] = "O outro jogador está se preparando pra atacar...\n";
            break;
            case 15:
                feedback[player_id] = Territory.attackNeighbors();
                feedback[(player_id + 1) % 2] = "O outro jogador está se preparando pra atacar...\n";
                break;
            default:
                feedback[player_id] = "Chamada errada de função!\n";
                feedback[(player_id + 1) % 2] = "Chamada errada de função!\n";
                break;
        }
        return feedback;
    }
    public String[] handle(int player_id, int action, int territory_id, int amount) {
        player Player = this.players.get(player_id);
        String [] feedback = new String[2];
        if(territory_id >= Player.territories().size()){
            feedback[player_id] = "Território Inválido\n";
            feedback[(player_id + 1)%2] = "O outro jogador tentou fazer uma jogada ilegal\n";
            return feedback;
        }
        territory Territory = Player.territories().get(territory_id);
        if(Player.territories().contains(Territory)){
            switch (action) {
                case 7:
                    int r_q = 0;
                    if(amount > 0 && amount < Territory.player_troops()) {
                        r_q = removeTroopsFromTerritory(Territory, amount);
                        if(r_q > 0) {
                            Player.increment_troops(r_q);
                            feedback[player_id] = "Você removeu " + r_q + " tropas de " + Territory.name() + ".\n";    
                            feedback[(player_id + 1) % 2] = "O outro jogador removeu " + r_q + " tropas de " + Territory.name() + ".\n";
                        }
                        else {
                            feedback[player_id] = "Você não tem tropas suficientes em " + Territory.name() + " para remover.\n";    
                            feedback[(player_id + 1) % 2] = "O outro jogador tentou remover tropas de " + Territory.name() + ".\n";
                        }
                    }
                    else {
                        feedback[player_id] = "Você não pode remover tropas de " + Territory.name() + ".\n";    
                        feedback[(player_id + 1) % 2] = "O outro jogador tentou remover tropas de " + Territory.name() + ".\n";
                    }
                    break;
                case 8:
                    if(amount <= Player.troops() && amount > 0) {
                            Player.decrement_troops(moveTroopsToTerritory(Territory, amount));
                            feedback[player_id] = "Você adicionou " + amount + " tropas à " + Territory.name() + ".\n";    
                            feedback[(player_id + 1) % 2] = "O outro jogador adicionou " + amount+ " tropas à " + Territory.name() + ".\n";
                    }
                    else {
                        feedback[player_id] = "Você não pode adicionar tropas à " + Territory.name() + ".\n";    
                        feedback[(player_id + 1) % 2] = "O outro jogador tentou adicionar tropas à " + Territory.name() + ".\n";
                    }
                    break;
                default:
                    feedback[player_id] = "Chamada errada de função!\n";
                    feedback[(player_id + 1) % 2] = "Chamada errada de função!\n";
                    break;
            }
        }
        else {
            feedback[player_id] = "Território não te pertence!\n";
            feedback[(player_id + 1) % 2] = "O outro jogador tentou acessar seu território!\n";
        }
        return feedback;
    }
    public String[] handle(int player_id, int action, int territory_id, int territory_2_id, int amount) {
        player Player = this.players.get(player_id);
        String [] feedback = new String[2];
        if(territory_id >= Player.territories().size()){
            feedback[player_id] = "Território de Origem Inválido\n";
            feedback[(player_id + 1) % 2] = "O outro jogador tentou fazer uma jogada ilegal\n";
            return feedback;
        }
        territory Territory = Player.territories().get(territory_id);
        switch (action) {
            case 9:
                if(territory_2_id >= Player.territories().size()){
                    feedback[player_id] = "Território de Destino Inválido\n";
                    feedback[(player_id + 1) % 2] = "O outro jogador tentou fazer uma jogada ilegal\n";
                    return feedback;
                }
                territory Territory_2 = Player.territories().get(territory_2_id);
                feedback[player_id] = changeTroops(Territory, Territory_2, amount);
                feedback[(player_id + 1) % 2] = "O outro jogador moveu tropas de " + Territory.name() + " para " + Territory_2.name();
                break;
            case 10:
                if(territory_2_id >= Territory.getAttackNeighbors().size()){
                    feedback[player_id] = "Território de Destino Inválido\n";
                    feedback[(player_id + 1) % 2] = "O outro jogador tentou fazer uma jogada ilegal\n";
                    return feedback;
                }
                territory Territory_A = Territory.getAttackNeighbors().get(territory_2_id);
                feedback = attack(Player, Territory, Territory_A, amount);
                break;
            default:
                feedback[player_id] = "Chamada errada de função!\n";
                feedback[(player_id + 1) % 2] = "Chamada errada de função!\n";
                break;
        }
        return feedback;
    }
    public int addPlayer(String name, ArrayList<territory> territories, deck hand) {
        this.players.add(new player(name, territories, hand));
        return this.players.size();
    }
    public void addCard(int player_id){
        this.players.get(player_id).hand().addCard(cards.getCard());
    }
    private String showMap(){
        return this.Mapa.showTerritories();
    }
    private int updatePlayerTroops(player Player) {
        int amount = Player.continentBonus()*2 + Player.territories().size()/3;
        Player.increment_troops(amount);
        return amount;
    }
    public String[] handDown(int player_id, int... indexes){
        player Player = this.players.get(player_id);
        String feedback[] = new String[2];
        if(indexes.length == 3 && Player.setDown(indexes)){
            this.hands_downed++;
            Player.increment_troops(hands_downed*3);
            feedback[player_id] = "Mão baixada!\n";
            feedback[(player_id + 1) % 2] =  "O outro jogador baixou uma mão!\n";
        }
        else {
            feedback[player_id] = "As cartas selecionadas são inválidas!\n";
            feedback[(player_id + 1) % 2] =  "O outro jogador tentou baixar uma mão!\n";
        }
        return feedback;
    } 
    private int removeTroopsFromTerritory(territory Territory, int amount){
        if(Territory.player_troops() <= amount){
            return 0;
        }
        Territory.decrement_player_troops(amount);
        return amount;
    }
    private int moveTroopsToTerritory(territory Territory, int amount){
        Territory.increment_player_troops(amount);
        return amount;
    }
    private String changeTroops(territory Origin, territory Destiny, int amount){
        return moveTroopsToTerritory(Destiny, removeTroopsFromTerritory(Origin, amount)) + " tropas foram movidas de " + Origin.name() + " para " + Destiny.name() + "!\n";
    }
    private String[] attack(player Player, territory Origin, territory toAttack, int amount){
        String feedback[] = {"", ""};
        if (Player.validate_attack(Origin, toAttack, amount)) {
            int attackChances = amount;
            int defendChances = 1 + (toAttack.player_troops() > 1 ? 1: 0);
            ArrayList <Integer> attdices = new ArrayList <Integer>();
            ArrayList <Integer> defdices = new ArrayList <Integer>();
            for (int i = 0; i < attackChances; i++) attdices.add(dice.nextInt(6) + 1);
            for (int i = 0; i < defendChances; i++) defdices.add(dice.nextInt(6) + 1); 
            Collections.sort(attdices, Collections.reverseOrder());
            Collections.sort(defdices, Collections.reverseOrder());
            for(int i = 0, j = 0; i < attackChances && j < defendChances; i++, j++) {
                if (attdices.get(i) <= defdices.get(j)) {
                    Origin.decrement_player_troops(1);
                    amount--;
                    feedback[Player.id()] += attdices.get(i) + "x" + defdices.get(j) + ": Você perdeu uma peça em " + Origin.name() + " atacando " + toAttack.name()+ "\n";
                    feedback[(Player.id() + 1) % 2] += "O outro jogador perdeu uma peça em " + Origin.name() + " tentando te atacar!\n";
                }
                else {
                    toAttack.decrement_player_troops(1);
                    feedback[Player.id()] += attdices.get(i) + "x" + defdices.get(j) + ": O jogador " + toAttack.player().id() + " perdeu uma peça em " + toAttack.name() + "\n";
                    feedback[(Player.id() + 1) % 2] += "Você perdeu uma peça em " + toAttack.name() + "!\n";
                }
            }
            if(verifyConquest(Player, toAttack, amount)) { 
                feedback[Player.id()] += "Você conquistou " + toAttack.name() + "!\n"; 
                feedback[(Player.id() + 1) % 2] += "O outro jogador conquistou " + toAttack.name() + "!\n"; 
                Player.hand().draw(cards);
            }
            Player.setContinentBonus(this.CalculateContinentBonus(Player));
            toAttack.player().setContinentBonus(this.CalculateContinentBonus(toAttack.player()));
            if(verifyWin(Player)) {
                feedback[Player.id()] += "Você ganhou o jogo!\n";
                feedback[(Player.id() + 1) % 2] += "O outro jogador ganhou o jogo!\n";
                on = false;
            }
            return feedback;
        }
        feedback[Player.id()] = "O ataque não é possivel!\n";
        feedback[(Player.id() + 1) % 2] += "O jogador " + Player.id() + " tentou te atacar!\n";
        return feedback;
    } 
    private boolean verifyConquest(player Player, territory toVerify, int amount){
        if(toVerify.player_troops() == 0){
            toVerify.change_player_and_troops(Player, amount);
            return true;
        }
        return false;
    }
    private boolean verifyWin(player Player){
        int validation = Player.id();
        for(territory Territory: this.Mapa.territories())
            if(!(Territory.player().id() == validation)) return false;
        return true;
    }   
    public void changePlayerName(int player_id, String name){
        this.players.get(player_id).setName(name);
    }
}