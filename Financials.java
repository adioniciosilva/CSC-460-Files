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
import javax.swing.SpinnerNumberModel;
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
import javax.swing.JScrollBar;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import java.math.RoundingMode;

public class Financials {

	// Database connection variables
    Connection conn = null;
//  // Connection based on SQLite within the Project Folder
//  String dbConnect = "jdbc:sqlite:../project/database/mamaspiddlins.sqlite";

    public JFrame frmFinancials;
    public JTable tblFinacials;
    public JTable tblDonations;
    public JTable tblInventory;
    private JLabel lblDisplayRevenue;
    private JLabel lblDisplayProfit;
    private JTextField txtSearchBoxInv;
    private JTextField txtProfitMargin;
    private JTextField txtMaterialCost;
    private JTextField txtVolumeAdjust;
    private JSpinner spinnerQuantitySold;
    private JLabel lblDisplayRecommended;
    private JLabel lblDisplayAdjusted;
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
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
    
        // The beginning setup for the Financials Page
        frmFinancials = new JFrame();
        frmFinancials.setTitle("Financials");
        frmFinancials.setBounds(100, 100, 1000, 700); // Increased width to accommodate more columns
        frmFinancials.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmFinancials.getContentPane().setBackground(new Color(216, 203, 175));
        frmFinancials.getContentPane().setLayout(null);
        
        // Allows user to view a table with associated columns within the Finacials tab
        tblFinacials = new JTable();
        tblFinacials.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[] {"Product ID", "Product Name", "Material Costs", "Sale Date", "Quantity Sold", "Total Price"}    
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
        mainPane.setBounds(31, 59, 900, 550);
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
        calculatorPanel.setLayout(null);
        

        
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
        btnHome.setBounds(824, 24, 107, 37);
        frmFinancials.getContentPane().add(btnHome);
        
        
        // Filtering options for user based on the associated options
        String[] sortOptions = new String [] {"Product Name", "Product Type", "Product Status"};
        JComboBox<String> cboxOption = new JComboBox<>(sortOptions);
        cboxOption.setBounds(499, 37, 141, 21);
        financialsPanel.add(cboxOption);
        cboxOption.setSelectedIndex(2);
        cboxOption.setFont(new Font("Tahoma", Font.BOLD, 10));
        
        // The label that will display the total revenue price
        lblDisplayRevenue = new JLabel("$0.00");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblDisplayRevenue.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblDisplayRevenue.setBounds(71, 420, 116, 21);
        financialsPanel.add(lblDisplayRevenue);
        
        // The label that will display the total profit price
        lblDisplayProfit = new JLabel("$0.00");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblDisplayProfit.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblDisplayProfit.setBounds(266, 420, 89, 21);
        financialsPanel.add(lblDisplayProfit);

        
		// The label that will display the total profit		
        JLabel lblTotalProfit = new JLabel("Total Profit");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblTotalProfit.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblTotalProfit.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblTotalProfit.setBounds(266, 385, 89, 25);
        financialsPanel.add(lblTotalProfit);	
        
        
		// The label that will display the total revenue		
        JLabel lblTotalRevenue = new JLabel("Total Revenue");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblTotalRevenue.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblTotalRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblTotalRevenue.setBounds(71, 385, 116, 25);
        financialsPanel.add(lblTotalRevenue);	
        
        
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
                viewFinancials(); 
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
        
        
        JLabel lblMaterialCost = new JLabel("Material Cost ($)");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblMaterialCost.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblMaterialCost.setBounds(39, 47, 129, 31);
        calculatorPanel.add(lblMaterialCost);
        


        JLabel lblProfitMargin = new JLabel("Desired Profit Margin (%)");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblProfitMargin.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblProfitMargin.setBounds(225, 47, 199, 31);
        calculatorPanel.add(lblProfitMargin);
        
        
        spinnerQuantitySold = new JSpinner();
        spinnerQuantitySold.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        spinnerQuantitySold.setBounds(434, 107, 129, 20);
        calculatorPanel.add(spinnerQuantitySold);

        
        JLabel lblQuantitySold = new JLabel("Quantity Sold");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblQuantitySold.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblQuantitySold.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblQuantitySold.setBounds(434, 47, 149, 31);
        calculatorPanel.add(lblQuantitySold);
        

