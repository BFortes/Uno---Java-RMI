import java.util.ArrayList;
import java.util.Random;

public class PlayerRandom extends Player {

  public PlayerRandom(String name)  {

    super(-1, name);
  }

  public String DoPlay(UnoLogic game) {

    for(int i = 0; i < m_cards.size(); i++) {

      if(game.IsPlayValid(m_cards.get(i)))
        return "" + (i+1);
    }

    if(game.m_playerCanBuyCard)
      return "b";
    else
      return "p";
  }

  public String ChoiceColor() {

    Random r = new Random();

    return "" + (r.nextInt(4) + 1);
  }
}
