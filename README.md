# RISKRMI
## Instruções de Execução
### Compilando
No terminal, execute o seguinte comando para compilar todos os arquivos
 ```javac *.java```
### Rodando o Servidor
Para abrir o servidor, execute em um terminal, o seguinte comando:
```java ServerApp```
Este terminal servirá como servidor, e mostrará feedback das ações dos jogadores
### Rodando o Client
Para executar o client, abra outro terminal e execute:
``` java ClientApp```
### Sobre o jogo
- O jogo só inicia se dois clients estiverem conectados;
- O jogo pode ter até 2 jogadores ao mesmo tempo! Qualquer outro jogador que tente logar será barrado;
- O jogador que estiver ativo tem 90 segundos para escolher uma ação, após isso, será dado como desconectado;
- Se o outro jogador for desconectado, o jogo será encerrado após o jogador ativo executar alguma ação.
### Menu
1. Ver Mapa
    1. Ver mapa completo
    2. Ver seus territórios
    3. Ver territórios inimigos
    4. Ver Continentes
2. Ver suas tropas atuais e quantas vai ganhar no próximo turno
3. Ver sua mão de cartas
    1. Escolher baixar 3 cartas
4. Posicionar tropas
    1. Remover de um território.
    2. Colocar tropas em um território.
    3. Mover tropas entre territórios.
5. Atacar um território inimigo.
6. Passar seu turno.
### Vencendo o jogo
O jogo acaba quando algum jogador conquistou todos os territorios.