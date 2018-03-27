import java.util.ArrayList;
import java.util.Collections;

public class Uno {

  static final public int TOTAL_CARDS        = 108;
  static final public int TOTAL_NUM_CARDS    = 76;
  static final public int TOTAL_ACTION_CARDS = 24;
  static final public int TOTAL_WILD_CARDS   = 8;
  static final public int START_CARDS        = 7;

  private ArrayList<Card> m_cardDeck;

  private Player[] m_players;

  boolean m_isRunning = false;

  public Uno(Player[] players) {

    InitDeck();

    m_players = players;
    for(int i = 0; i < players.length; i++)
      m_players[i] = new Player("Player " + (i+1), InitPlayerDeck());
  }

  void InitDeck() {

    m_cardDeck = new ArrayList<Card>();

    int num = 0;

    int loopNumCards    = TOTAL_NUM_CARDS/4;
    int loopActionCards = TOTAL_ACTION_CARDS/12;
    int loopWildCards   = TOTAL_WILD_CARDS/2;


    for(int i = 0; i < loopNumCards; i++) {

      m_cardDeck.add(new Card(Card.CardType.None, Card.CardColor.Blue, num));
      m_cardDeck.add(new Card(Card.CardType.None, Card.CardColor.Red, num));
      m_cardDeck.add(new Card(Card.CardType.None, Card.CardColor.Yellow, num));
      m_cardDeck.add(new Card(Card.CardType.None, Card.CardColor.Green, num));

      num = num == 9 ? 1 : num+1;
    }

    for(int i = 0; i < loopActionCards; i++) {

      m_cardDeck.add(new Card(Card.CardType.Reverse, Card.CardColor.Blue));
      m_cardDeck.add(new Card(Card.CardType.Reverse, Card.CardColor.Red));
      m_cardDeck.add(new Card(Card.CardType.Reverse, Card.CardColor.Yellow));
      m_cardDeck.add(new Card(Card.CardType.Reverse, Card.CardColor.Green));

      m_cardDeck.add(new Card(Card.CardType.Skip, Card.CardColor.Blue));
      m_cardDeck.add(new Card(Card.CardType.Skip, Card.CardColor.Red));
      m_cardDeck.add(new Card(Card.CardType.Skip, Card.CardColor.Yellow));
      m_cardDeck.add(new Card(Card.CardType.Skip, Card.CardColor.Green));

      m_cardDeck.add(new Card(Card.CardType.DrawTwo, Card.CardColor.Blue));
      m_cardDeck.add(new Card(Card.CardType.DrawTwo, Card.CardColor.Red));
      m_cardDeck.add(new Card(Card.CardType.DrawTwo, Card.CardColor.Yellow));
      m_cardDeck.add(new Card(Card.CardType.DrawTwo, Card.CardColor.Green));
    }

    for(int i = 0; i < loopWildCards; i++) {

      m_cardDeck.add(new Card(Card.CardType.Wild));
      m_cardDeck.add(new Card(Card.CardType.WildDrawFour));
    }

    Collections.shuffle(m_cardDeck);
  }

  ArrayList<Card> InitPlayerDeck() {

    ArrayList<Card> deck = new ArrayList<Card>();

    for(int i = 0; i < START_CARDS; i++ ) {

      deck.add( m_cardDeck.get(i));

      m_cardDeck.remove(i);
    }

    return deck;
  }

  void BuyCard(int playerIndex, int total) {

    if(m_cardDeck.size() > 0) {

      m_players[playerIndex].BuyCard(m_cardDeck.get(0));

      m_cardDeck.remove(0);
    }
    else {

      // nao sei
    }
  }

  public boolean IsRunning() {

    return m_isRunning;
  }
}
