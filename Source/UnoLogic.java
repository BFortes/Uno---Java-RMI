import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import com.google.gson.Gson;

public class UnoLogic {

  public enum GameState { Null, Playing, Results }

  static final public int TOTAL_CARDS        = 108;
  static final public int TOTAL_NUM_CARDS    = 76;
  static final public int TOTAL_ACTION_CARDS = 24;
  static final public int TOTAL_WILD_CARDS   = 8;
  static final public int START_CARDS        = 7;

  private GameState m_state = GameState.Null;

	private int m_gameId;

  public ArrayList<Card> m_cardDeck;
  public ArrayList<Card> m_cardStack;

  private ArrayList<Player> m_players;
  int m_playerTurn = 0;

  public boolean m_playerCanBuyCard  = false;
  public boolean m_playerCanPassTurn = false;

  public Card.CardColor m_cardCurColor = Card.CardColor.None;
  public Card.CardType m_cardTypeEffect = Card.CardType.None;

  boolean m_isRunning = false;

  Gson m_gson = new Gson();

	public UnoLogic() {}
	
  public UnoLogic(int id) {

		m_gameId = id;

		m_state = GameState.Null;	
	}

	public boolean StartGame() {

		if(m_players.size() < 2)
			return false;

  	Random r = new Random();
  
    InitDeck();

    for(int i = 0; i < m_players.size(); i++)
	    m_players.get(i).SetDeck(InitPlayerDeck());
    
    m_playerTurn = r.nextInt(m_players.size());

    m_cardStack = new ArrayList<Card>();
    m_cardStack.add(GetFirstDeckCard());

    m_state = GameState.Playing;

    return true;
  }

