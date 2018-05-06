import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Scanner;

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

    /*Player p = new Player(-1, "Elton Nanico");
    p.m_cards = new ArrayList<Card>();
    p.m_cards.add(new Card(Card.CardType.DrawTwo, Card.CardColor.Green));
    p.m_cards.add(new Card(Card.CardType.DrawTwo, Card.CardColor.Red));
    p.m_cards.add(new Card(Card.CardType.DrawTwo, Card.CardColor.Yellow));

    Gson gson = new Gson();
    System.out.println(gson.toJson(p.GetDeck()));
    System.out.println(gson.toJson(p.GetDeck().get(2)));*/
  }
}
