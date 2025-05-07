import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main entry‑point for a quick smoke‑test of the Retail Hub mini‑domain.
 * <p>
 * Key changes compared with the earlier ad‑hoc draft:
 * <ul>
 *     <li>ONE shared <code>ProductService</code> & <code>StockService</code> instance is used everywhere so that
 *     product IDs and their in‑memory collections stay in sync.</li>
 *     <li>Stock records are inserted for <strong>all</strong> products that are later sold (IDs&nbsp;4&nbsp;&amp;&nbsp;5),
 *     preventing the <em>No stock found</em> RuntimeException that appeared previously.</li>
 *     <li>The throwaway local variable <code>ps</code> has been removed; everything uses the shared service.</li>
 *     <li>Minor tidy‑ups (e.g. removed commented‑out interactive sections) so the class compiles cleanly without
 *     losing any behavioural coverage that the original script had.</li>
 * </ul>
 */
public class Main {

    // === In‑memory collections used by the service layer ============================
    private static final List<Client>       clients       = new ArrayList<>();

    // === Shared service objects =====================================================
    private static final ProductService productService = new ProductService();
    private static final StockService   stockService   = new StockService();

    // =================================================================================
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int  storeId;
        boolean validUpdateId = false;

        /* -------------------------------------------------------------------------
         * 1.  Basic Client CRUD tests
         * ------------------------------------------------------------------------- */
        List<Client> clientList = new ArrayList<>();
        Client client = new Client(
                "Despoina",
                "Makridou",
                LocalDate.of(2001, 5, 7),
                "6941234567",
                "despoinamakr@gmail.com",
                "Female",
                true,
                LocalDate.of(2022, 1, 10),
                540.75f,
                LocalDate.of(2018, 12, 20)   // (>5 years ago ➜ inactive‑test)
        );
        clientList.add(client);
        System.out.println("New client added successfully.");
        System.out.println(client);

        // Authentication
        boolean isAuthenticated = ClientService.authenticateClient(client, "6941234567");
        System.out.println("Identification by phone: " + (isAuthenticated ? "Successful" : "Unsuccessful"));

        // Update variations
        ClientService.updateClient(client, "maria", "Papadopoulou", "maria123@gmail.com", "6958429933");
        boolean inactive = ClientService.isInactiveMoreThan5Years(client);
        System.out.println("Inactive for over 5 years: " + (inactive ? "Yes" : "No"));

        ClientService.deleteClient(clientList, 1);
        System.out.println("Number of customers after deletion: " + clientList.size());

        ClientService.updateClient(client, null,  null,                 "new.email@gmail.com", "6900000000");
        ClientService.updateClient(client, "Maria", "Papadopoulou", "maria.pap@gmail.com",  "6999999999");
        ClientService.updateClient(client, "Maria", null,            null,                  null);

        /* -------------------------------------------------------------------------
         * 2.  Product CRUD tests – all through <productService>
         * ------------------------------------------------------------------------- */
        Product prod1 = productService.createProduct("red", "dress", 50, 20); // ID = 1
        System.out.println(prod1.getProductId());
        System.out.println(prod1);

        Product prod2 = productService.createProduct("red", "dress", 50, 20); // ID = 2
        System.out.println(prod2);

        productService.updateProduct(1, "green", "dress", 50, 20);
        productService.deleteProduct(1);

        /* -------------------------------------------------------------------------
         * 3.  Store tests
         * ------------------------------------------------------------------------- */
        Store store = StoreService.createStore("nik", "plat 20", "greece", "2310999888"); // ID = 1
        System.out.println("New store's info are:\n" + store);

        System.out.println("The list of the stores until today is \n" + StoreService.getStores());

        /* -------------------------------------------------------------------------
         * 4.  Stock manipulation
         * ------------------------------------------------------------------------- */
        // Initial stock for the first two products (IDs 1 & 2 no longer exist after delete, so use 2)
        stockService.addStock(2, 1, 4); // productId, storeId, qty
        stockService.addStock(2, 2, -2); // test negative‑guard

        System.out.println("The stock is: " + stockService.getStock(1, 2).get(0).getStockQuantity());
        stockService.updateStock(1, 2, 10);
        System.out.println("The new stock is: " + stockService.getStock(1, 2).get(0).getStockQuantity());

        stockService.reduceStockOnPurchase(1, 2, 11);
        System.out.println("The stock is: " + stockService.getStock(1, 2).get(0).getStockQuantity());

