import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import java.util.List;


public class ClientFrame extends JFrame {
    private final JPanel contentPanel;
    private final JTable clientTable;
    private final DefaultTableModel tableModel;
    
 
     public ClientFrame() {
        
        // Basic window settings
        setTitle("Client Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Background with image
        JPanel background = new JPanel();
        background.setOpaque(false);
        //BackgroundPanel background = new BackgroundPanel("RETAIL1.png");
        //background.setLayout(new BorderLayout());
        
        this.add(background);
        getContentPane().setBackground(Color.WHITE);

        // Left menu with action buttons
        JPanel leftMenu = new JPanel();
        leftMenu.setLayout(new GridLayout(9, 1, 10, 10));
        leftMenu.setOpaque(false);
        leftMenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 10));

        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        String[] actions = {
            "Create Client", "Authenticate Client", "Show Client",
            "Update Client", "Delete Client", "Delete Inactive Clients",
            "Get All Clients as JSON", "Back to Main Menu"
        };

        // Create and add buttons
        JButton[] buttons = new JButton[actions.length];
        for (int i = 0; i < actions.length; i++) {
            buttons[i] = new JButton(actions[i]);
            buttons[i].setFont(buttonFont);
            buttons[i].setFocusPainted(false);
            leftMenu.add(buttons[i]);
        }
        background.add(leftMenu, BorderLayout.WEST);


