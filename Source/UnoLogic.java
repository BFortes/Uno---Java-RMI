import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import com.google.gson.Gson;

public class UnoLogic {

  public enum GameState { Null, FindPlayer, Playing, Results, EndGame }

  static final public int TOTAL_CARDS        = 108;
  static final public int TOTAL_NUM_CARDS    = 76;
  static final public int TOTAL_ACTION_CARDS = 24;
  static final public int TOTAL_WILD_CARDS   = 8;
  static final public int START_CARDS        = 7;
  static final public int PLAY_TIMEOUT       = 60;
  static final public int FIND_TIMEOUT       = 120;
  static final public int DESTROY_TIMEOUT    = 60;

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

  long m_turnStartTime = 0;
  long m_findStartTime = 0;
  long m_destroyStartTime = 0;

  Gson m_gson = new Gson();

  public UnoLogic(int id) {

		m_gameId = id;

    m_players   = new ArrayList<Player>();
    m_cardDeck  = new ArrayList<Card>();
    m_cardStack = new ArrayList<Card>();

		ResetGame();
	}

  // inicia uma partida
	public boolean StartGame() {

    if(m_state == GameState.Null) {

      InitDeck();

      m_cardStack.add(GetFirstDeckCard());

      InitPlayerDeck();

      m_playerTurn = 0;

      m_turnStartTime = System.nanoTime();

      m_state = GameState.Playing;

      return true;
    }

    return false;
  }

  // volta ao estado de jogo null, onde o jogo espera por novos jogadores
	public void ResetGame() {
	
		m_state = GameState.Null;
	
		m_players.clear();
		m_cardDeck.clear();
		m_cardStack.clear();
		
		m_playerTurn = 0;
		
		m_playerCanBuyCard  = true;
		m_playerCanPassTurn = false;
		
		m_cardCurColor   = Card.CardColor.None;
		m_cardTypeEffect = Card.CardType.None;
	}

  // criacao do deck com todas as cartas do jogo. logo embaralhado
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

  // update chamado a todo frame para atualizar os tempos de timeout
  public void UpdateGameState() {

    switch(m_state) {

      case EndGame: {

        long time = System.nanoTime();
        double elapsed = (double)(time - m_destroyStartTime) / 1000000000.0;

        if((int)elapsed >= DESTROY_TIMEOUT)
          ResetGame();
      }

      case FindPlayer: {

        long time = System.nanoTime();
        double elapsed = (double)(time - m_findStartTime) / 1000000000.0;

        if((int)elapsed >= FIND_TIMEOUT)
          ChangeTurn();
      }

      case Playing: {

        long time = System.nanoTime();
        double elapsed = (double)(time - m_turnStartTime) / 1000000000.0;

        if((int)elapsed >= PLAY_TIMEOUT)
          ChangeTurn();
      }
    }
  }

