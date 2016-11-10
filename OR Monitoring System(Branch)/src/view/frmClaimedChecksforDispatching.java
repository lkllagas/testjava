package view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import javax.swing.JButton;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import controller.queryData;
import controller.sqliteConnection;
import model.Global;
import net.proteanit.sql.DbUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.UIManager;

public class frmClaimedChecksforDispatching extends JFrame {
	
	Connection conn = sqliteConnection.dbConnector();
	private JPanel contentPane;
	private JTable table;
	private JLabel lblNumofRecords;
	private String ans = "";
	private JButton btnVerify;
	private JButton btnPrintDraftTransmittal;
	private JButton btnAssignBarcode;
	private JTextField txtClaimedBy;
	private JTextField txtIDPresented;
	private JTextField txtClaimedDate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmClaimedChecksforDispatching frame = new frmClaimedChecksforDispatching();
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
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings("serial")
	
	public frmClaimedChecksforDispatching() throws NumberFormatException, SQLException {
		setTitle("Claimed Checks to Dispatch");
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				
				frmMainMenu frame = new frmMainMenu();
				frame.setVisible(true);
				dispose();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//setBounds(100, 100, 641, 432);
		setBounds(100, 100, 883, 509);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
      
        scrollPane.setBounds(10, 19, 851, 294);
        contentPane.add(scrollPane);
        table = new JTable() {
        	
        		
            @SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
            public final Class getColumnClass(int column) {
            	return getValueAt(0, column).getClass();
            }
	        
            public boolean isCellEditable(int row, int column) {
            	
            	if (column == 0 )return true;
            	
                return false;
                
            };
            
            
        
        };
        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		
        		try {
        			table =(JTable) e.getSource();
    		        Point p = e.getPoint();
    		        int row = table.rowAtPoint(p);
    				if (e.getClickCount() == 2) {
    					
    					dispose();
    					String checkNum = (table.getModel().getValueAt(row, 1)).toString();
    					String payee = (table.getModel().getValueAt(row, 2)).toString();
    					String checkDate = (table.getModel().getValueAt(row, 4)).toString();
    					String checkAmount = (table.getModel().getValueAt(row, 3)).toString();
    					String ORNum = (table.getModel().getValueAt(row, 5)).toString();
    					
    				     //Converting String to Date
    				      DateFormat formatter; 
    				      Date date; 
    				      formatter = new SimpleDateFormat("MM/dd/yyyy");
    				      date = (Date)formatter.parse(txtClaimedDate.getText());  
    					
    		            frmReviseClaimTagging frame;
    					frame = new frmReviseClaimTagging(checkNum, payee, checkDate, checkAmount, ORNum,txtClaimedBy.getText(),txtIDPresented.getText(),date);
    					frame.setVisible(true);

    		        }
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
	
        		
        	}
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		

				try {
					
					int row = table.getSelectedRow();

					String checkNum = (table.getModel().getValueAt(row, 1)).toString();
					String payeeName = (table.getModel().getValueAt(row, 2)).toString();
					String checkAmount = (table.getModel().getValueAt(row, 3)).toString();
					
					String query = "Select  ClaimedBy, ClaimedID, ClaimedDate from tblCheck where CheckNumber='"+ checkNum +"' and PayeeName = '" + payeeName + "' and CheckAmount = '" + checkAmount + "'";
					PreparedStatement pst = conn.prepareStatement(query);

					ResultSet rs = pst.executeQuery();
					
					while (rs.next()) {
						

						txtClaimedDate.setText(rs.getString("ClaimedDate"));
						txtClaimedBy.setText(rs.getString("ClaimedBy"));
						txtIDPresented.setText(rs.getString("ClaimedID"));
						
					}

					pst.close();
					rs.close();

				} catch (Exception ex) {
					
					ex.printStackTrace();
					
				}
        		
        		
        	}
        });

	        scrollPane.setViewportView(table);
	
       table.setPreferredScrollableViewportSize(table.getPreferredSize());
       
       JButton btnSelectAll = new JButton("Select All");
       btnSelectAll.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
       		
       		for(int i=0;i<table.getRowCount();i++)
                table.getModel().setValueAt(true, i, 0);
       		
       	}
       });
       btnSelectAll.setBounds(20, 409, 113, 23);
       contentPane.add(btnSelectAll);
       
       JButton btnUnselectAll = new JButton("Deselect All");
       btnUnselectAll.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
       		
       		for(int i=0;i<table.getRowCount();i++)
                table.getModel().setValueAt(false, i, 0);
       		
       	}
       });
       btnUnselectAll.setBounds(139, 409, 113, 23);
       contentPane.add(btnUnselectAll);
       
       btnAssignBarcode = new JButton("Assign Barcode and Generate Transmittal Report");
       btnAssignBarcode.setEnabled(false);
       btnAssignBarcode.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
       		
		       		queryData qd = new queryData();
		       		ArrayList<String> checkNumber = new ArrayList<String>();
		       		ArrayList<String> payeeName = new ArrayList<String>();
		       		ArrayList<String> payorName = new ArrayList<String>();
		       		ArrayList<String> checkAmount = new ArrayList<String>();
		       		ArrayList<String> CheckDate = new ArrayList<String>();
		       		ArrayList<String> ORNumber = new ArrayList<String>();

		       		if (table.getRowCount() == 0) {
		       			
		       			JOptionPane.showMessageDialog(null, "No records to dispatch..");
		       			return;
		       			
		       		}

		       		while (ans.isEmpty()) {
		
		       			ans = JOptionPane.showInputDialog(null, "Please input the barcode for dispatch to UCC","Assign Bar Code",JOptionPane.INFORMATION_MESSAGE);
		       			
		       			if (ans == null) {
		       				ans = "";
		       				return;
		       			}
		
		       		}
		       		
		       		if (validateBarCode(ans)) {
		       			JOptionPane.showMessageDialog(null, "Bar code: " + ans + " is existing in the database..");
		       			ans = "";
		       			return;
		       		}

		    	    String message = "Please confirm the Bar Code and click 'Yes' to proceed.\nBar code: " + ans.toUpperCase();
				    String title = "Assign Bar Code for UCC";
				    // display the JOptionPane showConfirmDialog
				    int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
				    if (reply == JOptionPane.YES_OPTION)
				    {
			       		for(int i=0;i<table.getRowCount();i++) {
			       			
			       			if (table.getModel().getValueAt(i, 0).toString() == "true") {
			       				
			       				checkNumber.add(table.getModel().getValueAt(i, 1).toString());
			       				payeeName.add(table.getModel().getValueAt(i,2).toString());
			       				payorName.add(table.getModel().getValueAt(i, 6).toString());
			       				checkAmount.add(table.getModel().getValueAt(i,3).toString());
			       				CheckDate.add(table.getModel().getValueAt(i, 4).toString());
			       				ORNumber.add(table.getModel().getValueAt(i,5).toString());
			       				
			       			}
			 
			       		}
			       		
			       		if (checkNumber.size() > 0) {
			       			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			       			qd.updateStatusToDispatchedToUCC(checkNumber, payeeName,ans);
			       			qd.generateDispatchToUCC(checkNumber, payeeName, ans);
			       			GenerateTransmittalReport(checkNumber, payeeName, payorName, checkAmount, CheckDate,ORNumber);
			       			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			       			refreshTable();
			       			JOptionPane.showMessageDialog(null, "Done!");
			       		}
			   			else JOptionPane.showMessageDialog(null, "Please select the check numbers to dispatch..","Dispatch to UCC",JOptionPane.WARNING_MESSAGE);
	       		
				    } else {
				    	ans = "";
				    }
		       		
       		
       	}
       });
       btnAssignBarcode.setBounds(264, 409, 338, 23);
       contentPane.add(btnAssignBarcode);
       
       lblNumofRecords = new JLabel("New label");
       lblNumofRecords.setForeground(Color.RED);
       lblNumofRecords.setHorizontalAlignment(SwingConstants.RIGHT);
       lblNumofRecords.setBounds(716, 447, 138, 16);
       contentPane.add(lblNumofRecords);
       
       btnPrintDraftTransmittal = new JButton("Print Draft Transmittal Sheet");
       btnPrintDraftTransmittal.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
       		
       		ArrayList<String> checkNumber = new ArrayList<String>();
       		ArrayList<String> payeeName = new ArrayList<String>();
       		ArrayList<String> payorName = new ArrayList<String>();
       		ArrayList<String> checkAmount = new ArrayList<String>();
       		ArrayList<String> CheckDate = new ArrayList<String>();
       		ArrayList<String> ORNumber = new ArrayList<String>();
       		
       		if (table.getRowCount() == 0) {
       			
       			JOptionPane.showMessageDialog(null, "No records to generate..");
       			return;
       			
       		}
       		
       		for(int i=0;i<table.getRowCount();i++) {
       			
       			if (table.getModel().getValueAt(i, 0).toString() == "true") {
       				
       				checkNumber.add(table.getModel().getValueAt(i, 1).toString());
       				payeeName.add(table.getModel().getValueAt(i,2).toString());
       				payorName.add(table.getModel().getValueAt(i, 6).toString());
       				checkAmount.add(table.getModel().getValueAt(i,3).toString());
       				CheckDate.add(table.getModel().getValueAt(i, 4).toString());
       				ORNumber.add(table.getModel().getValueAt(i,5).toString());
       				
       			}
 
       		}
       		
       		
       		if (checkNumber.size() > 0)
       		{
	       		GenerateDraftTransmittal(checkNumber, payeeName, payorName, checkAmount, CheckDate,ORNumber);
	       		btnVerify.setEnabled(true);
	       		btnPrintDraftTransmittal.setEnabled(false);
       		} else {
       			JOptionPane.showMessageDialog(null, "Please select the check numbers for generation of draft transmittal..");
       		}
       		
       		
       		
       	}
       });
       btnPrintDraftTransmittal.setBounds(20, 444, 232, 23);
       contentPane.add(btnPrintDraftTransmittal);
       
       btnVerify = new JButton("Verify\r\n");
       btnVerify.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
       		
       		queryData qd = new queryData();
			
       		ArrayList<String> checkNumber = new ArrayList<String>();
       		ArrayList<String> payeeName = new ArrayList<String>();
       		ArrayList<String> checkAmount = new ArrayList<String>();
       		
       		for(int i=0;i<table.getRowCount();i++) {
       			
       			if (table.getModel().getValueAt(i, 0).toString() == "true") {
       				
       				checkNumber.add(table.getModel().getValueAt(i, 1).toString());
       				payeeName.add(table.getModel().getValueAt(i,2).toString());
       				checkAmount.add(table.getModel().getValueAt(i,3).toString());
       				
       			}
 
       		}
       		
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
   				qd.generateClaimedChecksforUCC(checkNumber, payeeName, checkAmount);
   				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
   				
   				btnVerify.setEnabled(false);
   				btnAssignBarcode.setEnabled(true);
       		

       	}
       });
       btnVerify.setEnabled(false);
       btnVerify.setBounds(264, 444, 99, 23);
       contentPane.add(btnVerify);
       
       JPanel panel = new JPanel();
       panel.setLayout(null);
       panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Claiming Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
       panel.setBounds(10, 325, 851, 72);
       contentPane.add(panel);
       
       JLabel label_1 = new JLabel("Claimed By:");
       label_1.setBounds(244, 29, 91, 16);
       panel.add(label_1);
       
       txtClaimedBy = new JTextField();
       txtClaimedBy.setEditable(false);
       txtClaimedBy.setColumns(10);
       txtClaimedBy.setBounds(332, 23, 272, 28);
       panel.add(txtClaimedBy);
       
       JLabel label_2 = new JLabel("ID Presented:");
       label_2.setBounds(616, 29, 91, 16);
       panel.add(label_2);
       
       txtIDPresented = new JTextField();
       txtIDPresented.setEditable(false);
       txtIDPresented.setColumns(10);
       txtIDPresented.setBounds(698, 23, 136, 28);
       panel.add(txtIDPresented);
       
       txtClaimedDate = new JTextField();
       txtClaimedDate.setEditable(false);
       txtClaimedDate.setColumns(10);
       txtClaimedDate.setBounds(110, 23, 122, 28);
       panel.add(txtClaimedDate);
       
       JLabel label_3 = new JLabel("Claimed Date:");
       label_3.setBounds(21, 29, 91, 16);
       panel.add(label_3);
       
       loadRecords();
       
       
       
	}
	
   	private static String now(String dateFormat) {
		   
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    return sdf.format(cal.getTime());

	  }
   	
	private void GenerateTransmittalReport(ArrayList<String> checkNum, ArrayList<String> payeeN, ArrayList<String> payorN, ArrayList<String> checkAmt, ArrayList<String> checkD, ArrayList<String> ORNum) {
		
		
		try {
				
			String fileName = Global.getBranchName() + "Transmittal_" + now("ddMMYYYYhhmmss") + ".pdf";
			
			OutputStream file = new FileOutputStream(new File(".").getCanonicalPath() + "\\Temp\\" + fileName);

				DateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy");
				Date date = new Date();
				
				Font font1 = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
				Font font2 = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
				Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
				Font font4 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
				
				Document document = new Document();
				PdfWriter.getInstance(document, file);
				document.open();
		      
			    PdfPTable table = new PdfPTable(3);
			    PdfPCell defaultCell = table.getDefaultCell();
			    defaultCell.setBorder(PdfPCell.NO_BORDER);
		      
			    table.setWidthPercentage(250);
		      
		      //TITLE
		      table.addCell("");
		      table.addCell(new Phrase("         BRANCH TRANSMITTAL SHEET – Dispatched to UCC", font1));
		      table.addCell("");
		     
		      document.add(table);
		      addEmptyLine(document, 1);

		      document.add(new Paragraph("REPORT DATE:   " + dateFormat.format(date).toString(),font2));
		      document.add(new Paragraph("BAR CODE:          " + ans,font2));
		      
		      addEmptyLine(document, 1);
		      
		      //TABLE HEADER
	      	PdfPTable table2 = new PdfPTable(6);
	      	PdfPCell cell1 = new PdfPCell(new Phrase("CHECK NUMBER", font3));
	      	cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell2 = new PdfPCell(new Phrase("PAYOR'S NAME", font3));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell3 = new PdfPCell(new Phrase("PAYEE NAME", font3));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	      	PdfPCell cell4 = new PdfPCell(new Phrase("CHECK AMOUNT (PHP)", font3));
	      	cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell5 = new PdfPCell(new Phrase("CHECK DATE", font3));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell6 = new PdfPCell(new Phrase("OR NUMBER", font3));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		      
		      table2.setWidthPercentage(100);
		      table2.addCell(cell1);
		      table2.addCell(cell2);
		      table2.addCell(cell3);
		      table2.addCell(cell4);
		      table2.addCell(cell5);
		      table2.addCell(cell6);
		      
		      addEmptyLine(document, 1);
		      
		      for (int i = 0; i < checkNum.size();i++) {
		    	  
		    	  table2.addCell(new Phrase(checkNum.get(i), font4));
		    	  table2.addCell(new Phrase(payorN.get(i), font4));
		    	  table2.addCell(new Phrase(payeeN.get(i), font4));
		    	  table2.addCell(new Phrase(checkAmt.get(i), font4));
		    	  table2.addCell(new Phrase(checkD.get(i), font4));
		    	  table2.addCell(new Phrase(ORNum.get(i), font4));
		     
		      }
		      
		      document.add(table2);
		      
		      addEmptyLine(document, 1);
		      addEmptyLine(document, 1);
		   
	      	PdfPTable table3 = new PdfPTable(3);
		    PdfPCell defaultCell2 = table3.getDefaultCell();
		    defaultCell2.setBorder(PdfPCell.NO_BORDER);
		    table3.setWidthPercentage(100);
			    
		      table3.addCell(new Phrase("Report By: ", font3));
		      table3.addCell(new Phrase("Reviewed By: ", font3));
		      table3.addCell(new Phrase("Received By:", font3));
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(new Phrase("_________________________", font3));
		      table3.addCell(new Phrase("_________________________", font3));
		      table3.addCell(new Phrase("_________________________", font3));
		      table3.addCell(new Phrase(Global.getUserName(), font3));
		      table3.addCell(new Phrase("BRM", font3));
		      table3.addCell(new Phrase("Messenger", font3));
		      table3.addCell(new Phrase(Global.getBranchName() + " Branch", font3));
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(new Phrase("Total ORs: " + checkNum.size(), font3));
		      document.add(table3);
		      
		      document.close();
		      file.close();
		      
		      
		      String filename2 = Global.getBranchName() + "Transmittal_WithPageNum_" + now("ddMMYYYYhhmmss") + ".pdf";
		      
		      manipulatePdf(new File(".").getCanonicalPath() + "\\Temp\\" + fileName,new File(".").getCanonicalPath() + "\\Temp\\" + filename2);
 
		      if (Desktop.isDesktopSupported()) {
		    	    try {
		    	    	File myFile = new File(new File(".").getCanonicalPath() + "\\Temp\\" + filename2);
		    	        Desktop.getDesktop().open(myFile);
		    	    } catch (IOException ex) {
		    	    	JOptionPane.showMessageDialog(null, ex.toString());
		    	    }
		    	}
		      
			} catch (Exception e) {
				
				JOptionPane.showMessageDialog(null, e.toString());
				
			}
		
	}
	
	
