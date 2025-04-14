import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;

public class EditProduct {
    // Database connection variables
    Connection conn = null;

    // Declaring variable types
    public JFrame frmEditProduct;
    private JTextField txtProdName;
    private JTextField txtItemType;
    private JTextField txtProdPattern;
    private JTextField txtMaterialCost;
    private JTextField txtProdDSPrices;
    private JTextField txtTimeSpent;
    private JComboBox<String> cboxProductStatusAdd;
    private JComboBox<String> cboxDonSelAdd;
    private JComboBox<String> cboxCoozieSize;
    private JButton btnEditProduct;
    private JButton btnCancelProd;
    private JLabel lblProductQuantity;
    private JSpinner spinnerProductQuantity;
    private int currentProductId;
    
    /**
     * Launch the application.
     */
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EditProduct window = new EditProduct(0, "", "", "", "", "", 0.0, "", 0);
                    window.frmEditProduct.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Constructor for the Edit Products class
     * Initializes the database connection and UI component
     */
    
    public EditProduct(int productId, String name, String type, String pattern, 
            String status, String category, double materialCost, 
            String coozieSize, int quantity) {
        this.currentProductId = productId;
        try {
            Class.forName("org.sqlite.JDBC");
            // Fix the path as suggested above
            String dbPath = new File("database/mamaspiddlins.sqlite").getAbsolutePath();
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            
            if (conn != null) {
                System.out.println("Connection successful");
                initialize();
                
                // Set the fields with the product data
                txtProdName.setText(name);
                txtItemType.setText(type);
                txtProdPattern.setText(pattern);
                txtMaterialCost.setText(String.valueOf(materialCost));
                spinnerProductQuantity.setValue(quantity);  // Set the quantity spinner

                // Set combo box selections
                setComboBoxSelection(cboxProductStatusAdd, status);
                setComboBoxSelection(cboxDonSelAdd, category);
                setComboBoxSelection(cboxCoozieSize, coozieSize);
                
                // Load additional data from related tables
                loadAdditionalProductData();
                
                // Update the Save Changes button to handle updates
                btnEditProduct.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        updateProduct();
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "Failed to connect to database", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Method to load additional product data from related tables
    private void loadAdditionalProductData() throws SQLException {
        // Load time spent from time_logs table
        String timeQuery = "SELECT TIME_SPENT_NO FROM time_logs WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(timeQuery)) {
            stmt.setInt(1, currentProductId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                txtTimeSpent.setText(String.valueOf(rs.getInt("TIME_SPENT_NO")));
            }
        }
        
        // Load sale price from sales table (only for Sell category)
        String category = (String) cboxDonSelAdd.getSelectedItem();
        if ("Sell".equals(category)) {
            String salesQuery = "SELECT SALE_PRICE_AM, QUANTITY_SOLD_NO FROM sales WHERE ITEM_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(salesQuery)) {
                stmt.setInt(1, currentProductId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    txtProdDSPrices.setText(String.valueOf(rs.getDouble("SALE_PRICE_AM")));
                    spinnerProductQuantity.setValue(rs.getInt("QUANTITY_SOLD_NO"));
                }
            }
        } else if ("Donate".equals(category)) {
            // Load quantity from donations table
            String donationsQuery = "SELECT QUANTITY_DONATED_NO FROM donations WHERE ITEM_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(donationsQuery)) {
                stmt.setInt(1, currentProductId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    spinnerProductQuantity.setValue(rs.getInt("QUANTITY_DONATED_NO"));
                }
            }
        }
    }


