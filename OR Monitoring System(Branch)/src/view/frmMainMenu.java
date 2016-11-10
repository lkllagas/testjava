package view;


import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import model.Global;
import model.ReadExcel;
import controller.queryData;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFormattedTextField;
import javax.swing.JTextPane;
import javax.swing.JSpinner;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;

import com.toedter.calendar.JDateChooser;
import javax.swing.JInternalFrame;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseMotionAdapter;


@SuppressWarnings("serial")
public class frmMainMenu extends JFrame {

	private JPanel contentPane;
	private String strProcessedFolder;
	private String strReceivedByUCCFolder;
	JMenuItem mntmUserMaintenance;
	JMenuItem mntmSystemSetup;
	JLabel lblOutstandingChecks;
	JLabel lblClaimedChecks;
	JLabel lblDispatchedToUCC;
	JLabel lblReceivedByUCC;
	JDateChooser dateFrom;
	JDateChooser dateTo;
	JLabel statusLabel = new JLabel();
	private final Action action = new SwingAction();
	//test 123
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					frmMainMenu frame = new frmMainMenu();
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
	
	 private Date DateNow(String dateFormat) {
		   
		    Calendar cal = Calendar.getInstance();
		    return (Date)(cal.getTime());

	  }
	
	public frmMainMenu() {
		//setAutoRequestFocus(false);

		initialize();
		
		//enable User Maintenance and System Setup if Admin is currently logged-in
		if (Global.getUserLoggedIn().equals("Admin")) {
            mntmUserMaintenance.setEnabled(true);
            mntmSystemSetup.setEnabled(true);
		}
		
		//setting up the Temp Folder
		try {
			setTempFolder(new File(".").getCanonicalPath() + "\\");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		lblOutstandingChecks.requestFocus();

	}
	
	private void initialize() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				FrmLogin frame;
				try {
					frame = new FrmLogin();
					frame.setVisible(true);
					dispose();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 623, 364);
		this.setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		
		queryData qd = new queryData();
		qd.getCompanySetup();
		
		setTitle("CWORMS Ver. 1.0.0 - " + Global.getBranchName());
		
		JMenuItem mntmStartBod = new JMenuItem("Start BOD");
		mntmStartBod.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmStartBod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					setProcessedFolder(Global.getUploadPath().toString());
					setProcessedFolderForReceivedByUCC(Global.getUploadPath().toString());
					File dir = new File(Global.getUploadPath().toString());
					File[] directoryListing = dir.listFiles();
					
					ReadExcel rx = new ReadExcel();  	
					boolean withIssuedChecks = false;
					boolean withReceivedByUCC = false;

					setCursor(new Cursor(Cursor.WAIT_CURSOR));
				
					//reading all the issued checks in the upload directory
				    for (File child : directoryListing) {
  	
				    	//validate the branch code of the excel file

				    	if (validateBranchCode(child.getName(), Global.getBranchCode())) {
	
							String excelFilePath = child.toString();

							if (child.getName().indexOf("IssuedChecksReport") > 0) {
								
								withIssuedChecks = true;
								
								int cols[] = {0,1,2,3,4,5,6,7,8,9,10};

								// reading excel file
								rx.readExcelFileforImportingIssuedChecks(excelFilePath, cols, 1);
								refreshTotalChecks();
						        Files.move(FileSystems.getDefault().getPath(child.toString()), FileSystems.getDefault().getPath(getProcessedFolder() + "\\" + child.getName()), StandardCopyOption.REPLACE_EXISTING);

						        
							} else if (child.getName().indexOf("ReceivedByUCC") > 0) {
								
								withReceivedByUCC = true;
								
								int cols[] = {0,1,9};
								
								// reading excel file
								rx.readExcelFileforImportingReceivedByUCC(excelFilePath, cols, 1);
								refreshTotalChecks();
						        Files.move(FileSystems.getDefault().getPath(child.toString()), FileSystems.getDefault().getPath(getProcessedFolderForReceivedByUCC() + "\\" + child.getName()), StandardCopyOption.REPLACE_EXISTING);
		
							}

				    	}
				    	
				    }

				    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				    
				    if (withIssuedChecks == false) JOptionPane.showMessageDialog(null, "There's no 'Issued Checks Report' for " + Global.getBranchName().toString(),"No Issued Checks Report",JOptionPane.WARNING_MESSAGE);
				    if (withReceivedByUCC == false) JOptionPane.showMessageDialog(null, "There's no 'Received by UCC Report' for " + Global.getBranchName().toString(),"No Received from UCC Report",JOptionPane.WARNING_MESSAGE);
				    
				    
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.toString());
				}
			}
		});
		mnFile.add(mntmStartBod);
		
		JMenu mnGenerate = new JMenu("Generate");
		mnFile.add(mnGenerate);
		
		JMenuItem mntmTransmittalReport = new JMenuItem("Branch Transmittal Sheet To UCC");
		mntmTransmittalReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
				
				frmGenerateTransmittal frame = new frmGenerateTransmittal();
				
				frame.setVisible(true);
				
			}
		});
		mntmTransmittalReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mnGenerate.add(mntmTransmittalReport);
		
		
		mnFile.addSeparator();
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Logout");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
				FrmLogin frame;
				try {
					frame = new FrmLogin();
					frame.setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		mntmNewMenuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mnFile.add(mntmNewMenuItem_1);
		
		JMenuBar menuBar_1 = new JMenuBar();
		mnFile.add(menuBar_1);
		
		JMenu mnView = new JMenu("View");
		mnView.setMnemonic('V');
		menuBar.add(mnView);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Checks for Claiming");
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose();
				
				frmChecksforClaiming frame = new frmChecksforClaiming();
				
				frame.setVisible(true);
				
				
			}
		});
		
		JMenuItem mntmInquireCheckStatus = new JMenuItem("Check Status");
		mntmInquireCheckStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
				frmViewCheckStatus frame = new frmViewCheckStatus();
				frame.setVisible(true);
				
			}
		});
		mntmInquireCheckStatus.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mnView.add(mntmInquireCheckStatus);
		
		mnView.addSeparator();
		mnView.add(mntmNewMenuItem);
		
		
		
		JMenuItem mntmClaimedChecksFor = new JMenuItem("Claimed Checks to Dispatch");
		mntmClaimedChecksFor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				dispose();
				
				frmClaimedChecksforDispatching frame;
				try {
					frame = new frmClaimedChecksforDispatching();
					frame.setVisible(true);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		});
		mntmClaimedChecksFor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		mnView.add(mntmClaimedChecksFor);
		
		JMenuBar menuBar_2 = new JMenuBar();
		mnView.add(menuBar_2);
		
		JMenu mnUtilities = new JMenu("Utilities");
		menuBar.add(mnUtilities);
		
		mntmUserMaintenance = new JMenuItem("User Maintenance");
		mntmUserMaintenance.setEnabled(false);
		mntmUserMaintenance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
				frmUserMaintenance frame = new frmUserMaintenance();
				frame.setVisible(true);
				
			}
		});
		mnUtilities.add(mntmUserMaintenance);
		
		mntmSystemSetup = new JMenuItem("System Setup");
		mntmSystemSetup.setEnabled(false);
		mntmSystemSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					frmSystemSetup frame1 = new frmSystemSetup();
					
					frame1.setVisible(true);
					
					
					dispose();
				
			}
		});
		mnUtilities.add(mntmSystemSetup);
		contentPane = new JPanel();
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {

				
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBounds(6, 291, 610, 16);
		statusPanel.setBorder((Border) new BevelBorder(BevelBorder.LOWERED));
		getContentPane().add(statusPanel);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel.setText("Welcome to CWORMS: " + Global.getUserLoggedIn());
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		
		JPanel panel = new JPanel();
		panel.setForeground(new Color(0, 0, 255));
		panel.setBorder(new TitledBorder(null, Global.getBranchName() + " Dashboard", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		panel.setBounds(6, 6, 312, 259);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Total Outstanding Checks:");
		lblNewLabel.setFont(new Font("Cambria", Font.BOLD, 12));
		lblNewLabel.setForeground(new Color(0, 0, 205));
		lblNewLabel.setBounds(17, 25, 207, 16);
		panel.add(lblNewLabel);
		
		JLabel lblTotalClaimedChecks = new JLabel("Total Claimed Checks:");
		lblTotalClaimedChecks.setForeground(new Color(0, 0, 205));
		lblTotalClaimedChecks.setFont(new Font("Cambria", Font.BOLD, 12));
		lblTotalClaimedChecks.setBounds(17, 53, 207, 16);
		panel.add(lblTotalClaimedChecks);
		
		JLabel lblTotalChecksDispatched = new JLabel("Total ORs Dispatched To UCC:");
		lblTotalChecksDispatched.setForeground(new Color(0, 0, 205));
		lblTotalChecksDispatched.setFont(new Font("Cambria", Font.BOLD, 12));
		lblTotalChecksDispatched.setBounds(17, 81, 207, 16);
		panel.add(lblTotalChecksDispatched);
		
		JLabel lblTotalChecksReceived = new JLabel("Total ORs Received By UCC:");
		lblTotalChecksReceived.setForeground(new Color(0, 0, 205));
		lblTotalChecksReceived.setFont(new Font("Cambria", Font.BOLD, 12));
		lblTotalChecksReceived.setBounds(17, 109, 207, 16);
		panel.add(lblTotalChecksReceived);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(17, 133, 269, 2);
		panel.add(separator);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Date Range:", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		panel_1.setBounds(17, 147, 282, 99);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel label = new JLabel("From:");
		label.setForeground(Color.RED);
		label.setFont(new Font("Tahoma", Font.BOLD, 12));
		label.setBounds(6, 24, 51, 16);
		panel_1.add(label);
		
		
		
		
		dateFrom = new JDateChooser();
		dateFrom.setRequestFocusEnabled(false);
		dateFrom.setFocusable(false);
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
			            	
			            	Calendar today = Calendar.getInstance();
			         
			            	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			        		String todayDate = sdf.format(today.getTime());
			        		String objDate = sdf.format(e.getNewValue());
			            	
			        		if (!todayDate.equals(objDate)) {
			        			
			        			refreshTotalChecks();
			        			
			        		} else {
			        			
			        			if (dateTo.getDate() != null) {
			        				
			        				if (!todayDate.equals(objDate)) {
			        				
			        					refreshTotalChecksForCurrentDate(todayDate, sdf.format(dateTo.getDate()));
			        				}
			        			}
			        		}
			        		
			         
			            }
			        }
			    });

		dateFrom.setBounds(51, 18, 224, 28);
		panel_1.add(dateFrom);
		
		JLabel label_1 = new JLabel("To:");
		label_1.setForeground(Color.RED);
		label_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_1.setBounds(6, 60, 51, 16);
		panel_1.add(label_1);
		
		dateTo = new JDateChooser();
		dateTo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
		});
		dateTo.setRequestFocusEnabled(false);
		dateTo.setFocusable(false);
		dateTo.getDateEditor().addPropertyChangeListener(
			    new PropertyChangeListener() {
			        @Override
			        public void propertyChange(PropertyChangeEvent e) {
			            if ("date".equals(e.getPropertyName())) {
			            	
			            	
			             	Calendar today = Calendar.getInstance();
					         
			            	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			        		String todayDate = sdf.format(today.getTime());
			        		String objDate = sdf.format(e.getNewValue());
			            	
			        		if (!todayDate.equals(objDate)) {
			        			
			        			refreshTotalChecks();
			        			
			        		} else {
			        			
			        			if (!todayDate.equals(objDate)) {
			        				refreshTotalChecksForCurrentDate(todayDate, sdf.format(dateTo.getDate()));
			        			}
		        			}

			            }
			        }
			    });
		
		dateTo.setBounds(51, 54, 224, 28);
		panel_1.add(dateTo);
		
		lblOutstandingChecks = new JLabel("123,456");
		lblOutstandingChecks.addMouseListener(new MouseAdapter() {
			
			Font original;
			
		    @SuppressWarnings("unchecked")
			@Override
		    public void mouseEntered(MouseEvent e) {
		        original = e.getComponent().getFont();
		        @SuppressWarnings("rawtypes")
				Map attributes = original.getAttributes();
		        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		        e.getComponent().setFont(original.deriveFont(attributes));
		        setCursor(new Cursor(Cursor.HAND_CURSOR));
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
		        e.getComponent().setFont(original);
		    }

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String formattedDateFrom = sdf.format(dateFrom.getDate());
				String formattedDateTo = sdf.format(dateTo.getDate());
				
				dispose();
				frmViewCheckStatus frame = new frmViewCheckStatus("For Release",formattedDateFrom,formattedDateTo,"ForReleasingStamp");
				frame.setVisible(true);
				
				
			}
			
		});

		lblOutstandingChecks.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOutstandingChecks.setForeground(new Color(128, 0, 0));
		lblOutstandingChecks.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOutstandingChecks.setBounds(217, 25, 69, 16);
		panel.add(lblOutstandingChecks);
		
		lblClaimedChecks = new JLabel("123,456");
		lblClaimedChecks.addMouseListener(new MouseAdapter() {
			
			Font original;
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
		        original = e.getComponent().getFont();
		        @SuppressWarnings("rawtypes")
				Map attributes = original.getAttributes();
		        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		        e.getComponent().setFont(original.deriveFont(attributes));
		        setCursor(new Cursor(Cursor.HAND_CURSOR));
				
			}
			
		    @Override
		    public void mouseExited(MouseEvent e) {
		        e.getComponent().setFont(original);
		    }
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String formattedDateFrom = sdf.format(dateFrom.getDate());
				String formattedDateTo = sdf.format(dateTo.getDate());
				
				dispose();
				
				frmViewCheckStatus frame = new frmViewCheckStatus("Claimed",formattedDateFrom,formattedDateTo,"ClaimedStamp");
				frame.setVisible(true);
				
			}
			
		});
		lblClaimedChecks.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClaimedChecks.setForeground(new Color(128, 0, 0));
		lblClaimedChecks.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClaimedChecks.setBounds(217, 53, 69, 16);
		panel.add(lblClaimedChecks);
		
		lblDispatchedToUCC = new JLabel("123,456");
		lblDispatchedToUCC.addMouseListener(new MouseAdapter() {
			Font original;
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
		        original = e.getComponent().getFont();
		        @SuppressWarnings("rawtypes")
				Map attributes = original.getAttributes();
		        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		        e.getComponent().setFont(original.deriveFont(attributes));
		        setCursor(new Cursor(Cursor.HAND_CURSOR));
				
			}
			
		    @Override
		    public void mouseExited(MouseEvent e) {
		        e.getComponent().setFont(original);
		    }
		    
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String formattedDateFrom = sdf.format(dateFrom.getDate());
				String formattedDateTo = sdf.format(dateTo.getDate());
				
				dispose();
				

				frmViewCheckStatus frame = new frmViewCheckStatus("Dispatched To UCC",formattedDateFrom,formattedDateTo,"DispatchedToUCCStamp");
				frame.setVisible(true);
				
			}
			
		});
		lblDispatchedToUCC.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDispatchedToUCC.setForeground(new Color(128, 0, 0));
		lblDispatchedToUCC.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDispatchedToUCC.setBounds(217, 81, 69, 16);
		panel.add(lblDispatchedToUCC);
		
		lblReceivedByUCC = new JLabel("123,456");
		lblReceivedByUCC.addMouseListener(new MouseAdapter() {
			Font original;
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
		        original = e.getComponent().getFont();
		        @SuppressWarnings("rawtypes")
				Map attributes = original.getAttributes();
		        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		        e.getComponent().setFont(original.deriveFont(attributes));
		        setCursor(new Cursor(Cursor.HAND_CURSOR));
				
			}
			
		    @Override
		    public void mouseExited(MouseEvent e) {
		        e.getComponent().setFont(original);
		    }
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String formattedDateFrom = sdf.format(dateFrom.getDate());
				String formattedDateTo = sdf.format(dateTo.getDate());
				
				dispose();
				
				frmViewCheckStatus frame = new frmViewCheckStatus("Received By UCC",formattedDateFrom,formattedDateTo,"ReceivedByUCCStamp");
				frame.setVisible(true);
				
			}
		});
		lblReceivedByUCC.setHorizontalAlignment(SwingConstants.RIGHT);
		lblReceivedByUCC.setForeground(new Color(128, 0, 0));
		lblReceivedByUCC.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblReceivedByUCC.setBounds(217, 109, 69, 16);
		panel.add(lblReceivedByUCC);
		
		dateFrom.setDate(this.DateNow("MMM dd, yyyy"));
		dateTo.setDate(this.DateNow("MMM dd, yyyy"));
		

	
		
		refreshTotalChecksForCurrentDate(String.valueOf(dateFrom.getDate()),String.valueOf(dateTo.getDate()));
			

			
		
		
	}
	
	
	private String generateQuery(String status, String fromDate, String toDate) {
		
		//String query = "select * from "
		
		return "";
	}
	
	public void refreshTotalChecks() {

		queryData qd = new queryData();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDateFrom = sdf.format(dateFrom.getDate());
		String formattedDateTo = sdf.format(dateTo.getDate());
		
		if (!formattedDateFrom.isEmpty()){
			lblOutstandingChecks.setText(qd.getTotalChecks("For Release", "ForReleasingStamp",formattedDateFrom, formattedDateTo));
			lblClaimedChecks.setText(qd.getTotalChecks("Claimed", "ClaimedStamp",formattedDateFrom, formattedDateTo));
			lblDispatchedToUCC.setText(qd.getTotalChecks("Dispatched To UCC", "DispatchedToUCCStamp",formattedDateFrom, formattedDateTo));
			lblReceivedByUCC.setText(qd.getTotalChecks("Received By UCC", "ReceivedByUCCStamp",formattedDateFrom, formattedDateTo));
		}
		
	}
	
	public void refreshTotalChecksForCurrentDate(String fromDate, String toDate) {

		queryData qd = new queryData();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDateFrom = sdf.format(dateFrom.getDate());
		
		
		if (!fromDate.isEmpty()){
			
			lblClaimedChecks.setText(qd.getTotalChecks("Claimed", "ClaimedStamp","01/01/1900", toDate));
			lblDispatchedToUCC.setText(qd.getTotalChecks("Dispatched To UCC", "DispatchedToUCCStamp","01/01/1900", toDate));
			lblReceivedByUCC.setText(qd.getTotalChecks("Received By UCC", "ReceivedByUCCStamp",formattedDateFrom, toDate));
			lblOutstandingChecks.setText(qd.getTotalChecks("For Release", "ForReleasingStamp","01/01/1900", toDate));
			
		}
		
	}
	

	
	private boolean validateBranchCode(String excelBranchCode, String setupBranchCode) {
		
    	int underscorePos = 0;
    	underscorePos = excelBranchCode.indexOf("_");
		
    	
    	
    	if (excelBranchCode.equals("Processed Folder")) return false;
    	
    	if (excelBranchCode.substring(0, underscorePos).equals(setupBranchCode)) {
    		return true;
    	}

		return false;
		
	}
	
	private void setProcessedFolder(String defaultFolder) {

        String generateFolder = defaultFolder + "Processed Folder\\Issued Checks from UCC\\" + getCurrentDate() + "\\";

        new File(generateFolder).mkdirs();
        
        this.strProcessedFolder = generateFolder;

	}
	
	private void setProcessedFolderForReceivedByUCC(String defaultFolder) {

        String generateFolder = defaultFolder + "Processed Folder\\Received By UCC\\" + getCurrentDate() + "\\";

        new File(generateFolder).mkdirs();
        
        this.strReceivedByUCCFolder = generateFolder;
  
	}
	
	private String getCurrentDate() {
		
		 DateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy");
		   //get current date time with Date()
		 Date date = new Date();
		 
		 return dateFormat.format(date).toString();
		   
	}
	
	
	private String getProcessedFolder() {
		
		return strProcessedFolder;
		
	}
	
	private String getProcessedFolderForReceivedByUCC() {
		
		return strReceivedByUCCFolder;
		
	}
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	 private void setTempFolder(String defaultFolder) {

	        String generateFolder = defaultFolder + "Temp\\";

	        new File(generateFolder).mkdirs();
	        
	        this.strProcessedFolder = generateFolder;

	        
	}
	 
}
