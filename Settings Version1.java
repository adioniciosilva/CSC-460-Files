    import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.TextField;
import java.awt.BorderLayout;
import java.awt.Button;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Settings {

	public JFrame frame;
	public JTable tblFinacials;
	public JTable tblTime;
	private JTextField txtProdName;
	private JTextField txtProdPattern;
	private JTextField txtProdStatus;
	private JTextField txtMaterialCost;
	private JTextField txtProdInventory;
	private JTextField txtProdDS;
	private JTextField txtProdDSPrices;
	private JTextField txtTimeSpent;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_15;

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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(216, 203, 175));
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPaneMain = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneMain.setBounds(10, 72, 550, 535);
		frame.getContentPane().add(tabbedPaneMain);
		
		/* 1. Financial form codes 
		 * 2. Product form codes
		 * 3. Time log form code
		 */
		
		// 1. Financial
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
		
		
		tblFinacials = new JTable();
		tblFinacials.setModel(new DefaultTableModel(
		new Object[][]{},
		new String[] {"Product ID", "Product Name", "Material Costs", "Donate/Sell Prices"}	
		));
		JScrollPane scrollPaneItems = new JScrollPane(tblFinacials);
		scrollPaneItems.setForeground(Color.WHITE);
		scrollPaneItems.setBounds(38, 83, 437, 321);
		panelFinancials.add(scrollPaneItems);
		
		
		String[] sortOptions = new String [] {"Option 1", "Option 2", "Option 3"};
		JComboBox<String> cboxOption = new JComboBox<>(sortOptions);
		cboxOption.setSelectedIndex(2);
		cboxOption.setFont(new Font("Tahoma", Font.BOLD, 10));
		cboxOption.setBounds(313, 44, 141, 21);
		panelFinancials.add(cboxOption);
		
		JButton btnPrintReport = new JButton("Print Report");
		btnPrintReport.setBounds(74, 427, 104, 21);
		panelFinancials.add(btnPrintReport);
		
		
		// 2. Product 
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
		
		JLabel lblProductPattern = new JLabel("Quilt Pattern");
		lblProductPattern.setBounds(23, 54, 90, 13);
		panelAddProd.add(lblProductPattern);	
		
		JLabel lblProductName_1_1 = new JLabel("Product Status");
		lblProductName_1_1.setBounds(23, 89, 90, 13);
		panelAddProd.add(lblProductName_1_1);
		
		JLabel lblProductName_1_1_1 = new JLabel("Product Material Costs");
		lblProductName_1_1_1.setBounds(23, 131, 135, 13);
		panelAddProd.add(lblProductName_1_1_1);
		
		JLabel lblProductName_1_1_1_1 = new JLabel("Product Inventory");
		lblProductName_1_1_1_1.setBounds(23, 171, 135, 13);
		panelAddProd.add(lblProductName_1_1_1_1);
		
		JLabel lblProductName_1_1_1_1_1 = new JLabel("Product Donate/Sell ");
		lblProductName_1_1_1_1_1.setBounds(23, 208, 135, 13);
		panelAddProd.add(lblProductName_1_1_1_1_1);
		
		JLabel lblProductName_1_1_1_1_1_1 = new JLabel("Product Donate/Sell Prices");
		lblProductName_1_1_1_1_1_1.setBounds(23, 249, 161, 13);
		panelAddProd.add(lblProductName_1_1_1_1_1_1);
		
		JLabel lblProductName_1_1_1_1_1_1_1 = new JLabel("Time Spent (in minutes)");
		lblProductName_1_1_1_1_1_1_1.setBounds(23, 289, 161, 13);
		panelAddProd.add(lblProductName_1_1_1_1_1_1_1);
		
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
		
		 // Editable Dropdown with button 
		 String[] patterns = {"Quilts", "Option 2", "Option 3", "Option 4"};
         JComboBox<String> patternDropdown = new JComboBox<>(patterns);
         patternDropdown.setBounds(216, 51, 179, 21);
         panelAddProd.add(patternDropdown);

         JButton editButton = new JButton("Edit");
         editButton.setBounds(393, 51, 30, 21);
         panelAddProd.add(editButton);

         editButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 patternDropdown.setEditable(true);
                 JOptionPane.showMessageDialog(frame, "Dropdown is now editable. Type a custom pattern.");
             }
         });

		
		// Drop down menu for Product Status
		String[] Status = { "Start", "In Progress", "Completed"};
		JComboBox<String> statusDropdown = new JComboBox<>(Status);
		statusDropdown.setBounds(216, 89, 179, 21);
		panelAddProd.add(statusDropdown);
		
		txtMaterialCost = new JTextField();
		txtMaterialCost.setColumns(10);
		txtMaterialCost.setBounds(216, 128, 179, 19);
		panelAddProd.add(txtMaterialCost);
		
		txtProdInventory = new JTextField();
		txtProdInventory.setColumns(10);
		txtProdInventory.setBounds(216, 168, 179, 19);
		panelAddProd.add(txtProdInventory);
		
		/* Jaiven Comment:
		 * Added a drop down box in:
		 * 1. Add Product
		 * 2. Edit Product
		 * 3. Delete Product
		 */
		
		// 1. Drop down in Add Product 
		String[] options = {"Donate", "Sell"};
		JComboBox<String> donateSellDropdown = new JComboBox<>(options);
		donateSellDropdown.setBounds(216, 205, 179, 21);
		panelAddProd.add(donateSellDropdown);
				
		txtTimeSpent = new JTextField();
		txtTimeSpent.setColumns(10);
		txtTimeSpent.setBounds(216, 245, 179, 19);
		panelAddProd.add(txtTimeSpent);
		
		txtTimeSpent = new JTextField();
		txtTimeSpent.setColumns(10);
		txtTimeSpent.setBounds(216, 286, 179, 19);
		panelAddProd.add(txtTimeSpent);
		
		
		
		//Edit Product 
		JPanel panelEditProd = new JPanel();
		tabbedPane.addTab("Edit Product", null, panelEditProd, null);
		panelEditProd.setLayout(null);
		
		JButton btnCancelChange = new JButton("Cancel Changes");
		btnCancelChange.setBounds(215, 326, 135, 21);
		panelEditProd.add(btnCancelChange);
		
		JButton btnSaveProd = new JButton("Save Product");
		btnSaveProd.setBounds(81, 326, 114, 21);
		panelEditProd.add(btnSaveProd);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(215, 287, 179, 19);
		panelEditProd.add(textField);
		
		JLabel lblProductName_1_1_1_1_1_1_1_1 = new JLabel("Time Spent (in minutes)");
		lblProductName_1_1_1_1_1_1_1_1.setBounds(22, 290, 183, 13);
		panelEditProd.add(lblProductName_1_1_1_1_1_1_1_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(215, 247, 179, 19);
		panelEditProd.add(textField_1);
		
		JLabel lblProductName_1_1_1_1_1_1_2 = new JLabel("Product Donate/Sell Prices");
		lblProductName_1_1_1_1_1_1_2.setBounds(22, 250, 173, 13);
		panelEditProd.add(lblProductName_1_1_1_1_1_1_2);
		
		//Drop down menu here
		String[] options2 = {"Donate", "Sell"};
		JComboBox<String> donateSellDropdown1 = new JComboBox<>(options);
		donateSellDropdown1.setBounds(216, 205, 179, 21);
		panelEditProd.add(donateSellDropdown1);
		
		JLabel lblProductName_1_1_1_1_1_2 = new JLabel("Product Donate/Sell ");
		lblProductName_1_1_1_1_1_2.setBounds(22, 209, 135, 13);
		panelEditProd.add(lblProductName_1_1_1_1_1_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(215, 169, 179, 19);
		panelEditProd.add(textField_3);
		
		JLabel lblProductName_1_1_1_1_2 = new JLabel("Product Inventory");
		lblProductName_1_1_1_1_2.setBounds(22, 172, 135, 13);
		panelEditProd.add(lblProductName_1_1_1_1_2);
		
		JLabel lblProductName_1_1_1_2 = new JLabel("Product Material Costs");
		lblProductName_1_1_1_2.setBounds(22, 132, 135, 13);
		panelEditProd.add(lblProductName_1_1_1_2);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(215, 129, 179, 19);
		panelEditProd.add(textField_4);
		
		// Drop Down in Edit Product
		String[] Status2 = { "Start", "In Progress", "Completed"};
		JComboBox<String> statusDropdown2 = new JComboBox<>(Status);
		statusDropdown2.setBounds(216, 89, 179, 21);
		panelEditProd.add(statusDropdown2);
		
		// Editable Drop Down Menu with button
		 String[] patterns2 = {"Quilts", "Option 2", "Option 3", "Option 4"};
         JComboBox<String> patternDropdown2 = new JComboBox<>(patterns2);
         patternDropdown2.setBounds(216, 52, 179, 21);
         panelEditProd.add(patternDropdown2);

         JButton editButton2 = new JButton("Edit");
         editButton2.setBounds(393, 52, 30, 21);
         panelEditProd.add(editButton2);

         editButton2.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 patternDropdown2.setEditable(true);
                 JOptionPane.showMessageDialog(frame, "Dropdown is now editable. Type a custom pattern.");
             }
         });

		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(215, 20, 179, 19);
		panelEditProd.add(textField_7);
		
		JLabel lblProductName_1 = new JLabel("Product Name");
		lblProductName_1.setBounds(22, 23, 90, 13);
		panelEditProd.add(lblProductName_1);
		
		JLabel lblProductPattern_1 = new JLabel("Quilt Pattern");
		lblProductPattern_1.setBounds(22, 55, 90, 13);
		panelEditProd.add(lblProductPattern_1);
		
		JLabel lblProductName_1_1_2 = new JLabel("Product Status");
		lblProductName_1_1_2.setBounds(22, 90, 90, 13);
		panelEditProd.add(lblProductName_1_1_2);
		
		//Delete Product
		JPanel panelDeleteProd = new JPanel();
		tabbedPane.addTab("Delete Product", null, panelDeleteProd, null);
		panelDeleteProd.setLayout(null);
		
		JButton btnCancelChanges = new JButton("Cancel Product");
		btnCancelChanges.setBounds(217, 327, 135, 21);
		panelDeleteProd.add(btnCancelChanges);
		
		JButton btnDeleteProduct = new JButton("Delete Product");
		btnDeleteProduct.setBounds(83, 327, 114, 21);
		panelDeleteProd.add(btnDeleteProduct);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(217, 288, 179, 19);
		panelDeleteProd.add(textField_8);
		
		JLabel lblProductName_1_1_1_1_1_1_1_2 = new JLabel("Time Spent (in minutes)");
		lblProductName_1_1_1_1_1_1_1_2.setBounds(24, 291, 173, 13);
		panelDeleteProd.add(lblProductName_1_1_1_1_1_1_1_2);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(217, 248, 179, 19);
		panelDeleteProd.add(textField_9);
		
		JLabel lblProductName_1_1_1_1_1_1_3 = new JLabel("Product Donate/Sell Prices");
		lblProductName_1_1_1_1_1_1_3.setBounds(24, 251, 173, 13);
		panelDeleteProd.add(lblProductName_1_1_1_1_1_1_3);
		
		String[] options3 = {"Donate", "Sell"};
		JComboBox<String> donateSellDropdown2 = new JComboBox<>(options);
		donateSellDropdown2.setBounds(216, 205, 179, 21);
		panelDeleteProd.add(donateSellDropdown2);
		
		JLabel lblProductName_1_1_1_1_1_3 = new JLabel("Product Donate/Sell ");
		lblProductName_1_1_1_1_1_3.setBounds(24, 210, 135, 13);
		panelDeleteProd.add(lblProductName_1_1_1_1_1_3);
		
		textField_11 = new JTextField();
		textField_11.setColumns(10);
		textField_11.setBounds(217, 170, 179, 19);
		panelDeleteProd.add(textField_11);
		
		JLabel lblProductName_1_1_1_1_3 = new JLabel("Product Inventory");
		lblProductName_1_1_1_1_3.setBounds(24, 173, 135, 13);
		panelDeleteProd.add(lblProductName_1_1_1_1_3);
		
		JLabel lblProductName_1_1_1_3 = new JLabel("Product Material Costs");
		lblProductName_1_1_1_3.setBounds(24, 133, 157, 13);
		panelDeleteProd.add(lblProductName_1_1_1_3);
		
		textField_12 = new JTextField();
		textField_12.setColumns(10);
		textField_12.setBounds(217, 130, 179, 19);
		panelDeleteProd.add(textField_12);
		
		// Drop Down in Delete Product
		String[] Status3 = { "Start", "In Progress", "Completed"};
		JComboBox<String> statusDropdown3 = new JComboBox<>(Status);
		statusDropdown3.setBounds(216, 89, 179, 21);
		panelDeleteProd.add(statusDropdown3);
		
		// Editable Drop Down Menu with button
		 String[] patterns3 = {"Quilts", "Option 2", "Option 3", "Option 4"};
         JComboBox<String> patternDropdown3 = new JComboBox<>(patterns3);
         patternDropdown3.setBounds(216, 52, 179, 21);
         panelDeleteProd.add(patternDropdown3);

         JButton editButton3 = new JButton("Edit");
         editButton3.setBounds(393, 52, 30, 21);
         panelDeleteProd.add(editButton3);

         editButton3.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 patternDropdown3.setEditable(true);
                 JOptionPane.showMessageDialog(frame, "Dropdown is now editable. Type a custom pattern.");
             }
         });
		
		textField_15 = new JTextField();
		textField_15.setColumns(10);
		textField_15.setBounds(217, 21, 179, 19);
		panelDeleteProd.add(textField_15);
		
		JLabel lblProductName_2 = new JLabel("Product Name");
		lblProductName_2.setBounds(24, 24, 90, 13);
		panelDeleteProd.add(lblProductName_2);
		
		JLabel lblProductPattern_2 = new JLabel("Quilt Pattern");
		lblProductPattern_2.setBounds(24, 56, 90, 13);
		panelDeleteProd.add(lblProductPattern_2);
		
		JLabel lblProductName_1_1_3 = new JLabel("Product Status");
		lblProductName_1_1_3.setBounds(24, 91, 90, 13);
		panelDeleteProd.add(lblProductName_1_1_3);
		
		// 3. Time log
		JPanel panelTime = new JPanel();
		tabbedPaneMain.addTab("Time", null, panelTime, null);
		panelTime.setLayout(null);
		
		// 2 Columns with item and time
		String[] columns = {"Items", "Time"};
		String[][] data = {
		    {"Item 1", "2 hours"},
		    {"Item 2", "3 hours"},
		    {"Item 3", "1.5 hours"}
		};

		DefaultTableModel model = new DefaultTableModel(data, columns);
		tblTime = new JTable(model);

		JScrollPane scrollPaneTime = new JScrollPane(tblTime);
		scrollPaneTime.setBounds(38, 83, 437, 321);
		panelTime.add(scrollPaneTime);
		
		JPanel panelSettings = new JPanel();
		panelSettings.setBounds(10, 22, 125, 28);
		frame.getContentPane().add(panelSettings);
		
		JLabel lblSettings = new JLabel("Settings");
		lblSettings.setFont(new Font("Tahoma", Font.BOLD, 10));
		panelSettings.add(lblSettings);
		
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
		
	}
}
