import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.regex.Pattern;

class Game
{
  private Status status;
  private Input input;

  private ActionCache actionCache;

  /*
   * CARDS
   */
  private final ArrayList<Card> depositSpade;
  private final ArrayList<Card> depositHeart;
  private final ArrayList<Card> depositClub;
  private final ArrayList<Card> depositDiamond;

  private final ArrayList<Card> drawLeft;
  private final ArrayList<Card> drawRight;

  private final ArrayList<Card> tableOne;
  private final ArrayList<Card> tableTwo;
  private final ArrayList<Card> tableThree;
  private final ArrayList<Card> tableFour;
  private final ArrayList<Card> tableFive;
  private final ArrayList<Card> tableSix;
  private final ArrayList<Card> tableSeven;

  public Game()
  {
    status = Status.PLAY;
    input = Input.PILE;

    actionCache = null;

    depositSpade = new ArrayList<>();
    depositHeart = new ArrayList<>();
    depositClub = new ArrayList<>();
    depositDiamond = new ArrayList<>();
    
    drawLeft = new ArrayList<>();
    drawRight = new ArrayList<>();
    
    tableOne = new ArrayList<>();
    tableTwo = new ArrayList<>();
    tableThree = new ArrayList<>();
    tableFour = new ArrayList<>();
    tableFive = new ArrayList<>();
    tableSix = new ArrayList<>();
    tableSeven = new ArrayList<>();
  }

  public void prepare()
  {
    // collect all cards
    ArrayList<Card> allCards = new ArrayList<>();

    for(Suit suit : Suit.values())
      for(Number number : Number.values())
        allCards.add(new Card(suit, number));
    
    // randomise
    Collections.shuffle(allCards);

    // distribute
    tableOne.add(allCards.get(0));

    for(int i = 0; i < 2; i++)
      tableTwo.add(allCards.get(i + 1));
    
    for(int i = 0; i < 3; i++)
      tableThree.add(allCards.get(i + 3));
    
    for(int i = 0; i < 4; i++)
      tableFour.add(allCards.get(i + 6));

    for(int i = 0; i < 5; i++)
      tableFive.add(allCards.get(i + 10));
    
    for(int i = 0; i < 6; i++)
      tableSix.add(allCards.get(i + 15));
    
    for(int i = 0; i < 7; i++)
      tableSeven.add(allCards.get(i + 21));

    for(int i = 0; i < 24; i++)
      drawRight.add(allCards.get(i + 28));
    
    // set visibility
    ArrayList<ArrayList<Card>> tables = new ArrayList<>();
    tables.add(tableOne);
    tables.add(tableTwo);
    tables.add(tableThree);
    tables.add(tableFour);
    tables.add(tableFive);
    tables.add(tableSix);
    tables.add(tableSeven);

    for(ArrayList<Card> table : tables)
      table.get(table.size() - 1).setVisible(true);
    
    for(Card draw : drawRight)
      draw.setVisible(true);
  }

  public void action(String action)
  {
    if(action.equals(""))
      return;
    
    if(input == Input.PILE && action.equals("+"))
    {
      if(drawRight.isEmpty())
      {
        Collections.reverse(drawLeft);
        for(Card card : drawLeft)
          drawRight.add(card);
        
        drawLeft.clear();
      }
      else
      {
        Card card = drawRight.get(drawRight.size() - 1);
        drawLeft.add(card);
        drawRight.remove(card);
      }
    }

    else if(input == Input.PILE && action.equals("#"))
    {
      actionCache = new ActionCache('#');
      actionCache.setRow(drawLeft.size());
      input = Input.DESTINATION_PILE;
    }

    else if((input == Input.PILE || input == Input.DESTINATION_PILE) &&
            Arrays.asList(new String[]{"A", "B", "C", "D"}).contains(action))
    {
      if(input == Input.DESTINATION_PILE)
      {
        ArrayList<Card> fromPile = getPile(actionCache.getPile());
        Card card = fromPile.get(actionCache.getRow() - 1);
        ArrayList<Card> toPile = getPile(action.charAt(0));

        if(fromPile.size() - actionCache.getRow() > 0)
        {
          // reset
          input = Input.PILE;
        }
        else if(canMoveDeposit(card, toPile))
        {
          toPile.add(fromPile.get(actionCache.getRow() - 1));
          fromPile.remove(actionCache.getRow() - 1);
          
          input = Input.PILE;
          if(fromPile.size() > 0)
            fromPile.get(fromPile.size() - 1).setVisible(true);
        }
      }
      else
      {
        actionCache = new ActionCache(action.charAt(0));
        actionCache.setRow(getPile(action.charAt(0)).size());
        input = Input.DESTINATION_PILE;
      }
    }

    else if((input == Input.PILE || input == Input.DESTINATION_PILE) &&
            Arrays.asList(new String[]{"1", "2", "3", "4", "5", "6", "7"}).contains(action))
    {
      if(input == Input.DESTINATION_PILE)
      {
        ArrayList<Card> fromPile = getPile(actionCache.getPile());
        Card card = fromPile.get(actionCache.getRow() - 1);
        ArrayList<Card> toPile = getPile(action.charAt(0));
        
        if(canMove(card, toPile))
        {
          int amount = fromPile.size() - actionCache.getRow() + 1;

          for(int i = 0; i < amount; i++)
            toPile.add(fromPile.get(actionCache.getRow() - 1 + i));

          for(int i = 0; i < amount; i++)
            fromPile.remove(actionCache.getRow() - 1);
          
          input = Input.PILE;
          if(fromPile.size() > 0)
            fromPile.get(fromPile.size() - 1).setVisible(true);
        }
      }
      else
      {
        actionCache = new ActionCache(action.charAt(0));
        ArrayList<Card> fromPile = getPile(action.charAt(0));

        // only one card visible -> select card
        if(fromPile.size() == 1 || (!fromPile.get(fromPile.size() - 2).isVisible()))
        {
          actionCache.setRow(fromPile.size());
          input = Input.DESTINATION_PILE;
        }
        else
          input = Input.ROW;
      }
    }

    else if(input == Input.ROW)
    {
      if(Pattern.matches("[0-9]+[\\.]?[0-9]*", action))
      {
        actionCache.setRow(Integer.valueOf(action));
        input = Input.DESTINATION_PILE;
      }
      else // reset
        input = Input.PILE;
    }

    else
    {
      // reset
      input = Input.PILE;
    }
  }

