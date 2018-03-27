public class Card {

  public enum CardType  { None, Reverse, Skip, DrawTwo, Wild, WildDrawFour }
  public enum CardColor { None, Yellow, Red, Green, Blue }

  private CardType  m_type;
  private CardColor m_color;
  private int       m_number;

  // num cards
  public Card(CardType type, CardColor color, int number) {

    m_type = type;
    m_color = color;
    m_number = number;
  }

  // action color cards
  public Card(CardType type, CardColor color) {

    m_type = type;
    m_color = color;
  }

  // action cards
  public Card(CardType type) {

    m_type = type;
  }
  
  public CardType GetCardType() {
  
  	return m_type;
  }
  
  public boolean CompareCardColor(Card other) {
  
  	return m_color == other.m_color;
  }
  
  public boolean CompareCardNumber(Card other) {
  
		return m_number == other.m_number;
  }
}
