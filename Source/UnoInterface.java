import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UnoInterface extends Remote {
    
    public int registraJogador(String nome) throws RemoteException;
    //  id do jogador
    // -1  Jogador j� registrado
    // -2  Numero maximo de jogadores atingido!
    
    public int encerraPartida(int id) throws RemoteException;
    //  0 Ok
    // -1 Erro
    
    public int temPartida(int id) throws RemoteException;
    //  2 Sim, jogador 2 come�a
    //  1 Sim, jogador 1 come�a
    //  0 N�o h� partida
    // -1 Erro
    // -2 Tempo de espera esgotado
    
    public int obtemOponente(int id) throws RemoteException;
    //  "Nome" Achou oponente
    //  ""     Erro
    
    public int ehMinhaVez(int id) throws RemoteException;
    //  6 Perdedor por WO
    //  5 Vencedor por WO
    //  4 Empate
    //  3 Vc � o perdedor
    //  2 Vc � o vencedor
    //  1 Sim
    //  0 N�o
    // -1 Erro
    // -2 N�o h� 2 jogadores
    
    public int obtemNumCartasBaralho(int id) throws RemoteException;
    //  int N�mero de cartas do baralho
    // -1   Erro
    // -2   N�o h� 2 jogadores
    
    public int obtemNumCartas(int id) throws RemoteException;
    //  int N�mero de cartas do baralho do jogador
    // -1   Erro
    // -2   N�o h� 2 jogadores
    
    public int obtemNumCartasOponente(int id) throws RemoteException;
    //  int N�mero de cartas do baralho do oponente
    // -1   Erro
    // -2   N�o h� 2 jogadores
    
    public int mostraMao(int id) throws RemoteException;
    //  "TODO" Lista de cartas
    //  ""     Erro
    
    public int obtemCartaMesa(int id) throws RemoteException;
    //  "TODO" Carta da mesa
    //  ""     Erro
    
    public int obtemCorAtiva(int id) throws RemoteException;
    //  3 Azul
    //  2 Amarelo
    //  1 Verde
    //  0 Vermelho
    
    public int compraCarta(int id) throws RemoteException;
    //  0 Ok
    // -1 Erro
    
    public int jogaCarta(int id, int cartaMaoIndex, int corCarta) throws RemoteException;
    //  1 Ok
    //  0 Jogada inv�lida (cor n�o corresponde)
    // -1 Jogador n�o encontrado
    // -2 N�o h� 2 jogadores
    // -3 Par�metros inv�lidos
    // -4 N�o � a vez do jogador
    
    public int obtemPontos(int id) throws RemoteException;
    //  int Pontos totais
    // -1   Jogador n�o encontrado
    // -2   N�o h� 2 jogadores
    // -3   Partida n�o conclu�da
    
    public int obtemPontosOponente(int id) throws RemoteException;
    //  int Pontos totais
    // -1   Jogador n�o encontrado
    // -2   N�o h� 2 jogadores
    // -3   Partida n�o conclu�da
}