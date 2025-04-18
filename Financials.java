// Packages to import for the java project
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
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
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FontFormatException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import java.math.RoundingMode;
import javax.swing.table.TableRowSorter; // Used for the table sorting
import java.util.Collections; // Used for the table sorting
import javax.swing.border.LineBorder;



public class Financials {

	// Database connection variables
    Connection conn = null;

    // Declaring variable types
    public JFrame frmFinancials;
    public JTable tblFinacials;
    public JTable tblDonations;
    public JTable tblInventory;
    private JLabel lblDisplayRevenue;
    private JLabel lblDisplayProfit;
    private JTextField txtSearchBoxInv;
    private JTextField txtHoursSpent;
    private JTextField txtMaterialCost;
    private JSpinner spinnerQuantitySold;
    private JLabel lblDisplayRecommended;
    private JLabel lblDisplayAdjusted;
    private final DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
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
		// Will ensure a connection to a SQLite database
        try {
	    	// Load JDBC driver and establish database connection
            Class.forName("org.sqlite.JDBC");
            String dbPath = new File("database/mamaspiddlins.sqlite").getAbsolutePath();
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            
	        // Prints a success if connection works or error message if not
            if (conn != null) {
                System.out.println("Connection successful");
                initialize();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to connect to database", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
    	// Displays the error message to the user 
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
    
        // The beginning setup for the Financials Page
        frmFinancials = new JFrame();
        frmFinancials.setTitle("Financials");
        frmFinancials.setBounds(100, 100, 1000, 700); // Increased width to accommodate more columns
        frmFinancials.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmFinancials.getContentPane().setBackground(new Color(216, 203, 175));
        frmFinancials.getContentPane().setLayout(null);
        
		// Allows a custom window icon
		Image icon = Toolkit.getDefaultToolkit().getImage("images/bearLogo.png");
		frmFinancials.setIconImage(icon);
		
        // Allows user to view a table with associated columns within the Donations tab
        tblDonations = new JTable();
        tblDonations.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Donation ID", "Product ID", "Product Name", "Product Type", 
                         "Donation Date", "Donation Quantity", "Material Cost"}
        ));
        
        // Disable editing in the table
        tblDonations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDonations.setDefaultEditor(Object.class, null);
        tblDonations.getTableHeader().setReorderingAllowed(false);
        tblDonations.getTableHeader().setResizingAllowed(false);
        
        // Will be used to allow sorting for the tables by ascending/descending order
        TableRowSorter<DefaultTableModel> donationSorter = new TableRowSorter<>((DefaultTableModel) tblDonations.getModel());
        tblDonations.setRowSorter(donationSorter);

        // Will allow the sorting by column header
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
        
        // Allows user to view a table with associated columns within the Inventory tab
        tblInventory = new JTable();
        tblInventory.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Inventory ID", "Product Name", "Product Type", 
                         "Inventory Date", "Material Cost"}
        ));
        
        // Disable editing in the table
        tblInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblInventory.setDefaultEditor(Object.class, null);
        tblInventory.getTableHeader().setReorderingAllowed(false);
        tblInventory.getTableHeader().setResizingAllowed(false);
        
		// Will be used to allow sorting for the tables by ascending/descending order
        TableRowSorter<DefaultTableModel> inventorySorter = new TableRowSorter<>((DefaultTableModel) tblInventory.getModel());
        tblInventory.setRowSorter(inventorySorter);

		// Will allow the sorting by column header
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
        
        
		// The main panel for the panes, with each a represented tab
        JTabbedPane mainPane = new JTabbedPane(JTabbedPane.TOP);
        mainPane.setBounds(31, 59, 918, 570);
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
     
		// New code for the title of the page:
        // by: Jaiven Harris 
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
        
		// A button that will allow the user to return back to the home screen
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
        
        
        // The label that will display the total revenue price
        // by: Jaiven Harris 
        lblDisplayRevenue = new JLabel("$0.00");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblDisplayRevenue.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        
        // Allows user to view a table with associated columns within the Financials tab
        tblFinacials = new JTable();
        tblFinacials.setBorder(new LineBorder(new Color(64, 64, 64)));
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
                 
		// The table that that will display the information to the user 
        JScrollPane scrollPaneItems = new JScrollPane(tblFinacials);
        scrollPaneItems.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPaneItems.setViewportBorder(null);
        scrollPaneItems.setForeground(Color.WHITE);
        scrollPaneItems.setBounds(25, 75, 866, 300);
        financialsPanel.add(scrollPaneItems);
        
        
        
        lblDisplayRevenue.setBounds(71, 420, 786, 21);
        financialsPanel.add(lblDisplayRevenue);
        
        // The label that will display the total profit price
        // by: Jaiven Harris 
        lblDisplayProfit = new JLabel("$0.00");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblDisplayProfit.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblDisplayProfit.setBounds(71, 502, 786, 21);
        financialsPanel.add(lblDisplayProfit);

        
		// The label that will display the total profit		
        // by: Jaiven Harris 
        JLabel lblTotalProfit = new JLabel("Total Profit");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblTotalProfit.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblTotalProfit.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblTotalProfit.setBounds(71, 467, 89, 25);
        financialsPanel.add(lblTotalProfit);	
        
        
		// The label that will display the total revenue	
        // by: Jaiven Harris 
        JLabel lblTotalRevenue = new JLabel("Total Revenue");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblTotalRevenue.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblTotalRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblTotalRevenue.setBounds(71, 385, 116, 25);
        financialsPanel.add(lblTotalRevenue);	
        
		// A textfield to allow user to input product name or type 
        JTextField txtSearchBox = new JTextField();
        txtSearchBox.setToolTipText("Please enter product name or type");
        txtSearchBox.setBounds(25, 37, 220, 21);
        financialsPanel.add(txtSearchBox);
        
		// The button to search for items along with the use of a textfield
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                searchFinancials(txtSearchBox.getText());
        	}
        });
        btnSearch.setBounds(285, 36, 81, 21);
        financialsPanel.add(btnSearch);
        btnSearch.setFont(new Font("Dialog", Font.BOLD, 12));
        
        // The button to allow the user to reset the table content
        // by: Jaiven Harris 
        JButton btnReturn = new JButton("Reset");
        btnReturn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                viewFinancials(); 
        	}
        });
        btnReturn.setBounds(386, 36, 100, 21);
        financialsPanel.add(btnReturn);
        btnReturn.setFont(new Font("Dialog", Font.BOLD, 12));
        

        
		// *******************************************************************************************************	        
        // Financial Components
		
		// A panel for displaying the labels
        JPanel viewPanel = new JPanel();
        viewPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        viewPanel.setBounds(25, 385, 866, 148);
        financialsPanel.add(viewPanel);
        viewPanel.setLayout(null);
        

        
        
        
		// *******************************************************************************************************	        
        // Donation Components     
        
		// The table that that will display the information to the user 
        JScrollPane scrollPaneDonations = new JScrollPane(tblDonations);
        scrollPaneDonations.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPaneDonations.setViewportBorder(null);
        scrollPaneDonations.setForeground(Color.WHITE);
        scrollPaneDonations.setBounds(29, 79, 866, 335);
        donationPanel.add(scrollPaneDonations);
        
		// A textfield to search for items along with the use of a button
        JTextField txtSearchBoxDon = new JTextField();
        txtSearchBoxDon.setToolTipText("Please enter product name or type");
        txtSearchBoxDon.setBounds(29, 41, 222, 21);
        donationPanel.add(txtSearchBoxDon);
        
		// The button to search for items along with the use of a textfield
        JButton btnSearchDonations = new JButton("Search");
        btnSearchDonations.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                searchDonations(txtSearchBoxDon.getText());
        	}
        });
        btnSearchDonations.setBounds(278, 40, 81, 21);
        btnSearchDonations.setFont(new Font("Dialog", Font.BOLD, 12));
        donationPanel.add(btnSearchDonations);
        
        
        // The button to allow the user to reset the table content
        // by: Jaiven Harris 
        JButton btnReturnDon = new JButton("Reset");
        btnReturnDon.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		viewDonations();
        	}
        });
        btnReturnDon.setBounds(382, 40, 100, 21);
        btnReturnDon.setFont(new Font("Dialog", Font.BOLD, 12));
        donationPanel.add(btnReturnDon);
        
        
		// *******************************************************************************************************	        
        // Inventory Components
        
		// The table that that will display the information to the user 
        JScrollPane scrollPaneInventory = new JScrollPane(tblInventory);
        scrollPaneInventory.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPaneInventory.setViewportBorder(null);
        scrollPaneInventory.setForeground(Color.WHITE);
        scrollPaneInventory.setForeground(Color.WHITE);
        scrollPaneInventory.setBounds(29, 76, 856, 335);
        inventoryPanel.add(scrollPaneInventory);
        
        // The button to allow the user to reset the table content
        // by: Jaiven Harris 
        JButton btnReturnInv = new JButton("Reset");
        btnReturnInv.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		viewInventory();
        	}
        });
        btnReturnInv.setFont(new Font("Dialog", Font.BOLD, 12));
        btnReturnInv.setBounds(372, 38, 100, 21);
        inventoryPanel.add(btnReturnInv);
        
		// The button to search for items along with the use of a textfield
        JButton btnSearchInventory = new JButton("Search");
        btnSearchInventory.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                searchInventory(txtSearchBoxInv.getText());
        	}
        });
        btnSearchInventory.setFont(new Font("Dialog", Font.BOLD, 12));
        btnSearchInventory.setBounds(266, 38, 81, 21);
        inventoryPanel.add(btnSearchInventory);
        
		// A textfield to search for items along with the use of a button
        txtSearchBoxInv = new JTextField();
        txtSearchBoxInv.setToolTipText("Please enter product name or type");
        txtSearchBoxInv.setBounds(29, 38, 221, 21);
        inventoryPanel.add(txtSearchBoxInv);
        
        // A label that will display to the user material cost
        // by: Jaiven Harris 
        JLabel lblMaterialCost = new JLabel("Material Cost ($)");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblMaterialCost.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblMaterialCost.setBounds(39, 47, 129, 31);
        calculatorPanel.add(lblMaterialCost);
        

        // A label that will display the profit margin to the user
        // by: Jaiven Harris 
        JLabel lblProfitMargin = new JLabel("Time Spent in Hours");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblProfitMargin.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRevenue.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblProfitMargin.setBounds(253, 47, 199, 31);
        calculatorPanel.add(lblProfitMargin);
        
        // A spinner for product quantity inputs
        spinnerQuantitySold = new JSpinner();
        spinnerQuantitySold.setToolTipText("Enter the number of products that will be sold");
        spinnerQuantitySold.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        spinnerQuantitySold.setBounds(510, 107, 129, 20);
        calculatorPanel.add(spinnerQuantitySold);

        // A label that display to the user quantity sold
        // by: Jaiven Harris 
        JLabel lblQuantitySold = new JLabel("Quantity Sold");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblQuantitySold.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblQuantitySold.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblQuantitySold.setBounds(510, 47, 149, 31);
        calculatorPanel.add(lblQuantitySold);
        

		// A textfield that will take a material cost input 
        txtMaterialCost = new JTextField();
        txtMaterialCost.setToolTipText("Enter the amount spent on material cost");
        txtMaterialCost.setColumns(10);
        txtMaterialCost.setBounds(253, 107, 143, 19);
        calculatorPanel.add(txtMaterialCost);
       
		// A textfield that will take the hours input 
        txtHoursSpent = new JTextField();
        txtHoursSpent.setToolTipText("Enter the amount spent on material cost");
        txtHoursSpent.setColumns(10);
        txtHoursSpent.setBounds(39, 107, 143, 19);
        calculatorPanel.add(txtHoursSpent);

        
		// A label that will display to the user price adjusted
        // by: Jaiven Harris 
        lblDisplayAdjusted = new JLabel("$0.00");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblDisplayAdjusted.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayAdjusted.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblDisplayAdjusted.setBounds(39, 334, 806, 37);
        calculatorPanel.add(lblDisplayAdjusted);
        
		// A label that will display to the user recommended price
        // by: Jaiven Harris 
        lblDisplayRecommended = new JLabel("$0.00");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblDisplayRecommended.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblDisplayRecommended.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblDisplayRecommended.setBounds(39, 212, 806, 37);
        calculatorPanel.add(lblDisplayRecommended);
        
		// A label that will display to the base recommended price text
        // by: Jaiven Harris 
        JLabel lblRecommendPrice = new JLabel("Base Recommended Price");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblRecommendPrice.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblRecommendPrice.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblRecommendPrice.setBounds(39, 171, 208, 31);
        calculatorPanel.add(lblRecommendPrice);
        
		// A label that will display to the user adjusted price text
        // by: Jaiven Harris 
        JLabel lblAdjustPrice = new JLabel("Adjusted Price");
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 18f);
            lblAdjustPrice.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblAdjustPrice.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblAdjustPrice.setBounds(39, 293, 185, 31);
        calculatorPanel.add(lblAdjustPrice);
        
		// A button to calculate product price based on the associated fields
        JButton btnCalculate = new JButton("Calculate");
        btnCalculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculatePrices();
            }
        });
        btnCalculate.setBounds(39, 436, 107, 37);
        calculatorPanel.add(btnCalculate);

		// A button to clear product price based on the associated fields
        JButton btnClearCalculate = new JButton("Clear");
        btnClearCalculate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 clearCalculator();
        	}
        });
        btnClearCalculate.setBounds(203, 436, 107, 37);
        calculatorPanel.add(btnClearCalculate);
        
        
        
		// *******************************************************************************************************	        
        // Calculator Components
        
		// A panel for displaying the labels
        JPanel calculatorViewPanel = new JPanel();
        calculatorViewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        calculatorViewPanel.setBounds(20, 47, 806, 350);
        calculatorPanel.add(calculatorViewPanel);
        

        // A button to print the screen as a png
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
	    // Checks if the input is empty, and displays message to user
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frmFinancials, "Search bar cannot be empty.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            viewFinancials();
            return;
        }
        
        // Uses a query to search financial data
        String query = "SELECT i.ITEM_ID, i.ITEM_NM, i.MATERIAL_COST_AM, s.SALE_DT, s.QUANTITY_SOLD_NO, s.SALE_PRICE_AM " +
                      "FROM items i JOIN sales s ON i.ITEM_ID = s.ITEM_ID " +
                      "WHERE i.CATEGORY_CD = 'Sell' AND (i.ITEM_NM LIKE ? OR i.ITEM_TYPE_DE LIKE ?)";
        
        // Will execute the query and clear the existing rows from the associated tables
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Allows when searching to match with anything within parameters
        	stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            
            // Will execute the query and clear the existing rows from the associated table
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblFinacials.getModel();
            model.setRowCount(0);

            BigDecimal totalRevenue = BigDecimal.ZERO;
            BigDecimal totalMaterialCost = BigDecimal.ZERO;
            
            // Will go through the results and add the items info to the table to be displayed
            while (rs.next()) {
                BigDecimal materialCost = rs.getBigDecimal("MATERIAL_COST_AM");
                BigDecimal salePrice = rs.getBigDecimal("SALE_PRICE_AM");
                
                // Format the dates
                String saleDateStr = rs.getString("SALE_DT");
                String displayDate;
                try {
                    // First, try ISO format
                    LocalDate saleDate = LocalDate.parse(saleDateStr, dbFormatter);
                    displayDate = saleDate.format(displayFormatter);
                } catch (DateTimeParseException e1) {
                    try {
                        // If that fails, trys timestamp format
                        if (saleDateStr.matches("\\d+")) {
                            long timestamp = Long.parseLong(saleDateStr);
                            if (saleDateStr.length() > 10) timestamp /= 1000;
                            LocalDate saleDate = LocalDate.ofEpochDay(timestamp / 86400);
                            displayDate = saleDate.format(displayFormatter);
                            updateSaleDate(rs.getInt("ITEM_ID"), saleDate);
                        } else {
                            displayDate = "Invalid Date";
                        }
                    } catch (NumberFormatException e) {
                        displayDate = "Invalid Date";
                    }
                }
                
                // Format the numbers to always show 2 decimal places
                String formattedMaterialCost = String.format("%.2f", materialCost);
                String formattedSalePrice = String.format("%.2f", salePrice);
            
                // Will update the total amount
                totalRevenue = totalRevenue.add(salePrice);
                totalMaterialCost = totalMaterialCost.add(materialCost); // No quantity multiplier

                // Add row to the associated table
                model.addRow(new Object[]{
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    formattedMaterialCost,
                    displayDate,
                    rs.getInt("QUANTITY_SOLD_NO"),
                    formattedSalePrice
                });
            }

            // Calculate and display based on formula, profit = revenue - material costs 
            BigDecimal totalProfit = totalRevenue.subtract(totalMaterialCost);
            lblDisplayRevenue.setText(String.format("$%.2f", totalRevenue));
            lblDisplayProfit.setText(String.format("$%.2f", totalProfit));

	    // If error with the database, then a message will display
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error searching database: " + ex.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    // Function that will update donation dates within the database
    private void updateDonationDate(int donationId, LocalDate correctDate) {
    	// A query that updates only specified items for donations
        String updateQuery = "UPDATE donations SET DONATION_DT = ? WHERE DONATION_ID = ?";
        
		// Will prepare SQL statement to safely and ensure the preparedStatement is closed when done
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, correctDate.format(dbFormatter));
            stmt.setInt(2, donationId);
            stmt.executeUpdate();
        
        // If error with the database, then a message will display
        } catch (SQLException e) {
            System.err.println("Error updating donation date for donation " + donationId);
            e.printStackTrace();
        }
    }
    
    // Function that will display and load the financial data based on sales to the table
    private void viewFinancials() {
    	// Uses a query to join product and sales table based on sells items 
        String query = "SELECT i.ITEM_ID, i.ITEM_NM, i.MATERIAL_COST_AM, s.SALE_DT, s.QUANTITY_SOLD_NO, s.SALE_PRICE_AM " +
                      "FROM items i JOIN sales s ON i.ITEM_ID = s.ITEM_ID " +
                      "WHERE i.CATEGORY_CD = 'Sell'";
        
		// Will prepare SQL statement to safely and ensure the preparedStatement is closed when done
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Will execute the query and clear the existing rows from the associated table
        	ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblFinacials.getModel();
            model.setRowCount(0);

            // Will be used for tracking running totals for revenue and costs
            BigDecimal totalRevenue = BigDecimal.ZERO;
            BigDecimal totalMaterialCost = BigDecimal.ZERO; 

            while (rs.next()) {
                BigDecimal materialCost = rs.getBigDecimal("MATERIAL_COST_AM");
                BigDecimal salePrice = rs.getBigDecimal("SALE_PRICE_AM");
                int quantity = rs.getInt("QUANTITY_SOLD_NO");
                
                String saleDateStr = rs.getString("SALE_DT");
                String displayDate;
                try {
                    // First, try ISO format
                    LocalDate saleDate = LocalDate.parse(saleDateStr, dbFormatter);
                    displayDate = saleDate.format(displayFormatter);
                    
                } catch (DateTimeParseException e1) {
                    try {
                        // If that fails, trys timestamp format
                        if (saleDateStr.matches("\\d+")) {
                            long timestamp = Long.parseLong(saleDateStr);
                            if (saleDateStr.length() > 10) timestamp /= 1000;
                            LocalDate saleDate = LocalDate.ofEpochDay(timestamp / 86400);
                            displayDate = saleDate.format(displayFormatter);
                            // Update the database with corrected date
                            updateSaleDate(rs.getInt("ITEM_ID"), saleDate);
                        } else {
                            displayDate = "Invalid Date";
                        }
                    } catch (NumberFormatException e2) {
                        displayDate = "Invalid Date";
                    }
                }
                
                // Format the numbers to always show 2 decimal places
                String formattedMaterialCost = String.format("%.2f", materialCost);
                String formattedSalePrice = String.format("%.2f", salePrice);
                		
                // Revenue: Sum of sale prices
                totalRevenue = totalRevenue.add(salePrice);

                // Material Cost: Sum of material costs 
                totalMaterialCost = totalMaterialCost.add(materialCost);

                // Add row to the associated table
                model.addRow(new Object[]{
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    formattedMaterialCost,
                    displayDate, 
                    quantity,
                    formattedSalePrice      
                });
            }

            // Profit = Total Revenue - Total Material Cost 
            BigDecimal totalProfit = totalRevenue.subtract(totalMaterialCost);

            // Will update the associated labels
            lblDisplayRevenue.setText(String.format("$%.2f", totalRevenue));
            lblDisplayProfit.setText(String.format("$%.2f", totalProfit));

	   	// If error with the database, then a message will display
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error loading financial data: " + ex.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    // Function that will update sales date within the database
    private void updateSaleDate(int itemId, LocalDate correctDate) {
    	// A query that updates only specified items for sales
        String updateQuery = "UPDATE sales SET SALE_DT = ? WHERE ITEM_ID = ?";
        
		// Will prepare SQL statement to safely and ensure the preparedStatement is closed when done
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, correctDate.format(dbFormatter));
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
         
   	    // If error with the database, then a message will display
        } catch (SQLException e) {
            System.err.println("Error updating sale date for item " + itemId);
            e.printStackTrace();
        }
    }
    
 // Function to allow the user to search for donations by item name or item type
    private void searchDonations(String searchTerm) {
	    // Checks if the input is empty, and displays message to user
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frmFinancials, "Search bar cannot be empty.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            viewDonations();
            return;
        }
        
        // Use a query to join donations with product info filtered by donation items
        String query = "SELECT d.DONATION_ID, d.ITEM_ID, i.ITEM_NM, i.ITEM_TYPE_DE, " +
                "d.DONATION_DT, d.QUANTITY_DONATED_NO, i.MATERIAL_COST_AM " +
                "FROM donations d JOIN items i ON d.ITEM_ID = i.ITEM_ID " +
                "WHERE i.CATEGORY_CD = 'Donate' AND (i.ITEM_NM LIKE ? OR i.ITEM_TYPE_DE LIKE ?)";
        
		// Will prepare SQL statement to safely and ensure the preparedStatement is closed when done
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            
            // Will execute the query and clear the existing rows from the associated table
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblDonations.getModel();
            model.setRowCount(0);
            
            DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Updated format

            // Will go through the results and add the donation info to the table to be displayed
            while (rs.next()) {
                String dateStr = rs.getString("DONATION_DT");
                String displayDate;
                
                try {
                    // First, try ISO format
                    LocalDate date = LocalDate.parse(dateStr, dbFormatter);
                    displayDate = date.format(displayFormatter);
                } catch (DateTimeParseException e1) {
                    try {
                        // If that fails, trys timestamp format
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
                
                // Format material cost with 2 decimal places
                String formattedMaterialCost = String.format("%.2f", rs.getDouble("MATERIAL_COST_AM"));

                // Add row to the associated table
                model.addRow(new Object[] {
                    rs.getInt("DONATION_ID"),
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    rs.getString("ITEM_TYPE_DE"),
                    displayDate,
                    rs.getInt("QUANTITY_DONATED_NO"),
                    formattedMaterialCost
                });
            }

            // Will provide user friendly feedback
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frmFinancials, "No donations found.", 
                                            "Search Results", JOptionPane.INFORMATION_MESSAGE);
                viewDonations();
            }

	    // If error with the database, then a message will display
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error searching donations.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    

    // Function that will display and load the donation data in the table
    private void viewDonations() {
        String query = "SELECT d.DONATION_ID, d.ITEM_ID, i.ITEM_NM, i.ITEM_TYPE_DE, " +
                "d.DONATION_DT, d.QUANTITY_DONATED_NO, i.MATERIAL_COST_AM " +
                "FROM donations d JOIN items i ON d.ITEM_ID = i.ITEM_ID " +
                "WHERE i.CATEGORY_CD = 'Donate'";
        
		// Will prepare SQL statement to safely and ensure the preparedStatement is closed when done
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            
            // Will execute the query and clear the existing rows from the associated table
        	ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblDonations.getModel();
            model.setRowCount(0); 
            
            DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            
            while (rs.next()) {
                String dateStr = rs.getString("DONATION_DT");
                String displayDate;
                
                try {
                    // First, try ISO format
                    LocalDate date = LocalDate.parse(dateStr, dbFormatter);
                    displayDate = date.format(displayFormatter);
                } catch (DateTimeParseException e1) {
                    try {
                        // If that fails, trys timestamp format
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
                
                // Format the material cost with 2 decimal places
                String formattedMaterialCost = String.format("%.2f", rs.getDouble("MATERIAL_COST_AM"));
                
                
                // Add row to the associated table
                model.addRow(new Object[] {
                    rs.getInt("DONATION_ID"),
                    rs.getInt("ITEM_ID"),
                    rs.getString("ITEM_NM"),
                    rs.getString("ITEM_TYPE_DE"),
                    displayDate,
                    rs.getInt("QUANTITY_DONATED_NO"),
                    formattedMaterialCost
                });
            }
        
	    // If error with the database, then a message will display
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error loading donations: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }    
    }
    
    // Function that will search inventory items with only product name or type
    private void searchInventory(String searchTerm) {
	    // Checks if the input is empty, and displays message to user
    	if(searchTerm == null || searchTerm.trim().isEmpty()) {
	        JOptionPane.showMessageDialog(frmFinancials, "Search bar cannot be empty. Please enter a valid value.", "Validation Error", JOptionPane.WARNING_MESSAGE);
	        return;
    	}
    	
	    try {
	        // A query to search in both item name and item type columns based on inventory
	    	String query = "SELECT * FROM items WHERE (ITEM_NM LIKE ? OR ITEM_TYPE_DE LIKE ?) AND CATEGORY_CD = 'Inventory'";
	    	
			// Will prepare SQL statement to safely and ensure the preparedStatement is closed when done
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            // Allows when searching to match with anything within parameters
	            stmt.setString(1, "%" + searchTerm + "%");
	            stmt.setString(2, "%" + searchTerm + "%");
	            
	            // Will execute the query and clear the existing rows from the associated table
	            ResultSet rs = stmt.executeQuery();
	            DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
	            model.setRowCount(0);

	            boolean found = false;

	            // Will go through the results and add the item info to the table to be displayed
	            while (rs.next()) {
	                found = true;
	                // Format material cost for display
	                String formattedMaterialCost = String.format("%.2f", rs.getDouble("MATERIAL_COST_AM"));
	                
	                // Add row to the associated table
	                model.addRow(new Object[]{
	    				rs.getInt("ITEM_ID"),
	    				rs.getString("ITEM_NM"),
	    				rs.getString("ITEM_TYPE_DE"),
	    				rs.getString("DATE_CREATED_DT"),
	    				formattedMaterialCost  
	                });
	            }

	            // If no results were found, display a message
	            if (!found) {
	                JOptionPane.showMessageDialog(frmFinancials, "No results found for \"" + searchTerm + "\"", "Search Result", JOptionPane.INFORMATION_MESSAGE);
	            }

		    // If error with the database, then a message will display   
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(frmFinancials, "Database error while searching: " + ex.getMessage(), 
	                    "Error", JOptionPane.ERROR_MESSAGE);
	                ex.printStackTrace();
	        }
	    
	    // General error for any other exception
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(frmFinancials, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
    }
    
    // Function that will load and display the inventory items in the table
    private void viewInventory(){
    	// Uses a query to select all from items that are inventory
    	String query = "SELECT * FROM items WHERE CATEGORY_CD = 'Inventory'";
		
		// Will prepare SQL statement to safely and ensure the preparedStatement is closed when done
    	try(PreparedStatement stmt = conn.prepareStatement(query)){
	        ResultSet rs = stmt.executeQuery();
	        
            // Will execute the query and clear the existing rows from the associated table
			DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
			model.setRowCount(0);
			
            // Will go through the results and add the item info to the table to be displayed
			while (rs.next()) {
	            // Format material cost with 2 decimal places
	            String formattedMaterialCost = String.format("%.2f", rs.getDouble("MATERIAL_COST_AM"));
				
                // Add row to the associated table
				model.addRow(new Object[] {
					rs.getInt("ITEM_ID"),
					rs.getString("ITEM_NM"),
					rs.getString("ITEM_TYPE_DE"),
					rs.getString("DATE_CREATED_DT"),
					formattedMaterialCost
				});
			}
	    
		// If error with the database, then a message will display
		} catch(SQLException ex) {
	        JOptionPane.showMessageDialog(frmFinancials, 
	                "Error loading inventory: " + ex.getMessage(),
	                "Database Error", 
	                JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();		}
	}
	
 // Function that will calculate recommended prices based on input values
    private void calculatePrices() {
        try {
            // Clear previous results first
            lblDisplayRecommended.setText("$0.00");
            lblDisplayAdjusted.setText("$0.00");
            
            // Will get the input values
            BigDecimal materialCost = new BigDecimal(txtMaterialCost.getText());
            int quantity = (Integer) spinnerQuantitySold.getValue();
            BigDecimal hoursSpent = new BigDecimal(txtHoursSpent.getText());
            BigDecimal hourlyRate = new BigDecimal("15.00"); // Default hourly rate $15/hr
            
            // Will validate the material cost such as a value greater than 0
            if (materialCost.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Material cost must be greater than $0.00");
            }

            // Will validate the quantity to ensure it must be larger than 1
            if (quantity < 1) {
                throw new IllegalArgumentException("Quantity must be at least 1");
            }

            // Will validate the hours spent to ensure it will not be a negative number
            if (hoursSpent.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Hours spent cannot be negative");
            }

            // Will calculate labor cost based on hours * hourly rate
            BigDecimal laborCost = hoursSpent.multiply(hourlyRate);

            // Will calculate total cost based on materials + labor
            BigDecimal totalCost = materialCost.add(laborCost);

            // Will calculate price per unit based on total cost / quantity
            BigDecimal pricePerUnit = totalCost.divide(
                new BigDecimal(quantity), 
                2, 
                RoundingMode.HALF_UP);

     
            // Will display the final results to a label
            lblDisplayRecommended.setText(String.format("Total Cost: $%.2f", totalCost));
            lblDisplayAdjusted.setText(String.format("$%.2f per unit", pricePerUnit));
            
        // Will catch cases when invalid number is entered displaying a message along with it
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frmFinancials, 
                "Please enter valid numbers in all fields", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
            
        // Will catch cases when validation errors occurs
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frmFinancials, 
                ex.getMessage(), 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            
        // Will catch cases when unexpected errors occurs
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmFinancials, 
                "An error occurred during calculation: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    

    // Function that will allow the user to clear all options in case to restart
    private void clearCalculator() {
    	txtMaterialCost.setText("");
    	txtHoursSpent.setText("");
    	spinnerQuantitySold.setValue(1);
        lblDisplayRecommended.setText("Total Cost: $0.00");
        lblDisplayAdjusted.setText("$0.00 per unit");
    }
		
    // Function to print the report and save it to a file 
    private void printReport() {
        try {
            // Will ensure the directory exists
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

            // Will show the confirmation when export succeeds
            JOptionPane.showMessageDialog(frmFinancials, "Report exported successfully!", "Export Success", JOptionPane.INFORMATION_MESSAGE);
      
    	// General error for any other exception
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(frmFinancials, "Error exporting report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}