import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TransactionFrame extends JFrame {

    private JPanel contentPanel;

    public TransactionFrame() {

        super();
        this.setTitle("Transactions");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        BackgroundPanel background = new BackgroundPanel("RETAIL1.png");
        background.setLayout(new BorderLayout());
        this.setContentPane(background);

        background.add(contentPanel, BorderLayout.CENTER);
        

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JButton createBtn = new JButton("Create Transaction");
        createBtn.setMaximumSize(new Dimension(200, 30));
        JButton viewAllBtn = new JButton("View All Transactions");
        viewAllBtn.setMaximumSize(new Dimension(200, 30));
        JButton jsonBtn = new JButton("Get Transaction as JSON");
        jsonBtn.setMaximumSize(new Dimension(200, 30));
        JButton closeBtn = new JButton("Close");
        closeBtn.setMaximumSize(new Dimension(200, 30));
       
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(createBtn);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(viewAllBtn);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(jsonBtn);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(closeBtn);
        menuPanel.add(Box.createVerticalStrut(20));

        this.add(menuPanel, BorderLayout.WEST);

        this.pack();
        this.setSize(800, 600); 
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        createBtn.addActionListener(e -> {
            try {
                JPanel form = new JPanel(new GridBagLayout());
                form.setOpaque(false);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);  // padding around components
                gbc.anchor = GridBagConstraints.WEST;

                //JTextField input = new JTextField();
                //JTextField stid = new JTextField();
                //JTextField pid = new JTextField();
                //JTextField qty = new JTextField();
                JButton addProductBtn = new JButton("Add product");
                JButton submitTransactionBtn = new JButton("Submit transaction");

                gbc.gridx = 0; gbc.gridy = 0;
                form.add(new JLabel("Client's email or phone:"), gbc);
                gbc.gridx = 1;
                JTextField input = new JTextField(15);
                form.add(input, gbc);

                // Row 1 - Store ID
                gbc.gridx = 0; gbc.gridy = 1;
                form.add(new JLabel("Store ID:"), gbc);
                gbc.gridx = 1;
                JTextField stid = new JTextField(15);
                form.add(stid, gbc);

                // Row 2 - Product ID
                gbc.gridx = 0; gbc.gridy = 2;
                form.add(new JLabel("Product ID:"), gbc);
                gbc.gridx = 1;
                JTextField pid = new JTextField(15);
                form.add(pid, gbc);

                // Row 3 - Quantity
                gbc.gridx = 0; gbc.gridy = 3;
                form.add(new JLabel("Product quantity:"), gbc);
                gbc.gridx = 1;
                JTextField qty = new JTextField(15);
                form.add(qty, gbc);

                // Row 4 - Add Product Button
                gbc.gridx = 0; gbc.gridy = 4;
                gbc.gridwidth = 2;
                form.add(addProductBtn, gbc);

                // Row 5 - Submit Button
                gbc.gridx = 0; gbc.gridy = 5;
                gbc.gridwidth = 2;
                form.add(submitTransactionBtn, gbc);

               

                /*form.add(new JLabel("Client's email or phone:")); form.add(input);
                form.add(new JLabel(" Store id")); form.add(stid);
                form.add(new JLabel ("product id"));form.add(pid);
                form.add(new JLabel ("product quantity"));form.add(qty);
                form.add(addProductBtn);
                form.add(submitTransactionBtn); */

                contentPanel.removeAll();              // Clear previous content
                contentPanel.add(form, BorderLayout.CENTER); // Add your form
                contentPanel.revalidate();             // Refresh layout
                contentPanel.repaint();
                  
                List<Long> productIds = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                
                //boolean more = true;
               // while (more) {
               //ADD PRODUCT BUTTON
                addProductBtn.addActionListener(e1 -> {
                    try {
                        productIds.add(Long.parseLong(pid.getText().trim()));
                        quantities.add(Integer.parseInt(qty.getText().trim()));

                        JOptionPane.showMessageDialog(this, "Product added.");
                        pid.setText("");
                        qty.setText("");
                    } 
                    catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid product ID or quantity.");
                    }
                });

                submitTransactionBtn.addActionListener(e2 -> {
                    try {
                        Client client = ClientService.authenticateClient( input.getText().trim());
                        if (client == null) {
                            JOptionPane.showMessageDialog(this, "Client not found.");
                            return;
                        }

                        int storeId = Integer.parseInt(stid.getText());

                    // int option = JOptionPane.showConfirmDialog(this, "Add another product?", "Continue", JOptionPane.YES_NO_OPTION);
                    // more = (option == JOptionPane.YES_OPTION);
                    // }

                        String [] paymentMethods = {"card", "cash","credit"};
                        String selected = (String) JOptionPane.showInputDialog(
                            null,                 
                            "Choose a payment method:",   
                            "PAYMENT",                
                            JOptionPane.QUESTION_MESSAGE,     
                            null,                        
                            paymentMethods,                                                    
                            paymentMethods[0]);                      
                       // System.out.println("Selected: " + selected);  
                        
                        if (selected == null) return;

                        String answer = JOptionPane.showInputDialog(this,"Do you want to procceed with the transaction? (YES/NO)");
                        if (answer == null || !answer.equalsIgnoreCase("yes")) {
                            JOptionPane.showMessageDialog(this, "Transaction cancelled.");
                            return;
                        }
                        
                        ProductService ps = ProductFrame.getProductService();
                        
                        Transaction t = TransactionService.createTransaction(
                        productIds, quantities, storeId, client, selected,answer);
                
                        //TransactionService.getAllTransactions().add(t);
                        JOptionPane.showMessageDialog(this, "Transaction created:\n" + t);
                        productIds.clear();
                        quantities.clear();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                    
                });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }   
        });
                
        

        viewAllBtn.addActionListener(e -> {
           // List<Transaction> txs = TransactionService.getAllTransactions();
            if (TransactionService.getAllTransactions().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No transactions found.");
            } else {
                JOptionPane.showMessageDialog(this, TransactionService.getAllTransactions().toString());
            }
        });

        jsonBtn.addActionListener(e -> {
            String tid = JOptionPane.showInputDialog("Transaction ID:");
            long id = Long.parseLong(tid);

            Transaction found = null;
            for (Transaction t : TransactionService.getAllTransactions()) {
                if (t.getTransactionId() == id) {
                    found = t;
                    break;
                }
            }

            if (found == null) {
                JOptionPane.showMessageDialog(this, "Transaction not found.");
            } else {
                String json = TransactionService.getTransactionAsJson(id);
                JOptionPane.showMessageDialog(this, json);
            }
        });

        closeBtn.addActionListener(e -> dispose());

    }

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
