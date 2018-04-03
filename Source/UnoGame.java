import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class UnoGame {
	
	public enum GameState { Null, ReadInput, ChangeTurn }
	
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
	
	public UnoGame() {}
	
	public UnoGame(int id) {
	
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
	
	  m_state = GameState.ReadInput;
	  
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
	
	public void UpdateGameState() {
	
	  switch(m_state) {
	
	    case ReadInput: {
	
	      System.out.println("\n########## " + m_players.get(m_playerTurn).GetName() + " Turn ##########\n");
	
	      System.out.println("Table: ");
	      m_cardStack.get(m_cardStack.size()-1).PrintCard();
	
	      System.out.println("Cards: ");
	
	      Scanner reader = new Scanner(System.in);
	
	      ArrayList<Card> pDeck = m_players.get(m_playerTurn).GetDeck();
	      for(int i = 0; i < pDeck.size(); i++) {
	
	        Card card = pDeck.get(i);
	
	        System.out.println((i+1) + " - " + card.ToString());
	      }
	
	      System.out.println("\nActions: ");
	      System.out.println("B - Buy a card");
	      System.out.println("P - Pass turn");
	
	      System.out.print("\nSelect a play: ");
	      String op = reader.next().toLowerCase();
	
	      if(op.equals("b") || op.equals("p")) {
	
	        if(op.equals("b")) {
	
	          BuyCard(m_playerTurn, 1);
	        }
	        else {
	
	          m_state = GameState.ChangeTurn;
	        }
	      }
	      else {
	
	        try {
	
	          int c = Integer.parseInt(op);
	
	          if(c <= 0 || c > pDeck.size()) {
	
	            System.out.println("\nInvalid option!\n");
	          }
	          else {
	
	            Card card = pDeck.get(c-1);
	
	            if(IsPlayValid(card)) {
	
	              m_cardStack.add(card);
	
	              m_players.get(m_playerTurn).SelectCard(c-1);
	
	              m_cardTypeEffect = card.GetCardType();
	
	              m_state = GameState.ChangeTurn;
	            }
	            else {
	
	              System.out.println("\nInvalid Play!\n");
	            }
	          }
	        }
	        catch(Exception e) {
	
	          System.out.println("\nInvalid option! " + op + "\n");
	        }
	      }
	    }
	    break;
	
	    case ChangeTurn: {
	
	      if(m_players.get(m_playerTurn).GetTotalCards() == 0) {
	
	        m_isRunning = false;
	      }
	      else {
	
	        m_playerTurn = (m_playerTurn+1)%m_players.size();
	
	        switch(m_cardTypeEffect) {
	
	          case Reverse:
	          case Skip: {
	
	            m_playerTurn = (m_playerTurn+1)%m_players.size();
	
	          }
	          break;
	
	          case DrawTwo: {
	
	            BuyCard(m_playerTurn, 2);
	
	            m_playerTurn = (m_playerTurn+1)%m_players.size();
	          }
	          break;
	
	          case Wild: {
	
	
	          }
	          break;
	
	          case WildDrawFour: {
	
	            BuyCard(m_playerTurn, 4);
	          }
	          break;
	        }
	
	        m_cardTypeEffect = Card.CardType.None;
	
	        m_state = GameState.ReadInput;
	      }
	    }
	    break;
	  }
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
	
	      return card;
	    }
	  }
	}
	
	boolean IsPlayValid(Card card) {
	  
		if(m_cardStack.size() > 0) {
	
			Card firstCard = m_cardStack.get(m_cardStack.size()-1); 
		
			switch(card.GetCardType()) {
			
				case None: {
	
	  			if(!card.CompareCardColor(firstCard) && !card.CompareCardNumber(firstCard))
	  				return false;
	
	  			if(card.CompareCardColor(firstCard) || card.CompareCardNumber(firstCard))
	  				return true;
	  		}
				break;
				
				case Reverse:
				case Skip:
				case DrawTwo: {
	
				  return card.CompareCardType(firstCard) || card.CompareCardColor(firstCard);
				}
				
				case Wild: {
	
				  return true;
				}
				case WildDrawFour: {
	
				  boolean canPlay = true;
				  for(Card c : m_players.get(m_playerTurn).GetDeck()) {
	
	          if(c.GetCardType() != Card.CardType.WildDrawFour) {
	
	            if(c.GetCardType() == Card.CardType.Wild)
	              canPlay = false;
	            else if(c.CompareCardColor(firstCard))
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
	
	void BuyCard(int playerIndex, int total) {
	
	  if(m_cardDeck.size() > 0) {
	
	    for(int i = 0; i < total; i++) {
	
	      if(i < m_cardDeck.size()) {
	
	        Card card = m_cardDeck.get(i);
	
	        m_players.get(m_playerTurn).BuyCard(card);
	
	        m_cardDeck.remove(i);
	      }
	    }
	  }
	  else {
	
	    System.out.println("NO CARDS TO BUY!");
	  }
	}
	
	public String GetWinner() {
	
	  return m_players.get(m_playerTurn).GetName();
	}
	
	public boolean AddPlayer(Player newPlayer) {
	
		if(TotalPlayers() == 2)
			return false;
	
		m_players.add(newPlayer);
		
		return true;
	}
	
	public int TotalPlayers() {
	
		return m_players.size();
	}
	
	public int GetGameId() {
	
		return m_gameId;
	}
	
	public boolean IsRunning() {
	
	  return m_state != GameState.Null;
	}
}