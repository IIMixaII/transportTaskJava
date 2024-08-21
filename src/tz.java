import java.util.ArrayList;

class tz {
  public static void main(String[] args) {


    int m = 3;
    int n = 5;


    int[] A = new int[] { 200, 150, 350 };


    int[] B = new int[] { 120, 120, 200, 180, 110 };


    int[][] C = new int[][] {new int[] {1, 2, 3, 5, 2}, new int[] {4, 6, 7, 3, 1}, new int[] {2, 2, 3, 4, 5}};

    System.out.println("������ �������: " + String.valueOf(m) + " x " + String.valueOf(n));
    System.out.println("����������� ��������������: " + toString(A));
    System.out.println("����� ������������: " + toString(B));
    System.out.println("������ ���������:");
    printMatrix(C);
    System.out.println();

    TransportationTable transport = new TransportationTable(m, n);
    transport.setProducers(A);
    transport.setConsumers(B);
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            transport.element(i, j).setCost(C[i][j]);

    if (!transport.checkBalance()) {
        System.out.println("������ �� �������");
        transport.balance();
        System.out.println("�������� ������:");
        System.out.println("������ �������: " + String.valueOf(transport.dimensionRows()) + " x " + String.valueOf(transport.dimensionColumns()));
        System.out.println("����������� ��������������: " + toString(transport.producers()));
        System.out.println("����� ������������: " + toString(transport.consumers()));
        System.out.println("������ ���������:");
        printTransportationsCost(transport);
    } else
        System.out.println("������ �������");
    System.out.println();

    transport.northWestCorner();
    System.out.println("������� ����, ���������� ������� ������-��������� ����:");
    printTransportationsQuantity(transport);
    System.out.println("��������� ��������� �������� �����: " + String.valueOf(transport.calculateTransportationsCost()));
    System.out.println();

    if (transport.checkDegeneracy()) {
        System.out.println("���������� ���� - �����������");
        transport.removeDegeneracy();
        System.out.println("����� ������� ����:");
        printTransportationsQuantity(transport);
        System.out.println("��������� ��������� �������� �����: " + String.valueOf(transport.calculateTransportationsCost()));
    } else
        System.out.println("���������� ���� �� �������� �����������");
    System.out.println();

    int count = 0;
    while (!transport.potentialMethod()) {
        System.out.println("����������� ����� ������� �����������, �������� �" + String.valueOf(count + 1) + ":");
        printTransportationsQuantity(transport);
        System.out.println("��������� ��������� �������� �����: " + String.valueOf(transport.calculateTransportationsCost()));
        count++;
        System.out.println();
    }
    System.out.println("����������� ����, ���������� ������� �����������: ");
    printTransportationsQuantity(transport);
    System.out.println("��������� ��������� �������� �����: " + String.valueOf(transport.calculateTransportationsCost()));

  }

  public static String toString(int[] arr) {
    String result = "";
    for (int a : arr)
        result += String.valueOf(a) + " ";
    return result;
  }

  public static String toString(ArrayList<Integer> arr) {
    String result = "";
    for (int a : arr)
        result += String.valueOf(a) + " ";
    return result;
  }

  public static void printMatrix(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[i].length; j++)
            System.out.print(String.valueOf(matrix[i][j]) + " ");
        System.out.println();
    }
  }

  public static void printTransportationsCost(TransportationTable table) {
    ArrayList<ArrayList<Transportation>> T = table.transportations();
    for (ArrayList<Transportation> row : T) { // ������ �� ��������� ���������
        for (Transportation transportation : row) // ������ �� ��������� ������
            System.out.print(String.valueOf(transportation.cost()) + " ");
        System.out.println();
    }
  }

  public static void printTransportationsQuantity(TransportationTable table) {
    ArrayList<ArrayList<Transportation>> T = table.transportations(); // ��������� ������
    for (ArrayList<Transportation> row : T) {
        for (Transportation transportation : row)
            if (transportation.quantity() != Integer.MAX_VALUE) // �������������
                System.out.print(Math.round(transportation.quantity()) + " ");
            else
                System.out.print("- ");
        System.out.println();
    }
  }
}
