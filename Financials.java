import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.TextField;
import java.awt.Button;
import java.awt.Font;
import java.awt.Graphics2D;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
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
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
//import java.awt.Component;

//Jaiven Comment: Added import statements for the Financial class
import java.awt.FontFormatException;
import java.io.File;


public class Financials {

    // Database connection variables
    Connection conn = null;
    // Connection based on SQLite within the Project Folder
    String dbConnect = "jdbc:sqlite:../project/database/mamaspiddlins.sqlite";
    //  String dbConnect = "jdbc:sqlite:C:/Users/adria/Downloads/School/LR 2024-2025/Spring 2025/CSC 460/Work/Project/Project/database/mamaspiddlins.db";
    //  String dbUserName = "root";
    //  String dbPass = "root";
	
	public JFrame frmFinancials;
	public JTable tblFinacials;
	
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
	
	/* NOTE Jaiven Comment:
	 * 
	 * CHANGES:
	 * 1. Added TOTAL_REVENUE and PROFIT_AM columns to the table
	 * 2.
	 * 
	 */
	private void initialize() {
		// Allows user to view a table with associated columns within the Finacials tab
		tblFinacials = new JTable();
		tblFinacials.setModel(new DefaultTableModel(
		new Object[][]{},
		new String[] {"Product ID", "Product Name", "Material Costs", "Sell Prices", "Total Revenue", "Profit Amount"}	
		));
		
		// The beginning setup for the Financial Page
		frmFinancials = new JFrame();
		frmFinancials.setTitle("Financials");
		frmFinancials.setBounds(100, 100, 800, 800);
		frmFinancials.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFinancials.getContentPane().setBackground(new Color(216, 203, 175));
		frmFinancials.getContentPane().setLayout(null);
		
		
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
		lblFinancials.setBounds(25, 24, 200, 35);
		frmFinancials.getContentPane().add(lblFinancials);

		/* Here is the original code for the title of the page:
		 * 
		// The panel that will contain the title of the page
		JPanel financialsPanel = new JPanel();
		financialsPanel.setBounds(10, 10, 166, 40);
		frmFinancials.getContentPane().add(financialsPanel);
		
		// The label that will display the title of the page
		JLabel lblFinancials = new JLabel("Financial");
		lblFinancials.setFont(new Font("Tahoma", Font.BOLD, 10));
		financialsPanel.add(lblFinancials);
		*/

		// A textfield to search within the database 
		TextField txtSearchBox = new TextField();
		txtSearchBox.setBounds(60, 120, 151, 21);
		txtSearchBox.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        searchFinancials(txtSearchBox.getText());
		    }
		});
		frmFinancials.getContentPane().add(txtSearchBox);

		// A button to search within the database based on search textfield
		Button btnSearch1 = new Button("Search");
		btnSearch1.setBounds(220, 120, 66, 21); // Changed x coordinate from 291 to 220
		btnSearch1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        searchFinancials(txtSearchBox.getText());
		    }
		});
		btnSearch1.setFont(new Font("Dialog", Font.BOLD, 12));
		frmFinancials.getContentPane().add(btnSearch1);
		
			/* NOTE: Jaiven Comment:
			 * 
			 * CHANGES:
			 * 1. Added a option for the user to click on the selected table
			 * 2.
			 */
			tblFinacials.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
	        if (!event.getValueIsAdjusting() && tblFinacials.getSelectedRow() != -1) {
	            try {
	                String productId = tblFinacials.getValueAt(tblFinacials.getSelectedRow(), 0).toString();
	                String productName = tblFinacials.getValueAt(tblFinacials.getSelectedRow(), 1).toString();
	                String materialCost = tblFinacials.getValueAt(tblFinacials.getSelectedRow(), 2).toString();
	                selectedTable();
	                System.out.println("Selected Product - ID: " + productId + ", Name: " + productName + ", Material Cost: " + materialCost);
	            } catch (ArrayIndexOutOfBoundsException ex) {
	                System.err.println("Error accessing selected row data: " + ex.getMessage());

	            }
	        }
	    }
			
	});

		// Filtering options for user based on the associated options
		String[] sortOptions = new String [] {"Product Name", "Material Cost", "Sell Price", "Total Revenue", "Profit Amount"};
		JComboBox<String> cboxOption = new JComboBox<>(sortOptions);
		cboxOption.setBounds(542, 120, 141, 21);
		cboxOption.setSelectedIndex(2);
		cboxOption.setFont(new Font("Tahoma", Font.BOLD, 10));
		
		
		frmFinancials.getContentPane().add(cboxOption);
		
		/*NOTE: Jaiven Comment:
		 * 
		 * CHANGES: Added a return button to reset the table content
		 * 
		 */
		JButton btnReturn = new JButton("Reset");
		btnReturn.setBounds(300, 120, 100, 21); 
		btnReturn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        viewFinacials(); 
		    }
		});
		btnReturn.setFont(new Font("Dialog", Font.BOLD, 12));
		frmFinancials.getContentPane().add(btnReturn);

		// Allows user to view a table with associated columns within the Financials tab
		JScrollPane scrollPaneItems = new JScrollPane(tblFinacials);
		scrollPaneItems.setBounds(60, 170, 666, 408);
		scrollPaneItems.setForeground(Color.WHITE);
		frmFinancials.getContentPane().add(scrollPaneItems);
		
		// A panel background that will hold the button to return back to the background
		JPanel financialsBackgroundPanel = new JPanel();
		financialsBackgroundPanel.setBounds(10, 80, 755, 590);
		frmFinancials.getContentPane().add(financialsBackgroundPanel);
		financialsBackgroundPanel.setLayout(null);
		
		// A button to print a report based on the Financial table
		JButton btnPrintReport = new JButton("Print Report");
		btnPrintReport.setBounds(51, 536, 104, 21);
		
		/*Note Jaiven Comment:
		 * 
		 * CHANGES:
		 * Removed the gray panel that was around the home button 
		 * 
		 */
		
		//Here is new code for the button 
		financialsBackgroundPanel.add(btnPrintReport);
		JButton btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        frmFinancials.setVisible(false);
		        Dashboard dashboardPage = new Dashboard();
		        dashboardPage.frmDashboard.setVisible(true);
		    }
		});
		btnHome.setBounds(589, 24, 85, 28);
		frmFinancials.getContentPane().add(btnHome);
		
		 /*Here is the original code for the button:
	
		JButton btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmFinancials.setVisible(false);
				Dashboard dashboardPage = new Dashboard();
				dashboardPage.frmDashboard.setVisible(true);
			}
		});
		btnHome.setBounds(50, 10, 132, 28);
		homePanel.add(btnHome);
		 */
		
		/* Note: Jaiven Comment:
		 * 
		 * CHANGES:
		 * 1. Added a button to move to the products tab
		 * 2. Added a button to move to the dashboard tab
		 * 
		 */
		
		//Button to move to the next tab 
		JButton btnProducts = new JButton("Products");
		btnProducts.setBounds(612, 532, 100, 28);
		financialsBackgroundPanel.add(btnProducts);
		btnProducts.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			frmFinancials.setVisible(false);
			Products productsPage = new Products();
			productsPage.frmProducts.setVisible(true);
			}
		});
		
		// Button to move to the dashboard tab
		JButton btnDashboard = new JButton("Dashboard");
		btnDashboard.setBounds(502, 532, 100, 28);
		financialsBackgroundPanel.add(btnDashboard);
		btnDashboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmFinancials.setVisible(false);
				Dashboard dashboardPage = new Dashboard();
				dashboardPage.frmDashboard.setVisible(true);
			}
		});
		
		// A button that will print a report based on the current financials page
		btnPrintReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printReport();
			}
		});
		
		// Will call the function to display all information based on the time log column from database
		viewFinacials();
		
	}
	
	/* NOTE Jaiven Comment:
	 * 
	 * CHANGES:
	 * Updated the search function for incorrect input to return to the full table
	 * 
	 * PROBLEMS:
	 * The search function does not work
	 * User is not able to return to the full table 
	 * Table goes blank whenever the user Input a incorrect item in the search bar
	 * 
	 * SOLUTION:
	 * call the viewFinacials() function to reset the table to the full table
	 * modified the search function to return to the full table if the search bar is empty
	 */
	
	// Function to allow the user to search for finances 
	private void searchFinancials(String financeInfo) {
	    if (financeInfo == null || financeInfo.trim().isEmpty()) {
	    	JOptionPane.showMessageDialog(frmFinancials, "Search bar cannot be empty", "Incorrect Input", JOptionPane.WARNING_MESSAGE);
	        viewFinacials(); // Reset to full table when search is empty
	        return;
	    }
	    
	    String query = "SELECT ITEM_ID, ITEM_NM, MATERIAL_COST_AM FROM items WHERE ITEM_NM LIKE ?";
	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, "%" + financeInfo + "%");
	        ResultSet rs = stmt.executeQuery();
	
	        DefaultTableModel model = (DefaultTableModel) tblFinacials.getModel();
	        model.setRowCount(0);
	
	        while (rs.next()) {
	            model.addRow(new Object[]{
	                rs.getInt("ITEM_ID"),
	                rs.getString("ITEM_NM"),
	                rs.getBigDecimal("MATERIAL_COST_AM")
	            });
	        }
	
	        if (model.getRowCount() == 0) {
	            JOptionPane.showMessageDialog(frmFinancials, "No items found matching: " + financeInfo, "Search Results", JOptionPane.INFORMATION_MESSAGE);
	            viewFinacials(); // Reset table if no results found
	        }
	
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frmFinancials, "Error searching database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
	
	/* Note: Jaiven Comment:
	 * 
	 * CHANGES:
	 * 1. Added a function to allow the user to select for finances
	 * 
	 * PROBLEMS:
	 * 1. The row was not displaying correctly when selected.
	 * 
	 * SOLUTIONS:
	 * Exception Handling to x
	 */
	private void selectedTable() {
	    tblFinacials.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent event) {
	            if (!event.getValueIsAdjusting() && tblFinacials.getSelectedRow() != -1) {
	                try {
	                    
	                    String selectedProduct = tblFinacials.getValueAt(tblFinacials.getSelectedRow(), 1).toString();
	                    
	                    // Query for the selected product only
	                    String query = "SELECT ITEM_ID, ITEM_NM, MATERIAL_COST_AM FROM items WHERE ITEM_NM = ?";
	                    PreparedStatement stmt = conn.prepareStatement(query);
	                    stmt.setString(1, selectedProduct);
	                    ResultSet rs = stmt.executeQuery();

	                    // Update table to show only selected row
	                    DefaultTableModel model = (DefaultTableModel) tblFinacials.getModel();
	                    model.setRowCount(0);

	                    if (rs.next()) {
	                        model.addRow(new Object[]{
	                            rs.getInt("ITEM_ID"),
	                            rs.getString("ITEM_NM"),
	                            rs.getBigDecimal("MATERIAL_COST_AM")
	                        });
	                    }

	                } catch (SQLException | ArrayIndexOutOfBoundsException ex) {
	                    JOptionPane.showMessageDialog(frmFinancials, "Error accessing selected row: " + ex.getMessage(), 
	                        "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }
	    });
	}


	// Function to print the report and save it to a file 
		private void printReport() {
			try {
				// Ensure the directory exists
				File directory = new File("images");
	            if (!directory.exists()) {
	                directory.mkdirs(); // Create the directory if it doesn't exist
	            }
	            
	            // Capture the content of the JFrame
				BufferedImage image = new BufferedImage(frmFinancials.getWidth(), frmFinancials.getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = image.createGraphics();
				
	            // Render the frame to the image
				frmFinancials.paint(graphics);
				
	            // Save the image to a file (PNG format)
				File file = new File("reports/financialReport.png");
				ImageIO.write(image, "PNG", file);
				
				JOptionPane.showMessageDialog(frmFinancials, "Report exported successfully!", "Export Success", JOptionPane.INFORMATION_MESSAGE);
	
			} catch(IOException ex) {
				JOptionPane.showMessageDialog(frmFinancials, "Error exporting report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
	
		}
		
		// Function to allow the user to fetch finacials information
		private void viewFinacials() {
			String query = "SELECT * FROM items";
			try(PreparedStatement stmt = conn.prepareStatement(query)){
				ResultSet rs = stmt.executeQuery();
				
				DefaultTableModel model = (DefaultTableModel) tblFinacials.getModel();
				model.setRowCount(0);
				
				while(rs.next()) {
					model.addRow(new Object[] {
							rs.getInt("ITEM_ID"),
							rs.getString("ITEM_NM"),
							rs.getBigDecimal("MATERIAL_COST_AM"),
					});
				}
				
		        // Disable editing in the table
				tblFinacials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				tblFinacials.setDefaultEditor(Object.class, null);  // Disable editor for the entire table
				tblFinacials.getTableHeader().setReorderingAllowed(false);
	
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}