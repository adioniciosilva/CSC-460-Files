// Packages to import for the java project
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
// import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.TextField;
import java.awt.Button;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
// import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class Settings {
	
    // Database connection variables
    Connection conn = null;
    String dbConnect = "jdbc:mysql://localhost:3306/mamaspiddlin";
    String dbUserName = "root";
    String dbPass = "root";

	public JFrame frame;
	public JTable tblFinacials;
	public JTable tblTime;
	public JTable tblList;
	private JTextField txtProdName;
	private JTextField txtProdPattern;
	private JTextField txtMaterialCost;
	private JTextField txtProdDSPrices;
	private JTextField txtTimeSpent;
	private JTextField txtTimeSpentEd;
	private JTextField txtProdDSPricesEd;
	private JTextField txtMaterialCostEd;
	private JTextField txtProdPatternEd;
	private JTextField txtProdNameEd;
	private JTextField txtProductDelId;
	private JTextField txtProductId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Settings window = new Settings();
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
	public Settings() {
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
		// Allows user to view a table with associated columns within the Time tab
		tblTime = new JTable();
		tblTime.setModel(new DefaultTableModel(
		new Object[][]{},
		new String[] {"Time Log ID", "Item ID", "Time Spent (in hours) "}	
		));
		
		tblList = new JTable();
		tblList.setModel(new DefaultTableModel(
		new Object[][]{},
		new String[] {"Product ID", "Product Name"}
		));
		
		
		tblFinacials = new JTable();
		tblFinacials.setModel(new DefaultTableModel(
		new Object[][]{},
		new String[] {"Product ID", "Product Name", "Material Costs", "Donate/Sell Prices"}	
		));
		
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(216, 203, 175));
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPaneMain = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneMain.setBounds(10, 72, 550, 535);
		frame.getContentPane().add(tabbedPaneMain);
		
		// The start for the Financial tab for the Settings Page
		JPanel panelFinancials = new JPanel();
		tabbedPaneMain.addTab("Financial", null, panelFinancials, null);
		panelFinancials.setLayout(null);
		
		TextField txtSearchBox = new TextField();
		txtSearchBox.setBounds(38, 44, 151, 21);
		panelFinancials.add(txtSearchBox);
		
		Button btnSearch = new Button("Search");
		btnSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSearch.setBounds(215, 44, 66, 21);
		panelFinancials.add(btnSearch);
		
		
		// Allows user to view a table with associated columns within the Financials tab
		JScrollPane scrollPaneItems = new JScrollPane(tblFinacials);
		scrollPaneItems.setForeground(Color.WHITE);
		scrollPaneItems.setBounds(38, 83, 437, 321);
		panelFinancials.add(scrollPaneItems);
		
		// Filtering options for user based on the associated options
		String[] sortOptions = new String [] {"Item Name", "Item Type", "Item Status"};
		JComboBox<String> cboxOption = new JComboBox<>(sortOptions);
		cboxOption.setSelectedIndex(2);
		cboxOption.setFont(new Font("Tahoma", Font.BOLD, 10));
		cboxOption.setBounds(313, 44, 141, 21);
		panelFinancials.add(cboxOption);
		
		JButton btnPrintReport = new JButton("Print Report");
		btnPrintReport.setBounds(74, 427, 104, 21);
		panelFinancials.add(btnPrintReport);
		
		// The start for the Product tab for the Settings Page
		JPanel panelProduct = new JPanel();
		tabbedPaneMain.addTab("Product", null, panelProduct, null);
		panelProduct.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(38, 30, 447, 408);
		panelProduct.add(tabbedPane);
		
		JPanel panelAddProd = new JPanel();
		tabbedPane.addTab("Add Product", null, panelAddProd, null);
		panelAddProd.setLayout(null);
		
		JLabel lblProductName = new JLabel("Product Name");
		lblProductName.setBounds(23, 22, 90, 13);
		panelAddProd.add(lblProductName);
		
		JLabel lblQuiltPattern = new JLabel("Quilt Pattern");
		lblQuiltPattern.setBounds(23, 54, 90, 13);
		panelAddProd.add(lblQuiltPattern);
		
		JLabel lblProductStatus = new JLabel("Product Status");
		lblProductStatus.setBounds(23, 89, 90, 13);
		panelAddProd.add(lblProductStatus);
		
		JLabel lblProductMC = new JLabel("Product Material Costs");
		lblProductMC.setBounds(23, 131, 135, 13);
		panelAddProd.add(lblProductMC);
		
		JLabel lblProductCategory = new JLabel("Product Category");
		lblProductCategory.setBounds(23, 170, 135, 13);
		panelAddProd.add(lblProductCategory);
		
		JLabel lblProductDSPrices = new JLabel("Product Donate/Sell Prices");
		lblProductDSPrices.setBounds(23, 214, 161, 13);
		panelAddProd.add(lblProductDSPrices);
		
		JLabel lblProductTimeSpent = new JLabel("Time Spent (in hours)");
		lblProductTimeSpent.setBounds(23, 255, 161, 13);
		panelAddProd.add(lblProductTimeSpent);
		
		JButton btnAddProd = new JButton("Add Product");
		btnAddProd.setBounds(82, 325, 114, 21);
		panelAddProd.add(btnAddProd);
		
		JButton btnCancelProd = new JButton("Cancel Product");
		btnCancelProd.setBounds(216, 325, 135, 21);
		panelAddProd.add(btnCancelProd);
		
		txtProdName = new JTextField();
		txtProdName.setBounds(216, 19, 179, 19);
		panelAddProd.add(txtProdName);
		txtProdName.setColumns(10);
		
		txtProdPattern = new JTextField();
		txtProdPattern.setColumns(10);
		txtProdPattern.setBounds(216, 51, 179, 19);
		panelAddProd.add(txtProdPattern);
		
		txtMaterialCost = new JTextField();
		txtMaterialCost.setColumns(10);
		txtMaterialCost.setBounds(216, 128, 179, 19);
		panelAddProd.add(txtMaterialCost);
		
		txtProdDSPrices = new JTextField();
		txtProdDSPrices.setColumns(10);
		txtProdDSPrices.setBounds(216, 211, 179, 19);
		panelAddProd.add(txtProdDSPrices);
		
		txtTimeSpent = new JTextField();
		txtTimeSpent.setColumns(10);
		txtTimeSpent.setBounds(216, 252, 179, 19);
		panelAddProd.add(txtTimeSpent);
		
		String[] sortStatus = new String[] {"Finished", "Not Started", "Not Finished"};
		JComboBox<String> cboxProductStatusAdd = new JComboBox<>(sortStatus);
		cboxProductStatusAdd.setBounds(216, 85, 179, 21);
		panelAddProd.add(cboxProductStatusAdd);
		
		String[] sortCategory = new String [] {"Inventory", "Sell", "Donate"};
		JComboBox<String> cboxDonSelAdd = new JComboBox<>(sortCategory);
		cboxDonSelAdd.setBounds(216, 166, 179, 21);
		panelAddProd.add(cboxDonSelAdd);
		
		JPanel panelEditProd = new JPanel();
		tabbedPane.addTab("Edit Product", null, panelEditProd, null);
		panelEditProd.setLayout(null);
		
		JButton btnCancelChange = new JButton("Cancel Changes");
		btnCancelChange.setBounds(215, 336, 135, 21);
		panelEditProd.add(btnCancelChange);
		
		JButton btnSaveProd = new JButton("Save Product");
		btnSaveProd.setBounds(81, 336, 114, 21);
		panelEditProd.add(btnSaveProd);
		
		txtTimeSpentEd = new JTextField();
		txtTimeSpentEd.setColumns(10);
		txtTimeSpentEd.setBounds(215, 285, 179, 19);
		panelEditProd.add(txtTimeSpentEd);
		
		JLabel lblProductTimeSpentEd = new JLabel("Time Spent (in hours)");
		lblProductTimeSpentEd.setBounds(22, 288, 183, 13);
		panelEditProd.add(lblProductTimeSpentEd);
		
		txtProdDSPricesEd = new JTextField();
		txtProdDSPricesEd.setColumns(10);
		txtProdDSPricesEd.setBounds(215, 240, 179, 19);
		panelEditProd.add(txtProdDSPricesEd);
		
		JLabel lblProductDSPricesEd = new JLabel("Product Donate/Sell Prices");
		lblProductDSPricesEd.setBounds(22, 243, 173, 13);
		panelEditProd.add(lblProductDSPricesEd);
		
		JLabel lblProductCategoryEd = new JLabel("Product Category");
		lblProductCategoryEd.setBounds(22, 199, 135, 13);
		panelEditProd.add(lblProductCategoryEd);
		
		JLabel lblProductMCEd = new JLabel("Product Material Costs");
		lblProductMCEd.setBounds(22, 164, 135, 13);
		panelEditProd.add(lblProductMCEd);
		
		txtMaterialCostEd = new JTextField();
		txtMaterialCostEd.setColumns(10);
		txtMaterialCostEd.setBounds(215, 161, 179, 19);
		panelEditProd.add(txtMaterialCostEd);
		
		txtProdPatternEd = new JTextField();
		txtProdPatternEd.setColumns(10);
		txtProdPatternEd.setBounds(215, 84, 179, 19);
		panelEditProd.add(txtProdPatternEd);
		
		txtProdNameEd = new JTextField();
		txtProdNameEd.setColumns(10);
		txtProdNameEd.setBounds(215, 52, 179, 19);
		panelEditProd.add(txtProdNameEd);
		
		JLabel lblProductNameEd = new JLabel("Product Name");
		lblProductNameEd.setBounds(22, 55, 90, 13);
		panelEditProd.add(lblProductNameEd);
		
		JLabel lblProductPatternEd = new JLabel("Quilt Pattern");
		lblProductPatternEd.setBounds(22, 87, 90, 13);
		panelEditProd.add(lblProductPatternEd);
		
		JLabel lblProductStatusEd = new JLabel("Product Status");
		lblProductStatusEd.setBounds(22, 122, 90, 13);
		panelEditProd.add(lblProductStatusEd);
		
		JComboBox<String> cboxProductStatusEd = new JComboBox<>(sortStatus);
		cboxProductStatusEd.setBounds(215, 118, 179, 21);
		panelEditProd.add(cboxProductStatusEd);
		
		JComboBox<String> cboxDonSelEd = new JComboBox<>(sortCategory);
		cboxDonSelEd.setBounds(215, 195, 179, 21);
		panelEditProd.add(cboxDonSelEd);
		
		JLabel lblProductId = new JLabel("Product Id");
		lblProductId.setBounds(22, 26, 90, 13);
		panelEditProd.add(lblProductId);
		
		txtProductId = new JTextField();
		txtProductId.setColumns(10);
		txtProductId.setBounds(215, 23, 179, 19);
		panelEditProd.add(txtProductId);
		
		JPanel panelDeleteProd = new JPanel();
		tabbedPane.addTab("Delete Product", null, panelDeleteProd, null);
		panelDeleteProd.setLayout(null);
		
		JButton btnCancelChanges = new JButton("Cancel Product");
		btnCancelChanges.setBounds(217, 327, 135, 21);
		panelDeleteProd.add(btnCancelChanges);
		
		JButton btnDeleteProduct = new JButton("Delete Product");
		btnDeleteProduct.setBounds(83, 327, 124, 21);
		panelDeleteProd.add(btnDeleteProduct);
		
		txtProductDelId = new JTextField();
		txtProductDelId.setColumns(10);
		txtProductDelId.setBounds(216, 286, 179, 19);
		panelDeleteProd.add(txtProductDelId);
		
		JLabel lblProductDelId = new JLabel("Product ID");
		lblProductDelId.setBounds(30, 289, 90, 13);
		panelDeleteProd.add(lblProductDelId);
		
		// Allows user to view a table with associated columns within the Delete Product tab
		JScrollPane scrollPaneList = new JScrollPane(tblList);
		scrollPaneList.setBounds(30, 23, 362, 230);
		panelDeleteProd.add(scrollPaneList);
		
		
		// The start for the Time tab for the Settings Page
		JPanel panelTime = new JPanel();
		tabbedPaneMain.addTab("Time", null, panelTime, null);
		panelTime.setLayout(null);
		
		TextField txtSearchTimeLog = new TextField();
		txtSearchTimeLog.setBounds(46, 48, 151, 21);
		panelTime.add(txtSearchTimeLog);
		
		JButton btnSearchTime = new JButton("Search");