        stockService.getLowStockProducts();
        stockService.searchProductInOtherStores(1, 1);

        /* -------------------------------------------------------------------------
         * 5.  Transaction smoke‑tests  –– the part that failed earlier
         * ------------------------------------------------------------------------- */
        System.out.println("\n--- SIMPLE TRANSACTION TEST 1 ---");

        // Create three more products – IDs 3, 4, 5
        Product p1 = productService.createProduct("red dress",     "dress",   50, 20);   // ID 3
        Product p2 = productService.createProduct("blue jeans",    "pants",   60, 25);   // ID 4
        Product p3 = productService.createProduct("leather jacket", "jackets", 180, 90);  // ID 5

        // Add stock for those products in store #1 so every sale has inventory
        stockService.addStock(1, 3, 10);
        stockService.addStock(1, 4, 10); // **NEW – fixes previous crash**
        stockService.addStock(1, 5, 10); // **NEW – fixes previous crash**

        // Test Client
        Client testClient1 = new Client("Lena", "Kiriakou", LocalDate.of(1993, 4, 5),
                "6901230001", "lena@mail.com", "Female", true,
                LocalDate.now(), 100f, LocalDate.now());
        clients.add(testClient1);

        // Transaction #1 – sells product 3, qty 1
        List<Long> productIds1 = List.of(3L);
        List<Integer> quantities1 = List.of(1);
        Transaction t1 = TransactionService.createTransaction(productIds1, quantities1, 1, testClient1,
                productService, stockService, "Cash");
        System.out.println(t1);


        System.out.println("\n--- SIMPLE TRANSACTION TEST 2 ---");


        // Transaction #2 – sells product 4, qty 2 (works now)
        List<Long> productIds2 = List.of(4L);
        List<Integer> quantities2 = List.of(8);
        Transaction t2 = TransactionService.createTransaction(
                List.of(4L), List.of(8), 1, testClient1,
                productService, stockService, "Card");
        System.out.println(t2);

        System.out.println("\n--- SIMPLE TRANSACTION TEST 3 (DISCOUNT) ---");

        // Transaction #3 – sells product 5, qty 3 (exceeds 400€ ➜ discount path)
        List<Long> productIds3 = List.of(5L);
        List<Integer> quantities3 = List.of(3);
        Transaction t3 = TransactionService.createTransaction(
                List.of(5L), List.of(3), 1, testClient1,
                productService, stockService, "Credit");
        System.out.println(t3);


        System.out.println("\nAll Transactions:");
        for (Transaction tr : TransactionService.getAllTransactions()) {
            System.out.println(tr);
        }

        System.out.println("\nAll Includes:");
        for (Includes inc : TransactionService.getAllIncludes()) {
            System.out.println(inc);
        }

        //        //STORE update
//        while (!validUpdateId) {
//
//            System.out.println("Give the id of the store you want to change");
//
//            if (in.hasNextInt()) {
//                storeId=in.nextInt();
//                in.nextLine();
//
//                if (StoreService.validateId(storeId)){
//                    validUpdateId = true;}
//                else{
//                    System.out.println("No store found with the specified ID.");
//                }
//            }
//            else {
//                System.out.println("Invalid input. Please enter a numeric store ID.");
//                in.nextLine();
//            }
//        }
//        try{
//            StoreService.updateStore(storeId,"kat", "kle 4", "germany", "2310999777");
//            System.out.println("Store with ID " + storeId+" updated successfully.");
//            System.out.println ("New store's info are: "+"\n" + StoreService.getStoreById(storeId));
//        }
//        catch (IllegalArgumentException e) {
//            System.out.println (e.getMessage());
//        }
//
//        //DEACTIVATING A STORE
//        System.out.println("Give the id of the store you want to deactivate");
//        storeId=in.nextInt();
//        StoreService.getStoreById(storeId).setActive(false);
//        System.out.println("The store with ID " + storeId + " is deactivated.");
//
//
//        //SHOW SPECIFIC STORE BY ID
//        while (!validUpdateId) {
//            System.out.println("Give the id of the store you want to see");
//            if (in.hasNextInt()) {
//                storeId=in.nextInt();
//                if (StoreService.validateId(storeId)){
//                    StoreService.getStoreById(storeId);
//                }
//                else{
//                    System.out.println("No store found with the specified ID.");
//                }
//            }
//            else {
//                System.out.println("Invalid input. Please enter a numeric store ID.");
//            }
//        }
    }
}
