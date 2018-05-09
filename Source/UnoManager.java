import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class UnoManager extends UnicastRemoteObject implements UnoInterface {

	private static final long serialVersionUID = 3014726454955238436L;
	
	final int MAX_GAMES = 10;
	final int MAX_USERS = MAX_GAMES*2;
	
	private ArrayList<UnoLogic> m_games;
	private ArrayList<Player>  m_users;
	
	private Map<Integer, UnoLogic> m_sessions;
	
	private int m_playerId = 0;
	
	public UnoManager() throws RemoteException {
		
		m_games    = new ArrayList<UnoLogic>();
		m_users    = new ArrayList<Player>();
		m_sessions = new HashMap<Integer, UnoLogic>();
		
		for(int i = 0; i < MAX_GAMES; i++) {
			
			m_games.add(new UnoLogic(i));
		}

    // UPDATE GAMES THREAD
    new Thread() {

      @Override
      public void run() {
        while(true) {

          for(int i = 0; i < m_games.size(); i++)
            m_games.get(i).UpdateGameState();
        }
      }
    }.start();
	}
	
	@Override
	public int registraJogador(String nome) throws RemoteException {

		for(Player p : m_users)
			if(p.GetName().equals(nome))
				return -1;
	
		if(m_users.size() == MAX_USERS)
			return -2;
		
		int id = GetPlayerId();
		AddPlayerToList(id, nome);
		
		return id;
	}
	
	@Override
	public int encerraPartida(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    //game.ResetGame();
    game.EndGame();

		return 0;
	}
	
	@Override
	public int temPartida(int id) throws RemoteException {

		UnoLogic game = GetFreeGame();
		if(game != null)
			return AddPlayerToGame(game, id, GetPlayerName(id));

		return 0;
	}
	
	@Override
	public String obtemOponente(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game != null)
      return game.GetOpponentName(id);

		return "";
	}
	
	@Override
	public int ehMinhaVez(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.IsRunning()) {

      if(game.TotalPlayers() < 2) {

        if(game.IsPlayerInGame(id))
          return 5;
        else
          return 6;
      }
      else {

        int resultado = game.GetWinner(id);
        if(resultado != -1)
          return resultado;
        else
          return game.IsPlayerTurn(id) ? 1 : 0;
      }
    }
    else {

      if(game.TotalPlayers() == 1)
        return -2;
      else
        return -1;
    }
	}
	
	@Override
	public int obtemNumCartasBaralho(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.TotalPlayers() < 2)
      return -2;

    return game.GetTotalDeckNum();
	}
	
	@Override
	public int obtemNumCartas(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.TotalPlayers() < 2)
      return -2;

    return game.GetTotalPlayerDeckNum(id, false);
	}
	
	@Override
	public int obtemNumCartasOponente(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.TotalPlayers() < 2)
      return -2;

    return game.GetTotalPlayerDeckNum(id, true);
	}
	
	@Override
	public String mostraMao(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return "";

    return game.GetPlayerDeckString(id);
	}
	
	@Override
	public String obtemCartaMesa(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return "";

    return game.GetFirstStackCardString();
  }
	
	@Override
	public int obtemCorAtiva(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.TotalPlayers() < 2)
      return -2;

    return game.GetCurrentColor();
	}
	
	@Override
	public int compraCarta(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.TotalPlayers() < 2)
      return -2;

    return game.PlayerBuyCard(id) ? 0 : -1;
	}
	
	@Override
	public int jogaCarta(int id, int cartaMaoIndex, int corCarta) throws RemoteException {

    //  1 Ok
    //  0 Jogada invalida (cor nao corresponde)
    // -1 Jogador nao encontrado
    // -2 Nao ha 2 jogadores
    // -3 Parametros invalidos
    // -4 Nao a a vez do jogador

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.TotalPlayers() < 2)
      return -2;

    if(!game.IsPlayerTurn(id))
      return -4;

    return game.PlayACard(id, cartaMaoIndex, corCarta);
	}
	
	@Override
	public int obtemPontos(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.TotalPlayers() < 2)
      return -2;

    if(game.IsRunning() && !game.IsEndGame())
      return -3;

    return game.GetPlayerPoints(id, false);
	}
	
	@Override
	public int obtemPontosOponente(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.TotalPlayers() < 2)
      return -2;

    if(game.IsRunning() && !game.IsEndGame())
      return -3;

    return game.GetPlayerPoints(id, true);
	}

  @Override
  public int passaVez(int id) throws RemoteException {

    UnoLogic game = GetPlayerGame(id);
    if(game == null)
      return -1;

    if(game.TotalPlayers() < 2)
      return -2;

    return game.PassTheTurn(id);
  }

	private synchronized int GetPlayerId() {
	
		return m_playerId++;
	}

	private synchronized String GetPlayerName(int id) {

		for(int i = 0; i < m_users.size(); i++) {

			Player player = m_users.get(i);

			if(player.GetId() == id)
				return player.GetName();
		}

		return "";
	}

  private synchronized UnoLogic GetPlayerGame(int id) {

    for(int i = 0; i < m_games.size(); i++) {

      UnoLogic game = m_games.get(i);
      if(game.IsPlayerInGame(id))
        return game;
    }

    return null;
  }

	private synchronized UnoLogic GetFreeGame() {
	
		for(int i = 0; i < m_games.size(); i++) {

			UnoLogic g = m_games.get(i);
		
			if(!g.IsRunning() && g.TotalPlayers() < 2) {
				
				return g;
			}
		}
		
		return null;
	}
	
	private synchronized void AddPlayerToList(int id, String name) {
	
		Player newPlayer = new Player(id, name); 
		
		m_users.add(newPlayer);
	}
	
	private synchronized int AddPlayerToGame(UnoLogic game, int id, String name) {
		
		Player newPlayer = new Player(game.GetGameId(), id, name);
		
		return game.AddPlayer(newPlayer);
	}
}