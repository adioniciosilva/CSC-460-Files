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
import java.awt.event.ActionEvent;

public class EditProduct {
    // Database connection variables
    Connection conn = null;
    String dbConnect = "jdbc:sqlite:../project/database/mamaspiddlins.sqlite";
    
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

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EditProduct window = new EditProduct(0, "", "", "", "", "", 0.0, "");
                    window.frmEditProduct.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public EditProduct(int productId, String name, String type, String pattern, 
                      String status, String category, double materialCost, String coozieSize) {
        try {
            conn = DriverManager.getConnection(dbConnect);
            System.out.println("Connection successful");
        } catch(SQLException e) {
            System.out.println("An error has occurred during connection");
            e.printStackTrace();
        }
        initialize();
        
        // Set the fields with the product data
        txtProdName.setText(name);
        txtItemType.setText(type);
        txtProdPattern.setText(pattern);
        txtMaterialCost.setText(String.valueOf(materialCost));
        
        // Set combo box selections
        setComboBoxSelection(cboxProductStatusAdd, status);
        setComboBoxSelection(cboxDonSelAdd, category);
        setComboBoxSelection(cboxCoozieSize, coozieSize);
        
        // Update the Save Changes button to handle updates
        btnEditProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProduct(productId);
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
        txtProdName.setToolTipText("Insert Product Name");
        txtProdName.setColumns(10);
        txtProdName.setBounds(214, 35, 179, 19);
        editProductPanel.add(txtProdName);
        
        String[] sortCoozieSize = new String[] {"","Small", "Medium", "Large"};
        cboxCoozieSize = new JComboBox<>(sortCoozieSize);
        cboxCoozieSize.setBounds(214, 64, 179, 21);
        editProductPanel.add(cboxCoozieSize);
        
        JLabel lblCoozieSize = new JLabel("Coozie Size");
        lblCoozieSize.setBounds(33, 73, 114, 13);
        editProductPanel.add(lblCoozieSize);
        
        JLabel lblItemType = new JLabel("Item Type");
        lblItemType.setBounds(33, 107, 114, 13);
        editProductPanel.add(lblItemType);
        
        txtItemType = new JTextField();
        txtItemType.setColumns(10);
        txtItemType.setBounds(214, 104, 179, 19);
        editProductPanel.add(txtItemType);
        
        txtProdPattern = new JTextField();
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
        cboxProductStatusAdd.setBounds(214, 182, 179, 21);
        editProductPanel.add(cboxProductStatusAdd);
        
        txtMaterialCost = new JTextField();
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
        cboxDonSelAdd.setBounds(214, 269, 179, 21);
        editProductPanel.add(cboxDonSelAdd);
        
        txtProdDSPrices = new JTextField();
        txtProdDSPrices.setColumns(10);
        txtProdDSPrices.setBounds(214, 317, 179, 19);
        editProductPanel.add(txtProdDSPrices);
        
        JLabel lblProductDSPrices = new JLabel("Product Sell Prices");
        lblProductDSPrices.setBounds(33, 320, 161, 13);
        editProductPanel.add(lblProductDSPrices);
        
        JLabel lblProductTimeSpent = new JLabel("Time Spent (in hours)");
        lblProductTimeSpent.setBounds(33, 364, 161, 13);
        editProductPanel.add(lblProductTimeSpent);
        
        txtTimeSpent = new JTextField();
        txtTimeSpent.setColumns(10);
        txtTimeSpent.setBounds(214, 361, 179, 19);
        editProductPanel.add(txtTimeSpent);
        
        btnEditProduct = new JButton("Save Changes");
        btnEditProduct.setBounds(65, 413, 127, 21);
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
        btnCancelProd.setBounds(223, 413, 146, 21);
        editProductPanel.add(btnCancelProd);
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
    
    private void updateProduct(int productId) {
        try {
            // Get updated values from form
            String name = txtProdName.getText().trim();
            String type = txtItemType.getText().trim();
            String pattern = txtProdPattern.getText().trim();
            String status = (String) cboxProductStatusAdd.getSelectedItem();
            String category = (String) cboxDonSelAdd.getSelectedItem();
            String coozieSize = (String) cboxCoozieSize.getSelectedItem();
            double materialCost = Double.parseDouble(txtMaterialCost.getText().trim());
            
            // Validate inputs
            if (name.isEmpty() || type.isEmpty() || pattern.isEmpty() || status.isEmpty() || 
                category.isEmpty() || coozieSize.isEmpty()) {
                JOptionPane.showMessageDialog(frmEditProduct, "Please fill in all fields", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Update database
            String query = "UPDATE items SET ITEM_NM=?, ITEM_TYPE_DE=?, QUILT_PATTERN_CD=?, " +
                         "ITEM_STATUS_CD=?, CATEGORY_CD=?, MATERIAL_COST_AM=?, COOZIE_SIZE_DE=? " +
                         "WHERE ITEM_ID=?";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, type);
                stmt.setString(3, pattern);
                stmt.setString(4, status);
                stmt.setString(5, category);
                stmt.setDouble(6, materialCost);
                stmt.setString(7, coozieSize);
                stmt.setInt(8, productId);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frmEditProduct, "Product updated successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Return to Products window
                    frmEditProduct.dispose();
                    Products productsWindow = new Products();
                    productsWindow.frmProducts.setVisible(true);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmEditProduct, "Invalid material cost value", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frmEditProduct, "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}