	public void ResetGame() {
	
		m_state = GameState.Null;
	
		m_players.clear();
		m_cardDeck.clear();
		m_cardStack.clear();
		
		m_playerTurn = 0;
		
		m_playerCanBuyCard  = false;
		m_playerCanPassTurn = false;
		
		m_cardCurColor   = Card.CardColor.None;
		m_cardTypeEffect = Card.CardType.None;
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

  public int UpdateGameState(int id, int cardIndex, int color) {

    Player p = m_players.get(m_playerTurn);
    if(p.GetId() != id)
      return -4;

    Card card = p.GetDeck().get(cardIndex);
    if(IsPlayValid(card)) {

      m_cardStack.add(card);

      m_players.get(m_playerTurn).SelectCard(cardIndex);

      m_cardTypeEffect = card.GetCardType();
      m_cardCurColor   = m_cardTypeEffect == Card.CardType.Wild
                         || m_cardTypeEffect == Card.CardType.WildDrawFour ? Card.CardColor.values()[color]
                                                                           : card.GetCardColor();

      ChangeTurn();

      return 1;
    }
    else {

      return 0;
    }
  }

  void ChangeTurn() {

    if(m_players.get(m_playerTurn).GetTotalCards() == 0) {

      m_state = GameState.Results;
    }
    else if(m_cardDeck.size() == 0 && !HavePlayerAction()) {

      m_state = GameState.Results;
    }
    else {

      switch(m_cardTypeEffect) {

        case Reverse:
        case Skip: {

          m_playerTurn = (m_playerTurn+1)%m_players.size();
        }
        break;

        case DrawTwo: {

          m_playerTurn = (m_playerTurn+1)%m_players.size();

          BuyCard(m_playerTurn, 2);
        }
        break;

        case WildDrawFour: {

          BuyCard((m_playerTurn+1)%m_players.size(), 4);
        }
        break;
      }

      m_playerTurn = (m_playerTurn+1)%m_players.size();

      m_cardTypeEffect = Card.CardType.None;

      m_playerCanBuyCard = m_cardDeck.size() > 0;
    }
  }

  boolean IsPlayValid(Card card) {

    if(m_cardStack.size() > 0) {

      Card firstCard = m_cardStack.get(m_cardStack.size()-1);

      switch(card.GetCardType()) {

        case None: {

          if(!card.CompareCardColor(m_cardCurColor) && !card.CompareCardNumber(firstCard))
            return false;

          if(card.CompareCardColor(m_cardCurColor) || card.CompareCardNumber(firstCard))
            return true;
        }
        break;

        case Reverse:
        case Skip:
        case DrawTwo: {

          return card.CompareCardType(firstCard) || card.CompareCardColor(m_cardCurColor);
        }

        case Wild: {

          return true;
        }
        case WildDrawFour: {

          boolean canPlay = true;
          for(Card c : m_players.get(m_playerTurn).GetDeck()) {

            if(c.GetCardType() != Card.CardType.WildDrawFour) {

              if(c.GetCardType() == Card.CardType.Wild || c.CompareCardColor(m_cardCurColor))
                canPlay = false;
            }

            if(!canPlay)
              break;
          }

          return canPlay;
        }
      }
    }

    return false;
  }

  ArrayList<Card> InitPlayerDeck() {

    ArrayList<Card> deck = new ArrayList<Card>();

    for(int i = 0; i < START_CARDS; i++ ) {

      deck.add( m_cardDeck.get(i));

      m_cardDeck.remove(i);
    }

    return deck;
  }

  Card GetFirstDeckCard() {

    Random r = new Random();

    while(true) {

      int index = r.nextInt(m_cardDeck.size());

      Card card = m_cardDeck.get(index);

      if(card.GetCardType() == Card.CardType.None) {

        m_cardDeck.remove(index);

        m_cardCurColor = card.GetCardColor();

        return card;
      }
    }
  }

  void BuyCard(int playerIndex, int total) {

    if(m_cardDeck.size() > 0) {

      for(int i = 0; i < total; i++) {

        if(i < m_cardDeck.size()) {

          Card card = m_cardDeck.get(i);

          m_players.get(playerIndex).BuyCard(card);

          m_cardDeck.remove(i);
        }
      }

      m_playerCanBuyCard = false;
    }
    else {

      System.out.println("NO CARDS TO BUY!");
    }
  }

  public int GetWinner(int id) {

    if(m_state == GameState.Results) {

      int p1Points = 9999;
      int p2Points = 9999;
      int myNum = 0;

      for(int i = 0; i < m_players.size(); i++) {

        int p = m_players.get(i).GetTotalPoints();

        if(m_players.get(i).GetId() == id)
          myNum = i+1;

        if(i == 0)
          p2Points = p;
        else
          p1Points = p;
      }

      if(myNum == 2) {

        int aux = p2Points;
        p2Points = p1Points;
        p1Points = aux;
      }

      if(p1Points > p2Points)
        return 2;
      else if(p1Points < p2Points)
        return 3;
      else
        return 4;
    }

    return -1;
  }

	public int AddPlayer(Player newPlayer) {
	
		if(TotalPlayers() == 2)
			return 0;
	
		m_players.add(newPlayer);
		
		return TotalPlayers();
	}

	public boolean IsPlayerInGame(int id) {

    for(int i = 0; i < m_players.size(); i++)
      if(m_players.get(i).GetId() == id)
        return true;

    return false;
  }

  public boolean IsPlayerTurn(int id) {

    return m_players.get(m_playerTurn).GetId() == id;
  }

  public String GetOpponentName(int id) {

    for(int i = 0; i < m_players.size(); i++) {

      Player p = m_players.get(i);

      if(p.GetId() != id)
        return p.GetName();
    }

    return "";
  }

  public int GetTotalPlayerDeckNum(int id, boolean opponent) {

    for(int i = 0; i < m_players.size(); i++) {

      Player p = m_players.get(i);
      if(p.GetId() == id && !opponent
          || p.GetId() != id && opponent)
        return p.GetTotalCards();
    }

    return -1;
  }

  public int GetTotalDeckNum() {

    return m_cardDeck.size();
  }

  public int GetCurrentColor() {

    return m_cardCurColor.ordinal();
  }

	public int TotalPlayers() {
	
		return m_players.size();
	}

	public boolean PlayerBuyCard(int id) {

    int myIndex = -1;
    for(int i = 0; i < m_players.size(); i++)
      if(m_players.get(i).GetId() == id)
        myIndex = i;

    if(myIndex > 0)
      return false;

    BuyCard(myIndex, 1);

    return true;
  }

  public String GetPlayerDeckString(int id) {

    for(int i = 0; i < m_players.size(); i++) {

      Player p = m_players.get(i);

      if(p.GetId() == id) {

        return m_gson.toJson(p.GetDeck());
      }
    }

    return "";
  }

  public String GetFirstStackCardString () {

    if(m_cardStack.size() > 0)
      return m_gson.toJson(m_cardStack.get(m_cardStack.size()-1));

    return "";
  }

  public boolean HavePlayerAction() {

    ArrayList<Card> pDeck = m_players.get(m_playerTurn).GetDeck();
    for(int i = 0; i < pDeck.size(); i++) {

      if(IsPlayValid(pDeck.get(i)))
        return true;
    }

    return false;
  }

  public int GetPlayerPoints(int id, boolean opponent) {

    for(int i = 0; i < m_players.size(); i++) {

      Player p = m_players.get(i);
      if(p.GetId() == id && !opponent
          || p.GetId() != id && opponent)
        return p.GetTotalPoints();
    }

    return 0;
  }

	public int GetGameId() {
	
		return m_gameId;
	}

  public boolean IsRunning() {

    return m_state != GameState.Null;
  }
}
