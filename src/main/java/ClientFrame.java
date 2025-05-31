import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
//import java.util.List;

public class ClientFrame extends JFrame {
    private final JPanel contentPanel;
    private final JTable clientTable = new JTable();
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{
        "ID", "First Name", "Last Name", "Email", "Phone", "Gender",
        "Birth Date", "Active", "Date Joined", "Last Purchase", "Sum"
    }, 0);

    public ClientFrame() {
        applyGlobalUIFont(new Font("MinionPro", Font.PLAIN, 20)); //Updates all components that use the default font
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

        // Web-style top navigation bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBackground(new Color(239, 247, 255)); // dark background
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Navigation items
        String[] navItems  = {
             "Create", "Authenticate", "Show", "Update", "Delete", "Delete Inactive", "Export JSON", "Back"
        };

        JLabel logo = new JLabel("ClientMenu");
        logo.setFont(new Font("MinionPro", Font.BOLD, 25));
        logo.setForeground(Color.BLACK);
        topBar.add(logo);
        topBar.add(Box.createHorizontalStrut(30)); // space before nav items

        topBar.add(Box.createHorizontalGlue());

        Font navFont = new Font("MinionPro", Font.PLAIN, 24);

        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFont(navFont);
            navButton.setFocusPainted(false);
            navButton.setForeground(Color.BLACK);
            navButton.setBackground(new Color(239, 247, 255));
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navButton.setOpaque(true);
            navButton.setBorderPainted(false);
            navButton.setAlignmentY(Component.CENTER_ALIGNMENT);
            navButton.setMaximumSize(new Dimension(250, 30));
            navButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    navButton.setBackground(new Color(60, 60, 60));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    navButton.setBackground(new Color(45, 45, 45));
                }
            });

            // Adds spacing between buttons
            topBar.add(navButton);
            topBar.add(Box.createHorizontalStrut(10));

            // Adds action listeners based on button text
            switch (item) {
                case "Create" -> navButton.addActionListener(e -> menuCreateClient());
                case "Authenticate" -> navButton.addActionListener(e -> menuAuthenticateClient());
                case "Show" -> navButton.addActionListener(e -> menuShowClient());
                case "Update" -> navButton.addActionListener(e -> menuUpdateClient());
                case "Delete" -> navButton.addActionListener(e -> menuDeleteClient());
                case "Delete Inactive" -> navButton.addActionListener(e -> menuDeleteInactiveClients());
                case "Export JSON" -> navButton.addActionListener(e -> menuGetJson());
                case "Back" -> navButton.addActionListener(e -> dispose());
            }
        }

        background.setLayout(new BorderLayout());
        background.add(topBar, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        background.add(contentPanel, BorderLayout.CENTER); // NOTE: use CENTER instead of SOUTH

        // Show create form by default
        menuCreateClient();

        // Table to display clients
        // JPanel tablePanel = new JPanel(new BorderLayout());
        // tableModel = new DefaultTableModel(new String[]{
        //     "ID", "First Name", "Last Name", "Email", "Phone", "Gender",
        //     "Birth Date", "Active", "Date Joined", "Last Purchase", "Sum"
        // }, 0);
        // clientTable = new JTable(tableModel);
        // refreshTable(); // Populate table with data
        // tablePanel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        // background.add(tablePanel, BorderLayout.CENTER);

        // //  Main content panel (used to show forms dynamically)
        // contentPanel = new JPanel(new BorderLayout());
        // contentPanel.setOpaque(false);
        // background.add(contentPanel, BorderLayout.SOUTH);
        // this.contentPanel = contentPanel; // <-- Assign to field

        setVisible(true); // Show window
    }

    // Create a new client
    private void menuCreateClient() {
        contentPanel.removeAll(); // Clear previous content
        Font customFont = new Font("MinionPro", Font.PLAIN, 25);

        // Form layout
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setOpaque(false);
        JTextField fname = new JTextField(15);
        fname.setFont(customFont);
        fname.setPreferredSize(new Dimension(200, 30));
        fname.setBorder(BorderFactory.createCompoundBorder(
            fname.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));        
        
        JTextField lname = new JTextField(15);
        lname.setFont(customFont);
        lname.setPreferredSize(new Dimension(200, 30));
        lname.setBorder(BorderFactory.createCompoundBorder(
            lname.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));        

        JTextField phone = new JTextField(15);
        phone.setFont(customFont);
        phone.setPreferredSize(new Dimension(200, 30));
        phone.setBorder(BorderFactory.createCompoundBorder(
            phone.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));        
        
        JTextField email = new JTextField(15);
        email.setFont(customFont);
        email.setPreferredSize(new Dimension(200, 30));
        email.setBorder(BorderFactory.createCompoundBorder(
            email.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        )); 

        String[] genderOptions = {"MALE", "FEMALE", "OTHER"};
        JComboBox<String> genderBox = new JComboBox<>(genderOptions);
        genderBox.setFont(customFont);
        genderBox.setPreferredSize(new Dimension(200, 30));
        genderBox.setBorder(BorderFactory.createCompoundBorder(
            genderBox.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        )); 

        JTextField birth = new JTextField(15);
        birth.setFont(customFont);
        birth.setPreferredSize(new Dimension(200, 40));
        birth.setBorder(BorderFactory.createCompoundBorder(
            birth.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        )); 

        // Add fields to form
        JLabel label = new JLabel("First Name:");
        label.setFont(customFont);
        form.add(label); form.add(fname);

        JLabel lLabel = new JLabel("Last Name:");
        lLabel.setFont(customFont);
        form.add(lLabel); form.add(lname);

        JLabel pLabel = new JLabel("Phone Number:");
        pLabel.setFont(customFont);
        form.add(pLabel); form.add(phone);

        JLabel eLabel = new JLabel("Email:");
        eLabel.setFont(customFont);
        form.add(eLabel); form.add(email);

        JLabel gLabel = new JLabel("Gender:");
        gLabel.setFont(customFont);
        form.add(gLabel); form.add(genderBox);

        JLabel bLabel = new JLabel("Birth Date (dd/MM/yyyy):");
        bLabel.setFont(customFont);
        form.add(bLabel); form.add(birth);

        //ImageIcon formIcon = new ImageIcon(getClss().getResource("add-group.png"));
        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            "Create New Client",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("MinionPro", Font.BOLD, 20),
            Color.DARK_GRAY
        );

        form.setBorder(BorderFactory.createCompoundBorder(
            titled,
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // form.add(new JLabel("First Name:")); form.add(fname);
        // form.add(new JLabel("Last Name:")); form.add(lname);
        // form.add(new JLabel("Phone Number:")); form.add(phone);
        // form.add(new JLabel("Email:")); form.add(email);
        // form.add(new JLabel("Gender:")); form.add(genderBox);
        // form.add(new JLabel("Birth Date (dd/MM/yyyy):")); form.add(birth);

        // Submit button
        JButton submit = new JButton("Submit");
        submit.setBackground(new Color(128, 0, 128)); // Purple background
        submit.setForeground(Color.WHITE);      
        submit.setFont(new Font("MinionPro", Font.BOLD, 20));
        submit.setPreferredSize(new Dimension(200, 40));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(submit);

        // Wrapper for layout
        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);
        wrapper.setMaximumSize(new Dimension(1000, 700));

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
        contentPanel.removeAll(); // Clear previous content

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
    contentPanel.removeAll(); // Clear previous content
    contentPanel.setLayout(new BorderLayout());

    JPanel form = new JPanel();
    form.setLayout(new BorderLayout());
    form.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createEmptyBorder(20, 20, 20, 20),
        "Search Client",
        TitledBorder.LEFT,
        TitledBorder.TOP,
        new Font("MinionPro", Font.BOLD, 20),
        Color.DARK_GRAY
    ));

    JTextField inputField = new JTextField(20);
    JButton search = new JButton("Search");
    search.setBackground(new Color(128, 0, 128));
    search.setForeground(Color.WHITE);

    JPanel topPanel = new JPanel(new FlowLayout());
    topPanel.add(new JLabel("Enter Email or Phone:"));
    topPanel.add(inputField);
    topPanel.add(search);

    JTextArea resultArea = new JTextArea(10, 40);
    resultArea.setEditable(false);
    resultArea.setFont(new Font("MinionPro", Font.PLAIN, 20));
    JScrollPane scroll = new JScrollPane(resultArea);

    form.add(topPanel, BorderLayout.NORTH);
    form.add(scroll, BorderLayout.CENTER);

    search.addActionListener(e -> {
        String input = inputField.getText();
        Client client = ClientService.getClientDAO().getAllClients().stream()
            .filter(c -> input.equals(c.getEmail()) || input.equals(c.getPhoneNumber()))
            .findFirst()
            .orElse(null);

        if (client != null) {
            resultArea.setText(client.toString());
        } else {
            resultArea.setText("Client not found.");
        }
    });

    contentPanel.add(form, BorderLayout.CENTER);
    contentPanel.revalidate();
    contentPanel.repaint();
}

