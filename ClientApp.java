import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Server server = (Server) registry.lookup("Risk");

            if(server.gameOn()) {
                ClientImpl callback = new ClientImpl();
                if(server.registerClient(callback)) {
                    Scanner scanner = new Scanner(System.in);
                    boolean updated_amount = false, updated_turn = false;
                    while(server.gameOn()) {
                        if(server.minP()) {
                            long active_stamp = System.currentTimeMillis();
                            long wait_stamp = System.currentTimeMillis();
                            while (server.minP() && server.gameOn()) {
                                if(server.getIndex(callback) == server.getTurn()) {
                                    System.out.println("Seu turno!\nNúmero de peças: " + server.troopsTotal(callback));
                                    System.out.println(callback.menu());
                                    System.out.print(">> ");
                                    int action = scanner.nextInt();
                                    active_stamp = System.currentTimeMillis();
                                    switch (action) {
                                        case 1:
                                            System.out.print("0 - Voltar;\n1 - Mapa Completo;\n2 - Seus territórios;\n3 - Territórios Inimigos\n4 - Continentes\n>> ");
                                            int visu = scanner.nextInt();
                                            while(visu > 4 || visu < 0){System.out.println("Opção Inválida, Escolha de novo\n>> "); visu = scanner.nextInt();}
                                            if(visu > 0) server.broadcast(visu, callback);
                                            break;
                                        case 2:
                                            server.broadcast(5, callback);
                                            break;
                                        case 3:
                                            server.broadcast(6, callback);
                                            System.out.print("Baixar mão?\n1 - Sim\t0 - Não\n>> ");
                                            int down = scanner.nextInt();
                                            while(down > 1 || down < 0){System.out.println("Opção Inválida, Escolha de novo\n>> "); down = scanner.nextInt();}
                                            if(down == 1){
                                                System.out.println("índice da carta 1\n>> ");
                                                int a = scanner.nextInt();
                                                System.out.println("índice da carta 2\n>> ");
                                                int b = scanner.nextInt();
                                                System.out.println("índice da carta 3\n>> ");
                                                int c = scanner.nextInt();
                                                server.broadcast(12, callback, a, b, c);
                                            }
                                            break;
                                        case 4:
                                            System.out.print("0 - Voltar;\n1 - Remover tropas de um território;\n2 - Colocar tropas;\n3 - Mover tropas entre 2 territórios.\n>> ");
                                            int pos = scanner.nextInt();
                                            while(pos > 3 || pos < 0){System.out.println("Opção Inválida, Escolha de novo\n>> "); pos = scanner.nextInt();}
                                            switch (pos) {
                                                case 1:
                                                    server.broadcast(2, callback);
                                                    System.out.print("Escolha um território para remover\n>> ");
                                                    int terr = scanner.nextInt();
                                                    System.out.print("Qual a quantidade? Max: " + server.troopsTerritory(callback, terr) + "\n>> ");
                                                    int amr = scanner.nextInt();
                                                    server.broadcast(7, terr, amr, callback);
                                                    break;
                                                case 2:
                                                    server.broadcast(2, callback);
                                                    System.out.print("Escolha um território para colocar\n>> ");
                                                    int tera = scanner.nextInt();
                                                    System.out.print("Qual a quantidade? Max: " + server.troopsTotal(callback) + "\n>> ");
                                                    int ama = scanner.nextInt();
                                                    server.broadcast(8, tera, ama, callback);
                                                    break;
                                                case 3:
                                                    server.broadcast(2, callback);
                                                    System.out.print("Escolha um território para remover\n>> ");
                                                    int terrm = scanner.nextInt();
                                                    System.out.print("Escolha um território para adicionar\n>> ");
                                                    int teram = scanner.nextInt();
                                                    System.out.print("Qual a quantidade? Max: " + server.troopsTerritory(callback, terrm) + "\n>> ");
                                                    int amm = scanner.nextInt();
                                                    server.broadcast(9, terrm, teram, amm, callback);
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        case 5:
                                            server.broadcast(2, callback);
                                            System.out.print("Escolha um território para iniciar o ataque\n>> ");
                                            int at = scanner.nextInt();
                                            server.broadcast(15, at, callback);
                                            System.out.print("Escolha um território para atacar\n>> ");
                                            int atd = scanner.nextInt();
                                            System.out.print("Qual a quantidade? Max: " + (server.troopsTerritory(callback, at) > 3? 3: server.troopsTerritory(callback, at)) + "\n>> ");
                                            int amma = scanner.nextInt();
                                            server.broadcast(10, at, atd, amma, callback);
                                            break;
                                        case 6:
                                            server.broadcast(13, callback);
                                            break;
                                        default:
                                            System.out.println("Ação Inválida!");
                                            break;
                                    }
                                    if (action == 6) updated_turn = false;
                                    if(!server.gameOn()) server.handleEndGame();
                                }
                                else {
                                    if (!updated_turn) {
                                        System.out.println("Turno de: " + server.turnTitle());
                                        updated_turn = true;
                                    }
                                    Thread.sleep(1000);
                                    wait_stamp = System.currentTimeMillis();
                                    System.out.println((wait_stamp - active_stamp)/1000);
                                    if((wait_stamp - active_stamp)/1000 > 30){
                                        server.handleDesconnection(callback);
                                        System.exit(-1);
                                    }
                                    
                                }
                            }
                            System.out.println("O Jogo será finalizado!");
                            return;
                        }
                        else if(!updated_amount){
                            System.out.println("Esperando outro jogador!");
                            updated_amount = true;
                        }
                    }
                    System.out.println("Jogo finalizado!");
                    scanner.close();
                    System.exit(0);
                }
                else {System.out.println("Erro ao conectar");System.exit(1);}    
            }
            else System.out.println("Não há jogos ativos no momento!");
            System.out.println("Saindo!!!");
            System.exit(2);
        } catch (Exception e) {return;}
    }
}
