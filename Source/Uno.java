import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Uno extends UnicastRemoteObject implements UnoInterface {

	final int MAX_GAMES = 500;
	
	private ArrayList<UnoGame> m_games;
	private ArrayList<Player>  m_users;
	
	public Uno() throws RemoteException {
		
		m_games = new ArrayList<UnoGame>();
		m_users = new ArrayList<Player>();
	}
	
	@Override
	public int registraJogador(String nome) throws RemoteException {

		for(Player p : m_users) {
			
			if(p.GetName().equals(nome))
				return -1;
		}
	
		UnoGame game = null;
		for(int i = 0; i < m_games.size(); i++) {
		
			if()
		}
	
		return 0;
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
}
