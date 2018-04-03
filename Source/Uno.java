import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Uno extends UnicastRemoteObject implements UnoInterface {

	final int MAX_GAMES = 10;
	
	private ArrayList<UnoGame> m_games;
	private ArrayList<Player>  m_users;
	
	private int m_playerId = 0;
	
	public Uno() throws RemoteException {
		
		m_games = new ArrayList<UnoGame>();
		m_users = new ArrayList<Player>();
		
		for(int i = 0; i < MAX_GAMES; i++) {
			
			m_games.add(new UnoGame(i));
		}
	}
	
	@Override
	public int registraJogador(String nome) throws RemoteException {

		for(Player p : m_users) {
			
			if(p.GetName().equals(nome))
				return -1;
		}
	
		UnoGame game = null;
		for(int i = 0; i < m_games.size(); i++) {
		
			UnoGame g = m_games.get(i); 
		
			if(!g.IsRunning() && g.TotalPlayers() < 2) {
				
				game = g;
				
				break;
			}
		}
		
		if(game == null)
			return -2;
		
		Player newPlayer = new Player(game.GetGameId(), GetPlayerId(), nome); 
		
		game.AddPlayer(newPlayer);
		
		return newPlayer.GetId();
	}
	
	@Override
	public int encerraPartida(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int temPartida(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int obtemOponente(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int ehMinhaVez(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int obtemNumCartasBaralho(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int obtemNumCartas(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int obtemNumCartasOponente(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int mostraMao(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int obtemCartaMesa(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int obtemCorAtiva(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int compraCarta(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int jogaCarta(int id, int cartaMaoIndex, int corCarta) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int obtemPontos(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int obtemPontosOponente(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private synchronized int GetPlayerId() {
	
		return m_playerId++;
	}
}
