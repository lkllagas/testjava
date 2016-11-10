package view;

import java.awt.Cursor;
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
import controller.queryData;
import controller.sqliteConnection;
import model.Global;
import model.ReadExcel;

import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class frmChecksforClaiming extends JFrame {
	
	Connection conn = sqliteConnection.dbConnector();
	private JPanel contentPane;
	private JTable table;
	private JTextField txtCheckNumber;
	private JTextField txtPayeeName;
	private JTextField txtChecNumDetail;
	private JTextField txtPayeeNameDetail;
	private JTextField txtCheckAmount;
	private JTextField txtCheckDate;
	private JPanel panelImport;
	private JButton btnImport;
	private JLabel lblNumRecords;
	private JTextField txtImportExcel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmChecksforClaiming frame = new frmChecksforClaiming();
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
	public frmChecksforClaiming() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				frmMainMenu frame = new frmMainMenu();
				frame.setVisible(true);
				dispose();
				
			}
		});
		setResizable(false);
		setTitle("Checks for Claiming");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 828, 590);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setLocationRelativeTo(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("double click to tag the check as claimed");
		scrollPane.setBounds(10, 110, 792, 308);
		contentPane.add(scrollPane);
		
		
		
		
		table = new JTable() {

			 public boolean isCellEditable(int row, int column) {                
	                return false;               
	          };
	            
		};
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setToolTipText("double click to tag the check as claimed");
		table.addMouseListener(new MouseAdapter() {
	
			@Override
			public void mousePressed(MouseEvent e) {
				
				table =(JTable) e.getSource();
		        Point p = e.getPoint();
		        int row = table.rowAtPoint(p);
				if (e.getClickCount() == 2) {
					
					dispose();
					String checkNum = (table.getModel().getValueAt(row, 0)).toString();
					String payee = (table.getModel().getValueAt(row, 1)).toString();
					String checkDate = (table.getModel().getValueAt(row, 3)).toString();
					String checkAmount = (table.getModel().getValueAt(row, 2)).toString();
					
					
		            frmClaimTagging frame;
					try {
						frame = new frmClaimTagging(checkNum, payee, checkDate, checkAmount);
						
						 frame.setVisible(true);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
      
		        }
				
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				
				try {
					
					int row = table.getSelectedRow();
					String checkNum = (table.getModel().getValueAt(row, 0)).toString();
					String payeeName = (table.getModel().getValueAt(row, 1)).toString();
					
					String query = "Select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName from tblCheck where CheckNumber='"+ checkNum +"' and PayeeName = '" + payeeName + "'";
					PreparedStatement pst = conn.prepareStatement(query);
					
					
					ResultSet rs = pst.executeQuery();
					
					while (rs.next()) {
						
						txtChecNumDetail.setText(rs.getString("CheckNumber"));
						txtPayeeNameDetail.setText(rs.getString("PayeeName"));
						txtCheckAmount.setText(rs.getString("CheckAmount"));
						txtCheckDate.setText(rs.getString("CheckDate"));
						
					}

					pst.close();
					rs.close();

				} catch (Exception ex) {
					
					ex.printStackTrace();
					
				}
				
//				finally {
//					
//					  if (conn != null) {
//						  
//						    try {
//						      conn.close(); // <-- This is important
//						    } catch (SQLException e) {
//						      /* handle exception */
//						    }
//					  }
//				}
				
			}
		});
		scrollPane.setViewportView(table);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Search Filter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 292, 95);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblCheckNumber = new JLabel("Check Number:");
		lblCheckNumber.setBounds(16, 24, 99, 16);
		panel.add(lblCheckNumber);
		
		txtCheckNumber = new JTextField();
		txtCheckNumber.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				
				searchFilter();
				
			}
		});
		txtCheckNumber.setBounds(108, 18, 122, 28);
		panel.add(txtCheckNumber);
		txtCheckNumber.setColumns(10);
		
		JLabel lblPayeeName = new JLabel("Payee Name:");
		lblPayeeName.setBounds(16, 55, 79, 16);
		panel.add(lblPayeeName);
		
		txtPayeeName = new JTextField();
		txtPayeeName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				searchFilter();
				
			}
		});
		txtPayeeName.setColumns(10);
		txtPayeeName.setBounds(108, 49, 171, 28);
		panel.add(txtPayeeName);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Check Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 430, 782, 102);
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
		txtPayeeNameDetail.setBounds(356, 23, 409, 28);
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
		
		panelImport = new JPanel();
		panelImport.setBorder(new TitledBorder(null, "Import Function", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelImport.setBounds(314, 11, 484, 95);
		contentPane.add(panelImport);
		panelImport.setLayout(null);
		
		txtImportExcel = new JTextField();
		txtImportExcel.setEditable(false);
		txtImportExcel.setColumns(10);
		txtImportExcel.setBounds(16, 26, 362, 22);
		panelImport.add(txtImportExcel);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				queryData qd = new queryData();
				
				qd.getCompanySetup();
				
				String userDir = System.getProperty("user.home");

				JFileChooser db =  new JFileChooser(userDir +"/Desktop");
				
				FileFilter ft = new FileNameExtensionFilter("Excel Files",
						"xls");
				db.addChoosableFileFilter(ft);
				

				int returnVal = db.showOpenDialog(null);

				if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {

					java.io.File file = db.getSelectedFile();

					
					btnImport.setEnabled(true);
					
					txtImportExcel.setText(file.toString());

				}
				
				
			}
		});
		btnBrowse.setBounds(385, 26, 89, 23);
		panelImport.add(btnBrowse);
		
		btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				
				ReadExcel rx = new ReadExcel();

				// location of excel file
				String excelFilePath = txtImportExcel.getText();

				// excel column will start at index 0
				// 0 - check number
				// 1 - amount
				// 2 - or num
				// 3 - id
				// 4 - claimed by
				// 5 - claimed date
				
				int cols[] = {0, 1, 2, 3, 4, 5};
				
				// reading excel file
				rx.readExcelFileforImportingClaimedChecks(excelFilePath, cols, 1);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				loadRecords();
				
			}
		});
		btnImport.setEnabled(false);
		btnImport.setMnemonic('I');
		btnImport.setBounds(385, 53, 89, 23);
		panelImport.add(btnImport);
		
		lblNumRecords = new JLabel("");
		lblNumRecords.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumRecords.setForeground(Color.RED);
		lblNumRecords.setBounds(653, 535, 138, 16);
		contentPane.add(lblNumRecords);
		
		loadRecords();
		
		if (Global.isEnableImport()) panelImport.setVisible(true);
		else panelImport.setVisible(false);
		
		
	}
	
	private void loadRecords() {
		
		try {
			
			String query = "Select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName from tblCheck where Status = 'For Release'";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));

			
				
			tableProperty();
			
			pst.close();
			rs.close();
			
			lblNumRecords.setText("Number of record/s: " + table.getRowCount());
			
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
		
		String query = "";
		
		
		if (!txtCheckNumber.getText().isEmpty() && !txtPayeeName.getText().isEmpty()) {	
			query = "select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName from tblCheck where Status = 'For Release' and CheckNumber like '%"+ txtCheckNumber.getText() + "%' and PayeeName like '%"+ txtPayeeName.getText() + "%'";
		} else if (!txtCheckNumber.getText().isEmpty()) {
			query = "select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName from tblCheck where Status = 'For Release' and CheckNumber like '%"+ txtCheckNumber.getText() + "%'";
		}else if (!txtPayeeName.getText().isEmpty()) {
			query = "select CheckNumber, PayeeName, CheckAmount, CheckDate, CorporationName from tblCheck where Status = 'For Release' and PayeeName like '%"+ txtPayeeName.getText() + "%'";
		}
		
		return query;
		
	}
	
	private void searchFilter(){
		
		try {
			
			if (txtCheckNumber.getText().isEmpty() && txtPayeeName.getText().isEmpty()) {
				refreshTable();
			} else {
				
				PreparedStatement pst = conn.prepareStatement(loadQuery());
				ResultSet rs = pst.executeQuery();
	
				table.setModel(DbUtils.resultSetToTableModel(rs));
				tableProperty();
				pst.close();
				rs.close();
			
			}
			
		}  catch (Exception ex) {
			
			JOptionPane.showMessageDialog(null, ex.toString(),"Error loading data",JOptionPane.WARNING_MESSAGE);
			
		}
			
		
	}
}