private void GenerateDraftTransmittal(ArrayList<String> checkNum, ArrayList<String> payeeN, ArrayList<String> payorN, ArrayList<String> checkAmt, ArrayList<String> checkD, ArrayList<String> ORNum) {
		
		
		try {
				
			String fileName = Global.getBranchName() + "DraftTransmittal_" + now("ddMMYYYYhhmmss") + ".pdf";
			
			OutputStream file = new FileOutputStream(new File(".").getCanonicalPath() + "\\Temp\\" + fileName);

				DateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy");
				Date date = new Date();
				
				Font font1 = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
				Font font2 = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
				Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
				Font font4 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
				
				Document document = new Document();
				PdfWriter.getInstance(document, file);
				document.open();
		      
			    PdfPTable table = new PdfPTable(3);
			    PdfPCell defaultCell = table.getDefaultCell();
			    defaultCell.setBorder(PdfPCell.NO_BORDER);
		      
			    table.setWidthPercentage(250);
		      
		      //TITLE
		      table.addCell("");
		      table.addCell(new Phrase("         DRAFT TRANSMITTAL SHEET FOR CLAIMED CHECKS", font1));
		      table.addCell("");
		     
		      document.add(table);
		      addEmptyLine(document, 1);

		      document.add(new Paragraph("REPORT DATE:   " + dateFormat.format(date).toString(),font2));
		      //document.add(new Paragraph("BAR CODE:          " + ans,font2));
		      
		      addEmptyLine(document, 1);
		      
		      //TABLE HEADER
	      	PdfPTable table2 = new PdfPTable(6);
	      	PdfPCell cell1 = new PdfPCell(new Phrase("CHECK NUMBER", font3));
	      	cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell2 = new PdfPCell(new Phrase("PAYOR'S NAME", font3));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell3 = new PdfPCell(new Phrase("PAYEE NAME", font3));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	      	PdfPCell cell4 = new PdfPCell(new Phrase("CHECK AMOUNT (PHP)", font3));
	      	cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell5 = new PdfPCell(new Phrase("CHECK DATE", font3));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell6 = new PdfPCell(new Phrase("OR NUMBER", font3));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		      
		      table2.setWidthPercentage(100);
		      table2.addCell(cell1);
		      table2.addCell(cell2);
		      table2.addCell(cell3);
		      table2.addCell(cell4);
		      table2.addCell(cell5);
		      table2.addCell(cell6);
		      
		      addEmptyLine(document, 1);
		      
		      for (int i = 0; i < checkNum.size();i++) {
		    	  
		    	  table2.addCell(new Phrase(checkNum.get(i), font4));
		    	  table2.addCell(new Phrase(payorN.get(i), font4));
		    	  table2.addCell(new Phrase(payeeN.get(i), font4));
		    	  table2.addCell(new Phrase(checkAmt.get(i), font4));
		    	  table2.addCell(new Phrase(checkD.get(i), font4));
		    	  table2.addCell(new Phrase(ORNum.get(i), font4));
		     
		      }
		      
		      document.add(table2);
		      
		      addEmptyLine(document, 1);
		      addEmptyLine(document, 1);
		   
	      	PdfPTable table3 = new PdfPTable(3);
		    PdfPCell defaultCell2 = table3.getDefaultCell();
		    defaultCell2.setBorder(PdfPCell.NO_BORDER);
		    table3.setWidthPercentage(100);
			    
		      table3.addCell(new Phrase("Generated By: ", font3));
		      table3.addCell(" ");
		      table3.addCell(new Phrase("Reviewed By:", font3));
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(new Phrase("_________________________", font3));
		      table3.addCell(" ");
		      table3.addCell(new Phrase("_________________________", font3));
		      table3.addCell(new Phrase(Global.getUserName(), font3));
		      table3.addCell(" ");
		      table3.addCell(new Phrase("BRM", font3));
		      table3.addCell(new Phrase(Global.getBranchName() + " Branch", font3));
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(" ");
		      table3.addCell(new Phrase("Total ORs: " + checkNum.size(), font3));
		      document.add(table3);
		      
		      document.close();
		      file.close();
		      
		      
		      String filename2 = Global.getBranchName() + "DraftTransmittal_WithPageNum_" + now("ddMMYYYYhhmmss") + ".pdf";
		      
		      manipulatePdf(new File(".").getCanonicalPath() + "\\Temp\\" + fileName,new File(".").getCanonicalPath() + "\\Temp\\" + filename2);
 
		      if (Desktop.isDesktopSupported()) {
		    	    try {
		    	    	File myFile = new File(new File(".").getCanonicalPath() + "\\Temp\\" + filename2);
		    	        Desktop.getDesktop().open(myFile);
		    	    } catch (IOException ex) {
		    	    	JOptionPane.showMessageDialog(null, ex.toString());
		    	    }
		    	}
		      
			} catch (Exception e) {
				
				JOptionPane.showMessageDialog(null, e.toString());
				
			}
		
	}
	
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        Font font1 = new Font(Font.FontFamily.HELVETICA,9, Font.NORMAL);
        int n = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte pagecontent;
        for (int i = 0; i < n; ) {
            pagecontent = stamper.getOverContent(++i);
            ColumnText.showTextAligned(pagecontent, Element.ALIGN_RIGHT,
                    new Phrase(String.format("page %s of %s", i, n), font1), 559, 830, 0);
        }
        stamper.close();
        reader.close();
    }
	
	  private static void addEmptyLine(Document document, int number) throws DocumentException {
		    for (int i = 0; i < number; i++) {
		    	document.add(new Paragraph(" "));
		    }
	  }
	  
	private void loadRecords() {
		
		try {
			
			String query = "Select CheckNumber, PayeeName, CheckAmount, CheckDate, ORNo, CorporationName from tblCheck where Status = 'Claimed' order by CheckNumber";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			table.setModel(this.resultSetToTableModelWithCheckBox(rs));
			tableProperty();

			rs.close();
			pst.close();
			
			lblNumofRecords.setText("Number of Records: " + getRecordCount().toString());
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.toString(),"Error Loading Records..",JOptionPane.WARNING_MESSAGE);
		}
		
		
	}