    /**
     * Initialize the contents of the frame.
     * @wbp.parser.entryPoint
     */
    private void initialize() {
    	
		// The beginning setup for the Edit Products Page
        frmEditProduct = new JFrame();
        frmEditProduct.setTitle("Edit Product");
        frmEditProduct.getContentPane().setBackground(new Color(216, 203, 175));
        frmEditProduct.getContentPane().setLayout(null);
        frmEditProduct.setBounds(100, 100, 500, 600);
        frmEditProduct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The main panel for the window
        JPanel editProductPanel = new JPanel();
        editProductPanel.setBounds(24, 10, 437, 513);
        frmEditProduct.getContentPane().add(editProductPanel);
        editProductPanel.setLayout(null);
        
		// The label that will display the product name
        JLabel lblProductName = new JLabel("Product Name");
        lblProductName.setBounds(33, 38, 90, 13);
        editProductPanel.add(lblProductName);
        
		// A textfield that allow an edit to a product name 
        txtProdName = new JTextField();
        txtProdName.setToolTipText("Insert product name");
        txtProdName.setColumns(10);
        txtProdName.setBounds(214, 35, 179, 19);
        editProductPanel.add(txtProdName);
        
		// A combobox that allow an to products based on coozie size into database 
        String[] sortCoozieSize = new String[] {"","Small", "Medium", "Large"};
        cboxCoozieSize = new JComboBox<>(sortCoozieSize);
        cboxCoozieSize.setToolTipText("Choose coozie size if applicable");
        cboxCoozieSize.setBounds(214, 64, 179, 21);
        editProductPanel.add(cboxCoozieSize);
        
		// A label that will display to the user coozie size
        JLabel lblCoozieSize = new JLabel("Coozie Size");
        lblCoozieSize.setBounds(33, 73, 114, 13);
        editProductPanel.add(lblCoozieSize);
        
		// A label that will display to the user item type
        JLabel lblItemType = new JLabel("Item Type");
        lblItemType.setBounds(33, 107, 114, 13);
        editProductPanel.add(lblItemType);
        
		// A textfield that allow an edit to a product type 
        txtItemType = new JTextField();
        txtItemType.setToolTipText("Enter the product type");
        txtItemType.setColumns(10);
        txtItemType.setBounds(214, 104, 179, 19);
        editProductPanel.add(txtItemType);
        
		// A textfield that allow an edit to a product pattern
        txtProdPattern = new JTextField();
        txtProdPattern.setToolTipText("Enter the quilt pattern if applicable");
        txtProdPattern.setColumns(10);
        txtProdPattern.setBounds(214, 142, 179, 19);
        editProductPanel.add(txtProdPattern);
        
		// A label that will display to the user quilt pattern
        JLabel lblQuiltPattern = new JLabel("Quilt Pattern");
        lblQuiltPattern.setBounds(33, 145, 90, 13);
        editProductPanel.add(lblQuiltPattern);
        
		// A label that will display to the user product status
        JLabel lblProductStatus = new JLabel("Product Status");
        lblProductStatus.setBounds(33, 186, 90, 13);
        editProductPanel.add(lblProductStatus);
        
		// A combobox to edit a products based on product status into database 
        String[] sortStatus = new String[] {"", "Finished", "Not Started", "Not Finished"};
        cboxProductStatusAdd = new JComboBox<>(sortStatus);
        cboxProductStatusAdd.setToolTipText("Choose current status for the product if applicable");
        cboxProductStatusAdd.setBounds(214, 182, 179, 21);
        editProductPanel.add(cboxProductStatusAdd);
        
        
        
		// A textfield that allow an edit to a material cost
        txtMaterialCost = new JTextField();
        txtMaterialCost.setToolTipText("Enter the material cost ");
        txtMaterialCost.setColumns(10);
        txtMaterialCost.setBounds(214, 228, 179, 19);
        editProductPanel.add(txtMaterialCost);
        
		// A label that will display to the user product material costs
        JLabel lblProductMC = new JLabel("Product Material Costs");
        lblProductMC.setBounds(33, 231, 135, 13);
        editProductPanel.add(lblProductMC);
        
		// A label that will display to the user product category
        JLabel lblProductCategory = new JLabel("Product Category");
        lblProductCategory.setBounds(33, 273, 135, 13);
        editProductPanel.add(lblProductCategory);
        
		// A combobox to edit a product based on product category into database 
        String[] sortCategory = new String[] {"", "Inventory", "Sell", "Donate"};
        cboxDonSelAdd = new JComboBox<>(sortCategory);
        cboxDonSelAdd.setToolTipText("Choose the product category");
        cboxDonSelAdd.setBounds(214, 269, 179, 21);
        editProductPanel.add(cboxDonSelAdd);
        
		// A textfield that allow an edit to a product sale cost
        txtProdDSPrices = new JTextField();
        txtProdDSPrices.setToolTipText("Enter the product selling price");
        txtProdDSPrices.setColumns(10);
        txtProdDSPrices.setBounds(214, 356, 179, 19);
        editProductPanel.add(txtProdDSPrices);
        
		// A label that will display to the user product sell prices
        JLabel lblProductDSPrices = new JLabel("Product Sell Prices");
        lblProductDSPrices.setBounds(33, 359, 161, 13);
        editProductPanel.add(lblProductDSPrices);
        
		// A label that will display to the user time spent
        JLabel lblProductTimeSpent = new JLabel("Time Spent (in hours)");
        lblProductTimeSpent.setBounds(33, 403, 161, 13);
        editProductPanel.add(lblProductTimeSpent);
        
		// A textfield that allow an edit to time spent on a product
        txtTimeSpent = new JTextField();
        txtTimeSpent.setToolTipText("Enter the time as an integer");
        txtTimeSpent.setColumns(10);
        txtTimeSpent.setBounds(214, 400, 179, 19);
        editProductPanel.add(txtTimeSpent);
        
		// A button to save your edits to the product
        btnEditProduct = new JButton("Save Changes");
        btnEditProduct.setBounds(63, 444, 127, 21);
        editProductPanel.add(btnEditProduct);
        
		// A button to cancel your edits to the product
        btnCancelProd = new JButton("Cancel Product");
        btnCancelProd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Return to Products window without saving
                frmEditProduct.dispose();
                Products productsWindow = new Products();
                productsWindow.frmProducts.setVisible(true);
            }
        });
        btnCancelProd.setBounds(226, 444, 146, 21);
        editProductPanel.add(btnCancelProd);
        
        // A label that display to the user product category
        lblProductQuantity = new JLabel("Product Quantity");
        lblProductQuantity.setBounds(33, 316, 114, 13);
        editProductPanel.add(lblProductQuantity);
        
        // A spinner for product quantity inputs
        spinnerProductQuantity = new JSpinner();
        spinnerProductQuantity.setToolTipText("Enter the number of products if applicable");
        spinnerProductQuantity.setBounds(214, 313, 179, 20);
        editProductPanel.add(spinnerProductQuantity);
        
        

    }
    
    // Helper method to show consistent warning messages
    private void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(frmEditProduct, 
            message,
            title,
            JOptionPane.WARNING_MESSAGE);
    }

    // Function that will set the item in a combobox based on a string value
    private void setComboBoxSelection(JComboBox<String> comboBox, String value) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(value)) {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }
    
    // Function that will make the changes of a product to the database with the current values 
    private void updateProduct() {
        try {
            // Get updated values from form
            String name = txtProdName.getText().trim();
            String type = txtItemType.getText().trim();
            String pattern = txtProdPattern.getText().trim();
            String status = (String) cboxProductStatusAdd.getSelectedItem();
            String category = (String) cboxDonSelAdd.getSelectedItem();
            String coozieSize = (String) cboxCoozieSize.getSelectedItem();
            double materialCost = Double.parseDouble(txtMaterialCost.getText().trim());
            int quantity = (int) spinnerProductQuantity.getValue();
            String sellPriceStr = txtProdDSPrices.getText().trim();
            double sellPrice = 0.0;
//            int timeSpent = txtTimeSpent.getText().isEmpty() ? 0 : 
//                          Integer.parseInt(txtTimeSpent.getText().trim());

            // Validate required fields
            if (name.isEmpty()) {
            	showWarning("Required Field", "Please enter a product name.");
            	txtProdName.requestFocus();
            	return;
            }
            
            if (type.isEmpty()) {
            	showWarning("Required Field", "");
            	txtItemType.requestFocus();
            	return;
            }
            
            if (status == null || status.isEmpty()) {
            	showWarning("Required Field", "Please select a product status.");
            	cboxProductStatusAdd.requestFocus();
            }
            
            if (category == null || category.isEmpty()) {
                showWarning("Required Field", "Please select a product category.");
                cboxDonSelAdd.requestFocus();
                return;
            }
            
         // Donation-specific validation
            if ("Donate".equals(category) && !txtProdDSPrices.getText().trim().isEmpty()) {
                showWarning("Invalid Entry", "Sell price should not be entered for donated items.");
                txtProdDSPrices.setText("");
                txtProdDSPrices.requestFocus();
                return;
            }
         
            if ("Sell".equals(category)) {
                if (sellPriceStr.isEmpty()) {
                    showWarning("Field Required", "Please enter a sell price for items in sell category.");
                    txtProdDSPrices.requestFocus();
                    return;
                }
            }

            // Validate quilt pattern (only for quilts)
            if (type.equalsIgnoreCase("quilt")) {
                if (pattern.isEmpty()) {
                    showWarning("Required Field", "Please enter a quilt pattern for quilt items.");
                    txtProdPattern.requestFocus();
                    return;
                }
                // Ensure coozie size is not selected for quilts
                if (!((String)cboxCoozieSize.getSelectedItem()).isEmpty()) {
                    showWarning("Invalid Selection", "Coozie size should not be selected for quilt items.");
                    cboxCoozieSize.setSelectedIndex(0);
                    cboxCoozieSize.requestFocus();
                    return;
                }
            } 
            else if (type.equalsIgnoreCase("coozie")) {
                if (((String)cboxCoozieSize.getSelectedItem()).isEmpty()) {
                    showWarning("Required Field", "Please select a coozie size for coozie items.");
                    cboxCoozieSize.requestFocus();
                    return;
                }
                // Ensure quilt pattern is not entered for coozies
                if (!pattern.isEmpty()) {
                    showWarning("Invalid Entry", "Quilt pattern should not be entered for coozie items.");
                    txtProdPattern.setText("");
                    txtProdPattern.requestFocus();
                    return;
                }
            } 
            else {
                // For other item types, ensure neither is entered
                if (!pattern.isEmpty()) {
                    showWarning("Invalid Entry", "Quilt pattern should only be entered for quilt items.");
                    txtProdPattern.setText("");
                    txtProdPattern.requestFocus();
                    return;
                }
                if (!((String)cboxCoozieSize.getSelectedItem()).isEmpty()) {
                    showWarning("Invalid Selection", "Coozie size should only be selected for coozie items.");
                    cboxCoozieSize.setSelectedIndex(0);
                    cboxCoozieSize.requestFocus();
                    return;
                }
            }
            String timeSpentText = txtTimeSpent.getText().trim();
            if (timeSpentText.isEmpty()) {
                showWarning("Required Field", "Please enter the amount of time spent.");
                txtTimeSpent.requestFocus();
                return;
            }

            int timeSpent = txtTimeSpent.getText().isEmpty() ? 0 : 
                Integer.parseInt(txtTimeSpent.getText().trim()); // Safe to parse now
            

            // Validate quantity for Sell/Donate categories
            if (("Sell".equals(category) || "Donate".equals(category))) {
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(frmEditProduct, 
                        "Please enter a valid quantity (greater than 0) for Sell or Donate categories.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                // For Inventory items, quantity should be 1
                quantity = 1;
            }

            // Validate sell price for Sell category
            if ("Sell".equals(category)) {
                if (sellPriceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frmEditProduct, 
                        "Sell price is required for Sell category.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    sellPrice = Double.parseDouble(sellPriceStr);
                    if (sellPrice <= 0) {
                        JOptionPane.showMessageDialog(frmEditProduct, 
                            "Please enter a valid sell price (greater than 0) for Sell category.", 
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frmEditProduct, 
                        "Invalid sell price format", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Start transaction
            conn.setAutoCommit(false);
            
            try {
                // Update the items table
                String query = "UPDATE items SET ITEM_NM=?, ITEM_TYPE_DE=?, QUILT_PATTERN_CD=?, " +
                             "ITEM_STATUS_CD=?, CATEGORY_CD=?, MATERIAL_COST_AM=?, COOZIE_SIZE_DE=? " +
                             "WHERE ITEM_ID=?";
                
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, name);
                    stmt.setString(2, type);
                    stmt.setString(3, type.equalsIgnoreCase("quilt") ? pattern : null);
                    stmt.setString(4, status);
                    stmt.setString(5, category);
                    stmt.setDouble(6, materialCost);
                    stmt.setString(7, type.equalsIgnoreCase("coozie") ? coozieSize : null);
                    stmt.setInt(8, currentProductId);
                    stmt.executeUpdate();
                }

                // Update sales or donations based on category
                if ("Sell".equals(category)) {
                    updateSalesTable(sellPrice, quantity);
                } else if ("Donate".equals(category)) {
                    updateDonationsTable(quantity);
                } else {
                    // For inventory items, remove from sales/donations if they exist
                    deleteFromSalesOrDonations();
                }

                // Update time logs if time spent was provided
                if (timeSpent > 0) {
                    updateTimeLogs(timeSpent);
                }
                
                // Commit transaction if all updates are successful
                conn.commit();
                JOptionPane.showMessageDialog(frmEditProduct, 
                    "Product updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Return to Products window
                frmEditProduct.dispose();
                Products productsWindow = new Products();
                productsWindow.frmProducts.setVisible(true);
            } catch (SQLException e) {
                // Rollback transaction if any error occurs
                conn.rollback();
                throw e;
            } finally {
                // Restore auto-commit mode
                conn.setAutoCommit(true);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmEditProduct, 
                "Invalid numeric value", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frmEditProduct, 
                "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // New helper method to remove from sales/donations when category changes to Inventory
    private void deleteFromSalesOrDonations() throws SQLException {
        // Delete from sales if exists
        String deleteSales = "DELETE FROM sales WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSales)) {
            stmt.setInt(1, currentProductId);
            stmt.executeUpdate();
        }
        
        // Delete from donations if exists
        String deleteDonations = "DELETE FROM donations WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteDonations)) {
            stmt.setInt(1, currentProductId);
            stmt.executeUpdate();
        }
    }
    
    // Function that will allow updates to the sales table for the current products
    private void updateSalesTable(double sellPrice, int quantity) throws SQLException {
        // Will first check if a sale record exists
        String checkQuery = "SELECT 1 FROM sales WHERE ITEM_ID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, currentProductId);
            if (checkStmt.executeQuery().next()) {
                // Updates the existing sale
                String updateQuery = "UPDATE sales SET SALE_PRICE_AM=?, QUANTITY_SOLD_NO=? " +
                                    "WHERE ITEM_ID=?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setDouble(1, sellPrice);
                    updateStmt.setInt(2, quantity);
                    updateStmt.setInt(3, currentProductId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Will insert new sale
                String insertQuery = "INSERT INTO sales (ITEM_ID, SALE_DT, SALE_PRICE_AM, QUANTITY_SOLD_NO) " +
                                   "VALUES (?, CURRENT_DATE, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, currentProductId);
                    insertStmt.setDouble(2, sellPrice);
                    insertStmt.setInt(3, quantity);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
    
    // Function that will update the donations table for the current product
    private void updateDonationsTable(int quantity) throws SQLException {
        // Similar logic to updateSalesTable but for donations
        String checkQuery = "SELECT 1 FROM donations WHERE ITEM_ID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, currentProductId);
            if (checkStmt.executeQuery().next()) {
                // Update existing donation
                String updateQuery = "UPDATE donations SET QUANTITY_DONATED_NO=? " +
                                    "WHERE ITEM_ID=?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, quantity);
                    updateStmt.setInt(2, currentProductId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Insert new donation
                String insertQuery = "INSERT INTO donations (ITEM_ID, DONATION_DT, QUANTITY_DONATED_NO) " +
                                   "VALUES (?, CURRENT_DATE, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, currentProductId);
                    insertStmt.setInt(2, quantity);
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    // Function that update the time logs table for the current product
    private void updateTimeLogs(int timeSpent) throws SQLException {
        String checkQuery = "SELECT 1 FROM time_logs WHERE ITEM_ID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, currentProductId);
            if (checkStmt.executeQuery().next()) {
                // Updates an existing time log
                String updateQuery = "UPDATE time_logs SET TIME_SPENT_NO=? " +
                                     "WHERE ITEM_ID=?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, timeSpent);
                    updateStmt.setInt(2, currentProductId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Will insert new time log
                String insertQuery = "INSERT INTO time_logs (ITEM_ID, TIME_SPENT_NO) " +
                                    "VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, currentProductId);
                    insertStmt.setInt(2, timeSpent);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
}