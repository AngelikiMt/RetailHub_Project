
import java.util.*;

public class Main {

    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n========= Retail Hub Menu =========");
            System.out.println("1. CLIENT");
            System.out.println("2. PRODUCT");
            System.out.println("3. STORE (PIN)");
            System.out.println("4. STOCK");
            System.out.println("5. TRANSACTION");
            System.out.println("0. EXIT");
            System.out.print("CHOOSE: ");
            String choice = in.nextLine();

            switch (choice) {
                case "1": TestData.runClientTests(); break;
                case "2": TestData.runProductTests(); break;
                case "3": if (TestData.checkPIN()) {TestData.runStoreTests();} break;
                case "4": TestData.runStockTests(); break;
                case "5": TestData.runSimpleTransactionMenu(); break;
                case "0": running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }
}



