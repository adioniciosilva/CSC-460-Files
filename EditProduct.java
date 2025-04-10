import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
//    String dbConnect = "jdbc:sqlite:../project/database/mamaspiddlins.sqlite";
    
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
		
		// Update the Save Changes button to handle updates
		btnEditProduct.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
		      updateProduct();
		  	}
		});
	}

    private void initialize() {
        frmEditProduct = new JFrame();
        frmEditProduct.setTitle("Edit Product");
        frmEditProduct.getContentPane().setBackground(new Color(216, 203, 175));
        frmEditProduct.getContentPane().setLayout(null);
        
        JPanel editProductPanel = new JPanel();
        editProductPanel.setBounds(24, 10, 437, 513);
        frmEditProduct.getContentPane().add(editProductPanel);
        editProductPanel.setLayout(null);
        
        JLabel lblProductName = new JLabel("Product Name");
        lblProductName.setBounds(33, 38, 90, 13);
        editProductPanel.add(lblProductName);
        
        txtProdName = new JTextField();
        txtProdName.setToolTipText("Insert product name");
        txtProdName.setColumns(10);
        txtProdName.setBounds(214, 35, 179, 19);
        editProductPanel.add(txtProdName);
        
        String[] sortCoozieSize = new String[] {"","Small", "Medium", "Large"};
        cboxCoozieSize = new JComboBox<>(sortCoozieSize);
        cboxCoozieSize.setToolTipText("Choose coozie size if applicable");
        cboxCoozieSize.setBounds(214, 64, 179, 21);
        editProductPanel.add(cboxCoozieSize);
        
        JLabel lblCoozieSize = new JLabel("Coozie Size");
        lblCoozieSize.setBounds(33, 73, 114, 13);
        editProductPanel.add(lblCoozieSize);
        
        JLabel lblItemType = new JLabel("Item Type");
        lblItemType.setBounds(33, 107, 114, 13);
        editProductPanel.add(lblItemType);
        
        txtItemType = new JTextField();
        txtItemType.setToolTipText("Enter the product type");
        txtItemType.setColumns(10);
        txtItemType.setBounds(214, 104, 179, 19);
        editProductPanel.add(txtItemType);
        
        txtProdPattern = new JTextField();
        txtProdPattern.setToolTipText("Enter the quilt pattern if applicable");
        txtProdPattern.setColumns(10);
        txtProdPattern.setBounds(214, 142, 179, 19);
        editProductPanel.add(txtProdPattern);
        
        JLabel lblQuiltPattern = new JLabel("Quilt Pattern");
        lblQuiltPattern.setBounds(33, 145, 90, 13);
        editProductPanel.add(lblQuiltPattern);
        
        JLabel lblProductStatus = new JLabel("Product Status");
        lblProductStatus.setBounds(33, 186, 90, 13);
        editProductPanel.add(lblProductStatus);
        
        String[] sortStatus = new String[] {"", "Finished", "Not Started", "Not Finished"};
        cboxProductStatusAdd = new JComboBox<>(sortStatus);
        cboxProductStatusAdd.setToolTipText("Choose current status for the product if applicable");
        cboxProductStatusAdd.setBounds(214, 182, 179, 21);
        editProductPanel.add(cboxProductStatusAdd);
        
        txtMaterialCost = new JTextField();
        txtMaterialCost.setToolTipText("Enter the material cost ");
        txtMaterialCost.setColumns(10);
        txtMaterialCost.setBounds(214, 228, 179, 19);
        editProductPanel.add(txtMaterialCost);
        
        JLabel lblProductMC = new JLabel("Product Material Costs");
        lblProductMC.setBounds(33, 231, 135, 13);
        editProductPanel.add(lblProductMC);
        
        JLabel lblProductCategory = new JLabel("Product Category");
        lblProductCategory.setBounds(33, 273, 135, 13);
        editProductPanel.add(lblProductCategory);
        
        String[] sortCategory = new String[] {"", "Inventory", "Sell", "Donate"};
        cboxDonSelAdd = new JComboBox<>(sortCategory);
        cboxDonSelAdd.setToolTipText("Choose the product category");
        cboxDonSelAdd.setBounds(214, 269, 179, 21);
        editProductPanel.add(cboxDonSelAdd);
        
        txtProdDSPrices = new JTextField();
        txtProdDSPrices.setToolTipText("Enter the product selling price");
        txtProdDSPrices.setColumns(10);
        txtProdDSPrices.setBounds(214, 356, 179, 19);
        editProductPanel.add(txtProdDSPrices);
        
        JLabel lblProductDSPrices = new JLabel("Product Sell Prices");
        lblProductDSPrices.setBounds(33, 359, 161, 13);
        editProductPanel.add(lblProductDSPrices);
        
        JLabel lblProductTimeSpent = new JLabel("Time Spent (in hours)");
        lblProductTimeSpent.setBounds(33, 403, 161, 13);
        editProductPanel.add(lblProductTimeSpent);
        
        txtTimeSpent = new JTextField();
        txtTimeSpent.setToolTipText("Enter the time as an integer");
        txtTimeSpent.setColumns(10);
        txtTimeSpent.setBounds(214, 400, 179, 19);
        editProductPanel.add(txtTimeSpent);
        
        btnEditProduct = new JButton("Save Changes");
        btnEditProduct.setBounds(63, 444, 127, 21);
        editProductPanel.add(btnEditProduct);
        
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
        
        lblProductQuantity = new JLabel("Product Quantity");
        lblProductQuantity.setBounds(33, 316, 114, 13);
        editProductPanel.add(lblProductQuantity);
        
        spinnerProductQuantity = new JSpinner();
        spinnerProductQuantity.setToolTipText("Enter the number of products if applicable");
        spinnerProductQuantity.setBounds(214, 313, 179, 20);
        editProductPanel.add(spinnerProductQuantity);
        
        
        frmEditProduct.setBounds(100, 100, 500, 600);
        frmEditProduct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setComboBoxSelection(JComboBox<String> comboBox, String value) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(value)) {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }
    
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
            double sellPrice = txtProdDSPrices.getText().isEmpty() ? 0 : 
                             Double.parseDouble(txtProdDSPrices.getText().trim());
            int timeSpent = txtTimeSpent.getText().isEmpty() ? 0 : 
                          Integer.parseInt(txtTimeSpent.getText().trim());

            // Validate inputs
            if (name.isEmpty() || type.isEmpty() || status.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(frmEditProduct, 
                    "Please fill in all required fields", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Start transaction
            conn.setAutoCommit(false);
            
            try {
                // Update items table
                String query = "UPDATE items SET ITEM_NM=?, ITEM_TYPE_DE=?, QUILT_PATTERN_CD=?, " +
                             "ITEM_STATUS_CD=?, CATEGORY_CD=?, MATERIAL_COST_AM=?, COOZIE_SIZE_DE=? " +
                             "WHERE ITEM_ID=?";
                
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, name);
                    stmt.setString(2, type);
                    stmt.setString(3, pattern.isEmpty() ? null : pattern);
                    stmt.setString(4, status);
                    stmt.setString(5, category);
                    stmt.setDouble(6, materialCost);
                    stmt.setString(7, coozieSize.isEmpty() ? null : coozieSize);
                    stmt.setInt(8, currentProductId);
                    stmt.executeUpdate();
                }

                // Update sales or donations based on category
                if ("Sell".equals(category)) {
                    updateSalesTable(sellPrice, quantity);
                } else if ("Donate".equals(category)) {
                    updateDonationsTable(quantity);
                }

                // Update time logs if time spent was provided
                if (timeSpent > 0) {
                    updateTimeLogs(timeSpent);
                }

                conn.commit();
                JOptionPane.showMessageDialog(frmEditProduct, 
                    "Product updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Return to Products window
                frmEditProduct.dispose();
                Products productsWindow = new Products();
                productsWindow.frmProducts.setVisible(true);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
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
    
    private void updateSalesTable(double sellPrice, int quantity) throws SQLException {
        // First check if a sale record exists
        String checkQuery = "SELECT 1 FROM sales WHERE ITEM_ID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, currentProductId);
            if (checkStmt.executeQuery().next()) {
                // Update existing sale
                String updateQuery = "UPDATE sales SET SALE_PRICE_AM=?, QUANTITY_SOLD_NO=? " +
                                    "WHERE ITEM_ID=?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setDouble(1, sellPrice);
                    updateStmt.setInt(2, quantity);
                    updateStmt.setInt(3, currentProductId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Insert new sale
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

    private void updateTimeLogs(int timeSpent) throws SQLException {
        // Similar logic for time logs
        String checkQuery = "SELECT 1 FROM time_logs WHERE ITEM_ID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, currentProductId);
            if (checkStmt.executeQuery().next()) {
                // Update existing time log
                String updateQuery = "UPDATE time_logs SET TIME_SPENT_NO=? " +
                                     "WHERE ITEM_ID=?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, timeSpent);
                    updateStmt.setInt(2, currentProductId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Insert new time log
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