private void menuDeleteClient() {
    contentPanel.removeAll();
    contentPanel.setLayout(new BorderLayout());

    JPanel form = new JPanel(new BorderLayout());
    form.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createEmptyBorder(20, 20, 20, 20),
        "Delete Client",
        TitledBorder.LEFT,
        TitledBorder.TOP,
        new Font("MinionPro", Font.BOLD, 20),
        Color.DARK_GRAY
    ));

    JTextField inputField = new JTextField(20);
    JButton delete = new JButton("Delete");
    delete.setBackground(new Color(128, 0, 128));
    delete.setForeground(Color.WHITE);

    JPanel topPanel = new JPanel(new FlowLayout());
    topPanel.add(new JLabel("Enter Email or Phone:"));
    topPanel.add(inputField);
    topPanel.add(delete);

    form.add(topPanel, BorderLayout.NORTH);

    delete.addActionListener(e -> {
        String input = inputField.getText();
        Client client = ClientService.getClientDAO().getAllClients().stream()
            .filter(c -> input.equals(c.getEmail()) || input.equals(c.getPhoneNumber()))
            .findFirst()
            .orElse(null);

        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client not found.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete client: " + client.getFirstName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ClientService.deleteClient(client.getClientId());
            refreshTable();
            JOptionPane.showMessageDialog(this, "Client deleted.");
        }
    });

    contentPanel.add(form, BorderLayout.CENTER);
    contentPanel.revalidate();
    contentPanel.repaint();
}

