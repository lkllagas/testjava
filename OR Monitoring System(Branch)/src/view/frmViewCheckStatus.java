package view;

import java.awt.EventQueue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import net.proteanit.sql.DbUtils;
import controller.queryData;
import controller.sqliteConnection;

import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

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
import java.awt.Cursor;

import javax.swing.SwingConstants;
import com.toedter.calendar.JDateChooser;
import javax.swing.UIManager;
import com.toedter.calendar.JCalendar;

@SuppressWarnings("serial")
public class frmViewCheckStatus extends JFrame {
	
	Connection conn = sqliteConnection.dbConnector();
	private JPanel contentPane;
	private JTable table;
	private JTextField txtCheckNumber;
	private JTextField txtPayeeName;
	private JTextField txtBatchNumber;
	private JTextField txtPayorName;

	@SuppressWarnings("rawtypes")
	JComboBox cmbStatus;
	private JTextField txtReceived;
	private JTextField txtClaimed;
	private JTextField txtDispatched;
	private JTextField txtReceivedByUCC;
	private JTextField txtORNumber;
	private JTextField txtClaimedBy;
	private JTextField txtIDPresented;
	private JTextField txtClaimedDate;
	private JTextField txtReleasedOn;
	private String status = "";
	private String fromDate = "";
	private String toDate = "";
	private String typeofDate = "";
	JPanel panel;
	private JTextField txtUCCBarCode;
	private JLabel lblClientsBarCode;
	private JLabel lblOrNumber_1;
	private JTextField txtORSearch;
	private JLabel lblNumRecords;
	private JDateChooser dateFrom;
	private JDateChooser dateTo;

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
	
	public frmViewCheckStatus(String st, String fDate, String tDate, String typeDate) {
		
		this.status = st;
		this.fromDate = fDate;
		this.toDate = tDate;
		this.typeofDate = typeDate;
		initialize();
		LoadCustomQuery(status, fromDate, toDate, typeofDate);
		getTableCount();
		disableSearch();
		
	}
	
	public frmViewCheckStatus() {
		
		initialize();
		
		loadRecords();
		
	}
	
