public class Main {

  public static void main(String[] args) {

  	//Player[] players = { new Player("Elton Nanico"), new PlayerRandom("Pedro Peludo") };
  
    /*Uno game = new Uno(players);
    while(game.IsRunning()) {

    	game.UpdateGameState();
    }

    System.out.println(game.GetWinner() + " Wins!");*/

    long lastTime = System.nanoTime();
    while(true) {

      long time = System.nanoTime();
      double delta_time = (double)(time - lastTime) / 1000000000.0;

      System.out.println(delta_time);
    }
  }
}
