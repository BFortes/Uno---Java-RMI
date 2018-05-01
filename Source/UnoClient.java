import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.Gson;

public class UnoClient {

	static public int m_playerID = -1;

	public static void main(String[] args) {

    Gson gson = new Gson();

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

              Thread.sleep(1000);
            }
          }
          else if(p == 2) {

            gameFound = true;

            opName = uno.obtemOponente(m_playerID);
          }

          System.out.println("Achou! ->  " + opName);

          break;
        }

        Thread.sleep(1000);
			}

			if(gameFound) {

        while(true) {

          // -1 Erro
          // -2 Nao ha 2 jogadores

          int minhaVez = uno.ehMinhaVez(m_playerID);
          if(minhaVez > 1) {

            if(minhaVez == 2)
              System.out.println("VOCE VENCEU!!!");
            if(minhaVez == 3)
              System.out.println("VOCE PERDEU!!!");
            if(minhaVez == 4)
              System.out.println("EMPATE!!!");
            if(minhaVez == 5)
              System.out.println("VOCE VENCEU POR WO!!!");
            if(minhaVez == 6)
              System.out.println("VOCE PERDEU POR WO!!!");

            uno.encerraPartida(m_playerID);

            gameFound = false;

            break;
          }
          else {

            if(minhaVez == 1) {

              while(true) {

                String sCartaMesa = uno.obtemCartaMesa(m_playerID);
                String sMinhaMao  = uno.mostraMao(m_playerID);
                int    corAtiva   = uno.obtemCorAtiva(m_playerID);

                Card            cartaMesa = gson.fromJson(sCartaMesa, Card.class);
                ArrayList<Card> minhaMao  = gson.fromJson(sMinhaMao, ArrayList.class);

                String acao = EscolheAcao(cartaMesa, minhaMao, corAtiva);

                if(acao.equals("b")) {

                  uno.compraCarta(m_playerID);
                }
                else {

                  try {

                    int c = Integer.parseInt(acao);

                    if(minhaMao.get(c).GetCardType() == Card.CardType.Wild || minhaMao.get(c).GetCardType() == Card.CardType.WildDrawFour)
                      corAtiva = EscolheCor();

                    int jogaCarta = uno.jogaCarta(m_playerID, c, corAtiva);
                    if(jogaCarta == 1)
                      break;
                  }
                  catch(Exception e) {

                    System.out.println("\nInvalid option! " + acao + "\n");
                  }
                }
              }
            }
          }

          Thread.sleep(1000);
        }
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
		catch(InterruptedException e) {
      e.printStackTrace();
    }
  }

	static String EscolheAcao(Card cartaMesa, ArrayList<Card> deck, int cor) {

    System.out.println("Table: ");
    cartaMesa.PrintCard();
    System.out.println("Cur Color: " + (Card.CardColor.values()[cor]) + "\n");

    System.out.println("Cards: ");

    for(int i = 0; i < deck.size(); i++) {

      Card card = deck.get(i);

      System.out.println((i+1) + " - " + card.ToString());
    }

    System.out.println("\nActions: ");
    System.out.println("B - Buy a card");

    System.out.print("\nSelect a play: ");

    Scanner reader = new Scanner(System.in);

    while(true) {

      String op = reader.next().toLowerCase();

      if(op.equals("b")) {

        return op;
      }
      else {

        try {

          int c = Integer.parseInt(op);

          if(c <= 0)
            System.out.println("\nInvalid option! " + op + "\n");
          else if(c > deck.size())
            System.out.println("\nInvalid option! " + op + "\n");

          return "" + (c-1);
        }
        catch(Exception e) {

          System.out.println("\nInvalid option! " + op + "\n");
        }
      }
    }
  }

  static int EscolheCor() {

    //  3 Azul
    //  2 Amarelo
    //  1 Verde
    //  0 Vermelho

    System.out.println("\nColors: ");
    System.out.println("1 - Red");
    System.out.println("2 - Green");
    System.out.println("3 - Yellow");
    System.out.println("4 - Blue");

    System.out.print("\nSelect a color: ");

    Scanner reader = new Scanner(System.in);

    while(true) {

      String op = reader.next().toLowerCase();

      if(op.equals("1"))
        return 0;
      else if(op.equals("2"))
        return 1;
      else if(op.equals("3"))
        return 2;
      else if(op.equals("4"))
        return 3;
      else
        System.out.println("INVALID CHOICE!!!");
    }
  }
}
