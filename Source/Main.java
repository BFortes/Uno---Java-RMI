public class Main {

  public static void main(String[] args) {

  	Player[] players = { new Player("Elton Nanico"), new Player("Pedro Peludo") };
  
    Uno game = new Uno(players);
    while(game.IsRunning()) {

    	game.UpdateGameState();
    }

    System.out.println("Player " + game.GetWinner() + " Wins!");
  }
}
