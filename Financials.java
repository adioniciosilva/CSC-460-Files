import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Graphics2D;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FontFormatException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JTabbedPane;
import java.awt.Component;

public class Financials {

    // Database connection variables
    Connection conn = null;
    // Connection based on SQLite within the Project Folder
    String dbConnect = "jdbc:sqlite:../project/database/mamaspiddlins.sqlite";
    
    public JFrame frmFinancials;
    public JTable tblFinacials;
    public JTable tblDonations;
    public JTable tblInventory;
    private JLabel lblDisplayRevenue;
    private JLabel lblDisplayProfit;
    private JTextField txtSearchBoxInv;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Financials window = new Financials();
                    window.frmFinancials.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Financials() {
        try {
            conn = DriverManager.getConnection(dbConnect);
            System.out.println("Connection successful");
        }
        catch(SQLException e) {
            System.out.println("An error has occured during conection");
            e.printStackTrace();
        }
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
    
        // The beginning setup for the Financials Page
        frmFinancials = new JFrame();
        frmFinancials.setTitle("Financials");
        frmFinancials.setBounds(100, 100, 1000, 800); // Increased width to accommodate more columns
        frmFinancials.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmFinancials.getContentPane().setBackground(new Color(216, 203, 175));
        frmFinancials.getContentPane().setLayout(null);
        
        // Allows user to view a table with associated columns within the Finacials tab
        tblFinacials = new JTable();
        tblFinacials.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[] {"Product ID", "Product Name", "Material Costs", "Sale Date", "Quantity Sold", "Unit Price", "Total Price"}    
        ));
        // Disable editing in the table
        tblFinacials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblFinacials.setDefaultEditor(Object.class, null);  // Disable editor for the entire table
        tblFinacials.getTableHeader().setReorderingAllowed(false);
        tblFinacials.getTableHeader().setResizingAllowed(false);
        
        // Will be used to allow sorting for the tables by ascending/descending order
        TableRowSorter<DefaultTableModel> finacialsSorter = new TableRowSorter<>((DefaultTableModel) tblFinacials.getModel());
        tblFinacials.setRowSorter(finacialsSorter);

        // Will allow the sorting by column header
        tblFinacials.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = tblFinacials.columnAtPoint(evt.getPoint());
                if (column >= 0) {
                    // Get current sort order for the column
                    boolean ascending = finacialsSorter.getSortKeys().isEmpty() || finacialsSorter.getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING;
                    // Toggle between ascending and descending
                    finacialsSorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
                }
            }
        });    
        
        tblDonations = new JTable();
        tblDonations.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Donation ID", "Product ID", "Product Name", "Product Type", 
                         "Donation Date", "Donation Quantity"}
        ));
        
        tblDonations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDonations.setDefaultEditor(Object.class, null);
        tblDonations.getTableHeader().setReorderingAllowed(false);
        tblDonations.getTableHeader().setResizingAllowed(false);
        
        TableRowSorter<DefaultTableModel> donationSorter = new TableRowSorter<>((DefaultTableModel) tblDonations.getModel());
        tblDonations.setRowSorter(donationSorter);

        tblDonations.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = tblDonations.columnAtPoint(evt.getPoint());
                if (column >= 0) {
                    boolean ascending = donationSorter.getSortKeys().isEmpty() || 
                    		donationSorter.getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING;
                    donationSorter.setSortKeys(Collections.singletonList(
                        new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
                }
            }
        }); 
        
        
        tblInventory = new JTable();
        tblInventory.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Inventory ID", "Product Name", "Product Type", 
                         "Inventory Date"}
        ));
        
        tblInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblInventory.setDefaultEditor(Object.class, null);
        tblInventory.getTableHeader().setReorderingAllowed(false);
        tblInventory.getTableHeader().setResizingAllowed(false);
        
        TableRowSorter<DefaultTableModel> inventorySorter = new TableRowSorter<>((DefaultTableModel) tblInventory.getModel());
        tblInventory.setRowSorter(inventorySorter);

        tblInventory.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = tblInventory.columnAtPoint(evt.getPoint());
                if (column >= 0) {
                    boolean ascending = inventorySorter.getSortKeys().isEmpty() || 
                    		inventorySorter.getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING;
                    inventorySorter.setSortKeys(Collections.singletonList(
                        new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
                }
            }
        });
        
        // 
        JTabbedPane mainPane = new JTabbedPane(JTabbedPane.TOP);
        mainPane.setBounds(31, 59, 900, 650);
        frmFinancials.getContentPane().add(mainPane);

        
        JPanel financialsPanel = new JPanel();
        mainPane.addTab("Finance", null, financialsPanel, null);
        financialsPanel.setLayout(null);
  
        JPanel donationPanel = new JPanel();
        mainPane.addTab("Donation", null, donationPanel, null);
        donationPanel.setLayout(null);
        
        JPanel inventoryPanel = new JPanel();
        mainPane.addTab("Inventory", null, inventoryPanel, null);
        inventoryPanel.setLayout(null);
        
        JPanel calculatorPanel = new JPanel();
        mainPane.addTab("Calculator", null, calculatorPanel, null);
        
        
		// The label that will display the title of the page
		/* NOTE Jaiven Comment:
		 * Title of the page: Financial
		 * 
		 * CHANGES:
		 * Removed the gray panel that was around the title of the page and moved the title to the top of the page with a custom font
		 * 
		 * PROBLEM: 
		 * The font size cannot increase because the label will be cut off
		 * 
		 * SOLUTION:
		 * Increase the label width and increase the font size
		 */
		// New code for the title of the page:
		JLabel lblFinancials = new JLabel("Financials");
		try {
		    Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
		    caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 40f);
		    lblFinancials.setFont(caveatBrush);
		} catch (IOException | FontFormatException e) {
		    lblFinancials.setFont(new Font("Tahoma", Font.BOLD, 23)); 
		    e.printStackTrace();
		}
		lblFinancials.setBounds(31, 14, 200, 35);
		frmFinancials.getContentPane().add(lblFinancials);
        

        
		// *******************************************************************************************************	        
        // Financial Components
        
        JButton btnHome = new JButton("Home");
        btnHome.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                frmFinancials.setVisible(false);
                Dashboard dashboardPage = new Dashboard();
                dashboardPage.frmDashboard.setVisible(true);
        	}
        });
        btnHome.setBounds(813, 21, 107, 37);
        frmFinancials.getContentPane().add(btnHome);
        
        
        // Filtering options for user based on the associated options
        String[] sortOptions = new String [] {"Product Name", "Product Type", "Product Status"};
        JComboBox<String> cboxOption = new JComboBox<>(sortOptions);
        cboxOption.setBounds(499, 37, 141, 21);
        financialsPanel.add(cboxOption);
        cboxOption.setSelectedIndex(2);
        cboxOption.setFont(new Font("Tahoma", Font.BOLD, 10));
        
        lblDisplayRevenue = new JLabel("$0.00");
        lblDisplayRevenue.setBounds(71, 491, 116, 13);
        financialsPanel.add(lblDisplayRevenue);
        
        lblDisplayProfit = new JLabel("$0.00");
        lblDisplayProfit.setBounds(266, 491, 89, 13);
        financialsPanel.add(lblDisplayProfit);
        
        JLabel lblTotalProfit = new JLabel("Total Profit");
        lblTotalProfit.setBounds(266, 428, 89, 13);
        financialsPanel.add(lblTotalProfit);
        
        JLabel lblTotalRevenue = new JLabel("Total Revenue");
        lblTotalRevenue.setBounds(71, 428, 116, 13);
        financialsPanel.add(lblTotalRevenue);
        
        JButton btnPrintReport = new JButton("Print Report");
        btnPrintReport.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		printReport();
        	}
        });
        btnPrintReport.setBounds(58, 566, 104, 21);
        financialsPanel.add(btnPrintReport);
        
        
        JScrollPane scrollPaneItems = new JScrollPane(tblFinacials);
        scrollPaneItems.setForeground(Color.WHITE);
        scrollPaneItems.setBounds(25, 75, 866, 300);
        financialsPanel.add(scrollPaneItems);
        
        JTextField txtSearchBox = new JTextField();
        txtSearchBox.setToolTipText("Please enter product name or type");
        txtSearchBox.setBounds(25, 37, 151, 21);
        financialsPanel.add(txtSearchBox);
        
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                searchFinancials(txtSearchBox.getText());
        	}
        });
        btnSearch.setBounds(238, 37, 81, 21);
        financialsPanel.add(btnSearch);
        btnSearch.setFont(new Font("Dialog", Font.BOLD, 12));
        
        JButton btnReturn = new JButton("Reset");
        btnReturn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                viewFinacials(); 
        	}
        });
        btnReturn.setBounds(349, 37, 100, 21);
        financialsPanel.add(btnReturn);
        btnReturn.setFont(new Font("Dialog", Font.BOLD, 12));
        
 
        
		// *******************************************************************************************************	        
        // Donation Components

        
        JScrollPane scrollPaneDonations = new JScrollPane(tblDonations);
        scrollPaneDonations.setBounds(29, 79, 866, 335);
        scrollPaneDonations.setForeground(Color.WHITE);
        donationPanel.add(scrollPaneDonations);
        
        JTextField txtSearchBoxDon = new JTextField();
        txtSearchBoxDon.setToolTipText("Please enter product name or type");
        txtSearchBoxDon.setBounds(29, 41, 151, 21);
        donationPanel.add(txtSearchBoxDon);
        
        JButton btnSearchDonations = new JButton("Search");
        btnSearchDonations.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                searchDonations(txtSearchBoxDon.getText());
        	}
        });
        btnSearchDonations.setBounds(242, 41, 81, 21);
        btnSearchDonations.setFont(new Font("Dialog", Font.BOLD, 12));
        donationPanel.add(btnSearchDonations);
        
		/*NOTE: Jaiven Comment:
		 * 
		 * CHANGES: Added a return button to reset the table content
		 * 
		 */
        JButton btnReturnDon = new JButton("Reset");
        btnReturnDon.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		viewDonations();
        	}
        });
        btnReturnDon.setBounds(353, 41, 100, 21);
        btnReturnDon.setFont(new Font("Dialog", Font.BOLD, 12));
        donationPanel.add(btnReturnDon);
        
        JComboBox<String> cboxOptionDon = new JComboBox<>(sortOptions);
        cboxOptionDon.setBounds(503, 41, 141, 21);
        cboxOptionDon.setSelectedIndex(2);
        cboxOptionDon.setFont(new Font("Tahoma", Font.BOLD, 10));
        donationPanel.add(cboxOptionDon);
        
        
		// *******************************************************************************************************	        
        // Donation Components
        JScrollPane scrollPaneInventory = new JScrollPane(tblInventory);
        scrollPaneInventory.setForeground(Color.WHITE);
        scrollPaneInventory.setBounds(29, 76, 856, 335);
        inventoryPanel.add(scrollPaneInventory);
        
        JButton btnReturnInv = new JButton("Reset");
        btnReturnInv.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		viewInventory();
        	}
        });
        btnReturnInv.setFont(new Font("Dialog", Font.BOLD, 12));
        btnReturnInv.setBounds(353, 38, 100, 21);
        inventoryPanel.add(btnReturnInv);
        
        JButton btnSearchInventory = new JButton("Search");
        btnSearchInventory.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                searchInventory(txtSearchBoxInv.getText());
        	}
        });
        btnSearchInventory.setFont(new Font("Dialog", Font.BOLD, 12));
        btnSearchInventory.setBounds(242, 38, 81, 21);
        inventoryPanel.add(btnSearchInventory);
        
        txtSearchBoxInv = new JTextField();
        txtSearchBoxInv.setToolTipText("Please enter product name or type");
        txtSearchBoxInv.setBounds(29, 38, 151, 21);
        inventoryPanel.add(txtSearchBoxInv);
        
        
        
        
        
		// *******************************************************************************************************	        
        // Calculator Components
        
        
        
        
        // Will call the function to display all information based on the time log column from database
        viewFinacials();
        viewDonations();
        viewInventory();
    }
    
    // Function to allow the user to search for finances 
 // Function to allow the user to search for finances by Item Name or Item Type
    private void searchFinancials(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frmFinancials, "Search bar cannot be empty. Please enter a valid value.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            viewFinacials();
            return;
        }
        
        String query = "SELECT i.ITEM_ID, i.ITEM_NM, i.MATERIAL_COST_AM, s.SALE_DT, s.QUANTITY_SOLD_NO, s.SALE_PRICE_AM " +
                      "FROM items i JOIN sales s ON i.ITEM_ID = s.ITEM_ID " +
                      "WHERE i.CATEGORY_CD = 'Sell' AND (i.ITEM_NM LIKE ? OR i.ITEM_TYPE_DE LIKE ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblFinacials.getModel();
            model.setRowCount(0);
            
            double totalRevenue = 0;
            double totalProfit = 0;
            DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Updated format

            while (rs.next()) {
                BigDecimal materialCost = rs.getBigDecimal("MATERIAL_COST_AM");
                BigDecimal salePrice = rs.getBigDecimal("SALE_PRICE_AM");
                int quantity = rs.getInt("QUANTITY_SOLD_NO");
                BigDecimal totalPrice = salePrice.multiply(new BigDecimal(quantity));
                
                String dateStr = rs.getString("SALE_DT");
                String formattedDate;
                
                try {
                    LocalDate date = LocalDate.parse(dateStr, dbFormatter);
                    formattedDate = date.format(displayFormatter);
                } catch (DateTimeParseException e1) {
                    try {
                        if (dateStr.matches("\\d+")) {
                            long timestamp = Long.parseLong(dateStr);
                            if (dateStr.length() > 10) timestamp /= 1000;
                            LocalDate date = LocalDate.ofEpochDay(timestamp / 86400);
                            formattedDate = date.format(displayFormatter);
                            updateSaleDate(rs.getInt("ITEM_ID"), date);
                        } else {
                            formattedDate = "Invalid Date";
                        }
                    } catch (NumberFormatException e2) {
                        formattedDate = "Invalid Date";
                    }
                }
                
                model.addRow(new Object[]{
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    materialCost,
                    formattedDate,
                    quantity,
                    salePrice,
                    totalPrice
                });
                
                totalRevenue += totalPrice.doubleValue();
                totalProfit += (totalPrice.doubleValue() - (materialCost.doubleValue() * quantity));
            }

            lblDisplayRevenue.setText(String.format("$%.2f", totalRevenue));
            lblDisplayProfit.setText(String.format("$%.2f", totalProfit));

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frmFinancials, "No items found matching: " + searchTerm, "Search Results", JOptionPane.INFORMATION_MESSAGE);
                viewFinacials();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error searching database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }    
 // Add this helper method before viewFinacials()
    private void updateSaleDate(int itemId, LocalDate correctDate) {
        String updateQuery = "UPDATE sales SET SALE_DT = ? WHERE ITEM_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, correctDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating date for item " + itemId);
            e.printStackTrace();
        }
    }
    
    private void updateDonationDate(int donationId, LocalDate correctDate) {
        String updateQuery = "UPDATE donations SET DONATION_DT = ? WHERE DONATION_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, correctDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            stmt.setInt(2, donationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating donation date " + donationId);
            e.printStackTrace();
        }
    }
    
    // Function to allow the user to fetch finacials information
    private void viewFinacials() {
        String query = "SELECT i.ITEM_ID, i.ITEM_NM, i.MATERIAL_COST_AM, s.SALE_DT, s.QUANTITY_SOLD_NO, s.SALE_PRICE_AM " +
                "FROM items i JOIN sales s ON i.ITEM_ID = s.ITEM_ID " +
                "WHERE i.CATEGORY_CD = 'Sell'";
        
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) tblFinacials.getModel();
            model.setRowCount(0);
            
            double totalRevenue = 0;
            double totalProfit = 0;
            DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // For database dates
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Changed to YYYY-MM-DD format

            while(rs.next()) {
                BigDecimal materialCost = rs.getBigDecimal("MATERIAL_COST_AM");
                BigDecimal salePrice = rs.getBigDecimal("SALE_PRICE_AM");
                int quantity = rs.getInt("QUANTITY_SOLD_NO");
                BigDecimal totalPrice = salePrice.multiply(new BigDecimal(quantity));
                
                // Handle date parsing and formatting
                String dateStr = rs.getString("SALE_DT");
                String displayDate;
                
                try {
                    // First try to parse as ISO date (YYYY-MM-DD)
                    LocalDate date = LocalDate.parse(dateStr, dbFormatter);
                    displayDate = date.format(displayFormatter);
                } catch (DateTimeParseException e1) {
                    try {
                        // If that fails, try to parse as timestamp (for existing malformed data)
                        if (dateStr.matches("\\d+")) {
                            long timestamp = Long.parseLong(dateStr);
                            if (dateStr.length() > 10) timestamp /= 1000;
                            LocalDate date = LocalDate.ofEpochDay(timestamp / 86400);
                            displayDate = date.format(displayFormatter);
                            updateSaleDate(rs.getInt("ITEM_ID"), date);
                        } else {
                            displayDate = "Invalid Date";
                        }
                    } catch (NumberFormatException e2) {
                        displayDate = "Invalid Date";
                    }
                }
                
                model.addRow(new Object[] {
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    materialCost,
                    displayDate,
                    quantity,
                    salePrice,
                    totalPrice
                });
                
                totalRevenue += totalPrice.doubleValue();
                totalProfit += (totalPrice.doubleValue() - (materialCost.doubleValue() * quantity));
            }
            
            // Update totals
            lblDisplayRevenue.setText(String.format("$%.2f", totalRevenue));
            lblDisplayProfit.setText(String.format("$%.2f", totalProfit));

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
 // Function to allow the user to search for donations by Item Name or Item Type
    // Updated searchDonations() with similar date handling
    private void searchDonations(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frmFinancials, "Search bar cannot be empty.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            viewDonations();
            return;
        }
        
        String query = "SELECT d.DONATION_ID, d.ITEM_ID, i.ITEM_NM, i.ITEM_TYPE_DE, d.DONATION_DT, d.QUANTITY_DONATED_NO " +
                     "FROM donations d JOIN items i ON d.ITEM_ID = i.ITEM_ID " +
                     "WHERE i.CATEGORY_CD = 'Donate' AND (i.ITEM_NM LIKE ? OR i.ITEM_TYPE_DE LIKE ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblDonations.getModel();
            model.setRowCount(0);
            
            DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Updated format

            while (rs.next()) {
                String dateStr = rs.getString("DONATION_DT");
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
                            updateDonationDate(rs.getInt("DONATION_ID"), date);
                        } else {
                            displayDate = "Invalid Date";
                        }
                    } catch (NumberFormatException e2) {
                        displayDate = "Invalid Date";
                    }
                }
                
                model.addRow(new Object[] {
                    rs.getInt("DONATION_ID"),
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    rs.getString("ITEM_TYPE_DE"),
                    displayDate,
                    rs.getInt("QUANTITY_DONATED_NO")
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frmFinancials, "No donations found.", 
                                            "Search Results", JOptionPane.INFORMATION_MESSAGE);
                viewDonations();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error searching donations.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    

    
    private void viewDonations() {
        String query = "SELECT d.DONATION_ID, d.ITEM_ID, i.ITEM_NM, i.ITEM_TYPE_DE, d.DONATION_DT, d.QUANTITY_DONATED_NO " +
                       "FROM donations d JOIN items i ON d.ITEM_ID = i.ITEM_ID " +
                       "WHERE i.CATEGORY_CD = 'Donate'";
        
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) tblDonations.getModel();
            model.setRowCount(0); 
            DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (rs.next()) {
                String dateStr = rs.getString("DONATION_DT");
                String displayDate;
                
                try {
                    // First try to parse as ISO date (YYYY-MM-DD)
                    LocalDate date = LocalDate.parse(dateStr, dbFormatter);
                    displayDate = date.format(displayFormatter);
                } catch (DateTimeParseException e1) {
                    try {
                        // If that fails, try to parse as timestamp (for existing malformed data)
                        if (dateStr.matches("\\d+")) {
                            long timestamp = Long.parseLong(dateStr);
                            if (dateStr.length() > 10) timestamp /= 1000;
                            LocalDate date = LocalDate.ofEpochDay(timestamp / 86400);
                            displayDate = date.format(displayFormatter);
                            updateDonationDate(rs.getInt("DONATION_ID"), date);
                        } else {
                            displayDate = "Invalid Date";
                        }
                    } catch (NumberFormatException e2) {
                        displayDate = "Invalid Date";
                    }
                }
                
                model.addRow(new Object[] {
                    rs.getInt("DONATION_ID"),
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    rs.getString("ITEM_TYPE_DE"),
                    displayDate,
                    rs.getInt("QUANTITY_DONATED_NO")
                });
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error loading donations: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }    
    }
    
    private void searchInventory(String searchTerm) {
    	if(searchTerm == null || searchTerm.trim().isEmpty()) {
	        JOptionPane.showMessageDialog(frmFinancials, "Search bar cannot be empty. Please enter a valid value.", "Validation Error", JOptionPane.WARNING_MESSAGE);
	        return;
    	}
    	
	    try {
	        // Query to search in both ITEM_NM and ITEM_TYPE_DE columns
	    	String query = "SELECT * FROM items WHERE (ITEM_NM LIKE ? OR ITEM_TYPE_DE LIKE ?) AND CATEGORY_CD = 'Inventory'";
	    	
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            // Bind parameters for both search columns
	            stmt.setString(1, "%" + searchTerm + "%");
	            stmt.setString(2, "%" + searchTerm + "%");

	            ResultSet rs = stmt.executeQuery();
	            DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();

	            // Clear previous search results from the table
	            model.setRowCount(0);

	            boolean found = false;

	            while (rs.next()) {
	                found = true;
	                model.addRow(new Object[]{
	    				rs.getInt("ITEM_ID"),
	    				rs.getString("ITEM_NM"),
	    				rs.getString("ITEM_TYPE_DE"),
	    				rs.getString("DATE_CREATED_DT"),
	                });
	            }

	            // If no results were found, display a message
	            if (!found) {
	                JOptionPane.showMessageDialog(frmFinancials, "No results found for \"" + searchTerm + "\"", "Search Result", JOptionPane.INFORMATION_MESSAGE);
	            }

	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(frmFinancials, "Error fetching data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();
	        }

	    } catch (Exception e) {
	        // General error for any other exception
	        JOptionPane.showMessageDialog(frmFinancials, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
    }
    
    private void viewInventory(){
    	String query = "SELECT * FROM items WHERE CATEGORY_CD = 'Inventory'";
		try(PreparedStatement stmt = conn.prepareStatement(query)){
	        ResultSet rs = stmt.executeQuery();
	        
			DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
			model.setRowCount(0);
			
			while (rs.next()) {
				model.addRow(new Object[] {
					rs.getInt("ITEM_ID"),
					rs.getString("ITEM_NM"),
					rs.getString("ITEM_TYPE_DE"),
					rs.getString("DATE_CREATED_DT"),
				});
			}

		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
		
		
    // Function to print the report and save it to a file 
    private void printReport() {
        try {
            // Ensure the directory exists
            File directory = new File("reports");
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it doesn't exist
            }

            // Capture the content of the JFrame
            BufferedImage image = new BufferedImage(frmFinancials.getWidth(), frmFinancials.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();

            // Render the frame to the image
            frmFinancials.paint(graphics);

            // Generate a timestamp for the filename
            String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
            File file = new File("reports/financialReport_" + timestamp + ".png");

            // Save the image to a file (PNG format)
            ImageIO.write(image, "PNG", file);

            JOptionPane.showMessageDialog(frmFinancials, "Report exported successfully!", "Export Success", JOptionPane.INFORMATION_MESSAGE);

        } catch(IOException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error exporting report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}