package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.border.TitledBorder;

import controller.sqliteConnection;
import model.Global;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class frmGenerateTransmittal extends JFrame {

	Connection conn = sqliteConnection.dbConnector();
	private JPanel contentPane;
	private JTextField txtBarCode;
	private JLabel lblValid;
	private JButton btnGenerate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmGenerateTransmittal frame = new frmGenerateTransmittal();
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
	public frmGenerateTransmittal() {
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
		setBounds(100, 100, 354, 174);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Generate Transmittal Report", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 327, 124);
		contentPane.add(panel);
		panel.setLayout(null);
		
		txtBarCode = new JTextField();
		txtBarCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				if (validateBarCode(txtBarCode.getText().trim().toString())) {
					
					lblValid.setText(" ");
					btnGenerate.setEnabled(true);
					
				} else {
					
					lblValid.setText("Invalid Bar Code!");
					btnGenerate.setEnabled(false);
				}
				
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				 if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					 if (!lblValid.getText().equals("Invalid Bar Code!")) {
						 btnGenerate.requestFocus();
					 }
					 
				   }
				
			}
		});
		txtBarCode.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				
			
				
			}
		});

		txtBarCode.setBounds(168, 30, 131, 20);
		panel.add(txtBarCode);
		txtBarCode.setColumns(10);
		
		JLabel lblInputOrNumber = new JLabel("Input Bar Code for UCC:");
		lblInputOrNumber.setBounds(27, 33, 141, 14);
		panel.add(lblInputOrNumber);
		
		btnGenerate = new JButton("Generate");
		btnGenerate.setEnabled(false);
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
		       		ArrayList<String> checkNumber = new ArrayList<String>();
		       		ArrayList<String> payeeName = new ArrayList<String>();
		       		ArrayList<String> payorName = new ArrayList<String>();
		       		ArrayList<String> checkAmount = new ArrayList<String>();
		       		ArrayList<String> CheckDate = new ArrayList<String>();
		       		ArrayList<String> ORNumber = new ArrayList<String>();
					
					String query = "select CheckNumber, PayeeName, CorporationName, CheckAmount, CheckDate, ORNo from tblCheck where UCCBarCode = '" + txtBarCode.getText().trim() + "' and Status = 'Dispatched To UCC'";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					
					while (rs.next())
					{

	       				checkNumber.add(rs.getString("CheckNumber"));
	       				payeeName.add(rs.getString("PayeeName"));
	       				payorName.add(rs.getString("CorporationName"));
	       				checkAmount.add(rs.getString("CheckAmount"));
	       				CheckDate.add(rs.getString("CheckDate"));
	       				ORNumber.add(rs.getString("ORNo"));
						
					}
					
					GenerateTransmittalReport(checkNumber, payeeName, payorName, checkAmount, CheckDate,ORNumber);
					
					
					
				} catch (Exception e) {
					
					JOptionPane.showMessageDialog(null, e.toString());
					
				}

			}
		});
		btnGenerate.setBounds(116, 80, 89, 23);
		panel.add(btnGenerate);
		
		lblValid = new JLabel("");
		lblValid.setHorizontalAlignment(SwingConstants.CENTER);
		lblValid.setForeground(Color.RED);
		lblValid.setBounds(178, 55, 105, 14);
		panel.add(lblValid);
		this.setLocationRelativeTo(null);
		
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
		      document.add(new Paragraph("BAR CODE:          " + txtBarCode.getText().trim(),font2));
		      
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

private static void addEmptyLine(Document document, int number) throws DocumentException {
    for (int i = 0; i < number; i++) {
    	document.add(new Paragraph(" "));
    }
}
	
	private boolean validateBarCode(String code) {
		
	try { 
			
			String query = "select * from tblCheck where UCCBarCode=?";

			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, code);
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
		//	finally {
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
		
		return false;
		
		
	}
}
