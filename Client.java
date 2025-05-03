import java.time.LocalDate;

public class Client {

    private static long nextId = 1;

    //orizw ta pedia gia tin klasi client
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
    private LocalDate lastPurchaseDate;


    public Client(String firstName, String lastName, LocalDate birthDate,
                  String phoneNumber, String email, String gender, boolean activeStatus,
                  LocalDate dateJoined, double clientSumTotal, LocalDate lastPurchaseDate) 
                  {

        this.clientId = nextId++; // Auto set id. The clientId got removed from the constructor parameters as it will be automatically set
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.activeStatus = activeStatus;
        this.dateJoined = dateJoined;
        this.clientSumTotal = clientSumTotal;
        this.lastPurchaseDate = lastPurchaseDate;
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
        System.out.println("Last purchases: " + lastPurchaseDate);
    }

    // Getters & Setters
    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastName) {
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

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public double getClientSumTotal() {
        return clientSumTotal;
    }

    public void setClientSumTotal(double clientSumTotal) {
        this.clientSumTotal = clientSumTotal;
    }

    public LocalDate getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(LocalDate lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }

    @Override
    public String toString() {
     return "Client ID: "+ clientId + "/nFirst Name: " + firstName + "/nLast Name: " + lastName + "/nBirthdate: " + birthDate + "/nPhone Number: " + phoneNumber + "/nEmai: " + email + "/nGender: " + gender + "/nActive Status: " +
     activeStatus + "/nDate Joined: " + dateJoined + "/nClient Sum Total: " + clientSumTotal + "/nLast Purchase Date: " +  lastPurchaseDate;
    
    }
}