        txtProfitMargin = new JTextField();
        txtProfitMargin.setText("0"); 
        txtProfitMargin.setBounds(225, 107, 149, 19);
        calculatorPanel.add(txtProfitMargin);
        txtProfitMargin.setColumns(10);

        txtMaterialCost = new JTextField();
        txtMaterialCost.setColumns(10);
        txtMaterialCost.setBounds(39, 107, 143, 19);
        calculatorPanel.add(txtMaterialCost);
        
        
        JLabel lblVolumeAdjust = new JLabel("Material Cost ($)");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblVolumeAdjust.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblVolumeAdjust.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblVolumeAdjust.setBounds(620, 56, 178, 13);
        calculatorPanel.add(lblVolumeAdjust);

        txtVolumeAdjust = new JTextField();
        txtVolumeAdjust.setText("1.0"); // Default no adjustment
        txtVolumeAdjust.setColumns(10);
        txtVolumeAdjust.setBounds(620, 107, 112, 19);
        calculatorPanel.add(txtVolumeAdjust);
        

        JLabel lblDisplayAdjusted = new JLabel("$0.00");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblDisplayAdjusted.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayAdjusted.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblDisplayAdjusted.setBounds(364, 338, 185, 13);
        calculatorPanel.add(lblDisplayAdjusted);
        
        
        JLabel lblDisplayRecommended = new JLabel("$0.00");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblDisplayRecommended.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRecommended.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblDisplayRecommended.setBounds(39, 338, 315, 13);
        calculatorPanel.add(lblDisplayRecommended);
        
