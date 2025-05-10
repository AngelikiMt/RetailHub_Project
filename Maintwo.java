
    import java.util.*;
    
    public class Maintwo {
    
        private static final Scanner in = new Scanner(System.in);
    
        public static void main(String[] args) {
            boolean running = true;
    
            // Αρχικά δεδομένα (από το παλιό script)
            //TestData.setupInitialData();
    
            while (running) {
                System.out.println("\n========= Retail Hub Menu =========");
                System.out.println("1. CLIENT");
                System.out.println("2. PRODUCT");
                System.out.println("3. STORE");
                System.out.println("4. Διαχείριση Καταστήματος (μόνο με PIN)");
                System.out.println("5. Stock");
                System.out.println("6. Transaction");
                System.out.println("0. Έξοδος");
                System.out.print("Επιλογή: ");
                String choice = in.nextLine();
    
                switch (choice) {
                    case "1": TestData.runClientTests(); break;
                    case "2": TestData.runProductTests(); break;
                    case "3": TestData.createStore(); break;
                    case "4": TestData.manageStore(); break;
                    case "5": TestData.runStockTests(); break;
                    //case "6": TestData.runTransactionTests(); break;
                    case "0": running = false; break;
                    default: System.out.println("Μη έγκυρη επιλογή.");
                }
            }
        }
    }
       
    