//		btnSearchTime.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				searchTimeLog(txtSearchTimeLog.getText());
//			}
//		});
		btnSearchTime.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSearchTime.setBounds(223, 48, 110, 21);
		panelTime.add(btnSearchTime);
		
		
		
		JScrollPane scrollPaneTime = new JScrollPane(tblTime);
		scrollPaneTime.setForeground(Color.WHITE);
		scrollPaneTime.setBounds(46, 87, 437, 321);
		panelTime.add(scrollPaneTime);
		
		// A panel that will display the title of the page
		JPanel panelSettings = new JPanel();
		panelSettings.setBounds(10, 22, 125, 28);
		frame.getContentPane().add(panelSettings);
		
		// Will display the title of the page named Settings
		JLabel lblSettings = new JLabel("Settings");
		lblSettings.setFont(new Font("Tahoma", Font.BOLD, 10));
		panelSettings.add(lblSettings);
		
		// Will allow the user to return to the dashboard screen once pressed
		JButton btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dashboard generateReportWindow = new Dashboard();
				generateReportWindow.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		btnHome.setBounds(442, 21, 118, 29);
		frame.getContentPane().add(btnHome);
		
		
		frame.setBounds(100, 100, 600, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Will call the function to display all information based on the time log column from database
		viewTimeLog();
		
		viewProductInfo();
		
		viewFinacials();
	}
	
	// Function to allow the user to fetch product information
	private void viewProductInfo() {
		String query = "SELECT * FROM ITEMS";
		try(PreparedStatement stmt = conn.prepareStatement(query)){
			ResultSet rs = stmt.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) tblList.getModel();
			model.setRowCount(0);
			
			while (rs.next()) {
				model.addRow(new Object[] {
						rs.getInt("ITEM_ID"),
						rs.getString("ITEM_NM"),
				});
			}
			
	        // Disable editing in the table
            tblList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tblList.setDefaultEditor(Object.class, null);  // Disable editor for the entire table
	     
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	// Function to allow the user to fetch time log information
	private void viewTimeLog() {
		String query = "SELECT * FROM TIME_LOGS";
		try(PreparedStatement stmt = conn.prepareCall(query)){
	        ResultSet rs = stmt.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) tblTime.getModel();
			model.setRowCount(0);
			
			while (rs.next()) {
				model.addRow(new Object[] {
					rs.getInt("TIME_LOG_ID"),
					rs.getInt("ITEM_ID"),
					rs.getInt("TIME_SPENT_NO")				
				});
			}
	        // Disable editing in the table
			tblTime.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblTime.setDefaultEditor(Object.class, null);  // Disable editor for the entire table
	        
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	// Function to allow the user to fetch finacials information
	private void viewFinacials() {
		String query = "SELECT * FROM ITEMS";
		try(PreparedStatement stmt = conn.prepareCall(query)){
			ResultSet rs = stmt.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) tblFinacials.getModel();
			model.setRowCount(0);
			
			while(rs.next()) {
				model.addRow(new Object[] {
						rs.getInt("ITEM_ID"),
						rs.getString("ITEM_NM"),
						rs.getBigDecimal("MATERIAL_COST_AM")
				});
			}
			
	        // Disable editing in the table
			tblFinacials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblFinacials.setDefaultEditor(Object.class, null);  // Disable editor for the entire table
	        
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	// Function to allow the user to fetch and search time log information
//	private void searchTimeLog(String timeLog) {
//		if (timeLog == null || timeLog.trim().isEmpty()){
//			JOptionPane.showMessageDialog(frame, "Search bar cannot be empty", "Validation Error", JOptionPane.WARNING_MESSAGE);
//			return;
//		}
//		
//		String query = "SELECT * FROM TIME_LOGS WHERE ITEM_ID = ?";
//		try(PreparedStatement pst = conn.prepareCall(query)){
//			pst.setString(1, timeLog);
//			
//			ResultSet rs = pst.executeQuery();
//            DefaultTableModel model = new DefaultTableModel(
//                    new String[]{"TIME_LOG_ID", "ITEM_ID", "TIME_SPENT_NO"}, 0);
//            
//            boolean found = false;
//            
//            while(rs.next()) {
//            	found = true;
//            	model.addRow(new Object [] {
//            			rs.getInt("TIME_LOG_ID"),
//            			rs.getInt("ITEM_ID"),
//            			rs.getInt("TIME_SPENT_NO")
//            	});
//            }
//            
//            if(!found) {
//                JOptionPane.showMessageDialog(frame, "No product found with the ID '" + timeLog + "'.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
//                return;
//            }
//    		tblTime.setModel(model);
//            
//		} catch (SQLException ex) {
//            JOptionPane.showMessageDialog(frame, "Error fetching data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();	
//		}
//
//	}
}
