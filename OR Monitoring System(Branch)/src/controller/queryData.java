package controller;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import model.Check;
import model.CreateExcelFile;
import model.Global;
import model.ImportClaim;
import model.ReceivedByUCC;


public class queryData {
	 
	private String queryExcel = "";
	 Connection conn = sqliteConnection.dbConnector();
	 
	 
		 private static String now(String dateFormat) {
		   
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		    return sdf.format(cal.getTime());

		  }
	    
		   	
		   public String getTotalChecks(String status, String typeofDate, String fromDate, String toDate) {
			   
			   try {
				   	
			   	String query = "select Count(CheckNumber) as TotalOutstanding from tblCheck where Status = '" + status + "' and " + typeofDate + " BETWEEN '" + fromDate + "' and '" + toDate + " 23:59:00'";
				PreparedStatement pst = conn.prepareStatement(query);
				ResultSet rs = pst.executeQuery();

				while (rs.next()) {
					
					return rs.getString(1);
					
				}
				
				pst.close();
				rs.close();
				
	
			   } catch (Exception ex) {
				   JOptionPane.showMessageDialog(null, ex.toString());
			   } 

			return "0";
		
			 
		   }
		   
		   
		   public String getGrandTotalofChecks(String fromDate, String toDate) {
			   
			   try {
				   	
				   	String query = "select Count(CheckNumber) from tblCheck where ForReleasingStamp BETWEEN '" + fromDate + "' and '" + toDate + " 23:59:00'";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					
					
					while (rs.next()) {
						
						return rs.getString(1);
						
					}
					
					pst.close();
					rs.close();
					
		
				   } catch (Exception ex) {
					   JOptionPane.showMessageDialog(null, ex.toString());
				   } 
	   
				return "0";
			   
		   }
		   
