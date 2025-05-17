import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientFrame extends JFrame {
    private JComboBox<String> menu;
    private JButton selectBtn;
    

    public ClientFrame() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Client Menu");
        setSize(450, 200);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] options = {
            "Select an option",
            "1. Create Client",
            "2. Authenticate Client",
            "3. Show Client",
            "4. Update Client",
            "5. Delete Client",
            "6. Delete Inactive Clients (PIN)",
            "7. Get All Clients as JSON",
        };

        menu = new JComboBox<>(options);
        selectBtn = new JButton("Go");

        selectBtn.addActionListener(e -> handleOption(menu.getSelectedIndex()));

        add(menu);
        add(selectBtn);
        setVisible(true);
    }

    private void handleOption(int index) {
        switch (index) {
            case 1 -> menuCreateClient();
            case 2 -> menuAuthenticateClient();
            case 3 -> menuShowClient();
            case 4 -> menuUpdateClient();
            case 5 -> menuDeleteClient();
            case 6 -> menuDeleteInactiveClients();
            case 7 -> menuGetJson();
            default -> JOptionPane.showMessageDialog(this, "Please select a valid option.");
        }
    }

    private void menuCreateClient() {
        JTextField fname = new JTextField();
        JTextField lname = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();
        String[] genderOptions = {"MALE", "FEMALE", "OTHER"};
        JComboBox<String> genderBox = new JComboBox<>(genderOptions);
        JTextField birth = new JTextField(); // dd/MM/yyyy

        Object[] fields = {
            "First Name:", fname,
            "Last Name:", lname,
            "Phone Number:", phone,
            "Email:", email,
            "Gender:", genderBox,
            "Birth Date (dd/MM/yyyy):", birth
        };

        int ok = JOptionPane.showConfirmDialog(this, fields, "Create Client", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            try {
                LocalDate birthDate = birth.getText().isEmpty() ? null :
                        LocalDate.parse(birth.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                Client client = ClientService.createClient(
                        TestData.clients,
                        fname.getText(),
                        lname.getText(),
                        birthDate,
                        phone.getText(),
                        email.getText(),
                        genderBox.getSelectedItem().toString(),
                        true
                );
                TestData.clients.add(client);
                JOptionPane.showMessageDialog(this, "Client created with ID: " + client.getClientId());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void menuAuthenticateClient() {
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        long clientId = ClientService.authenticateClient1(TestData.clients, input);
        if (clientId == -1) {
            JOptionPane.showMessageDialog(this, "Client not found.");
        }
    }

    private void menuShowClient() {
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        try {
            Client client = ClientService.authenticateClient(TestData.clients, input);
            JOptionPane.showMessageDialog(this, client.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Client not found.");
        }
    }

    private void menuUpdateClient() {
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone to identify client:");
        Client client = null;
        for (Client c : TestData.clients) {
            if (c.getEmail().equals(input) || c.getPhoneNumber().equals(input)) {
                client = c;
                break;
            }
        }

        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client not found.");
            return;
        }

        JTextField newFname = new JTextField(client.getFirstName());
        JTextField newLname = new JTextField(client.getLastName());
        JTextField newEmail = new JTextField(client.getEmail());
        JTextField newPhone = new JTextField(client.getPhoneNumber());

        Object[] fields = {
            "New First Name:", newFname,
            "New Last Name:", newLname,
            "New Email:", newEmail,
            "New Phone Number:", newPhone
        };

        int ok = JOptionPane.showConfirmDialog(this, fields, "Update Client", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            try {
                ClientService.updateClient(TestData.clients, client,
                        newFname.getText(), newLname.getText(), newEmail.getText(), newPhone.getText());
                JOptionPane.showMessageDialog(this, "Client updated.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void menuDeleteClient() {
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        Client client = null;
        for (Client c : TestData.clients) {
            if (c.getEmail().equals(input) || c.getPhoneNumber().equals(input)) {
                client = c;
                break;
            }
        }

        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client not found.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ClientService.deleteClient(TestData.clients, client.getClientId());
            JOptionPane.showMessageDialog(this, "Client deleted.");
        }
    }

    private void menuDeleteInactiveClients() {
        String ADMIN_PIN = "12345";
        String pin = JOptionPane.showInputDialog(this, "Enter PIN:");
        if (pin.equals(ADMIN_PIN)) {
            ClientService.isInactiveMoreThan5Years(TestData.clients);
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect PIN.");
        }
    }

    private void menuGetJson() {
        StringBuilder sb = new StringBuilder();
        for (Client client : TestData.clients) {
            sb.append(ClientService.getClientAsJson(client)).append("\n\n");
        }

        JTextArea area = new JTextArea(sb.toString(), 20, 40);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(this, scroll, "All Clients as JSON", JOptionPane.INFORMATION_MESSAGE);
    }
}
