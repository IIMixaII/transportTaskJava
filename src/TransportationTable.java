import java.lang.Math;
import java.lang.RuntimeException;
import java.util.ArrayList;

class TransportationTable {
  private double epsilon = 2.220446049250313e-16;
  private int maxsize = 0x7FFFFFFF;

  private int rowsAmount;
  private int columnsAmount;
  private ArrayList<Integer> producers;
  private int producersSum;
  private ArrayList<Integer> consumers;
  private int consumersSum;
  private ArrayList<ArrayList<Transportation>> transportations;

  public TransportationTable(int rowsAmount, int columnsAmount) {
      this.rowsAmount = rowsAmount;
      this.columnsAmount = columnsAmount;
      this.producers = new ArrayList<Integer>();
      this.producersSum = 0;
      this.consumers = new ArrayList<Integer>();
      this.consumersSum = 0;
      this.transportations = new ArrayList<ArrayList<Transportation>>();

      for (int i = 0; i < this.rowsAmount; i++) {
          ArrayList<Transportation> transportationsRow = new ArrayList<Transportation>();
          for (int j = 0; j < this.columnsAmount; j++)
              transportationsRow.add(new Transportation(i, j));
          this.transportations.add(transportationsRow);
      }
  }

  Transportation element(int index1, int index2) {
      return this.transportations.get(index1).get(index2);
  }

  int dimensionRows() {
      return this.rowsAmount;
  }

  int dimensionColumns() {
      return this.columnsAmount;
  }

  ArrayList<Integer> producers() {
      ArrayList<Integer> result = new ArrayList<Integer>();
      for (int i = 0; i < this.producers.size(); i++)
          result.add(this.producers.get(i));
      return result;
  }

  void setProducers(int[] producers) { // обновляет список производителей и их сумму
      if (producers.length == this.rowsAmount) {
          this.producers.clear();
          this.producersSum = 0;
          for (int i = 0; i < producers.length; i++) {
              this.producers.add(producers[i]);
              this.producersSum += producers[i];
          }
      } else
          System.out.println("Длина входного массива не совпадает с измерением таблицы");
  }

  ArrayList<Integer> consumers() {
      ArrayList<Integer> result = new ArrayList<Integer>();
      for (int i = 0; i < this.consumers.size(); i++)
          result.add(this.consumers.get(i));
      return result;
  }

  void setConsumers(int[] consumers) {
      if (consumers.length == this.columnsAmount) {
          this.consumers.clear();
          this.consumersSum = 0;
          for (int i = 0; i < consumers.length; i++) {
              this.consumers.add(consumers[i]);
              this.consumersSum += consumers[i];
          }
      } else
          System.out.println("Длина входного массива не совпадает с измерением таблицы");
  }

  ArrayList<ArrayList<Transportation>> transportations() { // Создаёт копию
      ArrayList<ArrayList<Transportation>> result = new ArrayList<ArrayList<Transportation>>();
      for (int i = 0; i < this.transportations.size(); i++) {
          ArrayList<Transportation> row = new ArrayList<Transportation>();
          for (int j = 0; j < this.transportations.get(i).size(); j++)
              row.add(this.transportations.get(i).get(j).copy());
          result.add(row);
      }
      return result;
  }

  boolean checkBalance() {
      return this.consumersSum == this.producersSum;
  }

  void balance() {
      if (!this.checkBalance()) {
          if (this.consumersSum > this.producersSum) {
              this.rowsAmount += 1;

              this.producers.add(this.consumersSum - this.producersSum);

              ArrayList<Transportation> transportationRow = new ArrayList<Transportation>(); // Создаётся новая строка, а там создаётся новый объект
              for (int j = 0; j < this.columnsAmount; j++)
                  transportationRow.add(new Transportation(this.rowsAmount - 1, j, Integer.MAX_VALUE, 0));
              this.transportations.add(transportationRow);
          } else {
              this.columnsAmount += 1;

              this.consumers.add(this.producersSum - this.consumersSum);

              for (int i = 0; i < this.rowsAmount; i++)
                  this.transportations.get(i).add(new Transportation(i, this.columnsAmount - 1, Integer.MAX_VALUE, 0));
          }
      }
  }

  void northWestCorner() {
      ArrayList<Integer> producersCopy = this.producers(); // Копирование
      ArrayList<Integer> consumersCopy = this.consumers();

      for (int i = 0; i < this.rowsAmount; i++) { // Цикл по строкам
          int j = 0; // Перебор потребителей с 0
          while (producersCopy.get(i) != 0) {
              int difference = producersCopy.get(i) - consumersCopy.get(j);

              if (difference >= 0) {
                  if (consumersCopy.get(j) != 0)
                      this.transportations.get(i).get(j).setQuantity(consumersCopy.get(j));
                  producersCopy.set(i, difference);
                  consumersCopy.set(j, 0);
              } else {
                  this.transportations.get(i).get(j).setQuantity(producersCopy.get(i));
                  consumersCopy.set(j, Math.abs(difference));
                  producersCopy.set(i, 0);
              }

              j++;
          }
      }
  }

  ArrayList<Transportation> transportationsToList() {
      ArrayList<Transportation> transportationsList = new ArrayList<Transportation>();
      for (int i = 0; i < this.rowsAmount; i++)
          for (int j = 0; j < this.columnsAmount; j++)
              if (this.transportations.get(i).get(j).quantity() != Integer.MAX_VALUE)
                  transportationsList.add(this.transportations.get(i).get(j).copy());
      return transportationsList;
  }

