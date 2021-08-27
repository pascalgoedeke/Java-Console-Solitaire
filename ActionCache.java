class ActionCache
{
  private char pile;
  private int row;

  public ActionCache(char pile)
  {
    this.pile = pile;
  }

  public char getPile()
  {
    return pile;
  }

  public int getRow()
  {
    return row;
  }

  public void setRow(int row)
  {
    this.row = row;
  }
}