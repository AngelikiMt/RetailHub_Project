import java.time.LocalDate;

public class Client {

    private static long nextId = 1;

    private long clientId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String gender;
    private boolean activeStatus;
    private LocalDate dateJoined;
    private double clientSumTotal;
    private double clientCurrentTotal;
    private LocalDate lastPurchaseDate;


    public Client(String firstName, String lastName, LocalDate birthDate,
                  String phoneNumber, String email, String gender, boolean activeStatus) 
                  {

        clientId = nextId++; // Auto-incremented internally 
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.activeStatus = activeStatus;
        dateJoined = LocalDate.now();
    }

    
    public void displayInfo() {
        System.out.println("ID: " + clientId);
        System.out.println("Name: " + firstName);
        System.out.println("Surname: " + lastName);
        System.out.println("Birthdate: " + birthDate);
        System.out.println("Phonenumber: " + phoneNumber);
        System.out.println("Email: " + email);
        System.out.println("Gender: " + gender);
        System.out.println("Avtive: " + (activeStatus ? "Yes" : "No"));
        System.out.println("Registration dates: " + dateJoined);
        System.out.println("Total purchases: " + clientSumTotal + "€");
        System.out.println("Current purchases for discount: " + clientCurrentTotal + "€");
        System.out.println("Last purchases: " + lastPurchaseDate);
    }

    // Getters & Setters
    public long getClientId() {
        return clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public double getClientSumTotal() {
        return clientSumTotal;
    }

    public void setClientSumTotal(double clientSumTotal) {
        this.clientSumTotal = clientSumTotal;
    }

    public double getClientCurrentTotal() {
        return clientCurrentTotal;
    }

    public void setClientCurrentTotal(double clientCurrentTotal) {
        this.clientCurrentTotal = clientCurrentTotal;
    }
    public LocalDate getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(LocalDate lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }

    @Override
    public String toString() {
     return "Client ID: "+ clientId + "\n" + "First Name: " + firstName + "\n" + "Last Name: " + lastName + "\n" + "Birthdate: " + birthDate + "\n" + "Phone Number: " + phoneNumber + "\n" + "Emai: " + email + "\n" + "Gender: " + gender + "\n" + "Active Status: " +
     activeStatus + "\n" + "Date Joined: " + dateJoined + "\n" + "Client Sum Total: " + clientSumTotal + "\n" + "Client Current Total: " + clientCurrentTotal + "\n" + "Last Purchase Date: " +  lastPurchaseDate + "\n";
    
    }
}

