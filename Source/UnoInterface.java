import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UnoInterface extends Remote {
    
    public int registraJogador(String nome) throws RemoteException;
    //  id do jogador
    // -1  Jogador ja registrado
    // -2  Numero maximo de jogadores atingido!
    
    public int encerraPartida(int id) throws RemoteException;
    //  0 Ok
    // -1 Erro
    
    public int temPartida(int id) throws RemoteException;
    //  2 Sim, jogador 1 comeca
    //  1 Sim, jogador 1 comeca
    //  0 Nao ha partida
    // -1 Erro
    // -2 Tempo de espera esgotado
    
    public String obtemOponente(int id) throws RemoteException;
    //  "Nome" Achou oponente
    //  ""     Erro
    
    public int ehMinhaVez(int id) throws RemoteException;
    //  6 Perdedor por WO
    //  5 Vencedor por WO
    //  4 Empate
    //  3 Vc eh o perdedor
    //  2 Vc eh o vencedor
    //  1 Sim
    //  0 Nao
    // -1 Erro
    // -2 Nao ha 2 jogadores
    
    public int obtemNumCartasBaralho(int id) throws RemoteException;
    //  int Numero de cartas do baralho
    // -1   Erro
    // -2   Nao ha 2 jogadores
    
    public int obtemNumCartas(int id) throws RemoteException;
    //  int N�umero de cartas do baralho do jogador
    // -1   Erro
    // -2   Nao ha 2 jogadores
    
    public int obtemNumCartasOponente(int id) throws RemoteException;
    //  int Numero de cartas do baralho do oponente
    // -1   Erro
    // -2   Nao ha 2 jogadores
    
    public String mostraMao(int id) throws RemoteException;
    //  String Json Lista de cartas
    //  ""          Erro
    
    public String obtemCartaMesa(int id) throws RemoteException;
    //  String Json Carta da mesa
    //  ""          Erro
    
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
    //  0 Jogada invalida (cor nao corresponde)
    // -1 Jogador nao encontrado
    // -2 Nao ha 2 jogadores
    // -3 Parametros invalidos
    // -4 Nao a a vez do jogador
    
    public int obtemPontos(int id) throws RemoteException;
    //  int Pontos totais
    // -1   Jogador nao encontrado
    // -2   Nao ha 2 jogadores
    // -3   Partida nao concluida
    
    public int obtemPontosOponente(int id) throws RemoteException;
    //  int Pontos totais
    // -1   Jogador nao encontrado
    // -2   Nao ha 2 jogadores
    // -3   Partida nao concluida
}