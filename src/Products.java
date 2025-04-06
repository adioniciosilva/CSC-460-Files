import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.FontFormatException;

public class Products {
    // Database connection variables
    Connection conn = null;
    String dbConnect = "jdbc:sqlite:../project/database/mamaspiddlins.sqlite";
    
    public JFrame frmProducts;
    public JTable tblFinacials;
    public JTable tblTime;
    public JTable tblList;
    public JTable tblProducts;
    private JTextField txtProdName;
    private JTextField txtProdPattern;
    private JTextField txtMaterialCost;
    private JTextField txtProdDSPrices;
    private JTextField txtTimeSpent;
    private JTextField txtProductDelId;
    private JComboBox<String> cboxProductStatusAdd;
    private JComboBox<String> cboxDonSelAdd;
    private JComboBox<String> cboxCoozieSize;
    private JTextField txtProductId;
    private JTextField txtItemType;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Products window = new Products();
                    window.frmProducts.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Products() {
        try {
            conn = DriverManager.getConnection(dbConnect);
            System.out.println("Connection successful");
        } catch(SQLException e) {
            System.out.println("An error has occurred during connection");
            e.printStackTrace();
        }
        initialize();
    }

    private void initialize() {
		// The beginning setup for the Products Page
		frmProducts = new JFrame();
		frmProducts.setTitle("Products");
		frmProducts.setBounds(100, 100, 1000, 1000);
		frmProducts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProducts.getContentPane().setBackground(new Color(216, 203, 175));
		frmProducts.getContentPane().setLayout(null);
		
		// The main panel for the panes, with each a represented tab
		JTabbedPane mainPanel = new JTabbedPane(JTabbedPane.TOP);
		mainPanel.setBounds(51, 85, 903, 678);
		frmProducts.getContentPane().add(mainPanel);
		
		JPanel panelAddProd = new JPanel();
		mainPanel.addTab("Add Product", null, panelAddProd, null);
		panelAddProd.setLayout(null);
		
		JPanel panelEditProd = new JPanel();
		mainPanel.addTab("Edit Product", null, panelEditProd, null);
		panelEditProd.setLayout(null);
		
		JPanel panelDeleteProd = new JPanel();
		mainPanel.addTab("Delete Product", null, panelDeleteProd, null);
		panelDeleteProd.setLayout(null);
		
		// *******************************************************************************************************	        
        // Add Product Components
		
		// A label that will display to the user product name
        JLabel lblProductName = new JLabel("Product Name");
        lblProductName.setBounds(95, 75, 90, 13);
        panelAddProd.add(lblProductName);
        
		// A textfield to add new products based on product name into database 
        txtProdName = new JTextField();
        txtProdName.setToolTipText("Insert Product Name");
        txtProdName.setBounds(276, 72, 179, 19);
        panelAddProd.add(txtProdName);
        txtProdName.setColumns(10);
        
		// A label that will display to the user coozie size
		JLabel lblCoozieSize = new JLabel("Coozie Size");
		lblCoozieSize.setBounds(95, 110, 114, 13);
		panelAddProd.add(lblCoozieSize);
		
		// A textfield to add new products based on coozie size into database 
		String[] sortCoozieSize = new String [] {"","Small", "Medium", "Large"};
		cboxCoozieSize = new JComboBox<>(sortCoozieSize);
		cboxCoozieSize.setBounds(276, 101, 179, 21);
		panelAddProd.add(cboxCoozieSize);
		
		// A label that will display to the user item type
		JLabel lblItemType = new JLabel("Item Type");
		lblItemType.setBounds(95, 144, 114, 13);
		panelAddProd.add(lblItemType);
        
		// A textfield to add new products based on item type into database 
		txtItemType = new JTextField();
		txtItemType.setBounds(276, 141, 179, 19);
		panelAddProd.add(txtItemType);
		txtItemType.setColumns(10);
		
		
		// A label that will display to the user quilt pattern
		JLabel lblQuiltPattern = new JLabel("Quilt Pattern");
		lblQuiltPattern.setBounds(95, 182, 90, 13);
		panelAddProd.add(lblQuiltPattern);
		
		// A textfield to add new products based on product pattern into database 
		txtProdPattern = new JTextField();
		txtProdPattern.setBounds(276, 179, 179, 19);
		txtProdPattern.setColumns(10);
		panelAddProd.add(txtProdPattern);
		
		// A label that will display to the user product status
		JLabel lblProductStatus = new JLabel("Product Status");
		lblProductStatus.setBounds(95, 223, 90, 13);
		panelAddProd.add(lblProductStatus);
        
		// A combobox to add new products based on product status into database 
        String[] sortStatus = new String[] {"", "Finished", "Not Started", "Not Finished"};
        cboxProductStatusAdd = new JComboBox<>(sortStatus);
        cboxProductStatusAdd.setBounds(276, 219, 179, 21);
        panelAddProd.add(cboxProductStatusAdd);
        
		// A textfield to add new products based on material costs into database 
        txtMaterialCost = new JTextField();    
        txtMaterialCost.setBounds(276, 265, 179, 19);
        txtMaterialCost.setColumns(10);
        panelAddProd.add(txtMaterialCost);
        
		// A label that will display to the user product material costs
		JLabel lblProductMC = new JLabel("Product Material Costs");
		lblProductMC.setBounds(95, 268, 135, 13);
		panelAddProd.add(lblProductMC);
		
		// A label that will display to the user product category
		JLabel lblProductCategory = new JLabel("Product Category");
		lblProductCategory.setBounds(95, 310, 135, 13);
		panelAddProd.add(lblProductCategory);
        
		// A combobox to add new products based on product category into database 
        String[] sortCategory = new String[] {"", "Inventory", "Sell", "Donate"};
        cboxDonSelAdd = new JComboBox<>(sortCategory);
        cboxDonSelAdd.setBounds(276, 306, 179, 21);
        panelAddProd.add(cboxDonSelAdd);
        
        
		// A label that will display to the user product sell prices
		JLabel lblProductDSPrices = new JLabel("Product Sell Prices");
		lblProductDSPrices.setBounds(95, 357, 161, 13);
		panelAddProd.add(lblProductDSPrices);
		
		// A textfield to add new products based on product sell price into database 
		txtProdDSPrices = new JTextField();
		txtProdDSPrices.setBounds(276, 354, 179, 19);
		txtProdDSPrices.setColumns(10);
		panelAddProd.add(txtProdDSPrices);
		
		// A label that will display to the user time spent
		JLabel lblProductTimeSpent = new JLabel("Time Spent (in hours)");
		lblProductTimeSpent.setBounds(95, 401, 161, 13);
		panelAddProd.add(lblProductTimeSpent);

		// A textfield to add new products based on product's time spent into database 
        txtTimeSpent = new JTextField();
        txtTimeSpent.setBounds(276, 398, 179, 19);
        txtTimeSpent.setColumns(10);
        panelAddProd.add(txtTimeSpent);
        
		// A button to add a new product based on the associated fields
        JButton btnAddProd = new JButton("Add Product");
        btnAddProd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        btnAddProd.setBounds(140, 450, 114, 21);
        panelAddProd.add(btnAddProd);
        
		//A button to cancel adding a new product based on the associated fields
        JButton btnCancelProd = new JButton("Cancel Product");
        btnCancelProd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtProdName.setText("");
                txtItemType.setText("");
                txtProdPattern.setText("");
                txtMaterialCost.setText("");
                txtProdDSPrices.setText("");
                txtTimeSpent.setText("");
                cboxCoozieSize.setSelectedIndex(-1); 
                cboxProductStatusAdd.setSelectedIndex(-1);
                cboxDonSelAdd.setSelectedIndex(-1); 
            }
        });
        btnCancelProd.setBounds(285, 450, 135, 21);
        panelAddProd.add(btnCancelProd);
        
        // *******************************************************************************************************
        // Edit Products Components
        tblProducts = new JTable();
        tblProducts.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Product ID", "Product Name", "Product Type", "Product Status", 
                         "Product Pattern", "Product Category","Product Date", "Coozie Size", "Material Cost"}
        ));
        
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProducts.setDefaultEditor(Object.class, null);
        tblProducts.getTableHeader().setReorderingAllowed(false);
        tblProducts.getTableHeader().setResizingAllowed(false);
        
        TableRowSorter<DefaultTableModel> productSorter = new TableRowSorter<>((DefaultTableModel) tblProducts.getModel());
        tblProducts.setRowSorter(productSorter);

        tblProducts.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = tblProducts.columnAtPoint(evt.getPoint());
                if (column >= 0) {
                    boolean ascending = productSorter.getSortKeys().isEmpty() || 
                                      productSorter.getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING;
                    productSorter.setSortKeys(Collections.singletonList(
                        new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
                }
            }
        });    
        
		// The table that that will display the information to the user 
        JScrollPane scrollPaneEditProduct = new JScrollPane(tblProducts);
        scrollPaneEditProduct.setBounds(19, 48, 879, 364);
        panelEditProd.add(scrollPaneEditProduct);
        
		// A label that will display to the user product id
		JLabel lblProductId = new JLabel("Product Id");
		lblProductId.setBounds(100, 437, 90, 13);
		panelEditProd.add(lblProductId);
		
		// A textfield to allow user to input product id to edit a product
		txtProductId = new JTextField();
		txtProductId.setColumns(10);
		txtProductId.setBounds(275, 434, 179, 19);
		panelEditProd.add(txtProductId);
        
		// A button to save your edits to the product
        JButton btnEditProd = new JButton("Edit Product");
        btnEditProd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editProduct();
            }
        });
        btnEditProd.setBounds(141, 473, 114, 21);
        panelEditProd.add(btnEditProd);
        
		// A button to cancel your edits to the product
        JButton btnCancelChange = new JButton("Cancel Changes");
        btnCancelChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtProductId.setText("");
            }
        });
        btnCancelChange.setBounds(275, 473, 135, 21);
        panelEditProd.add(btnCancelChange);
        
        // *******************************************************************************************************
        // Delete Products Components
        tblList = new JTable();
        tblList.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[] {"Product ID", "Product Type", "Product Name"}
        ));
        
        // Disable editing in the table
        tblList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblList.setDefaultEditor(Object.class, null);
        tblList.getTableHeader().setReorderingAllowed(false);
        
		// Will be used to allow sorting for the tables by ascending/descending order
        TableRowSorter<DefaultTableModel> listSorter = new TableRowSorter<>((DefaultTableModel) tblList.getModel());
        tblList.setRowSorter(listSorter);

		// Will allow the sorting by column header
        tblList.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = tblList.columnAtPoint(evt.getPoint());
                if (column >= 0) {
                    boolean ascending = listSorter.getSortKeys().isEmpty() || 
                                      listSorter.getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING;
                    listSorter.setSortKeys(Collections.singletonList(
                        new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
                }
            }
        });    
        
		// The table that that will display the information to the user 
        JScrollPane scrollPaneList = new JScrollPane(tblList);
        scrollPaneList.setBounds(141, 82, 362, 230);
        panelDeleteProd.add(scrollPaneList);
        
		// A label that will display to the user product id
        JLabel lblProductDelId = new JLabel("Product ID");
        lblProductDelId.setBounds(141, 348, 90, 13);
        panelDeleteProd.add(lblProductDelId);
        
		// A textfield to allow user to input product id to remove a product
        txtProductDelId = new JTextField();
        txtProductDelId.setColumns(10);
        txtProductDelId.setBounds(327, 345, 179, 19);
        panelDeleteProd.add(txtProductDelId);
        
		// A button to delete a product with use of an associated textfield
        JButton btnDeleteProduct = new JButton("Delete Product");
        btnDeleteProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteProduct(Integer.parseInt(txtProductDelId.getText()));
            }
        });
        btnDeleteProduct.setBounds(194, 386, 124, 21);
        panelDeleteProd.add(btnDeleteProduct);
        
		// A button to cancel the deletion of a product 
        JButton btnCancelChanges = new JButton("Cancel Product");
        btnCancelChanges.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtProductDelId.setText("");
            }
        });
        btnCancelChanges.setBounds(328, 386, 135, 21);
        panelDeleteProd.add(btnCancelChanges);
        
		// The panel that will contain the title of the page
        
        /* NOTE: Jaiven Comment:
         * 
         * CHANGES:
         * 1. Change the font 
         * 2. remove the grey panel
         * 
         */
        
		// The label that will display the title of the page
        JLabel lblProduct = new JLabel("Products");
        lblProduct.setFont(new Font("Tahoma", Font.BOLD, 20));

        try {
		    Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
		    caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 40f);
		    lblProduct.setFont(caveatBrush);
		    
		} catch (IOException | FontFormatException e) {
			lblProduct.setFont(new Font("Tahoma", Font.BOLD, 23)); 
		    e.printStackTrace();
		}
		
        lblProduct.setBounds(50, 24, 200, 35);
        frmProducts.getContentPane().add(lblProduct);
        
		// A button that will allow the user to return back to the home screen
        JButton btnHome = new JButton("Home");
        btnHome.setBounds(838, 47, 116, 28);
        frmProducts.getContentPane().add(btnHome);
        btnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frmProducts.setVisible(false);
                Dashboard dashboardPage = new Dashboard();
                dashboardPage.frmDashboard.setVisible(true);
            }
        });
        
        

