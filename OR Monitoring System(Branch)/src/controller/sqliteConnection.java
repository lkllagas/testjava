package controller;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import javax.swing.*;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.Pragma;
import org.sqlite.SQLiteConnection;

public class sqliteConnection {

	 Connection conn = null;
	 
	 public sqliteConnection() {
		 dbConnector();
	 }

	 public static Connection dbConnector() 
	 {
		 
		 try {
			 
			 String path = new File(".").getCanonicalPath();
			 String dbName = "cworms_branch.accdb";
			 
			 Connection conn=DriverManager.getConnection(
				        "jdbc:ucanaccess://"+ dbPath(path,dbName));

			 
			 return conn;
			 
		 } catch (SQLException | IOException ex) {
			 
			 //JOptionPane.showMessageDialog(null, ex.toString());
			 return null;
			 
		 }

		 
	 }
	 
	 private static String dbPath(String path, String dbName) {
		 return path + "\\" + dbName;
	 }
	 
	 private static boolean isFileExists(String pathDB) {
		 
		 File f = new File(pathDB);
		 
		 if (f.exists()) {
			 return true;
		 } else {
			 return false;
		 }

	 }
	 
	 
	
	
	
}
