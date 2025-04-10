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
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.RowSorter; // Used for the table sorting
import javax.swing.SortOrder; // Used for the table sorting
import javax.swing.table.TableRowSorter; // Used for the table sorting
import java.util.Collections; // Used for the table sorting
// import java.awt.Component;
// import javax.swing.ImageIcon;
//import java.awt.ScrollPane;
// import java.awt.FontFormatException;
// import java.io.File;


public class Dashboard {

    // Database connection variables
    Connection conn = null;
    // Connection based on SQLite within the Project Folder
    String dbConnect = "jdbc:sqlite:../project/database/mamaspiddlins.sqlite";
    //  String dbConnect = "jdbc:sqlite:C:/Users/adria/Downloads/School/LR 2024-2025/Spring 2025/CSC 460/Work/Project/Project/database/mamaspiddlins.db";
    //  String dbUserName = "root";
    //  String dbPass = "root";


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
		// Has to be initialized first before the rest to load the data
		// Creates a table to display the options for the item once searched
		tblItems = new JTable();
		tblItems.setModel(new DefaultTableModel(
		new Object[][]{},
		new String[] {"Product ID", "Product Name", "Product Type", "Product Pattern", "Product Status"}
		
		));
        // Disable editing in the table
        tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblItems.setDefaultEditor(Object.class, null);  // Disable editor for the entire table
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
		
		// The panel that will contain the title of the page
		JPanel panelDashboard = new JPanel();
		
		/* NOTE: Jaiven Comment:
		 * 
		 * CHANGES:
		 * 1. Change the title from Dashboard into MaMa's Piddlin'"
		 * 2. Added new Font in the label
		 * 
		 */
		// The label that will display the title of the page
		JLabel lblNewLabel = new JLabel("MaMa's Piddlin'");
		
		try {
		    Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
		    caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 40f);
		    lblNewLabel.setFont(caveatBrush);
		    
		} catch (IOException | FontFormatException e) {
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 23)); 
		    e.printStackTrace();
		}
		
		 lblNewLabel.setBounds(190, 10, 400, 35);
		frmDashboard.getContentPane().add(lblNewLabel);

		
		// The table that that will display the information to the user 
		JScrollPane scrollPaneItems = new JScrollPane(tblItems);
		scrollPaneItems.setForeground(new Color(255, 255, 255));
		scrollPaneItems.setBounds(10, 175, 550, 155);
		frmDashboard.getContentPane().add(scrollPaneItems);
		
		// A textfield to search for items along with the use of a button
		TextField txtSearchBox = new TextField();
		txtSearchBox.setBounds(10, 134, 151, 21);
		frmDashboard.getContentPane().add(txtSearchBox);
		
		// The button to search for items along with the use of a textfield
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchProduct(txtSearchBox.getText());
			}
		});
		btnSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSearch.setBounds(182, 134, 90, 21);
		frmDashboard.getContentPane().add(btnSearch);
		
		
		// Filtering options for user based on the associated options
		String[] sortOptions = new String [] {"Product Name", "Product Type", "Product Status"};
		JComboBox<String> cboxOption = new JComboBox<>(sortOptions);
		cboxOption.setToolTipText("Filter By:");
		cboxOption.setFont(new Font("Tahoma", Font.BOLD, 10));
		cboxOption.setSelectedIndex(2); // Due to indices starting at 0, specifies Option 3
		cboxOption.setBounds(405, 134, 140, 21);
		frmDashboard.getContentPane().add(cboxOption);
		
		// The background to the navigation bar
		JPanel navigationPanel = new JPanel();
		navigationPanel.setBounds(56, 60, 466, 52);
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
		
		// FIX ME
		JButton btnReturn = new JButton("Reset");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewProducts();
			}
		});
		btnReturn.setFont(new Font("Dialog", Font.BOLD, 12));
		btnReturn.setBounds(282, 134, 89, 21);
		frmDashboard.getContentPane().add(btnReturn);
		
		// Loads all products when dashboard is loaded
		viewProducts();

		
	}
	
	// Function to allow the user to search for products
	private void searchProduct(String productInfo) {
		// Checks if the search bar is empty
		if(productInfo == null || productInfo.trim().isEmpty()) {
		JOptionPane.showMessageDialog(frmDashboard, "Search bar cannot be empty", "Validation Error", JOptionPane.WARNING_MESSAGE);
		return;
	}
		// SQL query that search for items based on the Item Name
		String query = "SELECT * FROM items WHERE ITEM_NM LIKE ?";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)){
			// Allows partial matching and executes the query
			stmt.setString(1, "%" + productInfo + "%");
			ResultSet rs = stmt.executeQuery();
			
			// Get the table model 
			DefaultTableModel model = (DefaultTableModel) tblItems.getModel();
			model.setRowCount(0); // Will clear the existing rows
			
			// Will enter the results to the table based on the search results
			while (rs.next()) {
				model.addRow(new Object[] {
						rs.getInt("ITEM_ID"),
						rs.getString("ITEM_NM"),
						rs.getString("ITEM_TYPE_DE"),
						rs.getString("QUILT_PATTERN_CD"),
						rs.getString("ITEM_STATUS_CD")
						
				});
			}
			
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	// Function to allow the user to fetch products information
	// Used prepareCall() which is typically used for calling stored procedures in databases like MySQL or Oracle. In SQLite, you should use prepareStatement() instead.
	private void viewProducts() {
		String query = "SELECT * FROM items";
		try(PreparedStatement stmt = conn.prepareStatement(query)){
	        ResultSet rs = stmt.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) tblItems.getModel();
			model.setRowCount(0);
			
			while (rs.next()) {
				model.addRow(new Object[] {
					rs.getInt("ITEM_ID"),
					rs.getString("ITEM_NM"),
					rs.getString("ITEM_TYPE_DE"),
					rs.getString("QUILT_PATTERN_CD"),
					rs.getString("ITEM_STATUS_CD")
				});
			}

		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
}
