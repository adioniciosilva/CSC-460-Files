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
// import java.awt.ScrollPane;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
// import java.awt.Component;


public class Dashboard {
	
    // Database connection variables
    Connection conn = null;
    String dbConnect = "jdbc:mysql://localhost:3306/mamaspiddlin";
    String dbUserName = "root";
    String dbPass = "root";
    

	public JFrame frame;
	public JTable tblItems;
	public JTable tblTimeList;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dashboard window = new Dashboard();
					window.frame.setVisible(true);
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
			conn = DriverManager.getConnection(dbConnect, dbUserName, dbPass);
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
		new String[] {"ID", "Name", "Pattern", "Status"}
		
		));
		
		
		// 
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(216, 203, 175));
		frame.getContentPane().setLayout(null);
		
		JPanel panelDashboard = new JPanel();
		panelDashboard.setBounds(24, 56, 125, 28);
		frame.getContentPane().add(panelDashboard);
		
		JLabel lblNewLabel = new JLabel("Dashboard");
		panelDashboard.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 10));
			
		

		
		JScrollPane scrollPaneItems = new JScrollPane(tblItems);
		scrollPaneItems.setForeground(new Color(255, 255, 255));
		scrollPaneItems.setBounds(24, 153, 437, 155);
		frame.getContentPane().add(scrollPaneItems);
		
		// Textbox for 
		TextField txtSearchBox = new TextField();
		txtSearchBox.setBounds(21, 120, 151, 21);
		frame.getContentPane().add(txtSearchBox);
		
		Button btnSearch = new Button("Search");
		btnSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSearch.setBounds(190, 120, 66, 21);
		frame.getContentPane().add(btnSearch);
		
		// Filtering options for user based on the associated options
		String[] sortOptions = new String [] {"Item Name", "Item Type", "Item Status"};
		JComboBox<String> cboxOption = new JComboBox<>(sortOptions);
		cboxOption.setFont(new Font("Tahoma", Font.BOLD, 10));
		cboxOption.setSelectedIndex(2); // Due to indices starting at 0, specifies Option 3
		cboxOption.setBounds(296, 120, 141, 21);
		frame.getContentPane().add(cboxOption);
		
		// Button to allow the user to view the settings page after clicked
		JButton btnSettings = new JButton("Settings");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings generateReportWindow = new Settings();
				generateReportWindow.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		btnSettings.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnSettings.setBounds(347, 56, 90, 28);
		frame.getContentPane().add(btnSettings);
		
		JPanel panelmage = new JPanel();
		panelmage.setBounds(31, 344, 141, 28);
		frame.getContentPane().add(panelmage);
		
		JLabel lblImage = new JLabel("Image");
		lblImage.setFont(new Font("Tahoma", Font.BOLD, 10));
		panelmage.add(lblImage);
		
		JPanel panelTimeLog = new JPanel();
		panelTimeLog.setBounds(261, 344, 164, 28);
		frame.getContentPane().add(panelTimeLog);
		
		JLabel lblTimeLog = new JLabel("Time Log");
		lblTimeLog.setFont(new Font("Tahoma", Font.BOLD, 10));
		panelTimeLog.add(lblTimeLog);
		
		JLabel lblImageDisplay = new JLabel("");
		lblImageDisplay.setBounds(31, 393, 141, 116);
		frame.getContentPane().add(lblImageDisplay);
		
		// Works with the table and jpanel in timelog
		tblTimeList = new JTable();
		tblTimeList.setModel(new DefaultTableModel(
		new Object[][]{},
		new String[] {"Product ID", "Time Spent"}	
		));
		
		JScrollPane scrollPaneTimeDisplay = new JScrollPane(tblTimeList);
		scrollPaneTimeDisplay.setForeground(Color.WHITE);
		scrollPaneTimeDisplay.setBounds(261, 393, 164, 116);
		frame.getContentPane().add(scrollPaneTimeDisplay);
		
		// Loads all products when dashboard is loaded
		viewProducts();
		
		frame.setBounds(100, 100, 500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// Function to allow the user to fetch products information
	private void viewProducts() {
		String query = "SELECT * FROM ITEMS";
		try(PreparedStatement stmt = conn.prepareCall(query)){
	        ResultSet rs = stmt.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) tblItems.getModel();
			model.setRowCount(0);
			
			while (rs.next()) {
				model.addRow(new Object[] {
					rs.getInt("ITEM_ID"),
					rs.getString("ITEM_NM"),
					rs.getString("QUILT_PATTERN_CD"),
					rs.getString("ITEM_STATUS_CD")
				});
			}
	        // Disable editing in the table
            tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tblItems.setDefaultEditor(Object.class, null);  // Disable editor for the entire table
	        
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
}
