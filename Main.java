//Main.java
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Δημιουργία πελάτη
        Client client = new Client(
            1,
            "despoina",
            "makridou",
            LocalDate.of(2001, 5, 7),
            "6941234567",
            "despoinamakr@gmail.com",
            "Γυναίκα",
            true,
            LocalDate.of(2022, 1, 10),
            540.75f,
            LocalDate.of(2024, 12, 20)
        );

        client.setEpitheto("papadopoyloy");
        // Εμφάνιση πληροφοριών
        client.displayInfo();

    }
}