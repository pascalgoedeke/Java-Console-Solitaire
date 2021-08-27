import java.util.ArrayList;
import java.util.Arrays;

class Screen
{
  private final Game game;

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

  /*
   * SCREEN
   */
  private static final char emptyCornerTopLeft = '┌';
  private static final char emptyCornerTopRight = '┐';
  private static final char emptyCornerBottomLeft = '└';
  private static final char emptyCornerBottomRight = '┘';
  private static final char emptyHorizontal = '─';
  private static final char emptyVertical = '│';

  private static final char cardCornerTopLeft = '┏';
  private static final char cardCornerTopRight = '┓';
  private static final char cardCornerBottomLeft = '┗';
  private static final char cardCornerBottomRight = '┛';
  private static final char cardHorizontal = '━';
  private static final char cardVertical = '┃';

  private static final String ansiRed = "\u001B[31m";
  private static final String ansiReset = "\u001B[0m";

  public Screen(Game game)
  {
    this.game = game;

    depositSpade = game.getDepositSpade();
    depositHeart = game.getDepositHeart();
    depositClub = game.getDepositClub();
    depositDiamond = game.getDepositDiamond();

    drawLeft = game.getDrawLeft();
    drawRight = game.getDrawRight();

    tableOne = game.getTableOne();
    tableTwo = game.getTableTwo();
    tableThree = game.getTableThree();
    tableFour = game.getTableFour();
    tableFive = game.getTableFive();
    tableSix = game.getTableSix();
    tableSeven = game.getTableSeven();
  }

