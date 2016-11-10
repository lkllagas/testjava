package model;

public class Global {
	
	
	public Global() {
		
		
		
	}
	
	private static String branchCode = "";
	private static String branchName = "";
	private static String uploadPath = "";
	private static String downloadPath = "";
	private static String userLoggedIn = "";
	private static String userName = "";
	private static boolean enableImport;
	
	public static String getBranchCode() {
		return branchCode;
	}
	public static void setBranchCode(String branchCode) {
		Global.branchCode = branchCode;
	}
	
	public static String getUploadPath() {
		return uploadPath;
	}
	public static void setUploadPath(String uploadPath) {
		Global.uploadPath = uploadPath;
	}
	
	public static String getDownloadPath() {
		return downloadPath;
	}
	public static void setDownloadPath(String downloadPath) {
		Global.downloadPath = downloadPath;
	}
	public static String getBranchName() {
		return branchName;
	}
	public static void setBranchName(String branchName) {
		Global.branchName = branchName;
	}
	public static String getUserLoggedIn() {
		return userLoggedIn;
	}
	public static void setUserLoggedIn(String userLoggedIn) {
		Global.userLoggedIn = userLoggedIn;
	}
	public static String getUserName() {
		return userName;
	}
	public static void setUserName(String userName) {
		Global.userName = userName;
	}
	public static boolean isEnableImport() {
		return enableImport;
	}
	public static void setEnableImport(boolean enableImport) {
		Global.enableImport = enableImport;
	}
	
	
	
	
	
	
}
