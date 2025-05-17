import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClientFrame extends JFrame {
    private final JPanel contentPanel;

    public ClientFrame() {
        setTitle("Client Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // === Φόντο ===
        BackgroundPanel background = new BackgroundPanel("RETAIL1.png");
        background.setLayout(new BorderLayout());
        setContentPane(background);

        // === Πλαϊνό Μενού ===
        JPanel leftMenu = new JPanel();
        leftMenu.setLayout(new GridLayout(8, 1, 10, 10));
        leftMenu.setOpaque(false);
        leftMenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 10));

        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        String[] actions = {
            "Create Client", "Authenticate Client", "Show Client",
            "Update Client", "Delete Client", "Delete Inactive Clients",
            "Get All Clients as JSON", "Back to Main Menu"
        };

        JButton[] buttons = new JButton[actions.length];

        for (int i = 0; i < actions.length; i++) {
            buttons[i] = new JButton(actions[i]);
            buttons[i].setFont(buttonFont);
            buttons[i].setFocusPainted(false);
            leftMenu.add(buttons[i]);
        }

        background.add(leftMenu, BorderLayout.WEST);

        // === Περιεχόμενο ===
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        background.add(contentPanel, BorderLayout.CENTER);

        // === Συνδέσεις Κουμπιών ===
        buttons[0].addActionListener(e -> menuCreateClient());
        buttons[1].addActionListener(e -> menuAuthenticateClient());
        buttons[2].addActionListener(e -> menuShowClient());
        buttons[3].addActionListener(e -> menuUpdateClient());
        buttons[4].addActionListener(e -> menuDeleteClient());
        buttons[5].addActionListener(e -> menuDeleteInactiveClients());
        buttons[6].addActionListener(e -> menuGetJson());
        buttons[7].addActionListener(e -> dispose());

        setVisible(true);
    }

    private void menuCreateClient() {
        JTextField fname = new JTextField(15);
        JTextField lname = new JTextField(15);
        JTextField phone = new JTextField(15);
        JTextField email = new JTextField(15);
        String[] genderOptions = {"MALE", "FEMALE", "OTHER"};
        JComboBox<String> genderBox = new JComboBox<>(genderOptions);
        JTextField birth = new JTextField(10); // dd/MM/yyyy

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

    // === Οι υπόλοιπες μέθοδοι (menuAuthenticateClient, menuShowClient, κ.λπ.) παραμένουν όπως ήταν ===

    private void menuAuthenticateClient() {
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        long clientId = ClientService.authenticateClient1(TestData.clients, input);
        if (clientId == -1) {
            JOptionPane.showMessageDialog(this, "Client not found.");
        } else {
            JOptionPane.showMessageDialog(this, "Authenticated. ID: " + clientId);
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
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        Client client = TestData.clients.stream()
            .filter(c -> c.getEmail().equals(input) || c.getPhoneNumber().equals(input))
            .findFirst().orElse(null);

        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client not found.");
            return;
        }

        JTextField newFname = new JTextField(client.getFirstName(), 15);
        JTextField newLname = new JTextField(client.getLastName(), 15);
        JTextField newEmail = new JTextField(client.getEmail(), 15);
        JTextField newPhone = new JTextField(client.getPhoneNumber(), 15);

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
        Client client = TestData.clients.stream()
            .filter(c -> c.getEmail().equals(input) || c.getPhoneNumber().equals(input))
            .findFirst().orElse(null);

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
            JOptionPane.showMessageDialog(this, "Inactive clients deleted.");
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

    // === Background Panel ===
    static class BackgroundPanel extends JPanel {
        private final Image image;

        public BackgroundPanel(String path) {
            this.image = new ImageIcon(path).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