public Integer getRecordCount() throws NumberFormatException, SQLException{
	
	int numRec = 0;
	
	String query = "Select Count(*) as RecordCount from tblCheck where Status = 'Claimed'";
	PreparedStatement pst = conn.prepareStatement(query);
	ResultSet rs = pst.executeQuery();
	
	while (rs.next()) numRec = Integer.parseInt(rs.getString("RecordCount"));
	
	rs.close();
	pst.close();
	
//	  if (conn != null) {
//		  
//		    try {
//		      conn.close(); // <-- This is important
//		    } catch (SQLException e) {
//		      /* handle exception */
//		    }
//	  }
	  
	return numRec;
	


	
	
}

	
public void refreshTable() {
	
	try {
		
		String query = "Select CheckNumber, PayeeName, CheckAmount, CheckDate, ORNo, CorporationName from tblCheck where Status = 'Claimed'";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		table.setModel(this.resultSetToTableModelWithCheckBox(rs));
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Boolean Column");
		tableProperty();
		
		rs.close();
		pst.close();
		
		lblNumofRecords.setText("Number of Records: " + getRecordCount().toString());

	} catch (Exception ex) {
		
		//ex.printStackTrace();
		
	}

	
}
	
	public void tableProperty() {
		
		
		table.getColumnModel().getColumn(1).setHeaderValue("Check Number");
		table.getColumnModel().getColumn(1).setPreferredWidth(90);
		table.getColumnModel().getColumn(1).setMinWidth(20);
		table.getColumnModel().getColumn(2).setPreferredWidth(250);
		table.getColumnModel().getColumn(3).setHeaderValue("Payee's Name");
		table.getColumnModel().getColumn(3).setPreferredWidth(90);
		table.getColumnModel().getColumn(3).setHeaderValue("Check Amount");
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		table.getColumnModel().getColumn(4).setHeaderValue("Check Date");
		table.getColumnModel().getColumn(6).setPreferredWidth(150);
		table.getColumnModel().getColumn(6).setHeaderValue("Payor's Name");
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		
		
	}
	public TableModel resultSetToTableModelWithCheckBox(ResultSet rs) {
		try {
		    ResultSetMetaData metaData = rs.getMetaData();
		    int numberOfColumns = metaData.getColumnCount();
		    Vector<String> columnNames = new Vector<String>();
		    
		    columnNames.addElement("For Dispatch");
		    // Get the column names
		    for (int column = 0; column < numberOfColumns; column++) {
			columnNames.addElement(metaData.getColumnLabel(column + 1));
		    }
   
		    // Get all rows.
		    Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

		    while (rs.next()) {
			Vector<Object> newRow = new Vector<Object>();
			
			newRow.addElement(false);
			
			for (int i = 1; i <= numberOfColumns; i++) {
			    newRow.addElement(rs.getObject(i));
			}

				rows.addElement(newRow);
		    }

		    return new DefaultTableModel(rows, columnNames);
		} catch (Exception e) {
		    e.printStackTrace();

		    return null;
		}
	    }
	
	
	public boolean validateBarCode (String barCode) {
		
		try { 
			
			String query = "select UCCBarCode from tblCheck where UCCBarCode=?";

			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, barCode);
			ResultSet rst = pst.executeQuery();
			int count = 0;
			
			while (rst.next()) {
				count+=1;
			}
			
			if (count > 0) {
				pst.close();
				rst.close();
				return true;	
			} else {
				
				pst.close();
				rst.close();
				return false;
				
			}
			
		} catch (Exception ex) {
			
			JOptionPane.showMessageDialog(null, ex.toString());
			
		}
		
		return false;

	}
}
