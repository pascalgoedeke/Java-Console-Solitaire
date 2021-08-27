import java.util.Scanner;

class Main
{
  private static Screen screen;
  private static Game game;

  // IDEAS: auto-sort after win, only show possible row numbers for corresponding pile

  public static void main(String[] args)
  {
    game = new Game();
    game.prepare();

    screen = new Screen(game);

    Scanner scanner = new Scanner(System.in);

    while(game.getStatus() == Status.PLAY)
    {
      screen.update();

      if(game.getInput() == Input.PILE ||
         game.getInput() == Input.ROW)
        System.out.println("Select Card");
      else if(game.getInput() == Input.DESTINATION_PILE)
        System.out.println("Select Destination");

      if(game.getInput() == Input.PILE ||
         game.getInput() == Input.DESTINATION_PILE)
        System.out.println("Enter Pile:");
      else if(game.getInput() == Input.ROW)
        System.out.println("Enter Row:");

      String input = scanner.nextLine().toUpperCase();
      game.action(input);

      game.checkWon();
    }

    if(game.getStatus() == Status.WON)
    {
      screen.update();
      System.out.println("You win");
    }

    scanner.close();
  }
}