package controller;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import javax.swing.*;

import org.sqlite.SQLiteConnection;

public class sqliteConnection2 {

	 static Connection conn = null;
	 
	 public sqliteConnection2() {
		 dbConnector();
	 }

	 public static Connection dbConnector() 
	 {
		 
		 try {
			 
			 String path = new File(".").getCanonicalPath();
			 String dbName = "monitoringsystem_branch.db";

			 Class.forName("org.sqlite.JDBC");
			 Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath(path,dbName));
			 ((SQLiteConnection)conn).setBusyTimeout(35000);
 
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