	private void disableSearch() {
		
		txtPayorName.setEnabled(false);
		txtCheckNumber.setEnabled(false);
		txtBatchNumber.setEnabled(false);
		txtPayeeName.setEnabled(false);
		txtUCCBarCode.setEnabled(false);
		txtIDPresented.setEnabled(false);
		txtORSearch.setEnabled(false);
		cmbStatus.setEnabled(false);
		dateFrom.setEnabled(false);
		dateTo.setEnabled(false);
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
		setBounds(100, 100, 960, 772);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setLocationRelativeTo(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("");
		scrollPane.setBounds(20, 172, 914, 308);
		contentPane.add(scrollPane);
		
		table = new JTable() {
			

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
		table.setToolTipText("");
		table.addMouseListener(new MouseAdapter() {
	

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				
				try {
					
					int row = table.getSelectedRow();

					String checkNum = (table.getModel().getValueAt(row, 0)).toString();
					String payeeName = (table.getModel().getValueAt(row, 1)).toString();
					String checkAmount = (table.getModel().getValueAt(row, 2)).toString();
					
					String query = "Select ORNo, ClaimedBy, ClaimedID, ClaimedDate, ReleasingBranchName,  ForReleasingStamp, ClaimedStamp, DispatchedToUCCStamp, ReceivedByUCCStamp from tblCheck where CheckNumber='"+ checkNum +"' and PayeeName = '" + payeeName + "' and CheckAmount = '" + checkAmount + "'";
					PreparedStatement pst = conn.prepareStatement(query);

					ResultSet rs = pst.executeQuery();
					
					while (rs.next()) {
						

						txtORNumber.setText(rs.getString("ORNo"));
						txtClaimedBy.setText(rs.getString("ClaimedBy"));
						txtIDPresented.setText(rs.getString("ClaimedID"));
						txtClaimedDate.setText(rs.getString("ClaimedDate"));
						txtReleasedOn.setText(rs.getString("ReleasingBranchName"));
						txtReceived.setText(rs.getString("ForReleasingStamp"));
						txtClaimed.setText(rs.getString("ClaimedStamp"));
						txtDispatched.setText(rs.getString("DispatchedToUCCStamp"));
						txtReceivedByUCC.setText(rs.getString("ReceivedByUCCStamp"));

						
					}

					pst.close();
					rs.close();

				} catch (Exception ex) {
					
					ex.printStackTrace();
					
				}

				
			}
		});
		scrollPane.setViewportView(table);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Search Filter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(20, 6, 914, 154);
		contentPane.add(panel);
		
		txtPayorName = new JTextField();
		txtPayorName.setToolTipText("Payor's Name");
		txtPayorName.setBounds(110, 20, 171, 28);
		txtPayorName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				searchFilter();
			}
		});
		panel.setLayout(null);
		txtPayorName.setColumns(10);
		panel.add(txtPayorName);
		
		txtBatchNumber = new JTextField();
		txtBatchNumber.setToolTipText("Batch Number");
		txtBatchNumber.setBounds(413, 50, 171, 28);
		txtBatchNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				searchFilter();
			}
		});
		txtBatchNumber.setColumns(10);
		panel.add(txtBatchNumber);
		
		cmbStatus = new JComboBox();
		cmbStatus.setToolTipText("Check Status");
		cmbStatus.setBounds(720, 20, 171, 26);
		cmbStatus.addItemListener(new ItemListener() {
			@SuppressWarnings("unused")
			public void itemStateChanged(ItemEvent event) {
				
	
	            if (event.getStateChange() == ItemEvent.SELECTED) {
	                Object item = event.getItem();

	                searchFilter();
	             }
		            
		 	
		       	
			}
		});
		
		
		
		cmbStatus.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
			}
		});
		cmbStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				
				searchFilter();
				
			}
		});
		cmbStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		cmbStatus.setModel(new DefaultComboBoxModel(new String[] {"-All-", "For Release", "Claimed", "Dispatched To UCC", "Received By UCC", "Dispatched To Client", "Received By Client"}));
		panel.add(cmbStatus);
		
		JLabel lblPayeeName = new JLabel("Payee Name:");
		lblPayeeName.setBounds(301, 26, 79, 16);
		panel.add(lblPayeeName);
		
		JLabel lblBatchNumber = new JLabel("Batch Number:");
		lblBatchNumber.setBounds(301, 56, 87, 16);
		panel.add(lblBatchNumber);
		
		txtCheckNumber = new JTextField();
		txtCheckNumber.setToolTipText("Check Number");
		txtCheckNumber.setBounds(110, 50, 171, 28);
		txtCheckNumber.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				searchFilter();
				
			}

		});
		panel.add(txtCheckNumber);
		txtCheckNumber.setColumns(10);
		
		txtPayeeName = new JTextField();
		txtPayeeName.setToolTipText("Payee Name");
		txtPayeeName.setBounds(413, 20, 171, 28);
		txtPayeeName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				
				searchFilter();
				
			}

		});
		txtPayeeName.setColumns(10);
		panel.add(txtPayeeName);
		
		JLabel lblCheckNumber = new JLabel("Check Number:");
		lblCheckNumber.setBounds(18, 56, 99, 16);
		panel.add(lblCheckNumber);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(608, 26, 94, 16);
		panel.add(lblStatus);
		
		JLabel lblCorpCode = new JLabel("Payor's Name");
		lblCorpCode.setBounds(18, 26, 87, 16);
		panel.add(lblCorpCode);
		
		txtUCCBarCode = new JTextField();
		txtUCCBarCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				searchFilter();
				
			}
		});
		txtUCCBarCode.setToolTipText("Client's Bar Code");
		txtUCCBarCode.setBounds(110, 80, 171, 28);
		txtUCCBarCode.setColumns(10);
		panel.add(txtUCCBarCode);
		
		lblClientsBarCode = new JLabel("UCC Bar Code:");
		lblClientsBarCode.setBounds(18, 86, 105, 16);
		panel.add(lblClientsBarCode);
		
		lblOrNumber_1 = new JLabel("OR Number:");
		lblOrNumber_1.setBounds(301, 86, 87, 16);
		panel.add(lblOrNumber_1);
		
		txtORSearch = new JTextField();
		txtORSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				searchFilter();
				
			}
		});
		txtORSearch.setToolTipText("OR Number");
		txtORSearch.setBounds(413, 80, 171, 28);
		txtORSearch.setColumns(10);
		panel.add(txtORSearch);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Check Date", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(608, 50, 283, 98);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		dateFrom = new JDateChooser();

		dateFrom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
				
				
			}
			
			
		});
		
		dateFrom.getDateEditor().addPropertyChangeListener(
				 new PropertyChangeListener() {
			        @Override
			        public void propertyChange(PropertyChangeEvent e) {
			            if ("date".equals(e.getPropertyName())) {
	
			        		if (!(dateFrom.getDate() == null) && !(dateTo.getDate() == null)) {
			        			searchFilter();
			        		}

			            }
			        }
			    }
				
		);
		
		
		dateFrom.setToolTipText("Check Date From:");
		dateFrom.setBounds(104, 18, 162, 28);
		panel_3.add(dateFrom);
		
		JLabel lblFrom = new JLabel("From:");
		lblFrom.setBounds(23, 18, 55, 16);
		panel_3.add(lblFrom);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(23, 53, 55, 16);
		panel_3.add(lblTo);
		
		dateTo = new JDateChooser();
		dateTo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
		});
		
		dateTo.getDateEditor().addPropertyChangeListener(
				 new PropertyChangeListener() {
			        @Override
			        public void propertyChange(PropertyChangeEvent e) {
			            if ("date".equals(e.getPropertyName())) {
			            	
			            	if (!(dateFrom.getDate() == null) && !(dateTo.getDate() == null)) {
			        			searchFilter();
			        		}
			         
			            }
			        }
			    }
				
		);
		
		dateTo.setToolTipText("Check Date To:");
		dateTo.setBounds(104, 53, 162, 28);
		panel_3.add(dateTo);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(null, "Status' Date and Time Stamp", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(20, 602, 619, 105);
		contentPane.add(panel_2);
		
		JLabel lblImportedInThe = new JLabel("Imported in the System:");
		lblImportedInThe.setBounds(28, 27, 140, 16);
		panel_2.add(lblImportedInThe);
		
		txtReceived = new JTextField();
		txtReceived.setEditable(false);
		txtReceived.setColumns(10);
		txtReceived.setBounds(175, 21, 140, 28);
		panel_2.add(txtReceived);
		
		JLabel label_1 = new JLabel("Claimed By Client:");
		label_1.setBounds(329, 27, 118, 16);
		panel_2.add(label_1);
		
		txtClaimed = new JTextField();
		txtClaimed.setEditable(false);
		txtClaimed.setColumns(10);
		txtClaimed.setBounds(446, 21, 140, 28);
		panel_2.add(txtClaimed);
		
		JLabel label_2 = new JLabel("Dispatched To UCC:");
		label_2.setBounds(50, 61, 118, 16);
		panel_2.add(label_2);
		
		txtDispatched = new JTextField();
		txtDispatched.setEditable(false);
		txtDispatched.setColumns(10);
		txtDispatched.setBounds(175, 55, 140, 28);
		panel_2.add(txtDispatched);
		
		JLabel label_3 = new JLabel("Received By UCC:");
		label_3.setBounds(329, 61, 118, 16);
		panel_2.add(label_3);
		
		txtReceivedByUCC = new JTextField();
		txtReceivedByUCC.setEditable(false);
		txtReceivedByUCC.setColumns(10);
		txtReceivedByUCC.setBounds(446, 55, 140, 28);
		panel_2.add(txtReceivedByUCC);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Claiming Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(20, 492, 914, 105);
		contentPane.add(panel_1);
		
		JLabel lblOrNumber = new JLabel("OR Number:");
		lblOrNumber.setBounds(21, 29, 91, 16);
		panel_1.add(lblOrNumber);
		
		txtORNumber = new JTextField();
		txtORNumber.setEditable(false);
		txtORNumber.setColumns(10);
		txtORNumber.setBounds(110, 23, 136, 28);
		panel_1.add(txtORNumber);
		
		JLabel lblClaimedBy = new JLabel("Claimed By:");
		lblClaimedBy.setBounds(264, 29, 91, 16);
		panel_1.add(lblClaimedBy);
		
		txtClaimedBy = new JTextField();
		txtClaimedBy.setEditable(false);
		txtClaimedBy.setColumns(10);
		txtClaimedBy.setBounds(352, 23, 314, 28);
		panel_1.add(txtClaimedBy);
		
		JLabel lblIdPresented = new JLabel("ID Presented:");
		lblIdPresented.setBounds(678, 29, 91, 16);
		panel_1.add(lblIdPresented);
		
		txtIDPresented = new JTextField();
		txtIDPresented.setEditable(false);
		txtIDPresented.setColumns(10);
		txtIDPresented.setBounds(760, 23, 136, 28);
		panel_1.add(txtIDPresented);
		
		txtClaimedDate = new JTextField();
		txtClaimedDate.setEditable(false);
		txtClaimedDate.setColumns(10);
		txtClaimedDate.setBounds(110, 57, 136, 28);
		panel_1.add(txtClaimedDate);
		
		JLabel lblClaimedDate = new JLabel("Claimed Date:");
		lblClaimedDate.setBounds(21, 63, 91, 16);
		panel_1.add(lblClaimedDate);
		
		JLabel lblReleasedOn = new JLabel("Released At:");
		lblReleasedOn.setBounds(264, 63, 110, 16);
		panel_1.add(lblReleasedOn);
		
		txtReleasedOn = new JTextField();
		txtReleasedOn.setEditable(false);
		txtReleasedOn.setColumns(10);
		txtReleasedOn.setBounds(352, 57, 314, 28);
		panel_1.add(txtReleasedOn);
		
		lblNumRecords = new JLabel("");
		lblNumRecords.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumRecords.setForeground(Color.RED);
		lblNumRecords.setBounds(785, 716, 147, 16);
		contentPane.add(lblNumRecords);
		
		JButton btnNewButton = new JButton("Generate Excel File For This Inquiry");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (table.getRowCount() > 0) {
						
						queryData qd = new queryData();
						
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						ArrayList<String> checkNumber = new ArrayList<String>();
			       		ArrayList<String> payeeName = new ArrayList<String>();
			       		ArrayList<String> checkAmount = new ArrayList<String>();
			       		
			       		for(int i=0;i<table.getRowCount();i++) {
		
		       				checkNumber.add(table.getModel().getValueAt(i, 0).toString());
		       				payeeName.add(table.getModel().getValueAt(i,1).toString());
		       				checkAmount.add(table.getModel().getValueAt(i,2).toString());
		
			       		}
			       		
			       		qd.generateExcelForInquiry(checkNumber,payeeName,checkAmount);
			       		
			       		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			       		
				} else {
	       			JOptionPane.showMessageDialog(null, "No data to generate..");
	       		}
				
			}
		});
		btnNewButton.setBounds(30, 710, 245, 28);
		contentPane.add(btnNewButton);
		
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
			
			getTableCount();
			
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
			table.setAutoCreateRowSorter(false);
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
		
		//where Status = 'For Release' and PayeeName like '%"+ txtPayeeName.getText() + "%'";
	
		return query;
		
	}
	
	private String buildWhere() {
		
		String where = "";
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		if (!(dateFrom.getDate() == null) && !(dateTo.getDate() == null)) {
			
			String formattedDateFrom = sdf.format(dateFrom.getDate());
			String formattedDateTo = sdf.format(dateTo.getDate());
			
			//typeofDate + " BETWEEN '" + fromDate + "' and '" + toDate + " 23:59:00'";
			
			if (!where.equals("")) where = where + "and CheckDate BETWEEN '" + formattedDateFrom + "' and '" + formattedDateTo + "23:59:00'";
			else where = where + "CheckDate BETWEEN '" + formattedDateFrom + "' and '" + formattedDateTo + "23:59:00'";
			
		}
		
		if (!txtUCCBarCode.getText().trim().isEmpty()) { 
			
			if (!where.equals("")) where = where + "and UCCBarCode like '%" + txtUCCBarCode.getText().trim().toUpperCase() + "%'";
			else where = where + "UCCBarCode like '%" + txtUCCBarCode.getText().trim().toUpperCase() + "%'";
			
		} 
		

		
		if (!txtORSearch.getText().trim().isEmpty()) { 
			
			if (!where.equals("")) where = where + "and ORNo like '%" + txtORSearch.getText().trim().toUpperCase() + "%'";
			else where = where + "ORNo like '%" + txtORSearch.getText().trim().toUpperCase() + "%'";
			
		} 
		

		
		if (!txtBatchNumber.getText().trim().isEmpty()) { 
			
			if (!where.equals("")) where = where + "and BatchNumber like '%" + txtBatchNumber.getText().trim().toUpperCase() + "%'";
			else where = where + "BatchNumber like '%" + txtBatchNumber.getText().trim().toUpperCase() + "%'";
			
		} 
		
		if (!txtCheckNumber.getText().trim().isEmpty()) {
			
			if (!where.equals("")) where = where + "and CheckNumber like '%" + txtCheckNumber.getText().trim() + "%'";
			else where = where + "CheckNumber like '%" + txtCheckNumber.getText().trim() + "%'";
			
		} 
		
		if (!txtPayorName.getText().trim().isEmpty()) {
			
			if (!where.equals("")) where = where + "and CorporationName like '%" + txtPayorName.getText().trim().toUpperCase() + "%'";
			else where = where + "CorporationName like '%" + txtPayorName.getText().trim().toUpperCase() + "%'";
			
		} 
		
		if (!txtPayeeName.getText().trim().isEmpty()) {
			
			if (!where.equals("")) where = where + "and PayeeName like '%" + txtPayeeName.getText().trim().toUpperCase() + "%'";
			else where = where + "PayeeName like '%" + txtPayeeName.getText().trim().toUpperCase() + "%'";
			
		}
		
		if (!cmbStatus.getSelectedItem().equals("-All-")) {
			if (!where.equals("")) where = where + "and Status like '%" + cmbStatus.getSelectedItem() + "%'";
			else where = where + "Status like '%" + cmbStatus.getSelectedItem() + "%'";
		}
		

		return where;
		
	}
	
	private void searchFilter(){
		
		
		try {
			
			if (txtCheckNumber.getText().trim().isEmpty() && txtPayeeName.getText().trim().isEmpty() && dateFrom.getDate() == null && dateTo.getDate()==null && txtUCCBarCode.getText().trim().isEmpty() && txtORSearch.getText().trim().isEmpty() &&  txtUCCBarCode.getText().trim().isEmpty()  && txtPayorName.getText().trim().isEmpty() && txtBatchNumber.getText().trim().isEmpty() && cmbStatus.getSelectedItem().equals("")) {	

				   DefaultTableModel dm = (DefaultTableModel) table.getModel();

				   dm.setRowCount(0);
				   
			} else if (txtCheckNumber.getText().trim().isEmpty()  && dateFrom.getDate() == null && dateTo.getDate()==null && txtUCCBarCode.getText().trim().isEmpty() &&  txtORSearch.getText().trim().isEmpty() && txtUCCBarCode.getText().trim().isEmpty() && txtPayeeName.getText().trim().isEmpty()  && txtPayorName.getText().trim().isEmpty() && txtBatchNumber.getText().trim().isEmpty() && cmbStatus.getSelectedItem().equals("-All-")) {
				
					loadRecords();
					getTableCount();
					
			} else {
				
				PreparedStatement pst = conn.prepareStatement(loadQuery());
				ResultSet rs = pst.executeQuery();
	
				table.setModel(DbUtils.resultSetToTableModel(rs));
				table.setAutoCreateRowSorter(false);
				tableProperty();
				pst.close();
				rs.close();
				
				getTableCount();
			
			}
			
		}  catch (Exception ex) {
			
			JOptionPane.showMessageDialog(null, ex.toString(),"Error loading data",JOptionPane.WARNING_MESSAGE);
			
		}
	}
	
	private void LoadCustomQuery(String st, String fDate, String tDate, String typeDate) {
		
		try {
			
			String query = "select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName, Status from tblCheck where Status = '" + st + "' and " + typeDate + " BETWEEN '" + fDate + "' and '" + tDate + " 23:59:00'";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			table.setAutoCreateRowSorter(false);
			
				
			tableProperty();
			
			pst.close();
			rs.close();
			
			getTableCount();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.toString(),"Error Loading Records..",JOptionPane.WARNING_MESSAGE);
		}
		

	}
	
	private void getTableCount() {
		
		int rows = table.getRowCount();
		
		lblNumRecords.setText(String.valueOf("Record Count: " + rows));
	
	}
}
