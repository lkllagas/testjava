package view;

import java.awt.EventQueue;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import net.proteanit.sql.DbUtils;
import controller.sqliteConnection;

import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;

@SuppressWarnings("serial")
public class frmViewCheckStatus2 extends JFrame {
	
	Connection conn = sqliteConnection.dbConnector();
	private JPanel contentPane;
	private JTable table;
	private JTextField txtCheckNumber;
	private JTextField txtPayeeName;
	private JTextField txtChecNumDetail;
	private JTextField txtPayeeNameDetail;
	private JTextField txtCheckAmount;
	private JTextField txtCheckDate;
	private JTextField txtBatchNumber;
	private JTextField txtAccountNumber;
	private JTextField txtCorpCode;
	private int searchCounter = 0;
	JComboBox cmbStatus;
	JLabel lblTotalCount;
	private JTextField txtStatus;
	private String customQuery = "";
	private String status = "";
	private String fromDate = "";
	private String toDate = "";
	private String typeofDate = "";
	private JTextField txtReceived;
	private JTextField txtClaimed;
	private JTextField txtDispatched;
	private JTextField txtReceivedByUCC;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmViewCheckStatus frame = new frmViewCheckStatus();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	

	public frmViewCheckStatus2(String st, String fDate, String tDate, String typeDate) {
		
		this.status = st;
		this.fromDate = fDate;
		this.toDate = tDate;
		this.typeofDate = typeDate;
		initialize();
		LoadCustomQuery(status, fromDate, toDate, typeofDate);
		getTableCount();
		disableSearch();
		
	}
	
	
	public frmViewCheckStatus2() {

		initialize();
		
		loadRecords();
		

	}
	
