import java.time.LocalDate;

public class Client {
    //orizw ta pedia gia tin klasi client
    private int idClient;
    private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String gender;
    private boolean activeStatus;
    private LocalDate dateJoined;
    private float clientSumTotal;
    private LocalDate lastPurchaseDate;


    public Client(int idClient, String firstname, String lastname, LocalDate birthDate,
                  String phoneNumber, String email, String gender, boolean activeStatus,
                  LocalDate dateJoined, float clientSumTotal, LocalDate lastPurchaseDate) {
        this.idClient = idClient;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.activeStatus = activeStatus;
        this.dateJoined = dateJoined;
        this.clientSumTotal = clientSumTotal;
        this.lastPurchaseDate = lastPurchaseDate;
    }

    
    public void displayInfo(Client client) {
        System.out.println("ID: " + idClient);
        System.out.println("Όνομα: " + firstname);
        System.out.println("Επώνυμο: " + lastname);
        System.out.println("Ημ. Γέννησης: " + birthDate);
        System.out.println("Τηλέφωνο: " + phoneNumber);
        System.out.println("Email: " + email);
        System.out.println("Φύλο: " + gender);
        System.out.println("Ενεργός: " + (activeStatus ? "Ναι" : "Όχι"));
        System.out.println("Ημ. Εγγραφής: " + dateJoined);
        System.out.println("Σύνολο Αγορών: " + clientSumTotal + "€");
        System.out.println("Τελευταία Αγορά: " + lastPurchaseDate);
    }

    // Getters & Setters
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public float getClientSumTotal() {
        return clientSumTotal;
    }

    public void setClientSumTotal(float clientSumTotal) {
        this.clientSumTotal = clientSumTotal;
    }

    public LocalDate getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(LocalDate lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }
}

