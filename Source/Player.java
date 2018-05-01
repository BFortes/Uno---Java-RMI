import java.util.ArrayList;

public class Player {

  private int m_gameId = -1;

  private String m_name;
  private int m_id;

  public ArrayList<Card> m_cards;

  public Player(int gameId, int id, String name) {
  
  	m_gameId = gameId;
  
    m_id   = id;
	  m_name = name;
  }

  public Player() {}

  public Player(int id, String name) {
  
	  m_id   = id;
	  m_name = name;
	}
  
  public Player(int id, String name, ArrayList<Card> deck) {

  	m_id    = id;
    m_name  = name;
    m_cards = deck;
  }

  public int GetGameId() { return m_gameId; }
  
  public int GetId() { return m_id; }
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

  public int GetTotalPoints() {

    int sum = 0;
    for(int i = 0; i < m_cards.size(); i++)
      sum += m_cards.get(i).GetPoints();

    return sum;
  }

  public Card SelectCard(int index) {

    Card card = m_cards.get(index);

    m_cards.remove(index);

    return card;
  }

  public String DoPlay(UnoLogic game) { return ""; }
  public String ChoiceColor()    { return ""; }
}