	private void disableSearch() {
		
		txtCorpCode.setEnabled(false);
		txtCheckNumber.setEnabled(false);
		txtBatchNumber.setEnabled(false);
		txtPayeeName.setEnabled(false);
		txtAccountNumber.setEnabled(false);
		cmbStatus.setEnabled(false);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				frmMainMenu frame = new frmMainMenu();
				frame.setVisible(true);
				dispose();
				
			}
		});
		setResizable(false);
		setTitle("Check Status Inquiry");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 957, 651);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setLocationRelativeTo(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("");
		scrollPane.setBounds(20, 113, 914, 276);
		contentPane.add(scrollPane);
		
		table = new JTable() {
			
            @SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
            public final Class getColumnClass(int column) {
            	return getValueAt(0, column).getClass();
            }
            
			 public boolean isCellEditable(int row, int column) {                
	                return false;               
	          };
	            
		};
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setToolTipText("double click to tag the check as claimed");
		table.addMouseListener(new MouseAdapter() {
	

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				
				try {
					
					int row = table.getSelectedRow();
					String checkNum = (table.getModel().getValueAt(row, 0)).toString();
					String payeeName = (table.getModel().getValueAt(row, 1)).toString();
					
					String query = "Select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName, Status, ForReleasingStamp, ClaimedStamp, DispatchedToUCCStamp, ReceivedByUCCStamp from tblCheck where CheckNumber='"+ checkNum +"' and PayeeName = '" + payeeName + "'";
					PreparedStatement pst = conn.prepareStatement(query);
					
					
					ResultSet rs = pst.executeQuery();
					
					while (rs.next()) {
						
						txtChecNumDetail.setText(rs.getString("CheckNumber"));
						txtPayeeNameDetail.setText(rs.getString("PayeeName"));
						txtCheckAmount.setText(rs.getString("CheckAmount"));
						txtCheckDate.setText(rs.getString("CheckDate"));
						txtStatus.setText(rs.getString("Status"));
						txtReceived.setText(rs.getString("ForReleasingStamp"));
						txtClaimed.setText(rs.getString("ClaimedStamp"));
						txtDispatched.setText(rs.getString("DispatchedToUCCStamp"));
						txtReceivedByUCC.setText(rs.getString("DispatchedToUCCStamp"));
						
					}

					pst.close();
					rs.close();

				} catch (Exception ex) {
					
					ex.printStackTrace();
					
				}
		
				
			}
		});
		scrollPane.setViewportView(table);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Search Filter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(20, 6, 902, 95);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblCheckNumber = new JLabel("Check Number:");
		lblCheckNumber.setBounds(18, 56, 99, 16);
		panel.add(lblCheckNumber);
		
		txtCheckNumber = new JTextField();
		txtCheckNumber.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				
			
				
				searchFilter();
				
			}

		});
		txtCheckNumber.setBounds(110, 50, 171, 28);
		panel.add(txtCheckNumber);
		txtCheckNumber.setColumns(10);
		
		JLabel lblPayeeName = new JLabel("Payee Name:");
		lblPayeeName.setBounds(302, 56, 79, 16);
		panel.add(lblPayeeName);
		
		txtPayeeName = new JTextField();
		txtPayeeName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				
				searchFilter();
				
			}

		});
		txtPayeeName.setColumns(10);
		txtPayeeName.setBounds(393, 50, 171, 28);
		panel.add(txtPayeeName);
		
		JLabel lblBatchNumber = new JLabel("Batch Number:");
		lblBatchNumber.setBounds(301, 26, 87, 16);
		panel.add(lblBatchNumber);
		
		txtBatchNumber = new JTextField();
		txtBatchNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				searchFilter();
			}
		});
		txtBatchNumber.setColumns(10);
		txtBatchNumber.setBounds(393, 20, 171, 28);
		panel.add(txtBatchNumber);
		
		JLabel lblAccountNumber = new JLabel("Account Number:");
		lblAccountNumber.setBounds(588, 26, 105, 16);
		panel.add(lblAccountNumber);
		
		txtAccountNumber = new JTextField();
		txtAccountNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				searchFilter();
			}
		});
		txtAccountNumber.setColumns(10);
		txtAccountNumber.setBounds(694, 20, 171, 28);
		panel.add(txtAccountNumber);
		
		JLabel lblCorpCode = new JLabel("Payor's Name:");
		lblCorpCode.setBounds(18, 26, 87, 16);
		panel.add(lblCorpCode);
		
		txtCorpCode = new JTextField();
		txtCorpCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				searchFilter();
			}
		});
		txtCorpCode.setColumns(10);
		txtCorpCode.setBounds(110, 20, 171, 28);
		panel.add(txtCorpCode);
		
		cmbStatus = new JComboBox();
		cmbStatus.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				
	
	            if (event.getStateChange() == ItemEvent.SELECTED) {
	                Object item = event.getItem();

	                searchFilter();
	                
	             }
		            
		 	
		       	
			}
		});
		
		

		cmbStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				
				searchFilter();
				
			}
		});

		cmbStatus.setModel(new DefaultComboBoxModel(new String[] {"-All-", "For Release", "Claimed", "Dispatched To UCC", "Received By UCC"}));
		cmbStatus.setBounds(694, 51, 171, 26);
		panel.add(cmbStatus);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(588, 56, 94, 16);
		panel.add(lblStatus);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Check Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(20, 401, 914, 105);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblCheckNumber_1 = new JLabel("Check Number:");
		lblCheckNumber_1.setBounds(21, 29, 91, 16);
		panel_1.add(lblCheckNumber_1);
		
		txtChecNumDetail = new JTextField();
		txtChecNumDetail.setEditable(false);
		txtChecNumDetail.setBounds(110, 23, 136, 28);
		panel_1.add(txtChecNumDetail);
		txtChecNumDetail.setColumns(10);
		
		JLabel lblPayeeName_1 = new JLabel("Payee Name:");
		lblPayeeName_1.setBounds(278, 29, 91, 16);
		panel_1.add(lblPayeeName_1);
		
		txtPayeeNameDetail = new JTextField();
		txtPayeeNameDetail.setEditable(false);
		txtPayeeNameDetail.setColumns(10);
		txtPayeeNameDetail.setBounds(356, 23, 344, 28);
		panel_1.add(txtPayeeNameDetail);
		
		JLabel lblCheckAmount = new JLabel("Check Amount:");
		lblCheckAmount.setBounds(21, 63, 91, 16);
		panel_1.add(lblCheckAmount);
		
		txtCheckAmount = new JTextField();
		txtCheckAmount.setEditable(false);
		txtCheckAmount.setColumns(10);
		txtCheckAmount.setBounds(110, 57, 136, 28);
		panel_1.add(txtCheckAmount);
		
		txtCheckDate = new JTextField();
		txtCheckDate.setEditable(false);
		txtCheckDate.setColumns(10);
		txtCheckDate.setBounds(356, 57, 136, 28);
		panel_1.add(txtCheckDate);
		
		JLabel lblCheckDate = new JLabel("Check Date:");
		lblCheckDate.setBounds(278, 63, 91, 16);
		panel_1.add(lblCheckDate);
		
		JLabel lblStatus_1 = new JLabel("Status:");
		lblStatus_1.setBounds(710, 29, 44, 16);
		panel_1.add(lblStatus_1);
		
		txtStatus = new JTextField();
		txtStatus.setEditable(false);
		txtStatus.setColumns(10);
		txtStatus.setBounds(757, 23, 136, 28);
		panel_1.add(txtStatus);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(null, "Status' Date and Time Stamp", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(20, 512, 715, 105);
		contentPane.add(panel_2);
		
		JLabel lblClaimed = new JLabel("Received in Branch:");
		lblClaimed.setBounds(43, 28, 118, 16);
		panel_2.add(lblClaimed);
		
		txtReceived = new JTextField();
		txtReceived.setEditable(false);
		txtReceived.setColumns(10);
		txtReceived.setBounds(168, 22, 188, 28);
		panel_2.add(txtReceived);
		
		JLabel lblClaimedByClient = new JLabel("Claimed By Client:");
		lblClaimedByClient.setBounds(368, 28, 118, 16);
		panel_2.add(lblClaimedByClient);
		
		txtClaimed = new JTextField();
		txtClaimed.setEditable(false);
		txtClaimed.setColumns(10);
		txtClaimed.setBounds(485, 22, 188, 28);
		panel_2.add(txtClaimed);
		
		JLabel lblDispatchedToUcc = new JLabel("Dispatched To UCC:");
		lblDispatchedToUcc.setBounds(43, 62, 118, 16);
		panel_2.add(lblDispatchedToUcc);
		
		txtDispatched = new JTextField();
		txtDispatched.setEditable(false);
		txtDispatched.setColumns(10);
		txtDispatched.setBounds(168, 56, 188, 28);
		panel_2.add(txtDispatched);
		
		JLabel lblReceivedByUcc = new JLabel("Received By UCC:");
		lblReceivedByUcc.setBounds(368, 62, 118, 16);
		panel_2.add(lblReceivedByUcc);
		
		txtReceivedByUCC = new JTextField();
		txtReceivedByUCC.setEditable(false);
		txtReceivedByUCC.setColumns(10);
		txtReceivedByUCC.setBounds(485, 56, 188, 28);
		panel_2.add(txtReceivedByUCC);
		
		lblTotalCount = new JLabel("");
		lblTotalCount.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTotalCount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalCount.setForeground(Color.RED);
		lblTotalCount.setBounds(747, 590, 187, 16);
		contentPane.add(lblTotalCount);
		
	}
	
	private void loadRecords() {
		
		try {
			
			String query = "Select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName, Status from tblCheck";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			
				
			tableProperty();
			
			pst.close();
			rs.close();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.toString(),"Error Loading Records..",JOptionPane.WARNING_MESSAGE);
		}
