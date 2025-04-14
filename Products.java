import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerNumberModel;
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
import javax.swing.JSpinner;

public class Products {
	// Database connection variables
    Connection conn = null;

    // Declaring variable types
    public JFrame frmProducts;
    public JTable tblList;
    public JTable tblProducts;
    private JSpinner spinnerProductQuantity;
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
    private final DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Launch the application.
     */
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

    /**
     * Constructor for the Products class
     * Initializes the database connection and UI component
     */

    
    public Products() {
	    try {
	    	// Load JDBC driver and establish database connection
	        Class.forName("org.sqlite.JDBC");
	        String dbPath = new File("database/mamaspiddlins.sqlite").getAbsolutePath();
	        conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
	        
	        // In case of unsuccessful connection to initialize UI components
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
	}

    /**
     * Initialize the contents of the frame
     * Sets up all UI components such as tabs, tables, and buttons
     * @wbp.parser.entryPoint
     */
    private void initialize() {
    	
		// The beginning setup for the Products Page
		frmProducts = new JFrame();
		frmProducts.setTitle("Products");
		frmProducts.setBounds(100, 100, 1000, 700);
		frmProducts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProducts.getContentPane().setBackground(new Color(216, 203, 175));
		frmProducts.getContentPane().setLayout(null);
		
		// The main panel for the panes, with each a represented tab
		JTabbedPane mainPanel = new JTabbedPane(JTabbedPane.TOP);
		mainPanel.setBounds(51, 85, 903, 550);
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
		
		// The label that will display the title of the page
        JLabel lblProduct = new JLabel("Products");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 40f);
            lblProduct.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblProduct.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblProduct.setBounds(51, 38, 283, 37);
        frmProducts.getContentPane().add(lblProduct);

        
        
        // *******************************************************************************************************	        
        // Add Product Components
		
		// A label that will display to the user product name
        JLabel lblProductName = new JLabel("Product Name");
        lblProductName.setBounds(95, 23, 90, 13);
        panelAddProd.add(lblProductName);
        
		// A textfield to add new products based on product name into database 
        txtProdName = new JTextField();
        txtProdName.setToolTipText("Insert product name");
        txtProdName.setBounds(276, 20, 179, 19);
        panelAddProd.add(txtProdName);
        txtProdName.setColumns(10);
        
		// A label that will display to the user coozie size
		JLabel lblCoozieSize = new JLabel("Coozie Size");
		lblCoozieSize.setBounds(95, 105, 114, 13);
		panelAddProd.add(lblCoozieSize);
		
		// A combobox to add new products based on coozie size into database 
		String[] sortCoozieSize = new String [] {"","Small", "Medium", "Large"};
		cboxCoozieSize = new JComboBox<>(sortCoozieSize);
		cboxCoozieSize.setToolTipText("Choose coozie size if applicable");
		cboxCoozieSize.setBounds(276, 101, 179, 21);
		panelAddProd.add(cboxCoozieSize);
		
		// A label that will display to the user item type
		JLabel lblItemType = new JLabel("Item Type");
		lblItemType.setBounds(95, 57, 114, 13);
		panelAddProd.add(lblItemType);
        
		// A textfield to add new products based on item type into database 
		txtItemType = new JTextField();
		txtItemType.setToolTipText("Enter the product type");
		txtItemType.setBounds(276, 54, 179, 19);
		panelAddProd.add(txtItemType);
		txtItemType.setColumns(10);
		
		
		// A label that will display to the user quilt pattern
		JLabel lblQuiltPattern = new JLabel("Quilt Pattern");
		lblQuiltPattern.setBounds(95, 151, 90, 13);
		panelAddProd.add(lblQuiltPattern);
		
		// A textfield to add new products based on product pattern into database 
		txtProdPattern = new JTextField();
		txtProdPattern.setToolTipText("Enter the quilt pattern if applicable");
		txtProdPattern.setBounds(276, 148, 179, 19);
		txtProdPattern.setColumns(10);
		panelAddProd.add(txtProdPattern);
		
		// A label that will display to the user product status
		JLabel lblProductStatus = new JLabel("Product Status");
		lblProductStatus.setBounds(95, 205, 90, 13);
		panelAddProd.add(lblProductStatus);
        
		// A combobox to add new products based on product status into database 
        String[] sortStatus = new String[] {"", "Finished", "Not Started", "Not Finished"};
        cboxProductStatusAdd = new JComboBox<>(sortStatus);
        cboxProductStatusAdd.setToolTipText("Choose current status for the product if applicable");
        cboxProductStatusAdd.setBounds(276, 201, 179, 21);
        panelAddProd.add(cboxProductStatusAdd);

        
		// A textfield to add new products based on material costs into database 
        txtMaterialCost = new JTextField();    
        txtMaterialCost.setToolTipText("Enter the material cost ");
        txtMaterialCost.setBounds(276, 247, 179, 19);
        txtMaterialCost.setColumns(10);
        panelAddProd.add(txtMaterialCost);
        
		// A label that will display to the user product material costs
		JLabel lblProductMC = new JLabel("Product Material Costs");
		lblProductMC.setBounds(95, 250, 135, 13);
		panelAddProd.add(lblProductMC);
		
		// A label that will display to the user product category
		JLabel lblProductCategory = new JLabel("Product Category");
		lblProductCategory.setBounds(95, 293, 135, 13);
		panelAddProd.add(lblProductCategory);
        
		// A combobox to add new products based on product category into database 
        String[] sortCategory = new String[] {"", "Inventory", "Sell", "Donate"};
        cboxDonSelAdd = new JComboBox<>(sortCategory);
        cboxDonSelAdd.setToolTipText("Choose the product category");
        cboxDonSelAdd.setBounds(276, 289, 179, 21);
        panelAddProd.add(cboxDonSelAdd);
        
        
        
		// A label that will display to the user product sell prices
		JLabel lblProductDSPrices = new JLabel("Product Sell Prices");
		lblProductDSPrices.setBounds(95, 386, 161, 13);
		panelAddProd.add(lblProductDSPrices);
		
		// A textfield to add new products based on product sell price into database 
		txtProdDSPrices = new JTextField();
		txtProdDSPrices.setToolTipText("Enter the product selling price");
		txtProdDSPrices.setBounds(276, 383, 179, 19);
		txtProdDSPrices.setColumns(10);
		panelAddProd.add(txtProdDSPrices);
		
		// A label that will display to the user time spent
		JLabel lblProductTimeSpent = new JLabel("Time Spent (in hours)");
		lblProductTimeSpent.setBounds(95, 428, 161, 13);
		panelAddProd.add(lblProductTimeSpent);

		// A textfield to add new products based on product's time spent into database 
        txtTimeSpent = new JTextField();
        txtTimeSpent.setToolTipText("Enter the time as an integer");
        txtTimeSpent.setBounds(276, 425, 179, 19);
        txtTimeSpent.setColumns(10);
        panelAddProd.add(txtTimeSpent);
        
		// A button to add a new product based on the associated fields
        JButton btnAddProd = new JButton("Add Product");
        btnAddProd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        btnAddProd.setBounds(116, 466, 114, 21);
        panelAddProd.add(btnAddProd);
        
		//A button to cancel adding a new product based on the associated fields
        JButton btnCancelProd = new JButton("Cancel Product");
        btnCancelProd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	clearAddProductForm();
            }
        });
        btnCancelProd.setBounds(287, 466, 135, 21);
        panelAddProd.add(btnCancelProd);
        
        // A label that display to the user product category
        JLabel lblProductQuantity = new JLabel("Product Quantity");
        lblProductQuantity.setBounds(95, 339, 114, 13);
        panelAddProd.add(lblProductQuantity);
        
        // A spinner for product quantity inputs
        spinnerProductQuantity = new JSpinner();
        spinnerProductQuantity.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        spinnerProductQuantity.setToolTipText("Enter the number of products if applicable");
        spinnerProductQuantity.setBounds(276, 336, 179, 20);
        panelAddProd.add(spinnerProductQuantity);
        
        // *******************************************************************************************************
        // Edit Products Components
        
		// Allows user to view a table with associated columns within the Edit Product tab
        tblProducts = new JTable();
        tblProducts.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Product ID", "Product Name", "Product Type", "Product Status", 
                         "Product Pattern", "Product Category","Product Date", "Coozie Size", "Material Cost"}
        ));
        
        // Disable editing in the table
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProducts.setDefaultEditor(Object.class, null);
        tblProducts.getTableHeader().setReorderingAllowed(false);
        tblProducts.getTableHeader().setResizingAllowed(false);
        
		// Will be used to allow sorting for the tables by ascending/descending order
        TableRowSorter<DefaultTableModel> productSorter = new TableRowSorter<>((DefaultTableModel) tblProducts.getModel());
        tblProducts.setRowSorter(productSorter);

		// Will allow the sorting by column header
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
        scrollPaneEditProduct.setBounds(10, 45, 879, 364);
        panelEditProd.add(scrollPaneEditProduct);
        
		// A label that will display to the user product id
		JLabel lblProductId = new JLabel("Product Id");
		lblProductId.setBounds(100, 437, 90, 13);
		panelEditProd.add(lblProductId);
		
		// A textfield to allow user to input product id to edit a product
		txtProductId = new JTextField();
		txtProductId.setToolTipText("Enter the product id based on above");
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
        
		// Allows user to view a table with associated columns within the Delete Product tab
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
        scrollPaneList.setBounds(108, 80, 555, 230);
        panelDeleteProd.add(scrollPaneList);
        
		// A label that will display to the user product id
        JLabel lblProductDelId = new JLabel("Product ID");
        lblProductDelId.setBounds(141, 348, 90, 13);
        panelDeleteProd.add(lblProductDelId);
        
		// A textfield to allow user to input product id to remove a product
        txtProductDelId = new JTextField();
        txtProductDelId.setToolTipText("Enter the product id based on above");
        txtProductDelId.setColumns(10);
        txtProductDelId.setBounds(315, 345, 269, 19);
        panelDeleteProd.add(txtProductDelId);
        
		// A button to delete a product with use of an associated textfield
        JButton btnDeleteProduct = new JButton("Delete Product");
        btnDeleteProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = txtProductDelId.getText().trim();
                
                // Validate input before parsing
                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(frmProducts, 
                        "Search bar cannot be empty. Please enter a numeric value.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    int productId = Integer.parseInt(input);
                    deleteProduct(productId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frmProducts, 
                        "Search bar cannot be empty. Please enter a numeric value.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnDeleteProduct.setBounds(194, 401, 124, 21);
        panelDeleteProd.add(btnDeleteProduct);
        
		// A button to cancel the deletion of a product 
        JButton btnCancelChanges = new JButton("Cancel Product");
        btnCancelChanges.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtProductDelId.setText("");
            }
        });
        btnCancelChanges.setBounds(378, 401, 135, 21);
        panelDeleteProd.add(btnCancelChanges);
        
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
 // Helper method to show consistent warning messages
    private void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(frmProducts, 
            message,
            title,
            JOptionPane.WARNING_MESSAGE);
    }
    
    // Function that will hand the edit product based on validating input and opening a new window
    private void editProduct() {
        // Will validate the input, and display message if empty
    	String productIdStr = txtProductId.getText().trim();
        
        if (productIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(frmProducts, "Search bar cannot be empty. Please enter a valid value.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            
            // Will check if product exists within the database
            if (!productExists(productId)) {
                JOptionPane.showMessageDialog(frmProducts, "Product ID " + productId + " not found", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Will open window to edit the product
            openEditProductWindow(productId);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmProducts, "Invalid Product ID format", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frmProducts, "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Function that will check if product's ID actually exists in the database
    private boolean productExists(int productId) throws SQLException {
        String query = "SELECT 1 FROM items WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            // Returns true if a record does exist. 
            return stmt.executeQuery().next();
        }
    }

    // Function that opens the window for the specified product
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
                	    rs.getString("COOZIE_SIZE_DE"),
                	    getProductQuantity(rs.getInt("ITEM_ID"))  // You'll need to implement this method
                	);
                
                frmProducts.setVisible(false);
                editWindow.frmEditProduct.setVisible(true);
            }
        }
    }
    
    // Function that will check the quantity of a product from either sales or donations table
    private int getProductQuantity(int itemId) throws SQLException {
        // First check sales table
        String salesQuery = "SELECT QUANTITY_SOLD_NO FROM sales WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(salesQuery)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        // Then check donations table
        String donationsQuery = "SELECT QUANTITY_DONATED_NO FROM donations WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(donationsQuery)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0; // Default if not found in either table
    }
    
    // Function that will allow the user to add a new product to the database
    private void addProduct() {
        // Get all input values (same as before)
        String prodName = txtProdName.getText().trim();
        String prodPattern = txtProdPattern.getText().trim();
        String materialCostStr = txtMaterialCost.getText().trim();
        String productStatus = (String) cboxProductStatusAdd.getSelectedItem();
        String category = (String) cboxDonSelAdd.getSelectedItem();
        String itemType = txtItemType.getText().trim();
        String coozieSize = (String) cboxCoozieSize.getSelectedItem();
        String sellPriceStr = txtProdDSPrices.getText().trim();
        String timeSpentStr = txtTimeSpent.getText().trim();
        int quantity = (int) spinnerProductQuantity.getValue();

        // Input validation (same as before)
        if (prodName.isEmpty()) {
            showWarning("Required Field", "Please enter a product name.");
            txtProdName.requestFocus();
            return;
        }
        
        if (itemType.isEmpty()) {
            showWarning("Required Field", "Please specify the product type.");
            txtItemType.requestFocus();
            return;
        }
        
        if (productStatus == null || productStatus.isEmpty()) {
            showWarning("Required Field", "Please select a product status.");
            cboxProductStatusAdd.requestFocus();
            return;
        }
        
        if (category == null || category.isEmpty()) {
            showWarning("Required Field", "Please select a product category.");
            cboxDonSelAdd.requestFocus();
            return;
        }
        
        if (materialCostStr.isEmpty()) {
            showWarning("Required Field", "Please enter the material cost.");
            txtMaterialCost.requestFocus();
            return;
        }
        // Type-specific validations
        if (itemType.equalsIgnoreCase("quilt")) {
            if (prodPattern.isEmpty()) {
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
        else if (itemType.equalsIgnoreCase("coozie")) {
            if (((String)cboxCoozieSize.getSelectedItem()).isEmpty()) {
                showWarning("Required Field", "Please select a coozie size for coozie items.");
                cboxCoozieSize.requestFocus();
                return;
            }
            // Ensure quilt pattern is not entered for coozies
            if (!prodPattern.isEmpty()) {
                showWarning("Invalid Entry", "Quilt pattern should not be entered for coozie items.");
                txtProdPattern.setText("");
                txtProdPattern.requestFocus();
                return;
            }
        } 
        else {
            // For other item types, ensure neither is entered
            if (!prodPattern.isEmpty()) {
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
        
        // Category-specific validations
        if (("Sell".equals(category) || "Donate".equals(category))) {
            if (quantity <= 0) {
                showWarning("Invalid Quantity", "Please enter a quantity greater than 0 for sell/donate items.");
                spinnerProductQuantity.requestFocus();
                return;
            }
        }

        if(timeSpentStr.isEmpty()) {
        	showWarning("Required Field", "Please enter the amount of time spent.");
        	txtTimeSpent.requestFocus();
        	return;
        }
        
        // Parse numeric values (same as before)
        double materialCost = 0.0;
        double sellPrice = 0.0;
        int timeSpent = 0;
        
        try {
            materialCost = Double.parseDouble(materialCostStr);
            if (materialCost <= 0) {
                showWarning("Invalid Material Cost", "Material cost must be greater than 0.");
                txtMaterialCost.requestFocus();
                return;
            }
            
            if ("Sell".equals(category)) {
                if (sellPriceStr.isEmpty()) {
                    showWarning("Field Required", "Please enter a sell price for items in sell category.");
                    txtProdDSPrices.requestFocus();
                    return;
                }
                sellPrice = Double.parseDouble(sellPriceStr);
                
                if (sellPrice <= 0) {
                    showWarning("Invalid Sell Price", "Sell price must be greater than 0.");
                    txtProdDSPrices.requestFocus();
                    return;
                }
            }
            
            if (!timeSpentStr.isEmpty()) {
                timeSpent = Integer.parseInt(timeSpentStr);
                if (timeSpent <= 0) {
                    showWarning("Invalid Time", "Time spent must be greater than 0.");
                    txtTimeSpent.requestFocus();
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            showWarning("Invalid Number Format", "Please enter valid numbers in numeric fields.");
            return;
        }

        // Start transaction
        try {
            conn.setAutoCommit(false);
            
            // Insert into items table
            String itemQuery = "INSERT INTO items (ITEM_NM, ITEM_TYPE_DE, ITEM_STATUS_CD, " +
                             "QUILT_PATTERN_CD, CATEGORY_CD, DATE_CREATED_DT, " +
                             "COOZIE_SIZE_DE, MATERIAL_COST_AM) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            int itemId = -1;
            try (PreparedStatement itemStmt = conn.prepareStatement(itemQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                itemStmt.setString(1, prodName);
                itemStmt.setString(2, itemType);
                itemStmt.setString(3, productStatus);
                itemStmt.setString(4, itemType.equalsIgnoreCase("quilt") ? prodPattern : null);
                itemStmt.setString(5, category);
                itemStmt.setString(6, LocalDate.now().format(dbFormatter));
                itemStmt.setString(7, itemType.equalsIgnoreCase("coozie") ? coozieSize : null);
                itemStmt.setDouble(8, materialCost);
                
                int affectedRows = itemStmt.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Creating item failed, no rows affected.");
                }
                
                try (ResultSet generatedKeys = itemStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        itemId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating item failed, no ID obtained.");
                    }
                }
            }
            
            // Use helper methods for related tables
            if ("Sell".equals(category)) {
                addToSalesTable(itemId, sellPrice, quantity);  // Using your helper method
            } else if ("Donate".equals(category)) {
                addToDonationsTable(itemId, quantity);  // Using your helper method
            }
            
            if (!timeSpentStr.isEmpty() && timeSpent > 0) {
                addToTimeLogs(itemId, timeSpent);  // Using your helper method
            }
            
            // Commit transaction
            conn.commit();
            
            JOptionPane.showMessageDialog(frmProducts, 
                "Product added successfully with ID: " + itemId, 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearAddProductForm();
            viewProductInfo();
            editProductInfo();
            
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                ex.addSuppressed(e);
            }
            JOptionPane.showMessageDialog(frmProducts, 
                "Database error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Your existing helper methods remain exactly the same:
    private void addToSalesTable(int itemId, double sellPrice, int quantity) throws SQLException {
        String salesQuery = "INSERT INTO sales (ITEM_ID, SALE_DT, QUANTITY_SOLD_NO, SALE_PRICE_AM) " +
                          "VALUES (?, ?, ?, ?)";
        try (PreparedStatement salesStmt = conn.prepareStatement(salesQuery)) {
            salesStmt.setInt(1, itemId);
            salesStmt.setString(2, LocalDate.now().format(dbFormatter));  // Use formatted current date
            salesStmt.setInt(3, quantity);
            salesStmt.setDouble(4, sellPrice);
            salesStmt.executeUpdate();
        }
    }
    

    private void addToDonationsTable(int itemId, int quantity) throws SQLException {
        String donationsQuery = "INSERT INTO donations (ITEM_ID, DONATION_DT, QUANTITY_DONATED_NO) " +
                              "VALUES (?, ?, ?)";
        try (PreparedStatement donationsStmt = conn.prepareStatement(donationsQuery)) {
            donationsStmt.setInt(1, itemId);
            donationsStmt.setString(2, LocalDate.now().format(dbFormatter));
            donationsStmt.setInt(3, quantity);
            donationsStmt.executeUpdate();
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

    // Function that will allow the user to clear all options in case to restart
    private void clearAddProductForm() {
        txtProdName.setText("");
        txtProdPattern.setText("");
        txtMaterialCost.setText("");
        txtProdDSPrices.setText("");
        txtProdDSPrices.setEnabled(true); // Re-enable in case it was disabled
        txtTimeSpent.setText("");
        txtItemType.setText("");
        cboxProductStatusAdd.setSelectedIndex(0);
        cboxDonSelAdd.setSelectedIndex(0);
        cboxCoozieSize.setSelectedIndex(0);
        spinnerProductQuantity.setValue(0);
    }

    // Function that will delete a product and along with its associated information from the database
        private void deleteProduct(int productDelete) {
            if (productDelete <= 0) {
                JOptionPane.showMessageDialog(frmProducts, 
                    "Product ID must be a positive number. Please enter a valid ID.",
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // First check if the product exists
                String checkQuery = "SELECT 1 FROM items WHERE ITEM_ID = ?";
                try (PreparedStatement pst = conn.prepareStatement(checkQuery)) {
                    pst.setInt(1, productDelete);
                    
                    ResultSet rs = pst.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(frmProducts, "No product found with the ID '" + productDelete + "'.", 
                            "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }

                // Disable auto-commit to handle as a transaction
                conn.setAutoCommit(false);

                // Delete order is based on foreign key constraints
                // Delete from financials (if any exist)
                String deleteFinancials = "DELETE FROM financials WHERE SALE_ID IN (SELECT SALE_ID FROM sales WHERE ITEM_ID = ?)";
                try (PreparedStatement pst = conn.prepareStatement(deleteFinancials)) {
                    pst.setInt(1, productDelete);
                    pst.executeUpdate();
                }

                // Delete from sales
                String deleteSales = "DELETE FROM sales WHERE ITEM_ID = ?";
                try (PreparedStatement pst = conn.prepareStatement(deleteSales)) {
                    pst.setInt(1, productDelete);
                    pst.executeUpdate();
                }

                // Delete from donations
                String deleteDonations = "DELETE FROM donations WHERE ITEM_ID = ?";
                try (PreparedStatement pst = conn.prepareStatement(deleteDonations)) {
                    pst.setInt(1, productDelete);
                    pst.executeUpdate();
                }

                // Delete from time_logs
                String deleteTimeLogs = "DELETE FROM time_logs WHERE ITEM_ID = ?";
                try (PreparedStatement pst = conn.prepareStatement(deleteTimeLogs)) {
                    pst.setInt(1, productDelete);
                    pst.executeUpdate();
                }

                // Finally delete from items
                String deleteItems = "DELETE FROM items WHERE ITEM_ID = ?";
                try (PreparedStatement pst = conn.prepareStatement(deleteItems)) {
                    pst.setInt(1, productDelete);
                    int affectedRows = pst.executeUpdate();
                    
                    if (affectedRows > 0) {
                        conn.commit(); // Commit the transaction
                        JOptionPane.showMessageDialog(frmProducts, "Product and all related records deleted successfully.", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        viewProductInfo(); // Refresh the table
                        editProductInfo(); // Refresh the edit table
                        txtProductDelId.setText(""); // Clear the input field
                    } else {
                        conn.rollback(); // Rollback if no rows affected
                        JOptionPane.showMessageDialog(frmProducts, "Error deleting the product.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                try {
                    conn.rollback(); // Rollbacks on errors
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(frmProducts, "Error while deleting the product: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } finally {
                try {
                    conn.setAutoCommit(true); // Restore auto-commit
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
                // Get date as string directly
                String dateStr = rs.getString("DATE_CREATED_DT");
                String displayDate;
                try {
                    LocalDate date = LocalDate.parse(dateStr, dbFormatter);
                    displayDate = date.format(displayFormatter);
                } catch (DateTimeParseException e1) {
                    try {
                        if (dateStr.matches("\\d+")) {
                            long timestamp = Long.parseLong(dateStr);
                            if (dateStr.length() > 10) timestamp /= 1000;
                            LocalDate date = LocalDate.ofEpochDay(timestamp / 86400);
                            displayDate = date.format(displayFormatter);
                            // Update the database with corrected date
                            updateItemDate(rs.getInt("ITEM_ID"), date);
                        } else {
                            displayDate = "Invalid Date";
                        }
                    } catch (NumberFormatException e2) {
                        displayDate = "Invalid Date";
                    }
                }
                // Format material cost with 2 decimal places
                String formattedMaterialCost = String.format("%.2f", rs.getDouble("MATERIAL_COST_AM"));
                
                model.addRow(new Object[] {
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    rs.getString("ITEM_TYPE_DE"),
                    rs.getString("ITEM_STATUS_CD"),
                    rs.getString("QUILT_PATTERN_CD"),
                    rs.getString("CATEGORY_CD"),
                    displayDate,  // Use the string directly
                    rs.getString("COOZIE_SIZE_DE"),
                    formattedMaterialCost    // Changed to getDouble for decimal values
                });
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(frmProducts, "Error loading product data: " + ex.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
 // Add this helper method to update item dates
    private void updateItemDate(int itemId, LocalDate correctDate) {
        String updateQuery = "UPDATE items SET DATE_CREATED_DT = ? WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, correctDate.format(dbFormatter));
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating item date for item " + itemId);
            e.printStackTrace();
        }
    }
}