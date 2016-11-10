package controller;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import javax.swing.*;

public class sqliteConnectionMysql {

	 Connection conn = null;
	 
	 public sqliteConnectionMysql() {
		 dbConnector();
	 }

	 public static Connection dbConnector() 
	 {
		 
		 try {
			 
			 String path = new File(".").getCanonicalPath();
			 String dbName = "ucc";
			 
			/// Class.forName("org.sqlite.JDBC");

			 //Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath(path,dbName));

			 Class.forName("com.mysql.jdbc.Driver");  
			// Connection conn = DriverManager.getConnection("jdbc:h2:"+ dbPath(path,dbName), "sa", "");
			 
			 Connection conn=DriverManager.getConnection(  
					 "jdbc:mysql://localhost:3306/cworms_branch","root","787618homer");  
			 
			 conn.setAutoCommit(false);
			 
			 return conn;
			 
		 } catch (SQLException | ClassNotFoundException | IOException ex) {
			 
			 JOptionPane.showMessageDialog(null, ex.toString());
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