  // jogador tenta realizar uma jogada
  public int PlayACard(int id, int cardIndex, int color) {

    if(m_state == GameState.Playing) {

      Player p = m_players.get(m_playerTurn);
      if(p.GetId() != id)
        return -4;

      Card card = p.GetDeck().get(cardIndex);
      if(IsPlayValid(card)) {

        m_cardStack.add(card);

        p.SelectCard(cardIndex);

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

    return 1;
  }

  // troca turno e atualiza board, bem como o efeito da ultima carta jogada
  void ChangeTurn() {

    if(m_state == GameState.Playing) {

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

        m_turnStartTime = System.nanoTime();

        m_playerTurn = (m_playerTurn+1)%m_players.size();

        m_cardTypeEffect = Card.CardType.None;

        m_playerCanBuyCard = m_cardDeck.size() > 0;
      }
    }
  }

  // verifica se jogada feita por um jogador eh valida
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

  // distribui cartas para jogadores. 1 pra cada até 7
  void InitPlayerDeck() {

    int pIndex = 0;
    for(int i = 0; i < START_CARDS*2; i++ ) {

      m_players.get(pIndex).AddCard(m_cardDeck.get(i));
      m_cardDeck.remove(i);

      pIndex = (pIndex+1)%m_players.size();
    }
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

  // jogador tenta comprar uma carta
  boolean BuyCard(int playerIndex, int total) {

    if(total == 1 && !m_playerCanBuyCard)
      return false;

    if(m_cardDeck.size() > 0) {

      if(total > m_cardDeck.size())
        total = m_cardDeck.size()-1;

      for(int i = 0; i < total; i++) {

        if(i < m_cardDeck.size()) {

          Card card = m_cardDeck.get(i);

          m_players.get(playerIndex).BuyCard(card);

          m_cardDeck.remove(i);
        }
      }

      m_playerCanBuyCard = total > 1;

      return true;
    }
    else {

      System.out.println("NO CARDS TO BUY!");

      return false;
    }
  }

  // metodo para saber quem foi o ganhador por pontos
  public int GetWinner(int id) {

    if(m_state == GameState.Results || m_state == GameState.EndGame) {

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

      if(p1Points > p2Points) {

        System.out.println("VENCEDOR: " + GetPlayerName(id, false) + " " + GetPlayerPoints(id, false)
                        + " PERDEDOR: " + GetPlayerName(id, true)  + " " + GetPlayerPoints(id, true));

        return 2;
      }
      else if(p1Points < p2Points) {

        System.out.println("VENCEDOR: " + GetPlayerName(id, true)  + " " + GetPlayerPoints(id, true)
                        + " PERDEDOR: " + GetPlayerName(id, false) + " " + GetPlayerPoints(id, false));

        return 3;
      }
      else {

        System.out.println("EMPATE!");

        return 4;
      }
    }

    return -1;
  }

  // adicionar players a partida
	public int AddPlayer(Player newPlayer) {
	
		if(TotalPlayers() == 2)
			return 0;

		m_players.add(newPlayer);

		if(TotalPlayers() == 1) {

      //m_findStartTime = System.nanoTime();

      //m_state = GameState.FindPlayer;
    }
		
		return TotalPlayers();
	}

  // verifica se tal jogador esta na partida
	public boolean IsPlayerInGame(int id) {

    for(int i = 0; i < m_players.size(); i++)
      if(m_players.get(i).GetId() == id)
        return true;

    return false;
  }

  // verifica se tal jogador esta jogando
  public boolean IsPlayerTurn(int id) {

    return m_players.get(m_playerTurn).GetId() == id;
  }

  public String GetPlayerName(int id, boolean opponent) {

    for(int i = 0; i < m_players.size(); i++) {

      Player p = m_players.get(i);

      if(p.GetId() == id && !opponent
          || p.GetId() != id && opponent)
        return p.GetName();
    }

    return "";
  }

  // metodo para informar ao servidor que o oponente esta conectado e entao pode-se iniciar a partida
  public String GetOpponentName(int id) {

    for(int i = 0; i < m_players.size(); i++) {

      Player p = m_players.get(i);

      if(p.GetId() != id) {

        StartGame();

        return p.GetName();
      }
    }

    return "";
  }

  // numero total de cartas de um jogador
  public int GetTotalPlayerDeckNum(int id, boolean opponent) {

    for(int i = 0; i < m_players.size(); i++) {

      Player p = m_players.get(i);
      if(p.GetId() == id && !opponent
          || p.GetId() != id && opponent)
        return p.GetTotalCards();
    }

    return -1;
  }

  // numero total de cartas da mesa para compra
  public int GetTotalDeckNum() {

    return m_cardDeck.size();
  }

  public int GetCurrentColor() {

    return m_cardCurColor.ordinal();
  }

	public int TotalPlayers() {
	
		return m_players.size();
	}

  // tal jogador tenta comprar uma carta
	public boolean PlayerBuyCard(int id) {

    int myIndex = -1;
    for(int i = 0; i < m_players.size(); i++)
      if(m_players.get(i).GetId() == id)
        myIndex = i;

    return BuyCard(myIndex, 1);
  }

  // parser do deck de um jogador para ser enviado ao servidor e assim ao cliente
  public String GetPlayerDeckString(int id) {

    for(int i = 0; i < m_players.size(); i++) {

      Player p = m_players.get(i);

      if(p.GetId() == id) {

        return m_gson.toJson(p.GetDeck());
      }
    }

    return "";
  }

  // parser da primeira carta da pilha da mesa para ser enviado ao servidor e assim ao cliente
  public String GetFirstStackCardString () {

    if(m_cardStack.size() > 0)
      return m_gson.toJson(m_cardStack.get(m_cardStack.size()-1));

    return "";
  }

  // se o jogador corrente tem alguma acao para fazer
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

  // jogador passa a vez
  public int PassTheTurn(int id) {

    if(IsPlayerTurn(id)) {

      ChangeTurn();

      return 1;
    }

    return -1;
  }

	public int GetGameId() {
	
		return m_gameId;
	}

  // trigger para comecar a contagem ateh "destruir" a partida
  public void EndGame() {

    if(m_state == GameState.EndGame)
      return;

    m_destroyStartTime = System.nanoTime();

    m_state = GameState.EndGame;
  }

  public boolean IsRunning() {

    return m_state != GameState.Null;
  }

  public boolean IsEndGame() {

    return m_state == GameState.EndGame;
  }
}