//		finally {
//			
//			  if (conn != null) {
//				  
//				    try {
//				      conn.close(); // <-- This is important
//				    } catch (SQLException e) {
//				      /* handle exception */
//				    }
//			  }
//		}
		
	}
	
	private void LoadCustomQuery(String st, String fDate, String tDate, String typeDate) {
		
		try {
			
			String query = "select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName, Status from tblCheck where Status = '" + st + "' and " + typeDate + " BETWEEN '" + fDate + "' and '" + tDate + " 23:59:00'";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			table.setAutoCreateRowSorter(true);
			
				
			tableProperty();
			
			pst.close();
			rs.close();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.toString(),"Error Loading Records..",JOptionPane.WARNING_MESSAGE);
		}
		
//		finally {
//			
//			  if (conn != null) {
//				  
//				    try {
//				      conn.close(); // <-- This is important
//				    } catch (SQLException e) {
//				      /* handle exception */
//				    }
//			  }
//		}
		
	}
	
	
	public void refreshTable() {
		
		try {
			
			String query = "Select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName from tblCheck where Status = 'For Release'";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			
			table.setModel(DbUtils.resultSetToTableModel(rs));
			table.setAutoCreateRowSorter(true);
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("Boolean Column");
			tableProperty();
			
			pst.close();
			rs.close();
			
	
		} catch (Exception ex) {
			
			ex.printStackTrace();
			
		} 
//		finally {
//			
//			  if (conn != null) {
//				  
//				    try {
//				      conn.close(); // <-- This is important
//				    } catch (SQLException e) {
//				      /* handle exception */
//				    }
//			  }
//		}
		
	}
	
	public void tableProperty() {
		
		
		table.getColumnModel().getColumn(0).setHeaderValue("Check Number");
		table.getColumnModel().getColumn(0).setPreferredWidth(79);
		table.getColumnModel().getColumn(0).setMinWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(1).setHeaderValue("Payee's Name");
		table.getColumnModel().getColumn(2).setPreferredWidth(85);
		table.getColumnModel().getColumn(2).setHeaderValue("Check Amount");
		table.getColumnModel().getColumn(3).setPreferredWidth(66);
		table.getColumnModel().getColumn(3).setHeaderValue("Check Date");
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setHeaderValue("Payor's Name");
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		
		
	}
	
	private String loadQuery() {
		
		String query  = "select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName, Status from tblCheck where " + buildWhere();

		return query;
		
	}
	
	private String buildWhere() {
		
		String where = "";
		
		if (!txtAccountNumber.getText().trim().isEmpty()) {
			
			if (!where.equals("")) where = where + "AccountNumber = '" + txtAccountNumber.getText().trim() + "'";
			else where = where + "AccountNumber = '" + txtAccountNumber.getText().trim() + "'";

		} 
		
		if (!txtBatchNumber.getText().trim().isEmpty()) { 
			
			if (!where.equals("")) where = where + "and BatchNumber = '" + txtBatchNumber.getText().trim().toUpperCase() + "'";
			else where = where + "BatchNumber = '" + txtBatchNumber.getText().trim().toUpperCase() + "'";
			
		} 
		
		if (!txtCheckNumber.getText().trim().isEmpty()) {
			
			if (!where.equals("")) where = where + "and CheckNumber = '" + txtCheckNumber.getText().trim() + "'";
			else where = where + "CheckNumber = '" + txtCheckNumber.getText().trim() + "'";
			
		} 
		
		if (!txtCorpCode.getText().trim().isEmpty()) {
			
			if (!where.equals("")) where = where + "and CorporateCode = '" + txtCorpCode.getText().trim().toUpperCase() + "'";
			else where = where + "CorporateCode = '" + txtCorpCode.getText().trim().toUpperCase() + "'";
			
		} 
		
		if (!txtPayeeName.getText().trim().isEmpty()) {
			
			if (!where.equals("")) where = where + "and PayeeName = '" + txtPayeeName.getText().trim().toUpperCase() + "'";
			else where = where + "PayeeName = '" + txtPayeeName.getText().trim().toUpperCase() + "'";
			
		}
		
		if (!cmbStatus.getSelectedItem().equals("-All-")) {
			if (!where.equals("")) where = where + "and Status = '" + cmbStatus.getSelectedItem() + "'";
			else where = where + "Status = '" + cmbStatus.getSelectedItem() + "'";
		}
		

		return where;
		
	}
	
	private void searchFilter(){
		
		try {
			
			if (txtCheckNumber.getText().trim().isEmpty() && txtPayeeName.getText().trim().isEmpty() && txtAccountNumber.getText().trim().isEmpty() && txtCorpCode.getText().trim().isEmpty() && txtBatchNumber.getText().trim().isEmpty() && cmbStatus.getSelectedItem().equals("")) {	

				   DefaultTableModel dm = (DefaultTableModel) table.getModel();

				   dm.setRowCount(0);
				   
			} else if (txtCheckNumber.getText().trim().isEmpty() && txtPayeeName.getText().trim().isEmpty() && txtAccountNumber.getText().trim().isEmpty() && txtBatchNumber.getText().trim().isEmpty() && cmbStatus.getSelectedItem().equals("-All-")) {
				
				loadRecords();
			
			} else {
				PreparedStatement pst = conn.prepareStatement(loadQuery());
				ResultSet rs = pst.executeQuery();
	
				table.setModel(DbUtils.resultSetToTableModel(rs));
				table.setAutoCreateRowSorter(true);
				tableProperty();
				pst.close();
				rs.close();
			
			}
			
		}  catch (Exception ex) {
			
			JOptionPane.showMessageDialog(null, ex.toString(),"Error loading data",JOptionPane.WARNING_MESSAGE);
			
		}
			
		
	}
	
	
	private void getTableCount() {
		
		int rows = table.getRowCount();
		
		lblTotalCount.setText(String.valueOf("Record Count: " + rows));
	
	}
}