        JLabel lblRecommendPrice = new JLabel("Material Cost ($)");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblRecommendPrice.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblRecommendPrice.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblRecommendPrice.setBounds(39, 280, 208, 31);
        calculatorPanel.add(lblRecommendPrice);
        
        
        JLabel lblAdjustPrice = new JLabel("Material Cost ($)");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 20f);
            lblAdjustPrice.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblAdjustPrice.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblAdjustPrice.setBounds(364, 280, 185, 31);
        calculatorPanel.add(lblAdjustPrice);
        
                JButton btnCalculate = new JButton("Calculate");
                btnCalculate.setBounds(39, 189, 107, 37);
                calculatorPanel.add(btnCalculate);
                btnCalculate.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        calculatePrices();
                    }
                });

        
        JButton btnPrintReport = new JButton("Print Report");
        btnPrintReport.setBounds(681, 26, 107, 35);
        frmFinancials.getContentPane().add(btnPrintReport);
        btnPrintReport.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		printReport();
        	}
        });
       
        // Will call the function to display all information based on the time log column from database
        viewFinancials();
        viewDonations();
        viewInventory();
    }
    
 // Function to allow the user to search for finances by item name or item type
    private void searchFinancials(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frmFinancials, "Search bar cannot be empty.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            viewFinancials();
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

            BigDecimal totalRevenue = BigDecimal.ZERO;
            BigDecimal totalMaterialCost = BigDecimal.ZERO;

            while (rs.next()) {
                BigDecimal materialCost = rs.getBigDecimal("MATERIAL_COST_AM");
                BigDecimal salePrice = rs.getBigDecimal("SALE_PRICE_AM");

                totalRevenue = totalRevenue.add(salePrice);
                totalMaterialCost = totalMaterialCost.add(materialCost); // No quantity multiplier

                // Add row to table (unchanged)
                model.addRow(new Object[]{
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    materialCost,
                    rs.getString("SALE_DT"),
                    rs.getInt("QUANTITY_SOLD_NO"),
                    salePrice
                });
            }

            // Profit = Revenue - Material Costs (no quantity multiplier)
            BigDecimal totalProfit = totalRevenue.subtract(totalMaterialCost);

            lblDisplayRevenue.setText(String.format("$%.2f", totalRevenue));
            lblDisplayProfit.setText(String.format("$%.2f", totalProfit));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error searching database: " + ex.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
//  Add this helper method before viewFinancials()
//    private void updateSaleDate(int itemId, LocalDate correctDate) {
//        String updateQuery = "UPDATE sales SET SALE_DT = ? WHERE ITEM_ID = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
//            stmt.setString(1, correctDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//            stmt.setInt(2, itemId);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            System.err.println("Error updating date for item " + itemId);
//            e.printStackTrace();
//        }
//    }
    
    private void updateDonationDate(int donationId, LocalDate correctDate) {
        String updateQuery = "UPDATE donations SET DONATION_DT = ? WHERE DONATION_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, correctDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            stmt.setInt(2, donationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating donation date for donation " + donationId);
            e.printStackTrace();
        }
    }
    
    private void viewFinancials() {
        String query = "SELECT i.ITEM_ID, i.ITEM_NM, i.MATERIAL_COST_AM, s.SALE_DT, s.QUANTITY_SOLD_NO, s.SALE_PRICE_AM " +
                      "FROM items i JOIN sales s ON i.ITEM_ID = s.ITEM_ID " +
                      "WHERE i.CATEGORY_CD = 'Sell'";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblFinacials.getModel();
            model.setRowCount(0);

            BigDecimal totalRevenue = BigDecimal.ZERO;
            BigDecimal totalMaterialCost = BigDecimal.ZERO; // Sum of material costs (no quantity)
            DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (rs.next()) {
                BigDecimal materialCost = rs.getBigDecimal("MATERIAL_COST_AM");
                BigDecimal salePrice = rs.getBigDecimal("SALE_PRICE_AM");
                int quantity = rs.getInt("QUANTITY_SOLD_NO");

                // Revenue: Sum of sale prices (no quantity multiplier)
                totalRevenue = totalRevenue.add(salePrice);

                // Material Cost: Sum of material costs (no quantity multiplier)
                totalMaterialCost = totalMaterialCost.add(materialCost);

                // Add row to table
                model.addRow(new Object[]{
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    materialCost,
                    rs.getString("SALE_DT"), // Directly display date (formatting omitted for brevity)
                    quantity,
                    salePrice
                });
            }

            // Profit = Total Revenue - Total Material Cost (no quantity multiplier)
            BigDecimal totalProfit = totalRevenue.subtract(totalMaterialCost);

            // Update labels
            lblDisplayRevenue.setText(String.format("$%.2f", totalRevenue));
            lblDisplayProfit.setText(String.format("$%.2f", totalProfit));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error loading financial data: " + ex.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
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
		
    private void calculatePrices() {
        try {
            // Get input values
            BigDecimal materialCost = new BigDecimal(txtMaterialCost.getText());
            BigDecimal profitMargin = new BigDecimal(txtProfitMargin.getText()).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
            int quantitySold = (Integer) spinnerQuantitySold.getValue();
            BigDecimal volumeAdjustFactor = new BigDecimal(txtVolumeAdjust.getText());

            // Calculate base recommended price (material cost / (1 - profit margin))
            BigDecimal basePrice = materialCost.divide(BigDecimal.ONE.subtract(profitMargin), 2, RoundingMode.HALF_UP);
            
            // Calculate volume-adjusted price
            BigDecimal adjustedPrice = basePrice.multiply(volumeAdjustFactor).setScale(2, RoundingMode.HALF_UP);
            
            // Calculate total revenue (adjusted price * quantity)
            BigDecimal totalRevenue = adjustedPrice.multiply(new BigDecimal(quantitySold));
            
            // Calculate total profit (revenue - (material cost * quantity))
            BigDecimal totalProfit = totalRevenue.subtract(materialCost.multiply(new BigDecimal(quantitySold)));

            // Display results
            lblDisplayRecommended.setText(String.format("$%.2f (Revenue: $%.2f, Profit: $%.2f)", 
                basePrice, totalRevenue, totalProfit));
            lblDisplayAdjusted.setText(String.format("$%.2f per unit", adjustedPrice));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frmFinancials, 
                "Please enter valid numbers in all fields", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmFinancials, 
                "An error occurred during calculation: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
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