//		*******************************************************************************************************
        
        // Load initial data
        viewProductInfo();
        editProductInfo();
    }

    // TEST ME
    private void editProduct() {
        String productIdStr = txtProductId.getText().trim();
        
        if (productIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(frmProducts, "Please enter a Product ID", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            
            if (!productExists(productId)) {
                JOptionPane.showMessageDialog(frmProducts, "Product ID " + productId + " not found", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            openEditProductWindow(productId);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmProducts, "Invalid Product ID format", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frmProducts, "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // TEST ME
    private boolean productExists(int productId) throws SQLException {
        String query = "SELECT 1 FROM items WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            return stmt.executeQuery().next();
        }
    }

    // TEST ME
    private void openEditProductWindow(int productId) throws SQLException {
        String query = "SELECT * FROM items WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                EditProduct editWindow = new EditProduct(
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    rs.getString("ITEM_TYPE_DE"),
                    rs.getString("QUILT_PATTERN_CD"),
                    rs.getString("ITEM_STATUS_CD"),
                    rs.getString("CATEGORY_CD"),
                    rs.getDouble("MATERIAL_COST_AM"),
                    rs.getString("COOZIE_SIZE_DE")
                );
                
                frmProducts.setVisible(false);
                editWindow.frmEditProduct.setVisible(true);
            }
        }
    }

	// FIX ME
    private void addProduct() {
        // Get all input values
        String prodName = txtProdName.getText().trim();
        String prodPattern = txtProdPattern.getText().trim();
        String materialCostStr = txtMaterialCost.getText().trim();
        String productStatus = (String) cboxProductStatusAdd.getSelectedItem();
        String category = (String) cboxDonSelAdd.getSelectedItem();
        String itemType = txtItemType.getText().trim();
        String coozieSize = (String) cboxCoozieSize.getSelectedItem();
        String sellPriceStr = txtProdDSPrices.getText().trim();
        String timeSpentStr = txtTimeSpent.getText().trim();

        // Validate required fields
        if (prodName.isEmpty() || itemType.isEmpty() || productStatus == null || 
            productStatus.isEmpty() || category == null || category.isEmpty() || 
            materialCostStr.isEmpty()) {
            JOptionPane.showMessageDialog(frmProducts, 
                "Please fill in all the required fields (Name, Type, Status, Category, Material Cost).", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Parse numeric values
        double materialCost = 0.0;
        double sellPrice = 0.0;
        int timeSpent = 0;
        
        try {
            materialCost = Double.parseDouble(materialCostStr);
            if (!sellPriceStr.isEmpty()) {
                sellPrice = Double.parseDouble(sellPriceStr);
            }
            if (!timeSpentStr.isEmpty()) {
                timeSpent = Integer.parseInt(timeSpentStr);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frmProducts, 
                "Please enter valid numbers for Material Cost, Sell Price, and Time Spent.", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get current date
        java.sql.Date dateCreated = new java.sql.Date(System.currentTimeMillis());

        // Prepare SQL query
        String query = "INSERT INTO items (ITEM_NM, ITEM_TYPE_DE, ITEM_STATUS_CD, " +
                     "QUILT_PATTERN_CD, CATEGORY_CD, DATE_CREATED_DT, " +
                     "COOZIE_SIZE_DE, MATERIAL_COST_AM) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            stmt.setString(1, prodName);
            stmt.setString(2, itemType);
            stmt.setString(3, productStatus);
            stmt.setString(4, prodPattern.isEmpty() ? null : prodPattern);
            stmt.setString(5, category);
            stmt.setDate(6, dateCreated);
            stmt.setString(7, coozieSize == null || coozieSize.isEmpty() ? null : coozieSize);
            stmt.setDouble(8, materialCost);

            // Execute insert
            int rowsInserted = stmt.executeUpdate();
            
            if (rowsInserted > 0) {
                // Get the generated item ID
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                int itemId = -1;
                if (generatedKeys.next()) {
                    itemId = generatedKeys.getInt(1);
                }
                
                // If sell price was provided and category is "Sell", add to sales table
                if (sellPrice > 0 && "Sell".equals(category)) {
                    addToSalesTable(itemId, sellPrice);
                }
                
                // If time spent was provided, add to time logs
                if (timeSpent > 0) {
                    addToTimeLogs(itemId, timeSpent);
                }

                JOptionPane.showMessageDialog(frmProducts, 
                    "Product added successfully with ID: " + itemId, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                clearAddProductForm();
                
                // Refresh tables
                viewProductInfo();
                editProductInfo();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frmProducts, 
                "Database error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void addToSalesTable(int itemId, double sellPrice) throws SQLException {
        String salesQuery = "INSERT INTO sales (ITEM_ID, SALE_DT, QUANTITY_SOLD_NO, SALE_PRICE_AM) " +
                          "VALUES (?, ?, 1, ?)";
        try (PreparedStatement salesStmt = conn.prepareStatement(salesQuery)) {
            salesStmt.setInt(1, itemId);
            salesStmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            salesStmt.setDouble(3, sellPrice);
            salesStmt.executeUpdate();
        }
    }

    private void addToTimeLogs(int itemId, int timeSpent) throws SQLException {
        String timeQuery = "INSERT INTO time_logs (ITEM_ID, TIME_SPENT_NO) VALUES (?, ?)";
        try (PreparedStatement timeStmt = conn.prepareStatement(timeQuery)) {
            timeStmt.setInt(1, itemId);
            timeStmt.setInt(2, timeSpent);
            timeStmt.executeUpdate();
        }
    }

    private void clearAddProductForm() {
        txtProdName.setText("");
        txtProdPattern.setText("");
        txtMaterialCost.setText("");
        txtProdDSPrices.setText("");
        txtTimeSpent.setText("");
        txtItemType.setText("");
        cboxProductStatusAdd.setSelectedIndex(0);
        cboxDonSelAdd.setSelectedIndex(0);
        cboxCoozieSize.setSelectedIndex(0);
    }

	// TEST THIS
    private void deleteProduct(int productDelete) {
        if (productDelete == 0) {
            JOptionPane.showMessageDialog(frmProducts, "Product ID cannot be zero or empty.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String checkQuery = "SELECT * FROM time_logs WHERE ITEM_ID = ?";
        try (PreparedStatement pst = conn.prepareStatement(checkQuery)) {
            pst.setInt(1, productDelete);
            
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = new DefaultTableModel(
                new String[]{"TIME_LOG_ID", "ITEM_ID", "TIME_SPENT_NO"}, 0);
            
            boolean found = false;

            while (rs.next()) {
                found = true;
                model.addRow(new Object[]{
                    rs.getInt("TIME_LOG_ID"),
                    rs.getInt("ITEM_ID"),
                    rs.getInt("TIME_SPENT_NO")
                });
            }

            if (!found) {
                JOptionPane.showMessageDialog(frmProducts, "No product found with the ID '" + productDelete + "'.", 
                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String deleteQuery = "DELETE FROM ITEMS WHERE ITEM_ID = ?";
            try (PreparedStatement pstDelete = conn.prepareStatement(deleteQuery)) {
                pstDelete.setInt(1, productDelete);

                int affectedRows = pstDelete.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(frmProducts, "Product deleted successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    viewProductInfo();
                } else {
                    JOptionPane.showMessageDialog(frmProducts, "Error deleting the product.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frmProducts, "Error while deleting the product: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frmProducts, "Error fetching time log data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
	// Function to allow the user to fetch product information
    private void viewProductInfo() {
        String query = "SELECT * FROM items";
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) tblList.getModel();
            model.setRowCount(0);
            
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_TYPE_DE"),
                    rs.getString("ITEM_NM")
                });
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
	// Function to allow the user to fetch and view product information to edit
    private void editProductInfo() {
        String query = "SELECT * FROM items";
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) tblProducts.getModel();
            model.setRowCount(0);
            
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    rs.getString("ITEM_TYPE_DE"),
                    rs.getString("ITEM_STATUS_CD"),
                    rs.getString("QUILT_PATTERN_CD"),
                    rs.getString("CATEGORY_CD"),
                    rs.getString("DATE_CREATED_DT"),
                    rs.getString("COOZIE_SIZE_DE"),
                    rs.getInt("MATERIAL_COST_AM")
                });
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}