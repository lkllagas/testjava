package controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import model.Global;

public class validation {

	Connection conn = sqliteConnection.dbConnector();
    private String validatePWD;
	
	public boolean validateChangedPassword() {
		
		if (validatePWD.equals("1")) return true;
		else return false;
		
	}
	
public boolean validateClaim (String checknumber, String checkamount) {
		
		try { 
			
			String query = "select Checknumber,CheckAmount from tblCheck where Checknumber=? and CheckAmount=? and Status = 'For Release'";
			
			
			PreparedStatement pst = conn.prepareStatement(query);
			
			if (checknumber.length() < 10) {
				
				pst.setString(1, String.format("%010d", Integer.parseInt(checknumber)));
				
			} else {
				
				pst.setString(1, checknumber);
			}

			pst.setString(2, checkamount);
			ResultSet rst = pst.executeQuery();
			int count = 0;
			
			while (rst.next()) {
				
				count+=1;
			}
			
			if (count == 1) {
				
				
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
	
	public boolean validateUser (String username, String password) {
		
		try { 
			
			String query = "select * from tblUser where userID=? and password=?";

			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, username);
			pst.setString(2, password);
			ResultSet rst = pst.executeQuery();
			int count = 0;
			
			while (rst.next()) {
				Global.setUserLoggedIn(rst.getString("userID"));
				Global.setUserName(rst.getString("firstName").toUpperCase() + " " + rst.getString("MiddleName").toUpperCase() + " " + rst.getString("LastName").toUpperCase());
				validatePWD =  rst.getString("changedPassword");
				count+=1;
			}
			
			if (count == 1) {
				
				
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
	
	public boolean validateUserID (String username) {
		

		try { 
			
			String query = "select * from tblUser where userID=?";

			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, username);
			ResultSet rst = pst.executeQuery();
			int count = 0;
			
			while (rst.next()) {
				count+=1;
			}
			
			if (count == 1) {
				
				pst.close();
				rst.close();
				return false;
				
				
			} else {
				
				
				pst.close();
				rst.close();
				return true;
				
			}
			
		} catch (Exception ex) {
			
			JOptionPane.showMessageDialog(null, ex.toString());
			
		}
		
		return false;
		
	}
	
}
