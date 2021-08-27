enum Suit
{
  CLUB('♣', false),
  DIAMOND('♦', true),
  HEART('♥', true),
  SPADE('♠', false);

  private final char symbol;
  private final boolean red;

  Suit(char symbol, boolean red)
  {
    this.symbol = symbol;
    this.red = red;
  }

  public char getSymbol()
  {
    return symbol;
  }

  public boolean isRed()
  {
    return red;
  }
}