private void menuDeleteInactiveClients() {
    contentPanel.removeAll();
    contentPanel.setLayout(new BorderLayout());

    JPanel form = new JPanel(new BorderLayout());
    form.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createEmptyBorder(20, 20, 20, 20),
        "Delete Inactive Clients",
        TitledBorder.LEFT,
        TitledBorder.TOP,
        new Font("MinionPro", Font.BOLD, 20),
        Color.DARK_GRAY
    ));

    JTextField pinField = new JPasswordField(10);
    JButton confirm = new JButton("Confirm");
    confirm.setBackground(new Color(128, 0, 128));
    confirm.setForeground(Color.WHITE);

    JPanel centerPanel = new JPanel(new FlowLayout());
    centerPanel.add(new JLabel("Enter Admin PIN:"));
    centerPanel.add(pinField);
    centerPanel.add(confirm);

    form.add(centerPanel, BorderLayout.CENTER);

    confirm.addActionListener(e -> {
        String pin = pinField.getText();
        if ("12345".equals(pin)) {
            ClientService.isInactiveMoreThan5Years();
            refreshTable();
            JOptionPane.showMessageDialog(this, "Inactive clients deleted.");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect PIN.");
        }
    });

    contentPanel.add(form, BorderLayout.CENTER);
    contentPanel.revalidate();
    contentPanel.repaint();
}