  ArrayList<Transportation> neighbors(Transportation currentTransportation,
                     ArrayList<Transportation> transportationsList) {
      ArrayList<Transportation> neighbours = new ArrayList<Transportation>();
      neighbours.add(new Transportation(-1, -1, Integer.MAX_VALUE));
      neighbours.add(new Transportation(-1, -1, Integer.MAX_VALUE)); // резервируем места
      for (Transportation transportation : transportationsList)
          if (transportation.__ne__(currentTransportation)) {
              if (neighbours.get(0).quantity() != Integer.MAX_VALUE && neighbours.get(1).quantity() != Integer.MAX_VALUE)
                  break;
              if (transportation.positionRow() == currentTransportation.positionRow() &&
                      neighbours.get(0).quantity() == Integer.MAX_VALUE)
                  neighbours.set(0, transportation.copy());
              else if (transportation.positionColumn() == currentTransportation.positionColumn() &&
                      neighbours.get(1).quantity() == Integer.MAX_VALUE)
                  neighbours.set(1, transportation.copy());
          }
      return neighbours;
  }

  ArrayList<Transportation> cycle(Transportation startTransportation) {
      ArrayList<Transportation> path = this.transportationsToList();
      path.add(0, startTransportation.copy());

      int previousLength;
      while (true) {
          previousLength = path.size();

          int i = 0;
          while (true) {
              if (i >= path.size())
                  break;
              ArrayList<Transportation> neighbors = this.neighbors(path.get(i), path);
              if (neighbors.get(0).quantity() == Integer.MAX_VALUE || neighbors.get(1).quantity() == Integer.MAX_VALUE) {
                  path.remove(i);
                  break;
              }
              i++;
          }
          if (previousLength == path.size() || path.size() == 0)
              break;
      }
      ArrayList<Transportation> cycle = new ArrayList<Transportation>();
      for (int i = 0; i < path.size(); i++)
          cycle.add(new Transportation(-1, -1, Integer.MAX_VALUE));
      Transportation previousTransportation = startTransportation.copy();
      for (int i = 0; i < cycle.size(); i++) {
          cycle.set(i, previousTransportation);
          previousTransportation = this.neighbors(previousTransportation, path).get(i % 2);
      }
      return cycle;
  }

  int countBasis() {
      int basisAmount = 0;
      for (int row = 0; row < this.transportations.size(); row++)
          for (int col = 0; col < this.transportations.get(row).size(); col++)
              if (this.transportations.get(row).get(col).quantity() != Integer.MAX_VALUE)
                  basisAmount++;
      return basisAmount;
  }

  boolean checkDegeneracy() {
      return this.rowsAmount + this.columnsAmount - 1 > this.countBasis();
  }

  void removeDegeneracy() {
      while (this.checkDegeneracy()) {
          boolean zeroAdd = false;
          for (int i = 0; i < this.rowsAmount; i++) {
              for (int j = 0; j < this.columnsAmount; j++)
                  if (this.transportations.get(i).get(j).quantity() == Integer.MAX_VALUE) {
                      Transportation zero = new Transportation(i, j, epsilon, this.transportations.get(i).get(j).cost());
                      if (this.cycle(zero).size() == 0) {
                          this.transportations.get(i).set(j, zero);
                          zeroAdd = true;
                          break;
                      }
                  }
              if (zeroAdd)
                  break;
          }
      }
  }

  boolean potentialMethod() {
      int maxReduction = 0;
      ArrayList<Transportation> move = new ArrayList<Transportation>();

      Transportation leaving = null;
      boolean isNull = true;

      this.removeDegeneracy();

      for (int i = 0; i < this.rowsAmount; i++)
          for (int j = 0; j < this.columnsAmount; j++) {
              if (this.transportations.get(i).get(j).quantity() != Integer.MAX_VALUE)
                  continue;

              Transportation trial = new Transportation(i, j, 0, this.transportations.get(i).get(j).cost());
              ArrayList<Transportation> cycle = this.cycle(trial);

              int reduction = 0; // используется для подсчета уменьшения стоимости при перераспределении товаров в рамках цикла
              double lowestQuantity = maxsize; // используется для поиска транспортации с наименьшим количеством товаров в цикле
              Transportation leavingCandidate = null;

              boolean plus = true;
              for (Transportation transportation : cycle) {
                  if (plus)
                      reduction += transportation.cost();
                  else {
                      reduction -= transportation.cost();
                      if (transportation.quantity() < lowestQuantity) {
                          leavingCandidate = transportation.copy();
                          lowestQuantity = transportation.quantity();
                      }
                  }
                  plus = !plus;
              }
              if (reduction < maxReduction) {
                  isNull = false;
                  move = cycle;
                  leaving = leavingCandidate;
                  maxReduction = reduction;
              }
          }

      if (!isNull) {
          double quantity = leaving.quantity();
          boolean plus = true;
          for (Transportation transportation : move) {
              if (plus)
                 transportation.setQuantity(transportation.quantity() + quantity);
              else
                 transportation.setQuantity(transportation.quantity() - quantity);
              if (transportation.quantity() == 0)
                  this.transportations.get(transportation.positionRow()).set(transportation.positionColumn(),
                      new Transportation(transportation.positionRow(), transportation.positionColumn(), Integer.MAX_VALUE,
                                     this.transportations.get(transportation.positionRow()).get(transportation.positionColumn()).cost()));
              else
                  this.transportations.get(transportation.positionRow()).set(transportation.positionColumn(), transportation);
              plus = !plus;
          }
          return false;
      }
      return true;
  }

  int calculateTransportationsCost() {
      int transportationsCost = 0;
      for (ArrayList<Transportation> row : this.transportations)
          for (Transportation transportation : row)
              if (transportation.quantity() != Integer.MAX_VALUE)
                  transportationsCost += transportation.cost() * transportation.quantity();
      return transportationsCost;
  }

}
