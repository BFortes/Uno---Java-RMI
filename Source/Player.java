import java.util.ArrayList;

public class Player {

  private String m_name;
  private int m_id;

  private ArrayList<Card> m_cards;

  public Player(int id, String name) {
  	
    m_id   = id;
	  m_name = name;
  }

  public Player() {}

  public Player(int id, String name, ArrayList<Card> deck) {

  	m_id    = id;
    m_name  = name;
    m_cards = deck;
  }

  public String GetName() {

    return m_name;
  }

  public ArrayList<Card> GetDeck() {

    return m_cards;
  }

  public void SetDeck(ArrayList<Card> deck) {
  	
  	m_cards = deck;
  }
  
  public void BuyCard(Card card) {

    m_cards.add(card);

    System.out.println(m_name + " bought a card: " + card.ToString());
  }

  public int GetTotalCards() {

    return m_cards.size();
  }

  public Card SelectCard(int index) {

    Card card = m_cards.get(index);

    m_cards.remove(index);

    return card;
  }

  public String DoPlay(Uno game) { return ""; }
}
