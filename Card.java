class Card
{
  private final Suit suit;
  private final Number number;

  private boolean visible;

  public Card(Suit suit, Number number)
  {
    this.suit = suit;
    this.number = number;
    this.visible = false;
  }

  public Suit getSuit()
  {
    return this.suit;
  }

  public Number getNumber()
  {
    return this.number;
  }

  public boolean isVisible()
  {
    return this.visible;
  }

  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }
}