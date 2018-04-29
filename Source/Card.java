public class Card {

  public enum CardType  { Reverse, Skip, DrawTwo, Wild, WildDrawFour, None }
  public enum CardColor { Red, Green, Yellow, Blue, None }

  private CardType  m_type;
  private CardColor m_color;
  private int       m_number;

  // num cards
  public Card(CardType type, CardColor color, int number) {

    m_type = type;
    m_color = color;
    m_number = number;

    /*lines = [
      ['┌─────────┐'],
      ['│░░░░░░░░░│'],
      ['│░░░░░░░░░│'],
      ['│░░░░░░░░░│'],
      ['│░░░░░░░░░│'],
      ['│░░░░░░░░░│'],
      ['│░░░░░░░░░│'],
      ['│░░░░░░░░░│'],
      ['└─────────┘']
    ]*/
  }

  // action color cards
  public Card(CardType type, CardColor color) {

    m_type = type;
    m_color = color;
  }

  // action cards
  public Card(CardType type) {

    m_type = type;
    m_color = CardColor.None;
  }
  
  public CardType GetCardType() {
  
  	return m_type;
  }

  public CardColor GetCardColor() {

    return m_color;
  }

  public boolean CompareCardColor(Card other) {
  
  	return m_color == other.m_color;
  }

  public boolean CompareCardColor(CardColor color) {

    return m_color == color;
  }

  public boolean CompareCardNumber(Card other) {
  
		return m_number == other.m_number;
  }

  public boolean CompareCardType(Card other) {

    return m_type == other.m_type;
  }

  public void PrintCard() {

    if(m_type == CardType.None) {

      String n = ""+m_number;
      String c = m_color == CardColor.Blue ? "B" : m_color == CardColor.Yellow ? "Y" : m_color == CardColor.Red ? "R" : "G";

      String[] lines = {

              "┌─────────┐",
              "│"+n+" . . . .│",
              "│. . . . .│",
              "│. . . . .│",
              "│. . "+c+" . .│",
              "│. . . . .│",
              "│. . . . .│",
              "│. . . . "+n+"│",
              "└─────────┘"
      };

      for(String s : lines)
        System.out.println(s);

      System.out.println(ToString() + "\n");
    }
    else if(m_type == CardType.Reverse || m_type == CardType.Skip || m_type == CardType.DrawTwo) {

      String c = m_color == CardColor.Blue ? "B" : m_color == CardColor.Yellow ? "Y" : m_color == CardColor.Red ? "R" : "G";

      String[] lines = {};

      if(m_type == CardType.Reverse) {

        lines = new String [] {

                "┌─────────┐",
                "│R . . . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . "+c+" . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . . . R│",
                "└─────────┘"
        };
      }
      else if(m_type == CardType.Skip) {

        lines = new String [] {

                "┌─────────┐",
                "│S . . . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . "+c+" . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . . . S│",
                "└─────────┘"
        };
      }
      else {

        lines = new String [] {

                "┌─────────┐",
                "│+2. . . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . "+c+" . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . . .+2│",
                "└─────────┘"
        };
      }

      for(String s : lines)
        System.out.println(s);

      System.out.println(ToString() + "\n");
    }
    else {

      String[] lines = {};

      if(m_type == CardType.Wild) {

        lines = new String [] {

                "┌─────────┐",
                "│W . . . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . W . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . . . W│",
                "└─────────┘"
        };
      }
      else {

        lines = new String [] {

                "┌─────────┐",
                "│+4. . . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . W . .│",
                "│. . . . .│",
                "│. . . . .│",
                "│. . . .+4│",
                "└─────────┘"
        };
      }

      for(String s : lines)
        System.out.println(s);

      System.out.println(ToString() + "\n");
    }
  }

  public String ToString() {

    String s = "";

    if(m_type == CardType.None)
      s = m_number + " " + m_color;
    else if(m_type == CardType.Reverse || m_type == CardType.Skip || m_type == CardType.DrawTwo)
      s = m_type + " " + m_color;
    else
      s = m_type.toString();

    return s;
  }
}
