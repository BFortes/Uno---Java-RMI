import java.util.ArrayList;

public class Player {

  private String m_name;

  private ArrayList<Card> m_cards;

  public Player(String name) {

	  m_name  = name;
  }
  
  public Player(String name, ArrayList<Card> deck) {

    m_name  = name;
    m_cards = deck;
  }

  public void SetDeck(ArrayList<Card> deck) {
  	
  	m_cards = deck;
  }
  
  public void BuyCard(Card card) {

    m_cards.add(card);
  }
}
