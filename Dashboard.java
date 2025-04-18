// Packages to import for the java project
import java.awt.EventQueue;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.table.TableRowSorter; // Used for the table sorting
import java.util.Collections; // Used for the table sorting
import javax.swing.border.MatteBorder; 

public class Dashboard {

     // Database connection variables
    Connection conn = null;

    // Declaring variable types
	public JFrame frmDashboard;
	public JTable tblItems;
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dashboard window = new Dashboard();
					window.frmDashboard.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	public Dashboard() {
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
		// Has to be initialized first before the rest to load the data
		// Creates a table to display the options for the item once searched
		tblItems = new JTable();
		tblItems.setModel(new DefaultTableModel(
		new Object[][]{},
		new String[] {"Product ID", "Product Name", "Product Type", "Product Pattern", "Product Status"}
		
		));
		
        // Disable editing in the table
        tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblItems.setDefaultEditor(Object.class, null); 
        tblItems.getTableHeader().setReorderingAllowed(false);
		tblItems.getTableHeader().setResizingAllowed(false);
        
		
		// Will be used to allow sorting for the tables by ascending/descending order
		TableRowSorter<DefaultTableModel> listSorter = new TableRowSorter<>((DefaultTableModel) tblItems.getModel());
		tblItems.setRowSorter(listSorter);

		// Will allow the sorting by column header
		tblItems.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int column = tblItems.columnAtPoint(evt.getPoint());
		        if (column >= 0) {
		            // Get current sort order for the column
		            boolean ascending = listSorter.getSortKeys().isEmpty() || listSorter.getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING;
		            // Toggle between ascending and descending
		            listSorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
		        }
	        }
	    });		    
		
		// The beginning setup for the Dashboard Page
		frmDashboard = new JFrame();
		frmDashboard.setTitle("Dashboard");
		frmDashboard.getContentPane().setBackground(new Color(216, 203, 175));
		frmDashboard.getContentPane().setLayout(null);
		frmDashboard.setBounds(100, 100, 600, 600);
		frmDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Allows a custom window icon
		Image icon = Toolkit.getDefaultToolkit().getImage("images/bearLogo.png");
		frmDashboard.setIconImage(icon);
		
		// The label that will display the title of the page	
        // by: Jaiven Harris 
        JLabel lblNewLabel = new JLabel("MaMa's Piddlin");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
            caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 40f);
		    lblNewLabel.setFont(caveatBrush);
        } catch (IOException | FontFormatException e) {
        	lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 23)); 
            e.printStackTrace();
        }
        lblNewLabel.setBounds(153, 35, 279, 37);
        frmDashboard.getContentPane().add(lblNewLabel);		
			
		// The table that that will display the information to the user 
		JScrollPane scrollPaneItems = new JScrollPane(tblItems);
        scrollPaneItems.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPaneItems.setViewportBorder(null);
        scrollPaneItems.setForeground(Color.WHITE);
        scrollPaneItems.setForeground(new Color(255, 255, 255));
		scrollPaneItems.setBounds(10, 215, 550, 333);
		frmDashboard.getContentPane().add(scrollPaneItems);
		
		// A textfield to search for items along with the use of a button
		JTextField txtSearchBox = new JTextField();
		txtSearchBox.setToolTipText("Please enter product name or type");
		txtSearchBox.setBounds(10, 170, 183, 21);
		frmDashboard.getContentPane().add(txtSearchBox);
		
		// The button to search for items along with the use of a textfield
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchProduct(txtSearchBox.getText());
			}
		});
		btnSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSearch.setBounds(203, 170, 90, 21);
		frmDashboard.getContentPane().add(btnSearch);

		// The background to the navigation bar
		JPanel navigationPanel = new JPanel();
		navigationPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		navigationPanel.setBounds(51, 90, 466, 52);
		frmDashboard.getContentPane().add(navigationPanel);
		navigationPanel.setLayout(null);
		
		// A button that will take the user to the associated page
		JButton btnFinancials = new JButton("Financials");
		btnFinancials.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmDashboard.setVisible(false);
				Financials financialsPage = new Financials();
				financialsPage.frmFinancials.setVisible(true);
			}
		});
		btnFinancials.setBounds(10, 10, 128, 28);
		navigationPanel.add(btnFinancials);
		
		// A button that will take the user to the associated page
		JButton btnProducts = new JButton("Products");
		btnProducts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//
				frmDashboard.setVisible(false);
				Products productsPage = new Products();
				productsPage.frmProducts.setVisible(true);
			}
		});
		btnProducts.setBounds(172, 10, 128, 28);
		navigationPanel.add(btnProducts);
		
		// A button that will take the user to the associated page
		JButton btnTimeLog = new JButton("Time Log");
		btnTimeLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmDashboard.setVisible(false);
				TimeLog timeLogPage = new TimeLog();
				timeLogPage.frmTimeLog.setVisible(true);
			}
		});
		btnTimeLog.setBounds(328, 10, 128, 28);
		navigationPanel.add(btnTimeLog);
		
        // The button to allow the user to reset the table content
        // by: Jaiven Harris 
		JButton btnReturn = new JButton("Reset");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewProducts();
			}
		});
		btnReturn.setFont(new Font("Dialog", Font.BOLD, 12));
		btnReturn.setBounds(303, 170, 89, 21);
		frmDashboard.getContentPane().add(btnReturn);
	
		
		// Loads all products when dashboard is loaded
		viewProducts();

		
	}
	
	// Function to allow the user to search for products by Item Name or Item Type
	private void searchProduct(String searchTerm) {
	    // Checks if the input is empty, and displays message to user
	    if (searchTerm == null || searchTerm.trim().isEmpty()) {
	        JOptionPane.showMessageDialog(frmDashboard, "Search bar cannot be empty. Please enter a valid value.", "Validation Error", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    try {
	        // Uses a query to search in both item name and item type columns
	        String query = "SELECT * FROM items WHERE ITEM_NM LIKE ? OR ITEM_TYPE_DE LIKE ?";

			// Will prepare SQL statement to safely and ensure the preparedStatement is closed when done
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            // Allows when searching to match with anything within parameters
	            stmt.setString(1, "%" + searchTerm + "%");
	            stmt.setString(2, "%" + searchTerm + "%");

	            // Will execute the query and clear the existing rows from the associated table
	            ResultSet rs = stmt.executeQuery();
	            DefaultTableModel model = (DefaultTableModel) tblItems.getModel();
	            model.setRowCount(0);

	            boolean found = false;

	            // Will go through the results and add the item info to the table to be displayed
	            while (rs.next()) {
	                found = true;
	                model.addRow(new Object[]{
	                    rs.getInt("ITEM_ID"),
	                    rs.getString("ITEM_NM"),
	                    rs.getString("ITEM_TYPE_DE"),
	                    rs.getString("QUILT_PATTERN_CD"),
	                    rs.getString("ITEM_STATUS_CD")
	                });
	            }

	            // If no results were found, then a message will display
	            if (!found) {
	                JOptionPane.showMessageDialog(frmDashboard, "No results found for \"" + searchTerm + "\"", "Search Result", JOptionPane.INFORMATION_MESSAGE);
	            }
	        // If error with the database, then a message will display
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(frmDashboard, "Error fetching data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();
	        }
	    // General error for any other exception
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(frmDashboard, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	}
	
	// Function to allow the user to fetch products information
	private void viewProducts() {
        // Uses a query to search from all rows and columns of the items table
		String query = "SELECT * FROM items";
		
		// Will prepare SQL statement to safely and ensure the preparedStatement is closed when done
		try(PreparedStatement stmt = conn.prepareStatement(query)){
	        ResultSet rs = stmt.executeQuery();
	        
            // Will execute the query and clear the existing rows from the associated table
			DefaultTableModel model = (DefaultTableModel) tblItems.getModel();
			model.setRowCount(0);
			
            // Will go through the results and add the item info to the table to be displayed
			while (rs.next()) {
				model.addRow(new Object[] {
					rs.getInt("ITEM_ID"),
					rs.getString("ITEM_NM"),
					rs.getString("ITEM_TYPE_DE"),
					rs.getString("QUILT_PATTERN_CD"),
					rs.getString("ITEM_STATUS_CD")
				});
			}

	     // If error with the database, then a message will display
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
}