private void menuGetJson() {
    contentPanel.removeAll();
    contentPanel.setLayout(new BorderLayout());

    JPanel form = new JPanel(new BorderLayout());
    form.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createEmptyBorder(20, 20, 20, 20),
        "Clients as JSON",
        TitledBorder.LEFT,
        TitledBorder.TOP,
        new Font("MinionPro", Font.BOLD, 20),
        Color.DARK_GRAY
    ));

    JTextArea jsonArea = new JTextArea(20, 40);
    jsonArea.setEditable(false);
    jsonArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

    StringBuilder sb = new StringBuilder();
    for (Client client : ClientService.getClientDAO().getAllClients()) {
        sb.append(ClientService.getClientAsJson(client)).append("\n\n");
    }
    jsonArea.setText(sb.toString());

    JScrollPane scroll = new JScrollPane(jsonArea);
    form.add(scroll, BorderLayout.CENTER);

    contentPanel.add(form, BorderLayout.CENTER);
    contentPanel.revalidate();
    contentPanel.repaint();
}


    // private void menuShowClient() {
    //     contentPanel.removeAll(); // Clear previous content
    //     Font customFont = new Font("MinionPro", Font.PLAIN, 25);
    //     JPanel showClient = new JPanel();
    //     String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
    //     Client client = null;
    //     for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
    //         Client c = ClientService.getClientDAO().getAllClients().get(i);
    //         if (input.equals(c.getEmail()) || input.equals(c.getPhoneNumber())) {
    //             client = c;
    //             break;
    //         }
    //     }

    //     if (client == null) {
    //         JOptionPane.showMessageDialog(this, "Client not found.");
    //         return;
    //     }

    //     JTextArea area = new JTextArea(client.toString(), 15, 40);
    //     area.setEditable(false);
    //     area.setFont(customFont);
    //     area.setOpaque(false);
    //     JScrollPane scroll = new JScrollPane(area);
    //     scroll.getViewport().setOpaque(false);
    //     JOptionPane.showMessageDialog(this, scroll);
    // }

    // private void menuDeleteClient() {
    //     String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
    //     Client client = null;
    //     for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
    //         Client c = ClientService.getClientDAO().getAllClients().get(i);
    //         if (input.equals(c.getEmail()) || input.equals(c.getPhoneNumber())) {
    //             client = c;
    //             break;
    //         }
    //     }

    //     if (client == null) {
    //         JOptionPane.showMessageDialog(this, "Client not found.");
    //         return;
    //     }

    //     int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?", "Confirm", JOptionPane.YES_NO_OPTION);
    //     if (confirm == JOptionPane.YES_OPTION) {
    //         ClientService.deleteClient(client.getClientId());
    //         refreshTable();
    //         JOptionPane.showMessageDialog(this, "Client deleted.");
    //     }
    // }

    // private void menuDeleteInactiveClients() {
    //     String ADMIN_PIN = "12345";
    //     String pin = JOptionPane.showInputDialog(this, "Enter PIN:");
    //     if (pin != null && pin.equals(ADMIN_PIN)) {
    //         ClientService.isInactiveMoreThan5Years();
    //         refreshTable();
    //         JOptionPane.showMessageDialog(this, "Inactive clients deleted.");
    //     } else {
    //         JOptionPane.showMessageDialog(this, "Incorrect PIN.");
    //     }
    // }

    // private void menuGetJson() {
    //     StringBuilder sb = new StringBuilder();
    //     for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
    //         sb.append(ClientService.getClientAsJson(ClientService.getClientDAO().getAllClients().get(i))).append("\n\n");
    //     }

    //     JTextArea area = new JTextArea(sb.toString(), 20, 40);
    //     area.setEditable(false);
    //     area.setOpaque(false);
    //     JScrollPane scroll = new JScrollPane(area);
    //     scroll.getViewport().setOpaque(false);
    //     JOptionPane.showMessageDialog(this, scroll, "All Clients as JSON", JOptionPane.INFORMATION_MESSAGE);
    // }
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

    private void applyGlobalUIFont(Font font) {
    java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
        Object key = keys.nextElement();
        Object value = UIManager.get(key);
        if (value instanceof Font) {
            UIManager.put(key, font);
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
    }}