	    public void insertData(Check check) {
	        
	        try {
	        	

	            String query = "insert into tblCheck (CheckNumber, AccountNumber, CorporateCode, CorporationName, BatchNumber, ClientReferenceNumber, "
	                    + "CheckDate, PayeeName, CheckAmount, ReleasingBranch, ReleasingBranchName, Status, ForReleasingStamp) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	              
	            try (PreparedStatement pst = conn.prepareStatement(query)) {
	                pst.setString(1, check.getCheckNumber());
	                pst.setString(2, check.getAccountNumber());
	                pst.setString(3, check.getCorporateCode());
	                pst.setString(4, check.getCorporationName());
	                pst.setString(5, check.getBatchNumber());
	                pst.setString(6, check.getClientReferenceNumber());
	                pst.setString(7, check.getCheckDate());
	                pst.setString(8, check.getPayeeName());
	                pst.setString(9, check.getCheckAmount());
	                pst.setString(10, check.getReleasingBranchCode());
	                pst.setString(11, check.getReleasingBranch());
	                pst.setString(12, "For Release");
	                pst.setString(13, queryData.now("MM/dd/yyyy hh:mm:ss"));
	                
	                conn.setAutoCommit(true);
	                pst.execute();
	                pst.close();

	            }
	            
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, ex.toString());
	        }

	        
	    }
 
	    public void generateClaimedChecksforUCC(ArrayList<String> checkNum, ArrayList<String> payeeN, ArrayList<String> checkAmt) {
	    	
	    	try {
	    		
	    		
	    		getCompanySetup(); 
	    		Map<Integer, Object[]> data = new TreeMap<Integer,Object[]>();
	    		int startingData = 1;
	    		
				data.put(1, new Object[] {"Check Number","Payee Name","Check Amount", "Check Date","Client Reference Number","Payor's Name","OR Number","Claimed By","ID Presented","Claimed Date","Claimed Date and Timestamp","Status"});
				
				
	    		for (int i = 0; i <= checkNum.size() - 1;i++) {
	    			
						String query = "select ReleasingBranch, CheckNumber, PayeeName, CheckAmount, CheckDate, ClientReferenceNumber, CorporationName, ORNo, ClaimedBy, ClaimedDate, ClaimedID, ClaimedStamp, Status from tblCheck where Status = 'Claimed' and CheckNumber = '" + checkNum.get(i) + "' and PayeeName = '" + payeeN.get(i) + "' and CheckAmount = '" + checkAmt.get(i) + "'";
						PreparedStatement pst = conn.prepareStatement(query);
						ResultSet rs = pst.executeQuery();

						//setting the header of excel
		

						while (rs.next())
						{
		
							if (startingData == 1) {
								data.put(1, new Object[] {"Check Number","Payee Name","Check Amount", "Check Date","Client Reference Number","Payor's Name","OR Number","Claimed By","ID Presented","Claimed Date","Claimed Date and Timestamp","Status"});
								startingData++;
								data.put(startingData, new Object[] {rs.getString("CheckNumber"), rs.getString("PayeeName"), rs.getString("CheckAmount"),rs.getString("CheckDate"), rs.getString("ClientReferenceNumber"), rs.getString("CorporationName"),rs.getString("ORNo"), rs.getString("ClaimedBy"), rs.getString("ClaimedID"), rs.getString("ClaimedDate"), rs.getString("ClaimedStamp"), rs.getString("Status")});
							} else {
								startingData++;
								data.put(startingData, new Object[] {rs.getString("CheckNumber"), rs.getString("PayeeName"), rs.getString("CheckAmount"),rs.getString("CheckDate"), rs.getString("ClientReferenceNumber"), rs.getString("CorporationName"),rs.getString("ORNo"), rs.getString("ClaimedBy"), rs.getString("ClaimedID"), rs.getString("ClaimedDate"), rs.getString("ClaimedStamp"), rs.getString("Status")});
							}
							
						}
						
						pst.close();
						rs.close();
	    		}
	    		
	    		
						if (startingData > 1) { 
							@SuppressWarnings("unused")
							CreateExcelFile cx = new CreateExcelFile(data,0,Global.getBranchCode() + " - Claimed Checks","eBanking",Global.getBranchCode() + "_ClaimedChecksReport_" + queryData.now("MMddyyyy"), Global.getDownloadPath().toString(),true);
							JOptionPane.showMessageDialog(null, "There are " + (startingData - 1) + " claimed checks for " + Global.getBranchName(),"Generate Claimed Checks Report",JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "No records found..","Generate Claimed Checks Report",JOptionPane.INFORMATION_MESSAGE);
						}
						
					
						
						
	    		
	    	} catch (Exception ex) {
	    		
	    		JOptionPane.showMessageDialog(null, ex.toString(),"Error generating claimed checks report..",JOptionPane.WARNING_MESSAGE);
	    		
	    	} 

	    }

	    public void generateDispatchToUCC(ArrayList<String> checkNum, ArrayList<String> payeeN, String barCode) {
	    	
	    	try {
	    		
	    		String query = "";
				Map<Integer, Object[]> data = new TreeMap<Integer,Object[]>();
				Connection conn = sqliteConnection.dbConnector();
				int startingData = 5;

				getCompanySetup(); 
				//setting the header of excel
				
				data.put(1, new Object[] {"Bar Code: ", barCode});
				data.put(2, new Object[] {"Report Date: ",queryData.now("MMMM dd, yyyy")});
				data.put(3, new Object[] {""});
				data.put(4, new Object[] {"Check Number","Payee Name","Check Amount", "Check Date","Client Reference Number","Payor's Name","OR Number","Claimed By","Claimed Date","Dispatched to UCC Date and Timestamp","Status"});
				
	    		for (int i = 0; i <= checkNum.size() - 1;i++) {
	    			
	    			
	    			query = "select ReleasingBranch, CheckNumber, PayeeName, CheckAmount, CheckDate, ClientReferenceNumber, CorporationName, ORNo, ClaimedBy, ClaimedDate, DispatchedToUCCStamp, Status from tblCheck where Status = 'Dispatched To UCC' and CheckNumber = '" + checkNum.get(i) + "' and PayeeName = '" + payeeN.get(i) + "'";
	    			
	    			Statement s = conn.createStatement();
	    			ResultSet rs = s.executeQuery(query);
	    			
	    			while (rs.next()) {
	    				data.put(startingData, new Object[] {rs.getString("CheckNumber"), rs.getString("PayeeName"), rs.getString("CheckAmount"),rs.getString("CheckDate"), rs.getString("ClientReferenceNumber"), rs.getString("CorporationName"),rs.getString("ORNo"), rs.getString("ClaimedBy"), rs.getString("ClaimedDate"), rs.getString("DispatchedToUCCStamp"), rs.getString("Status")});
		    			startingData++;
	    			}
	    			
	    			s.close();
	    			rs.close();
	    			
	    		}

	    		@SuppressWarnings("unused")
				CreateExcelFile cx = new CreateExcelFile(data,0,Global.getBranchCode() + " - Dispatched to UCC","eBanking",Global.getBranchName() + " - Dispatched to UCC_" + barCode + "_" + queryData.now("MMddyyyy"), Global.getDownloadPath().toString(),false);

				
	    	} catch (Exception ex) {
	    		
	    		JOptionPane.showMessageDialog(null, ex.toString(),"Error generating claimed checks report..",JOptionPane.WARNING_MESSAGE);
	    		
	    	}
	    	

	    }
	    
	    public void updateStatusToDispatchedToUCC(ArrayList<String> checkNum, ArrayList<String> payeeN, String barCode) {
	    	
	    	try {
	    		
	    		String query = "update tblcheck set Status = 'Dispatched To UCC',DispatchedToUCCStamp = ?,UCCBarCode = ? where CheckNumber = ? and PayeeName = ?";
	    		PreparedStatement pst = conn.prepareStatement(query); 
	    		
	    		for (int i = 0; i < checkNum.size();i++) {
					
					pst.setString(1, queryData.now("MM/dd/yyyy hh:mm:ss"));
					pst.setString(2, barCode);
					pst.setString(3, checkNum.get(i));
					pst.setString(4, payeeN.get(i));
					pst.addBatch();

	    		}
	    		
	    		pst.executeBatch();
	    		pst.close();
	    		
	    	} catch (Exception ex) {
	    		
	    		JOptionPane.showMessageDialog(null, ex.toString(),"Error generating claimed checks report..",JOptionPane.WARNING_MESSAGE);
	    		
	    	}

	    }
	    
	    
	    public void updateStatusToReceivedByUCC(ReceivedByUCC rbucc) {
	    	
	    	try {

	    			String query = "update tblcheck set Status = 'Received By UCC',ReceivedByUCCStamp = ? where CheckNumber = ? and PayeeName = ? and Status = 'Dispatched To UCC'";
					PreparedStatement pst = conn.prepareStatement(query);
					pst.setString(1, rbucc.getReceivedByUCCDateAndTimestamp());
					pst.setString(2, rbucc.getCheckNumber());
					pst.setString(3, rbucc.getPayeeName());
	

					pst.executeUpdate();
					pst.close();
	    			
	    		
		
	    	} catch (Exception ex) {
	    		
	    		JOptionPane.showMessageDialog(null, ex.toString(),"Error generating claimed checks report..",JOptionPane.WARNING_MESSAGE);
	    		
	    	}
	
	    }
	    
	    
	    public void updateClaimedfromImport(ImportClaim importclaim) {
	    	
	    	try {

	    			String query = "update tblcheck set Status = 'Claimed',ORNo = ?,ClaimedBy = ?,ClaimedDate = ?,ClaimedStamp = ?,ClaimedID = ? where CheckNumber = ? and CheckAmount = ? and Status = 'For Release'";
					PreparedStatement pst = conn.prepareStatement(query);
					pst.setString(1, importclaim.getOrNum());
					pst.setString(2, importclaim.getClaimedBy().toUpperCase());
					pst.setString(3, importclaim.getClaimedDate());
					pst.setString(4, importclaim.getClaimedDate());
					pst.setString(5, importclaim.getClaimedID());
					
					
					if (importclaim.getCheckNum().length() < 10) {
						
						pst.setString(6, String.format("%010d", Integer.parseInt(importclaim.getCheckNum())));
						
					} else {
						
						pst.setString(6, importclaim.getCheckNum());
					}
					
					
					
					pst.setString(7, importclaim.getCheckAmount());
					
					pst.executeUpdate();
					pst.close();
	    			
	    		
		
	    	} catch (Exception ex) {
	    		
	    		JOptionPane.showMessageDialog(null, ex.toString(),"Error generating claimed checks report..",JOptionPane.WARNING_MESSAGE);
	    		
	    	}
	
	    }
 
	    public void getCompanySetup() {
	    	
	    	try {
	    		
				String query = "select * from tblSetup";
				PreparedStatement pst = conn.prepareStatement(query);
				ResultSet rs = pst.executeQuery();
				
				
				while (rs.next()) {
					
					Global.setBranchCode(rs.getString("BranchCode"));
					Global.setUploadPath(rs.getString("UploadPath"));
					Global.setDownloadPath(rs.getString("DownloadPath"));
					Global.setBranchName(rs.getString("BranchName"));
					Global.setEnableImport(rs.getBoolean("enableImport"));
					
				}
				
				
				rs.close();
				pst.close();
				
				
	    	} catch (Exception ex) {
	    		
	    		JOptionPane.showMessageDialog(null, ex.toString());
	    		
	    	}
			
	    	
	    }
	    
	    public void updateCompanySetup(String bCode, String dPath, String uPath, String where, String bName, boolean enableImport) {
	    	
	    	try {
	    		
				String query = "update tblSetup set BranchCode = ?,UploadPath = ?,DownloadPath = ?,BranchName = ?,enableImport = ? where BranchCode = ?";
				PreparedStatement pst = conn.prepareStatement(query);
				pst.setString(1, bCode);
				pst.setString(2, uPath);
				pst.setString(3, dPath);
				pst.setString(4, bName);
				pst.setBoolean(5, enableImport);
				pst.setString(6, where);

				
				pst.executeUpdate();

				JOptionPane.showMessageDialog(null, "System Setup has been updated!");
				
				pst.close();
				
				Global.setBranchCode(bCode);
				Global.setDownloadPath(dPath);
				Global.setUploadPath(uPath);
				Global.setBranchName(bName);
				Global.setEnableImport(enableImport);
				
				
				
				
	    	} catch (Exception ex) {
	    		
	    		JOptionPane.showMessageDialog(null, ex.toString());
	    		
	    	}
			
	    	
	    }
	    
    public void generateExcelForInquiry(ArrayList<String> checkNum, ArrayList<String> payeeN, ArrayList<String> checkAmt) {
	    	
	    	
			try {
	    		
	    
				Map<Integer, Object[]> data = new TreeMap<Integer,Object[]>();
				int startingData = 2;
				
				String strExcelFilename = "Check Inquiry_" +  queryData.now("ddMMYYYYhhmmss");
				
				getCompanySetup(); 
				//setting the header of excel
				
				data.put(1, new Object[] {"Report By: ", Global.getUserName()});
				data.put(2, new Object[] {"Releasing Branch: ",Global.getBranchName()});
				data.put(3, new Object[] {"Report Date: ",queryData.now("MMMM dd, yyyy")});
				data.put(4, new Object[] {""});
				data.put(5, new Object[] {"Check Number","Payee Name","Check Amount","Check Date","Payor's Name","OR Number","Claimed By","Claimed Date","ID Presented","Releasing Branch","Status"});
				
				
	    		for (int i = 0; i <= checkNum.size() - 1;i++) {

	    			queryExcel = "select ReleasingBranchName, CheckNumber, PayeeName, CheckAmount, CorporationName, CheckDate,  ORNo, ClaimedBy, ClaimedDate, ClaimedID, Status from tblCheck where CheckNumber = '" + checkNum.get(i) + "' and PayeeName = '" + payeeN.get(i) + "' and CheckAmount = '" + checkAmt.get(i) + "'";
					PreparedStatement pst = conn.prepareStatement(queryExcel);
					ResultSet rs = pst.executeQuery();
					
					while (rs.next()) {
		    			data.put(startingData, new Object[] {rs.getString("CheckNumber"), rs.getString("PayeeName"), rs.getString("CheckAmount"),rs.getString("CheckDate"), rs.getString("CorporationName"), rs.getString("ORNo"), rs.getString("ClaimedBy"), rs.getString("ClaimedDate"), rs.getString("ClaimedID"), rs.getString("ReleasingBranchName"), rs.getString("Status")});
		    			startingData++;
					}
	    			pst.close();
	    			rs.close();
	    			
	    		}

	    		@SuppressWarnings("unused")
				CreateExcelFile cx = new CreateExcelFile(data,0,strExcelFilename,"eBanking",strExcelFilename, new File(".").getCanonicalPath() + "\\Temp\\",false);

	    	      if (Desktop.isDesktopSupported()) {
	    	    	    try {
	    	    	        File myFile = new File(new File(".").getCanonicalPath() + "\\Temp\\" + strExcelFilename+".xlsx");
	    	    	        Desktop.getDesktop().open(myFile);
	    	    	    } catch (IOException ex) {
	    	    	    	JOptionPane.showMessageDialog(null, ex.toString());
	    	    	    }
	    	    	}
	    		
	    	} catch (Exception ex) {

	    		ex.printStackTrace();
	    		JOptionPane.showMessageDialog(null, ex.toString(),"Error generating claimed checks report..",JOptionPane.WARNING_MESSAGE);
	    		
	    	}
	    	

	    	
	    }
	    
}
