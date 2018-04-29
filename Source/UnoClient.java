import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class UnoClient {

	static public int m_playerID = -1;

	public static void main(String[] args) {

		long lastTime = System.nanoTime();

		Scanner scanner = new Scanner(System.in);
		
		//System.out.println("Digite o endereco da maquina seguido de <ENTER>...");
	
		//String endereco = scanner.next();
		
		System.out.println("Digite o seu nome para comecar a jogar seguido de <ENTER>...");
		
		String nome = scanner.next();
	
		try {
		
			UnoInterface uno = (UnoInterface)Naming.lookup("//localhost/PID");
			
			m_playerID = uno.registraJogador(nome);
			
			if(m_playerID == -1) {
			
				System.out.println("Jogador ja registrado!");
			}
			else {
			
				System.out.println("Jogador registrado com sucesso!");
			}
			
			System.out.println("Procurando partida...");
			boolean gameFound = false;
			while(true) {

				int p = uno.temPartida(m_playerID); 
			
				if(p == 0) {
				
					System.out.println("Servidor lotado!");
					System.exit(1);
				}
				else if(p == -1) {
					
					System.out.println("Erro!");
					System.exit(1);
				}
				else if(p == -2) {
				
					System.out.println("Tempo de espera esgotado!");
					System.exit(1);
				}
				else {

          System.out.println("Buscando oponente...");
          String opName = "";

          if(p == 1) {

            while(true) {

              opName = uno.obtemOponente(m_playerID);
              if(!opName.isEmpty()) {

                gameFound = true;

                break;
              }
            }
          }
          else if(p == 2) {

            gameFound = true;

            opName = uno.obtemOponente(m_playerID);
          }

          System.out.println("Achou! ->  " + opName);

          break;
        }
			}

			if(gameFound) {


      }
		} 
		catch (MalformedURLException e) {
		
			e.printStackTrace();
		} 
		catch (RemoteException e) {
		
			e.printStackTrace();
		} 
		catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	//private int

}
