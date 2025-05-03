import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
 
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner (System.in);
        int storeId=0;
        boolean validUpdateId=false;
        List<Client> clientList = new ArrayList<>();
 
        // 1. Δημιουργία νέου πελάτη μέσω ClientService
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
                LocalDate.of(2018, 12, 20) // για παράδειγμα >5 έτη πριν
        );
        clientList.add(client);
 
        // 2. Ταυτοποίηση πελάτη
        boolean isAuthenticated = ClientService.authenticateClient(client, "6941234567");
        System.out.println("Identification by phone: " + (isAuthenticated ? "Successful" : "Unsuccessful"));
 
        // 3. Ενημέρωση στοιχείων
        ClientService.updateClient(client,"maria","Papadopoulou","maria123@gmail.com", "6958429933" );
 
        // 4. Έλεγχος αν είναι ανενεργός πάνω από 5 χρόνια
        boolean isInactive = ClientService.isInactiveMoreThan5Years(client);
        System.out.println("Inactive for over 5 years: " + (isInactive ? "Yes" : "No"));
 
        // 5. Διαγραφή πελάτη
        ClientService.deleteClient(clientList, 1);
        System.out.println("Number of customers after deletion: " + clientList.size());
 
        // Ενημέρωση email και τηλεφώνου μόνο
        ClientService.updateClient(client, null, null, "new.email@gmail.com", "6900000000");
 
       // Ενημέρωση όλων
        ClientService.updateClient(client, "Maria", "Papadopoulou", "maria.pap@gmail.com", "6999999999");
 
       // Ενημέρωση μόνο του ονόματος
        ClientService.updateClient(client, "Maria", null, null, null);
    
       // Reset του nextId πριν από κάθε εκτέλεση του προγράμματος για να επαναφέρουμε το id σε 1
        Product.resetId(); 

        // Δημιουργία νέου προϊόντος με το Id να δημιουργείται αυτόματα
        Product product = new Product("red","dress", 50, 20);
        System.out.println(product.getProductId());
        System.out.println(product);
    
           
        // Ενημέρωση στοιχείων προϊόντος
        ProductService ps = new ProductService();
        Product newProduct = ps.createProduct("red","dress", 50, 20);
        System.out.println(newProduct);
        ps.updateProduct(1,"green","dress", 50, 20);

        // Διαγραφή του προϊόντος που δημιουργήσαμε
        ps.deleteProduct(1);
   
        //STORE create
        try{
            Store newStore=StoreService.createStore("nik", "plat 20", "greece", "2310999888");
            System.out.println ("New store's info are: "+"\n" + (newStore));
        } 
        catch (IllegalArgumentException e){
            System.out.println (e.getMessage());
        }
  
        //STORE update
        while (!validUpdateId) {
           
            System.out.println("Give the id of the store you want to change");
           
            if (in.hasNextInt()) {
                storeId=in.nextInt();
                in.nextLine();
 
                if (StoreService.validateId(storeId)){
                    validUpdateId = true;}
                else{
                    System.out.println("No store found with the specified ID.");
                }
            }
            else {
                System.out.println("Invalid input. Please enter a numeric store ID.");
                in.nextLine();
            }
        }
        try{
            StoreService.updateStore(storeId,"kat", "kle 4", "germany", "2310999777");  
            System.out.println("Store with ID " + storeId+" updated successfully.");
            System.out.println ("New store's info are: "+"\n" + StoreService.getStoreById(storeId));    
        }
        catch (IllegalArgumentException e) {
            System.out.println (e.getMessage());
        }
        
        //DEACTIVATING A STORE
        System.out.println("Give the id of the store you want to deactivate");
        storeId=in.nextInt();
        StoreService.getStoreById(storeId).setActive(false);
        System.out.println("The store with ID " + storeId + " is deactivated.");
        
        //ALL STORE LIST
        System.out.println("The list of the stores until today is \n" + StoreService.getStores());
 
        //SHOW SPECIFIC STORE BY ID
        while (!validUpdateId) {
            System.out.println("Give the id of the store you want to see");
            if (in.hasNextInt()) {
                storeId=in.nextInt();
                if (StoreService.validateId(storeId)){
                    StoreService.getStoreById(storeId);
                }  
                else{
                    System.out.println("No store found with the specified ID.");
                    }
            }
            else {
                System.out.println("Invalid input. Please enter a numeric store ID.");
            }
        }


        // Δημιουργία αντικειμένου κλάσης StockService
        StockService stockService = new StockService();
            
        // Δημιουργία νέου αποθέματος
        stockService.addStock(0001, 0001, 4);
        stockService.addStock(0002, 0001, -2); 
        
        // Εμφάνιση αποθέματος για συγκεκριμένο προιόν και κατάστημα 
        System.out.println("The stock is: " + stockService.getStock( 0001,0001).get(0).getStockQuantity());
        
        // Ενημέρωση ποσότητας αποθεμάτος 
        stockService.updateStock(0001,0001,10);
        System.out.println("The new stock is: " + stockService.getStock( 0001,0001).get(0).getStockQuantity());
        
        // Ενημέρωση αποθέματος έπειτα από αγορά 
        stockService.reduceStockOnPurchase(0001, 0001, 7);
        System.out.println("The stock is: " + stockService.getStock( 0001,0001).get(0).getStockQuantity());
        
        // Εμφάνιση προιόντων που έχουν χαμηλή ποσότητα αποθέματος ( <=3 )
        stockService.getLowStockProducts();
        
        // Εμφάνιση  διαθέσιμων προιόντων σε άλλα καταστήματα
        stockService.searchProductInOtherStores(0001, 0002);

        System.out.println("\n--- SIMPLE TRANSACTION TEST 1 ---");
 
        Client testClient1 = new Client(10, "Lena", "Kiriakou", LocalDate.of(1993, 4, 5), "6901230001", "lena@mail.com", "Γυναίκα", true, LocalDate.now(), 100f, LocalDate.now());
        clients.add(testClient1);
        
        List<Long> productIds1 = List.of((long) 1); // existing product ID
        List<Integer> quantities1 = List.of(1);
        
        TransactionService.createTransaction(
            productIds1,
            quantities1,
            1, // existing store ID
            testClient1,
            productService,
            stockService,
            includesList,
            transactions,
            "Cash"
        );
        
        System.out.println(transactions.get(transactions.size() - 1));

        
        
        System.out.println("\n--- SIMPLE TRANSACTION TEST 2 ---");
        
        List<Long> productIds2 = List.of((long) 2); // another product ID
        List<Integer> quantities2 = List.of(2);
        
        TransactionService.createTransaction(
            productIds2,
            quantities2,
            1,
            testClient1,
            productService,
            stockService,
            includesList,
            transactions,
            "Card"
        );
        
        System.out.println(transactions.get(transactions.size() - 1));

        
        
        System.out.println("\n--- SIMPLE TRANSACTION TEST 3 (DISCOUNT) ---");
        
        List<Long> productIds3 = List.of((long) 3); // higher value product ID
        List<Integer> quantities3 = List.of(3);     // enough to exceed total sum > 400
        
        TransactionService.createTransaction(
            productIds3,
            quantities3,
            1,
            testClient1,
            productService,
            stockService,
            includesList,
            transactions,
            "Credit"
        );
        
        System.out.println(transactions.get(transactions.size() - 1));
   
    }
}
