import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.Color;
import java.awt.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
//import java.awt.Component;

public class TimeLog {

    // Database connection variables
	Connection conn = null;
    String dbConnect = "jdbc:sqlite:../project/database/mamaspiddlins.sqlite";
	String dbUserName = "root";
	String dbPass = "root";
	
	public JFrame frmTimeLog;
	private JTable tblTime;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TimeLog window = new TimeLog();
					window.frmTimeLog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TimeLog() {
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
		// Allows user to view a table with associated columns within the Time tab
		tblTime = new JTable();
		tblTime.setModel(new DefaultTableModel(
			    new Object[][]{},
			    new String[] {"Time Log ID", "Item ID", "Item Name", "Item Type", "Time Spent (In Hours)"}  // Added 'Item Name' column
			));

        // Disable editing in the table
		tblTime.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblTime.setDefaultEditor(Object.class, null);  // Disable editor for the entire table
		tblTime.getTableHeader().setReorderingAllowed(false);
		tblTime.getTableHeader().setReorderingAllowed(false);
		
		// Will be used to allow sorting for the tables by ascending/descending order
		TableRowSorter<DefaultTableModel> timeSorter = new TableRowSorter<>((DefaultTableModel) tblTime.getModel());
		tblTime.setRowSorter(timeSorter);

		// Will allow the sorting by column header
		tblTime.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int column = tblTime.columnAtPoint(evt.getPoint());
		        if (column >= 0) {
		            // Get current sort order for the column
		            boolean ascending = timeSorter.getSortKeys().isEmpty() || timeSorter.getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING;
		            // Toggle between ascending and descending
		            timeSorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
		        }
	        }
	    });	
				
		// Main screen for the application
		frmTimeLog = new JFrame();
		frmTimeLog.setTitle("Time Log");
		frmTimeLog.setBounds(100, 100, 700, 700);
		frmTimeLog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTimeLog.getContentPane().setBackground(new Color(216, 203, 175));
		frmTimeLog.getContentPane().setLayout(null);
		
		// The panel that will contain the title of the page
		JPanel timeLogPanel = new JPanel();
		
		frmTimeLog.getContentPane().add(timeLogPanel);
		
		// The label that will display the title of the page
		JLabel lblTimeLog = new JLabel("Time Log");
		try {
		    Font caveatBrush = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/CaveatBrush-Regular.ttf"));
		    caveatBrush = caveatBrush.deriveFont(Font.PLAIN, 40);
		    lblTimeLog.setFont(caveatBrush);
		} catch (IOException | FontFormatException e) {
			lblTimeLog.setFont(new Font("Tahoma", Font.BOLD, 23)); 
		    e.printStackTrace();
		}
		lblTimeLog.setBounds(25, 30, 200, 50);
		frmTimeLog.getContentPane().add(lblTimeLog);
		
		
		// Will use a table to display information to the user based on time
		JScrollPane scrollPaneTime = new JScrollPane(tblTime);
		scrollPaneTime.setForeground(Color.WHITE);
		scrollPaneTime.setBounds(52, 180, 581, 321);
		frmTimeLog.getContentPane().add(scrollPaneTime);
		
		// A textfield to input search for components from the time table
		TextField txtSearchTimeLog = new TextField();
		txtSearchTimeLog.setBounds(52, 140, 186, 21);
		frmTimeLog.getContentPane().add(txtSearchTimeLog);
		
		// A button to be used with the associated textfield to search for a specific time 
		JButton btnSearchTime = new JButton("Search");
		btnSearchTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchTimeLog(txtSearchTimeLog.getText());
			}
		});
		btnSearchTime.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSearchTime.setBounds(264, 140, 110, 21);
		frmTimeLog.getContentPane().add(btnSearchTime);
		
		// A background panel that will be used for decoration for the page
		JPanel backgroundPanel = new JPanel();
		backgroundPanel.setBounds(29, 100, 627, 507);
		frmTimeLog.getContentPane().add(backgroundPanel);
		backgroundPanel.setLayout(null);
		
		JButton btnReturn = new JButton("Reset");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewTimeLog();
			}
		});
		btnReturn.setFont(new Font("Dialog", Font.BOLD, 12));
		btnReturn.setBounds(362, 40, 100, 21);
		backgroundPanel.add(btnReturn);
		
		// A button that will allow the user to return back to the home screen
		JButton btnHome = new JButton("Home");
		btnHome.setBounds(529, 39, 127, 27);
		frmTimeLog.getContentPane().add(btnHome);
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmTimeLog.setVisible(false);
				Dashboard dashboardPage = new Dashboard();
				dashboardPage.frmDashboard.setVisible(true);
			}
		});
	
		
		// Will call the function to display all information based on the time log column from database
		viewTimeLog();
		
	}
	
	// Function to allow the user to fetch time log information
	private void viewTimeLog() {
	    // A query to join time_logs and items tables
	    String query = "SELECT tl.*, i.ITEM_NM, i.ITEM_TYPE_DE " +
                "FROM time_logs tl " +
                "JOIN items i ON tl.ITEM_ID = i.ITEM_ID";
	    
	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        ResultSet rs = stmt.executeQuery();
	        
	        DefaultTableModel model = (DefaultTableModel) tblTime.getModel();
	        model.setRowCount(0);  // Clear existing rows

	        // Assuming result set has 4 columns
	        while (rs.next()) {
	            model.addRow(new Object[] {
	                rs.getInt("TIME_LOG_ID"),
	                rs.getInt("ITEM_ID"),
	                rs.getString("ITEM_NM"),
	                rs.getString("ITEM_TYPE_DE"),
	                rs.getInt("TIME_SPENT_NO")
	            });
	        }


	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	
	
	// Function to allow the user to fetch and search time log information
	// FIX ME
	private void searchTimeLog(String timeLog) {
	    // Check if the input is empty
	    if (timeLog == null || timeLog.trim().isEmpty()) {
	        JOptionPane.showMessageDialog(frmTimeLog, "Search bar cannot be empty. Please enter a valid value.", "Validation Error", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    try {
	        // Modify the query to allow searching by different columns
	        String query = "SELECT tl.*, i.ITEM_NM, i.ITEM_TYPE_DE " +
	                       "FROM time_logs tl " +
	                       "JOIN items i ON tl.ITEM_ID = i.ITEM_ID " +
	                       "WHERE ";

	        // Determine which column to search by (e.g., ITEM_ID, ITEM_NM, TIME_SPENT_NO, or ITEM_TYPE_DE)
	        String searchColumn = "";

	        // Determine if it's a number (for ITEM_ID or TIME_SPENT_NO)
	        try {
	            Integer.parseInt(timeLog);
	            searchColumn = "tl.TIME_SPENT_NO";  // Assuming the user wants to search by TIME_SPENT_NO if it's a number
	        } catch (NumberFormatException e) {
	            // If it's not a number, check if it's potentially a valid Item Type Description
	            // Check if the search term matches part of an item type description
	            if (timeLog.matches(".*[a-zA-Z]+.*")) {
	                searchColumn = "i.ITEM_TYPE_DE";  // If it's text, search by ITEM_TYPE_DE
	            } else {
	                searchColumn = "i.ITEM_NM";  // Else search by ITEM_NM
	            }
	        }

	        // Adjust the query based on the column selected for searching
	        query += searchColumn + " LIKE ?";  // Use LIKE for partial matches

	        // Prepare the query with the dynamically chosen column
	        try (PreparedStatement pst = conn.prepareStatement(query)) {
	            // Bind the parameter based on the column chosen
	            pst.setString(1, "%" + timeLog + "%"); // Using "%" for a LIKE search (partial match)

	            ResultSet rs = pst.executeQuery();
	            DefaultTableModel model = (DefaultTableModel) tblTime.getModel();

	            // Clear previous search results from the table
	            model.setRowCount(0);

	            boolean found = false;

	            while (rs.next()) {
	                found = true;
	                model.addRow(new Object[]{
	                        rs.getInt("TIME_LOG_ID"),
	                        rs.getInt("ITEM_ID"),
	                        rs.getString("ITEM_NM"),
	                        rs.getString("ITEM_TYPE_DE"),
	                        rs.getInt("TIME_SPENT_NO")
	                });
	            }

	            // If no results were found, display a message
	            if (!found) {
	                JOptionPane.showMessageDialog(frmTimeLog, "No results found for the entered search term.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
	            }

	            // Set the table model with the results
	            tblTime.setModel(model);

	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(frmTimeLog, "Error fetching data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();
	        }

	    } catch (Exception e) {
	        // General error for any other exception
	        JOptionPane.showMessageDialog(frmTimeLog, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	}


}
