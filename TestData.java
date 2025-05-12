import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

    public class TestData {
        private static final Scanner in = new Scanner(System.in);
    
        private static final List<Client> clients = new ArrayList<>();
        private static final List<Includes> includesList = new ArrayList<>();
        private static final List<Transaction> transactions = new ArrayList<>();
        
    
        public static final ProductService productService = new ProductService();
        public static final StockService stockService = new StockService();

        private static final String ADMIN_PIN = "12345";
        private static long clientId;
    
     // ======================================================== CLIENT TEST ===================================================================

        public static void runClientTests() {
            boolean running = true;
            while (running) {
                System.out.println("\n========= Retail Hub CLIENT Menu =========");
                System.out.println("1.CREATE CLIENT");
                System.out.println("2.AUTHENTICATE CLIENT");
                System.out.println("3.UPDATE CLIENT");
                System.out.println("4.DELETE CLIENT");
                System.out.println("5.GET CLIENT AS JSON");
                System.out.println("0.Exit");
                System.out.print("CHOOSE: ");
                String choice = in.nextLine();
               
            
                switch (choice) {
                        case "1": menuCreateClient();break;
                        case "2": menuAuthenticateClient();break;
                        case "3": menuUpdateClient(); break;
                        case "4": menuDeleteClient(); break;
                        case "5": menuGetJson(); break;
                        case "0": running = false; break;
                        default: System.out.println("Invalid option.");
                }
            }
        }
        // CCREATE NEW CLIENT 
        public static void menuCreateClient(){
            long clientId;
            Client client; 
            String firstName;
            String lastName;
            LocalDate birthDate=null;
            String phoneNumber;
            String email;
            String gender;
            boolean activeStatus=true;
            LocalDate dateJoined;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            System.out.println("First Name: ");
            firstName= in.nextLine();
            System.out.println("Last Name: ");
            lastName= in.nextLine();
            System.out.println("Phone Number: ");
            phoneNumber= in.nextLine();
            System.out.println("Email: ");
            email= in.nextLine();
            System.out.println("Gender:FEMALE/MALE/OTHER (Press enter to skip): ");
            gender = in.nextLine().toUpperCase();
            if (gender.isEmpty() || 
            !(gender.equals("MALE") || gender.equals("FEMALE") || gender.equals("OTHER"))) {
                gender = "OTHER"; 
            }
            System.out.println("Birthdate: dd/MM/yyyy (Press enter to skip): ");
            String birthDateInput = in.nextLine();
            if (!birthDateInput.isEmpty()) {
                birthDate = LocalDate.parse(birthDateInput, formatter);
            }else{
                birthDate=null;
            }

            try{
            client = ClientService.createClient(firstName, lastName, birthDate, phoneNumber, email, gender, activeStatus);
            clients.add(client);
            System.out.println("Client was successfully created with ID: " + client.getClientId());
            }
            catch(Exception x ){
                System.out.println(x.getMessage());
            }
        }
        // AUTHENTICATE CLIENT
        public static void menuAuthenticateClient(){
            String input;
            System.out.println("Email or Phone number of the client: ");
            input= in.nextLine();
            clientId= ClientService.authenticateClient1(clients,input);
        }

        //UPDATE CLIENT
        public static void menuUpdateClient(){
            menuAuthenticateClient();
            try {
                if (clientId != -1){
                    System.out.println("New first Name: ");
                    String newFirstName= in.nextLine();
                    System.out.println("New last Name: ");
                    String newLasttName= in.nextLine();
                    System.out.println("New Email: ");
                    String newEmail= in.nextLine();
                    System.out.println("New phone number: ");
                    String newPhoneNumber= in.nextLine();

                    for (Client client : clients) {
                        if (client.getClientId()== clientId){
                            ClientService.updateClient(client, newFirstName, newLasttName, newEmail, newPhoneNumber);
                        }
                    }
                    System.out.println("Client successfully updated.");
                }
                
            } 
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        //DELETE CLIENT 
        public static void menuDeleteClient(){
            System.out.println("Validation : Clients are inactive more than 5 years(YES/NO): ");
            String answer = in.nextLine();
            if(answer.equals("NO")){ 
                try {
                    menuAuthenticateClient();
                    if (clientId != -1){
                        ClientService.deleteClient(clients,clientId );
                        System.out.println("Client successfully deleted.");    
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            else if(answer.equals("YES")) {
                for (long inactiveId : ClientService.isInactiveMoreThan5Years(clients)) {
                    ClientService.deleteClient(clients,inactiveId);
                }       
            }  
        }

        //GET JSON 
        public static void menuGetJson() {
            List<String> jsonList = new ArrayList<>();

            for (Client client : clients) {
                String jSon = ClientService.getClientAsJson(client);
                jsonList.add(jSon);
            }

            for (String json : jsonList) {
                System.out.println(json);
            }
        }

    // =============================================== PRODUCT TEST================================================================================

        public static void runProductTests() {
            boolean running = true;
            while (running) {
                System.out.println("\n========= Retail Hub PRODUCT Menu =========");
                System.out.println("1.CREATE PRODUCT");
                System.out.println("2.UPDATE PRODUCT");
                System.out.println("3.DELETE PRODUCT");
                System.out.println("4.DISPLAY ALL PRODUCTS");
                System.out.println("5.SEARCH PRODUCT BY ID");
                System.out.println("6.GET PRODUCT AS Json");
                System.out.println("0.EXIT");
                System.out.print("CHOOSE: ");
                String choice = in.nextLine();

                long productId;
                switch (choice) {
                                    
                    case "1": //CREATE PRODUCT
                        System.out.println("Give product description");
                        String description=in.nextLine();
                        System.out.println("Give product category");
                        String category=in.nextLine();
                        System.out.println("Give product price");
                        double price=in.nextDouble();
                        in.nextLine();
                        System.out.println("Give product cost");
                        double cost=in.nextDouble();
                        in.nextLine();
                        productService.createProduct(description,category,price,cost); 
                    break;

                    case "2": //UPDATE PRODUCT
                        System.out.println("Give the id of the product you want to change"); 
                        productId = in.nextLong();
                        in.nextLine();
                        System.out.println("Give product new description");
                        String newDescription=in.nextLine();
                        System.out.println("Give product new category");
                        String newCategory=in.nextLine();
                        System.out.println("Give product new price");
                        double newPrice=in.nextDouble();
                        in.nextLine();
                        System.out.println("Give product new cost");
                        double newCost=in.nextDouble();
                        in.nextLine();
                        productService.updateProduct(productId,newDescription,newCategory,newPrice,newCost);
                    break;

                    case "3": //DELETE PRODUCT
                        System.out.println("Give the id of the product you want to delete"); 
                        productId = in.nextLong();
                        in.nextLine();
                        productService.deleteProduct(productId);
                    break;

                    case "4": productService.displayAllProducts (); break;

                    case "5":
                        System.out.println("Give the id of the product you want to search");
                        productId = in.nextLong();
                        in.nextLine();
                        System.out.println(productService.findProductById(productId));
                    break;

                    case "6":
                        System.out.println("Give the id of the product you want in json");
                        productId = in.nextLong();
                        in.nextLine();
                        System.out.println(ProductService.getProductAsJson(productService.findProductById(productId)));
                    break;

                    case "0": running=false; 
                    default: System.out.println("Not valid option.");
                    break;
                }
            }
        }

    //====================================================== STORE TEST ==========================================================================    
           public static boolean checkPIN() {
            System.out.print("Please enter your PIN: ");
            String pin = in.nextLine();
            if (!pin.equals(ADMIN_PIN)) {
                System.out.println("The PIN you entered is invalid.");
                return false;
            }
            else { return true; }
        } 

        public static void runStoreTests() {
            boolean running = true;
            while (running) {
                System.out.println("\n========= Retail Hub STORE Menu =========");
                System.out.println("1.CREATE STORE");
                System.out.println("2.UPDATE STORE");
                System.out.println("3.DEACTIVATE STORE");
                System.out.println("4.SHOW SPECIFIC STORE");
                System.out.println("5.SHOW ALL STORES");
                System.out.println("6.GET STORE AS Json");
                System.out.println("0.EXIT");
                System.out.print("CHOOSE: ");
                String choice = in.nextLine();
            
                int storeId;
                switch (choice) {

                    case "1": //CREATE STORE WITH ALL FIELDS NECESSARY & VALIDATION
                        System.out.println("Give store name");
                        String storeName=in.nextLine();
                        System.out.println("Give store address");
                        String address=in.nextLine();
                        System.out.println("Give store country");
                        String country=in.nextLine();
                        System.out.println("Give store phone number");
                        String phone=in.nextLine();
                        try{    
                            Store store = StoreService.createStore(storeName, address, country, phone);
                            System.out.println("New store's info are:\n" + store);
                        }   
                        catch (IllegalArgumentException e){
                            System.out.println (e.getMessage()); 
                        }
                    break;

                    case "2": //UPDATE STORE WITH OPTIONAL FIELDS
                        System.out.println("Give the id of the store you want to change");
                        if (in.hasNextInt()) {
                            storeId=in.nextInt();
                            in.nextLine();
                                if (StoreService.validateId(storeId)){
                                    System.out.println("Give new store name");
                                    String newName=in.nextLine();
                                    System.out.println("Give new store address");
                                    String newAddress=in.nextLine();
                                    System.out.println("Give new store country");
                                    String newCountry=in.nextLine();
                                    System.out.println("Give new store number");
                                    String newPhone=in.nextLine(); 
                                    StoreService.updateStore(storeId,newName,newAddress,newCountry,newPhone);   
                                    System.out.println("Store with ID " + storeId+" updated successfully.");
                                    System.out.println ("New store's info are: "+"\n" + StoreService.getStoreById(storeId));
                                }
                        }                    
                        else {
                            System.out.println("Invalid input. Please enter a numeric store ID.");
                            in.nextLine();
                            }
                    break;

                    case "3": //DEACTIVATE STORE
                        System.out.println("Give the id of the store you want to deactivate");
                        storeId=in.nextInt();
                        in.nextLine();
                        if (StoreService.validateId(storeId)){
                            StoreService.getStoreById(storeId).setActive(false);
                            System.out.println("The store with ID " + storeId + " is deactivated.");
                        }                  
                    break;

                    case "4": //SHOW SPECIFIC STORE
                        System.out.println("Give the id of the store you want to see");
                        storeId=in.nextInt();
                        in.nextLine();
                        if (StoreService.validateId(storeId)){
                            System.out.println(StoreService.getStoreById(storeId));
                        }
                    break;

                    case "5": System.out.println(StoreService.getStores()); break;

                    case "6":
                        System.out.println("Give the id of the store you want as json");
                        storeId = in.nextInt();
                        in.nextLine();
                        System.out.println(StoreService.getStoreAsJson(StoreService.getStoreById(storeId)));
                    break;

                    case "0": //EXIT
                        running= false; 
                        default: System.out.println("Not valid option.");
                    break;
                }
            }
        }

    // ================================================= STOCK TEST ===============================================================================
    
        public static void runStockTests() {

            boolean running = true;
            while (running) {
                System.out.println("\n========= Retail Hub STOCK Menu =========");
                System.out.println("1.ADD STOCK");
                System.out.println("2.GET STOCK FOR SPECIFIC PRODUCT AND STORE");
                System.out.println("3.UPDATE STOCK");
                System.out.println("4.GET LOW STOCK ( BELLOW 3) ");
                System.out.println("5.SEARCH STOCK IN OTHER STORE");
                System.out.println("6.GET STOCK AS JSON ");
                System.out.println("0. Exit");
                System.out.print("CHOOSE: ");
                String choice = in.nextLine();
               
            
                switch (choice) {
                    case "1": // ADD STOCK 
                        try {
                            System.out.print("Add Stock for product ID: ");
                            long productId = Long.parseLong(in.nextLine());

                            System.out.print("Store ID: ");
                            int storeId = Integer.parseInt(in.nextLine());

                            System.out.print("Quantity of the product: ");
                            int stockQuantity = Integer.parseInt(in.nextLine());

                            StockService.addStock(storeId, productId, stockQuantity);
                        } 
                        catch (NumberFormatException e) {
                            System.out.println("Invalid number input. Please enter valid numeric values.");
                        }
                    break;

                    case "2"://GET STOCK FOR SPECIFIC PRODUCT AND STORE
                        System.out.println("Stock for Product: ");
                        long productId =  in.nextLong();
                        in.nextLine();

                        System.out.println("Stock in Store: ");
                        int storeId = in.nextInt();
                        in.nextLine();

                        System.out.println("Stock: " + StockService.getStock(productId, storeId).get(0).getStockQuantity());
                    break;

                    case "3": //UPDATE STOCK
                        System.out.println("Update stock for product Id:");
                        long product1Id=in.nextLong();
                        in.nextLine();

                        System.out.println("Update stock in store Id: ");
                        int store1Id =  in.nextInt();
                        in.nextLine();

                        System.out.println("New quantity: ");
                        int newQuantity = in.nextInt();
                        in.nextLine();

                        StockService.updateStock(product1Id, store1Id,newQuantity);
                        System.out.println("Update Stock: " + StockService.getStock(product1Id, store1Id).get(0).getStockQuantity());
                    break;

                    case "4": //GET LOW STOCK ( BELOW 3)
                        StockService.getLowStockProducts();
                    break;

                    case "5": //SEARCH STOCK IN OTHER STORE
                        System.out.println("Stock for product Id:");
                        long product2Id =  in.nextLong();
                        in.nextLine();

                        System.out.println("Provide the store Id which has no stock: ");
                        int excludedStoreId = in.nextInt();
                        in.nextLine();
                        
                        StockService.searchProductInOtherStores(product2Id, excludedStoreId);
                    break;

                    case "6": // GET STOCK AS JSON 
                        try {
                            System.out.print("Enter product ID to export stock as JSON: ");
                            long jsonProductId = Long.parseLong(in.nextLine());
                            String jsonOutput = StockService.getStockAsJson(jsonProductId);
                            System.out.println("\nGenerated JSON:\n" + jsonOutput);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid product ID format.");
                        }
                    break;

                    case "0": running = false; break;
                    default: System.out.println("Invalid option.");
                }
            }
            
        }

    // ==============================================TRANSACTION TEST ============================================================================

        public static void runSimpleTransactionMenu() {
            Scanner in = new Scanner(System.in);
            boolean running = true;
            
            while (running) {
                    try {
                        System.out.println("\n===== Retail Hub TRANSACTION Menu ====");
                        System.out.println("1.Create Transaction");
                        System.out.println("2.View all Transactions");
                        System.out.println("3.Transaction in JSON");
                        System.out.println("0. Exit");
                        System.out.print("CHOOSE: ");
                        String choice = in.nextLine().trim();
            
                        switch (choice) {
                            //Ζητά από τον χρήστη email ή τηλέφωνο πελάτη.
                            //Κάνει αναγνώριση του πελάτη με ClientService.authenticateClient()
                            //Αν βρεθεί, ζητά:ID καταστήματος,ID προϊόντος,Ποσότητα,Τρόπο πληρωμή
                            //Δημιουργεί νέα συναλλαγή και την προσθέτει στη λίστα
                            case "1" :  
                                System.out.println("give us email or phone number");
                                String input = in.nextLine();
                                Client c= ClientService.authenticateClient(clients,input);
                
                                System.out.print("Store ID: ");
                                int storeId = Integer.parseInt(in.nextLine());
                
                                System.out.print("Product ID: ");
                                long productId = Long.parseLong(in.nextLine());
                
                                System.out.print("Quontity: ");
                                int quantity = Integer.parseInt(in.nextLine());
                
                                System.out.print("Payment Method (Cash/Card/Credit): ");
                                String payment = in.nextLine();
                
                                Transaction t = TransactionService.createTransaction(List.of(productId), List.of(quantity), storeId,
                                        c , productService, stockService, payment);
                
                                transactions.add(t);
                                System.out.println(" Create transaction \n" + t);
                            break;
                
                                //Παίρνει όλες τις συναλλαγές με TransactionService.getAllTransactions().
                                //Αν η λίστα είναι άδεια → εμφανίζει μήνυμα.
                                //Αλλιώς, τις εμφανίζει στην κονσόλα.
                            case "2":
                                List<Transaction> allTx = TransactionService.getAllTransactions();
                                if (allTx.isEmpty()) {
                                    System.out.println("The transaction was not found");
                                } else {
                                    allTx.forEach(System.out::println);
                                }
                            break;
            
            
                            //Ζητά από τον χρήστη το ID μιας συναλλαγής.
                            //Ψάχνει τη συναλλαγή με for loop.
                            //Αν τη βρει, εμφανίζει τα πλήρη στοιχεία της σε JSON μορφή (με προϊόντα, ποσότητες, πληρωμή κ.ά.).
                            case "3":
                                    System.out.print("ID Transaction: ");
                                long tid = Long.parseLong(in.nextLine());
            
                                Transaction tx = null;
                                for (Transaction trans : transactions) {
                                    if (trans.getTransactionId() == tid) {
                                        tx = trans;
                                        break;
                                    }
                                }
            
                                if (tx == null) {
                                    System.out.println("The transaction was not found.");
                                } else {
                                    String json = TransactionService.getTransactionAsJson(tx, TransactionService.getAllIncludes());
                                    System.out.println("Transaction in JSON:");
                                    System.out.println(json);
                                }
                            break;
                        
                            //Τερματίζει το μενού συναλλαγών και επιστρέφει στον χρήστη
                            case "0":
                                running = false;
                                System.out.println("Exiting the transaction menu.");
                            break;
            
                            default:
                                System.out.println(" Invalid selection. Please try again.");
                        }
                    } 
                    catch (NumberFormatException e) {
                        System.out.println("Invalid numeric value. Please try again.");
            
                    } 
                    catch (Exception e) {
                        System.out.println(" Error: " + e.getMessage());
                    }
            }
        }
            
        private static Client findClientById(long id) {
            for (Client c : clients) {
                if (c.getClientId() == id) return c;
            }
            return null;
        }
    }

