class Transportation {
  private int row;
  private int column;
  private double quantity;
  private int cost;

  public Transportation copy() {
      return new Transportation(row, column, quantity, cost);
  }

  public Transportation(int row, int column, double quantity, int cost) {
      this.row = row;
      this.column = column;
      this.quantity = quantity;
      this.cost = cost;
  }

  public Transportation(int row, int column, double quantity) {
      this.row = row;
      this.column = column;
      this.quantity = quantity;
      this.cost = -1;
  }

  public Transportation(int row, int column) {
      this.row = row;
      this.column = column;
      this.quantity = Integer.MAX_VALUE;
      this.cost = -1;
  }

  public int positionRow() {
      return this.row;
  }

  public int positionColumn() {
      return this.column;
  }

  public double quantity() {
      return this.quantity;
  }

  public void setQuantity(double quantity) {
      this.quantity = quantity;
  }

  public int cost() {
      return this.cost;
  }

  public void setCost(int cost) {
      this.cost = cost;
  }

  public boolean __eq__(Transportation other) {
      return this.row == other.positionRow() && this.column == other.positionColumn() && this.quantity == other.quantity() && this.cost == other.cost();
  }

  public boolean __ne__(Transportation other) {
      return ! __eq__(other);
  }
}
