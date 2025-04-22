//client.java
import java.time.LocalDate;

public class Client {
    // Πεδία
    private int idClient;
    private String onoma;
    private String epitheto;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String gender;
    private boolean activeStatus;
    private LocalDate dateJoined;
    private float clientSumTotal;
    private LocalDate lastPurchaseDate;

    // Κατασκευαστής
    public Client(int idClient, String onoma, String epitheto, LocalDate birthDate,
                  String phoneNumber, String email, String gender, boolean activeStatus,
                  LocalDate dateJoined, float clientSumTotal, LocalDate lastPurchaseDate) {
        this.idClient = idClient;
        this.onoma = onoma;
        this.epitheto = epitheto;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.activeStatus = activeStatus;
        this.dateJoined = dateJoined;
        this.clientSumTotal = clientSumTotal;
        this.lastPurchaseDate = lastPurchaseDate;
    }

    // Μέθοδος εμφάνισης στοιχείων πελάτη
    public void displayInfo() {
        System.out.println("ID: " + idClient);
        System.out.println("Όνομα: " + onoma);
        System.out.println("Επώνυμο: " + epitheto);
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

    public String getOnoma() {
        return onoma;
    }

    public void setOnoma(String onoma) {
        this.onoma = onoma;
    }

    public String getEpitheto() {
        return epitheto;
    }

    public void setEpitheto(String epitheto) {
        this.epitheto = epitheto;
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

