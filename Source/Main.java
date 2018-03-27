public class Main {

  public static void main(String[] args) {

  	Player[] players = { new Player("Player 1"), new Player("Player 2") };
  
    Uno game = new Uno(players);
    while(game.IsRunning()) {

    	game.UpdateGameState();
    }
  }
}