  public void update()
  {
    System.out.print("\033[H\033[2J");
    System.out.flush();

    /*
     * LETTERS
     */
    if(game.getInput() == Input.PILE ||
       game.getInput() == Input.DESTINATION_PILE)
    {
      StringBuilder letters = new StringBuilder();
      letters.append("  ")
            .append("A")
            .append("     ")
            .append("B")
            .append("     ")
            .append("C")
            .append("     ")
            .append("D");

      if(game.getInput() == Input.PILE)
      {
        letters.append("           ")
              .append("#")
              .append("     ")
              .append("+");
      }
      System.out.println(letters);
    }
    else
      System.out.println("");

    /*
     * DEPOSIT
     */
    StringBuilder depositFirst = new StringBuilder();
    StringBuilder depositSecond = new StringBuilder();
    StringBuilder depositThird = new StringBuilder();
    StringBuilder depositFourth = new StringBuilder();

    ArrayList<ArrayList<Card>> deposits = new ArrayList<>();
    deposits.add(depositSpade);
    deposits.add(depositHeart);
    deposits.add(depositClub);
    deposits.add(depositDiamond);

    for(ArrayList<Card> deposit : deposits)
    {
      if(deposit.isEmpty())
      {
        char symbol = '?';
        if(deposit == depositSpade)
          symbol = Suit.SPADE.getSymbol();
        else if(deposit == depositHeart)
          symbol = Suit.HEART.getSymbol();
        else if(deposit == depositClub)
          symbol = Suit.CLUB.getSymbol();
        else if(deposit == depositDiamond)
          symbol = Suit.DIAMOND.getSymbol();

        depositFirst.append(emptyCornerTopLeft)
                    .append(emptyHorizontal)
                    .append(emptyHorizontal)
                    .append(emptyHorizontal)
                    .append(emptyCornerTopRight);
        depositSecond.append(emptyVertical)
                     .append(" ")
                     .append(symbol)
                     .append(" ")
                     .append(emptyVertical);
        depositThird.append(emptyVertical)
                    .append("   ")
                    .append(emptyVertical);
        depositFourth.append(emptyCornerBottomLeft)
                    .append(emptyHorizontal)
                    .append(emptyHorizontal)
                    .append(emptyHorizontal)
                    .append(emptyCornerBottomRight);
      }
      else
      {
        // last card on top
        Card card = deposit.get(deposit.size() - 1);

        depositFirst.append(cardCornerTopLeft)
                    .append(cardHorizontal)
                    .append(cardHorizontal)
                    .append(cardHorizontal)
                    .append(cardCornerTopRight);
        
        depositSecond.append(cardVertical);
        if(card.getSuit().isRed())
          depositSecond.append(ansiRed);
        depositSecond.append(card.getNumber().getSymbol());
        if(card.getNumber().getSymbol().length() < 2)
          depositSecond.append(" ");
        depositSecond.append(card.getSuit().getSymbol());
        if(card.getSuit().isRed())
          depositSecond.append(ansiReset);
        depositSecond.append(cardVertical);

        depositThird.append(cardVertical);
        if(card.getSuit().isRed())
          depositThird.append(ansiRed);
        depositThird.append(card.getSuit().getSymbol());
        if(card.getNumber().getSymbol().length() < 2)
          depositThird.append(" ");
        depositThird.append(card.getNumber().getSymbol());
        if(card.getSuit().isRed())
          depositThird.append(ansiReset);
        depositThird.append(cardVertical);
        
        depositFourth.append(cardCornerBottomLeft)
                    .append(cardHorizontal)
                    .append(cardHorizontal)
                    .append(cardHorizontal)
                    .append(cardCornerBottomRight);
      }

      depositFirst.append(" ");
      depositSecond.append(" ");
      depositThird.append(" ");
      depositFourth.append(" ");
    }

    depositFirst.append("      ");
    depositSecond.append("      ");
    depositThird.append("      ");
    depositFourth.append("      ");

    ArrayList<ArrayList<Card>> draws = new ArrayList<>();
    draws.add(drawLeft);
    draws.add(drawRight);

    for(ArrayList<Card> draw : draws)
    {
      if(draw.isEmpty())
      {
        depositFirst.append(emptyCornerTopLeft)
                    .append(emptyHorizontal)
                    .append(emptyHorizontal)
                    .append(emptyHorizontal)
                    .append(emptyCornerTopRight);
        depositSecond.append(emptyVertical)
                    .append("   ")
                    .append(emptyVertical);
        depositThird.append(emptyVertical)
                    .append("   ")
                    .append(emptyVertical);
        depositFourth.append(emptyCornerBottomLeft)
                    .append(emptyHorizontal)
                    .append(emptyHorizontal)
                    .append(emptyHorizontal)
                    .append(emptyCornerBottomRight);
      }
      else
      {
        // last card on top
        Card card = draw.get(draw.size() - 1);

        depositFirst.append(cardCornerTopLeft)
                    .append(cardHorizontal)
                    .append(cardHorizontal)
                    .append(cardHorizontal)
                    .append(cardCornerTopRight);

        depositSecond.append(cardVertical);
        // only show left cards
        if(draw == drawLeft)
        {
          if(card.getSuit().isRed())
            depositSecond.append(ansiRed);
          depositSecond.append(card.getNumber().getSymbol());
          if(card.getNumber().getSymbol().length() < 2)
            depositSecond.append(" ");
          depositSecond.append(card.getSuit().getSymbol());
          if(card.getSuit().isRed())
            depositSecond.append(ansiReset);
        }
        else
          depositSecond.append("   ");
        depositSecond.append(cardVertical);

        depositThird.append(cardVertical);
        if(draw == drawLeft)
        {
          if(card.getSuit().isRed())
            depositThird.append(ansiRed);
          depositThird.append(card.getSuit().getSymbol());
          if(card.getNumber().getSymbol().length() < 2)
            depositThird.append(" ");
          depositThird.append(card.getNumber().getSymbol());
          if(card.getSuit().isRed())
            depositThird.append(ansiReset);
        }
        else
          depositThird.append("   ");
        depositThird.append(cardVertical);

        depositFourth.append(cardCornerBottomLeft)
                    .append(cardHorizontal)
                    .append(cardHorizontal)
                    .append(cardHorizontal)
                    .append(cardCornerBottomRight);
      }

      depositFirst.append(" ");
      depositSecond.append(" ");
      depositThird.append(" ");
      depositFourth.append(" ");
    }

    System.out.println(depositFirst);
    System.out.println(depositSecond);
    System.out.println(depositThird);
    System.out.println(depositFourth);

    /*
     * NUMBERS
     */
    if(game.getInput() == Input.PILE ||
       game.getInput() == Input.DESTINATION_PILE)
    {
      StringBuilder numbers = new StringBuilder();
      numbers.append("  ")
            .append("1")
            .append("     ")
            .append("2")
            .append("     ")
            .append("3")
            .append("     ")
            .append("4")
            .append("     ")
            .append("5")
            .append("     ")
            .append("6")
            .append("     ")
            .append("7");
      System.out.println(numbers);
    }
    else
      System.out.println("");

    /*
     * TABLE
     */
    ArrayList<StringBuilder> tableLines = new ArrayList<>();

    ArrayList<ArrayList<Card>> tables = new ArrayList<>();
    tables.add(tableOne);
    tables.add(tableTwo);
    tables.add(tableThree);
    tables.add(tableFour);
    tables.add(tableFive);
    tables.add(tableSix);
    tables.add(tableSeven);

    // calculate lines
    int maxCards = 0;

    for(ArrayList<Card> table : tables)
      if(table.size() > maxCards)
        maxCards = table.size();
    
    int maxLines = maxCards * 2 + 2;
    
    for(int i = 0; i < maxLines; i++)
      tableLines.add(new StringBuilder());
    
    int i = 0;
    for(StringBuilder line : tableLines)
    {
      for(ArrayList<Card> table : tables)
      {
        // card outline if first slot is empty
        if(Arrays.asList(new Integer[]{0, 1, 2, 3}).contains(i) && table.isEmpty())
        {
          if(i == 0)
          {
            line.append(emptyCornerTopLeft)
                .append(emptyHorizontal)
                .append(emptyHorizontal)
                .append(emptyHorizontal)
                .append(emptyCornerTopRight);
          }
          else if(i == 1 || i == 2)
          {
            line.append(emptyVertical)
                .append("   ")
                .append(emptyVertical);
          }
          else if(i == 3)
          {
            line.append(emptyCornerBottomLeft)
                .append(emptyHorizontal)
                .append(emptyHorizontal)
                .append(emptyHorizontal)
                .append(emptyCornerBottomRight);
          }
        }
        else if((table.size() * 2 + 2) <= i)
        {
          // no card at this position
          line.append("     ");
        }
        else
        {
          // last card bottom
          if((table.size() * 2 + 2) - i <= 2)
          {
            Card card = table.get((int)((i - 2) / 2));

            if(i == (table.size() * 2 + 2) - 2)
            {
              // first lower part
              if(card.isVisible())
              {
                line.append(cardVertical);
                if(card.getSuit().isRed())
                  line.append(ansiRed);
                line.append(card.getSuit().getSymbol());
                if(card.getNumber().getSymbol().length() < 2)
                  line.append(" ");
                line.append(card.getNumber().getSymbol());
                if(card.getSuit().isRed())
                  line.append(ansiReset);
                line.append(cardVertical);
              }
              else
              {
                line.append(cardVertical)
                    .append("   ")
                    .append(cardVertical);
              }
            }
            else
            {
              // second lower part
              line.append(cardCornerBottomLeft)
                  .append(cardHorizontal)
                  .append(cardHorizontal)
                  .append(cardHorizontal)
                  .append(cardCornerBottomRight);
            }
          }
          else
          {
            Card card = table.get((int) i / 2);

            if(i % 2 == 0)
            {
              // first upper part
              line.append(cardCornerTopLeft)
                  .append(cardHorizontal)
                  .append(cardHorizontal)
                  .append(cardHorizontal)
                  .append(cardCornerTopRight);
            }
            else
            {
              // second upper part
              if(card.isVisible())
              {
                line.append(cardVertical);
                if(card.getSuit().isRed())
                  line.append(ansiRed);
                line.append(card.getNumber().getSymbol());
                if(card.getNumber().getSymbol().length() < 2)
                  line.append(" ");
                line.append(card.getSuit().getSymbol());
                if(card.getSuit().isRed())
                  line.append(ansiReset);
                line.append(cardVertical);
              }
              else
              {
                line.append(cardVertical)
                    .append("   ")
                    .append(cardVertical);
              }
            }
          }
        }

        line.append(" ");
      }

      // row number after last pile
      if(game.getInput() == Input.ROW && i < maxLines - 2)
      {
        if(i % 2 == 1)
        {
          line.append(" ")
              .append(((int) i / 2) + 1);
        }
      }

      System.out.println(line);

      i++;
    }

  }
}