        // Table to display clients
        JPanel topPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{
            "ID", "First Name", "Last Name", "Email", "Phone", "Gender",
            "Birth Date", "Active", "Date Joined", "Last Purchase", "Sum"
        }, 0);
        clientTable = new JTable(tableModel);
        refreshTable(); // Populate table with data
        topPanel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        background.add(topPanel, BorderLayout.NORTH);

        // Main content panel (used to show forms dynamically)
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        background.add(contentPanel, BorderLayout.CENTER);

        // Add button listeners
        buttons[0].addActionListener(e -> menuCreateClient());
        buttons[1].addActionListener(e -> menuAuthenticateClient());
        buttons[2].addActionListener(e -> menuShowClient());
        buttons[3].addActionListener(e -> menuUpdateClient());
        buttons[4].addActionListener(e -> menuDeleteClient());
        buttons[5].addActionListener(e -> menuDeleteInactiveClients());
        buttons[6].addActionListener(e -> menuGetJson());
        buttons[7].addActionListener(e -> dispose()); // Close window

        setVisible(true); // Show window
    }

    // Create a new client
    private void menuCreateClient() {
        contentPanel.removeAll(); // Clear previous content

        // Form layout
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setOpaque(false);
        JTextField fname = new JTextField(15);
        JTextField lname = new JTextField(15);
        JTextField phone = new JTextField(15);
        JTextField email = new JTextField(15);
        String[] genderOptions = {"MALE", "FEMALE", "OTHER"};
        JComboBox<String> genderBox = new JComboBox<>(genderOptions);
        JTextField birth = new JTextField(15);

        // Add fields to form
        form.add(new JLabel("First Name:")); form.add(fname);
        form.add(new JLabel("Last Name:")); form.add(lname);
        form.add(new JLabel("Phone Number:")); form.add(phone);
        form.add(new JLabel("Email:")); form.add(email);
        form.add(new JLabel("Gender:")); form.add(genderBox);
        form.add(new JLabel("Birth Date (dd/MM/yyyy):")); form.add(birth);

        // Submit button
        JButton submit = new JButton("Create Client");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(submit);

        // Wrapper for layout
        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);
        wrapper.setMaximumSize(new Dimension(500, 350));

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(wrapper);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        // Create client on submit
        submit.addActionListener(e -> {
            try {
                LocalDate birthDate = birth.getText().isEmpty() ? null :
                    LocalDate.parse(birth.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                Client client = ClientService.createClient(
                    fname.getText(), lname.getText(), birthDate,
                    phone.getText(), email.getText(),
                    genderBox.getSelectedItem().toString(), true
                );

               // clients.add(client);
                refreshTable(); // Update table
                JOptionPane.showMessageDialog(this, "Client created with ID: " + client.getClientId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void menuUpdateClient() {
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        Client client = null;
        for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
            Client c = ClientService.getClientDAO().getAllClients().get(i);
            if (input.equals(c.getEmail()) || input.equals(c.getPhoneNumber())) {
                client = c;
                break;
            }
        }

        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client not found.");
            return;
        }

        contentPanel.removeAll();

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setOpaque(false);

        JTextField newFname = new JTextField(client.getFirstName(), 15);
        JTextField newLname = new JTextField(client.getLastName(), 15);
        JTextField newEmail = new JTextField(client.getEmail(), 15);
        JTextField newPhone = new JTextField(client.getPhoneNumber(), 15);

        form.add(new JLabel("New First Name:"));
        form.add(newFname);
        form.add(new JLabel("New Last Name:"));
        form.add(newLname);
        form.add(new JLabel("New Email:"));
        form.add(newEmail);
        form.add(new JLabel("New Phone Number:"));
        form.add(newPhone);

        JButton submit = new JButton("Update Client");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(submit);

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);
        wrapper.setMaximumSize(new Dimension(500, 300));

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(wrapper);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        Client finalClient = client;
        submit.addActionListener(e -> {
            try {
                ClientService.updateClient(finalClient,
                        newFname.getText(), newLname.getText(), newEmail.getText(), newPhone.getText());
                refreshTable();
                JOptionPane.showMessageDialog(this, "Client updated.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void menuAuthenticateClient() {
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        int rowCount = clientTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            clientTable.setRowSelectionAllowed(true);
            String email = (String) clientTable.getValueAt(i, 3);
            String phone = (String) clientTable.getValueAt(i, 4);
            if (input.equals(email) || input.equals(phone)) {
                clientTable.setRowSelectionInterval(i, i);
                clientTable.setSelectionBackground(Color.GREEN);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Client not found.");
    }

    private void menuShowClient() {
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        Client client = null;
        for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
            Client c = ClientService.getClientDAO().getAllClients().get(i);
            if (input.equals(c.getEmail()) || input.equals(c.getPhoneNumber())) {
                client = c;
                break;
            }
        }

        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client not found.");
            return;
        }

        JTextArea area = new JTextArea(client.toString(), 15, 40);
        area.setEditable(false);
        area.setOpaque(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.getViewport().setOpaque(false);
        JOptionPane.showMessageDialog(this, scroll);
    }

    private void menuDeleteClient() {
        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        Client client = null;
        for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
            Client c = ClientService.getClientDAO().getAllClients().get(i);
            if (input.equals(c.getEmail()) || input.equals(c.getPhoneNumber())) {
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
            ClientService.deleteClient(client.getClientId());
            refreshTable();
            JOptionPane.showMessageDialog(this, "Client deleted.");
        }
    }

    private void menuDeleteInactiveClients() {
        String ADMIN_PIN = "12345";
        String pin = JOptionPane.showInputDialog(this, "Enter PIN:");
        if (pin != null && pin.equals(ADMIN_PIN)) {
            ClientService.isInactiveMoreThan5Years();
            refreshTable();
            JOptionPane.showMessageDialog(this, "Inactive clients deleted.");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect PIN.");
        }
    }

    private void menuGetJson() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
            sb.append(ClientService.getClientAsJson(ClientService.getClientDAO().getAllClients().get(i))).append("\n\n");
        }

        JTextArea area = new JTextArea(sb.toString(), 20, 40);
        area.setEditable(false);
        area.setOpaque(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.getViewport().setOpaque(false);
        JOptionPane.showMessageDialog(this, scroll, "All Clients as JSON", JOptionPane.INFORMATION_MESSAGE);
    }
    // Updates the table with current client list
    private void refreshTable() {
        tableModel.setRowCount(0); //clear
        //List <Client> clients = ClientService.getClientDAO().getAllClients();
        for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
            Client c = ClientService.getClientDAO().getAllClients().get(i);
            tableModel.addRow(new Object[]{
                    c.getClientId(), c.getFirstName(), c.getLastName(), c.getEmail(),
                    c.getPhoneNumber(), c.getGender(), c.getBirthDate(),
                    c.isActiveStatus(), c.getDateJoined(), c.getLastPurchaseDate(), c.getClientSumTotal()
            });
        }
    }

    // Custom JPanel to paint background image
    // static class BackgroundPanel extends JPanel {
    //     private final Image image;

    //     public BackgroundPanel(String path) {
    //         this.image = new ImageIcon(getClass().getResource("/" + path)).getImage();
    //     }

    //     @Override
    //     protected void paintComponent(Graphics g) {
    //         super.paintComponent(g);
    //         if (image != null) {
    //             g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    //         }
    //     }
    // }
}