  public void checkWon()
  {
    if(depositSpade.size() == 13 &&
       depositHeart.size() == 13 &&
       depositClub.size() == 13 &&
       depositDiamond.size() == 13)
    {
      status = Status.WON;
    }
  }

  public boolean canMove(Card card, ArrayList<Card> toPile)
  {
    if(!card.isVisible())
      return false;

    Card to = null;
    if(toPile.size() > 0)
      to = toPile.get(toPile.size() - 1);

    if(to == null)
    {
      if(card.getNumber() == Number.KING)
        return true;
      else
        return false;
    }

    return card.getSuit().isRed() != to.getSuit().isRed() &&
           to.getNumber().getValue() - 1 == card.getNumber().getValue();
  }

  public boolean canMoveDeposit(Card card, ArrayList<Card> toPile)
  {
    if(!card.isVisible())
      return false;
    
    Card to = null;
    if(toPile.size() > 0)
      to = toPile.get(toPile.size() - 1);

    if(to == null)
    {
      if((toPile == depositSpade && card.getSuit() == Suit.SPADE) ||
         (toPile == depositHeart && card.getSuit() == Suit.HEART) ||
         (toPile == depositClub && card.getSuit() == Suit.CLUB) ||
         (toPile == depositDiamond && card.getSuit() == Suit.DIAMOND))
        return true;
      else
        return false;
    }
    
    return card.getSuit() == to.getSuit() &&
           card.getNumber().getValue() - 1 == to.getNumber().getValue();
  }

  public ArrayList<Card> getPile(char pile)
  {
    if(pile == '#')
      return drawLeft;
    else if(pile == 'A')
      return depositSpade;
    else if(pile == 'B')
      return depositHeart;
    else if(pile == 'C')
      return depositClub;
    else if(pile == 'D')
      return depositDiamond;
    else if(pile == '1')
      return tableOne;
    else if(pile == '2')
      return tableTwo;
    else if(pile == '3')
      return tableThree;
    else if(pile == '4')
      return tableFour;
    else if(pile == '5')
      return tableFive;
    else if(pile == '6')
      return tableSix;
    else if(pile == '7')
      return tableSeven;
    else
      return null;
  }

  public Status getStatus()
  {
    return status;
  }

  public Input getInput()
  {
    return input;
  }

  public ArrayList<Card> getDepositSpade()
  {
    return depositSpade;
  }

  public ArrayList<Card> getDepositHeart()
  {
    return depositHeart;
  }

  public ArrayList<Card> getDepositClub()
  {
    return depositClub;
  }

  public ArrayList<Card> getDepositDiamond()
  {
    return depositDiamond;
  }

  public ArrayList<Card> getDrawLeft()
  {
    return drawLeft;
  }

  public ArrayList<Card> getDrawRight()
  {
    return drawRight;
  }

  public ArrayList<Card> getTableOne()
  {
    return tableOne;
  }

  public ArrayList<Card> getTableTwo()
  {
    return tableTwo;
  }

  public ArrayList<Card> getTableThree()
  {
    return tableThree;
  }

  public ArrayList<Card> getTableFour()
  {
    return tableFour;
  }

  public ArrayList<Card> getTableFive()
  {
    return tableFive;
  }

  public ArrayList<Card> getTableSix()
  {
    return tableSix;
  }

  public ArrayList<Card> getTableSeven()
  {
    return tableSeven;
  }
}