package model;

import org.apache.poi.ss.usermodel.Cell;

public class Check {

	// 3 = corporate code
	// 4 = corporation name
	// 5 = account number
	// 7 = batch number
	// 11 = client reference number
	// 12 = check number
	// 13 = check date
	// 15 = payee name
	// 16 = check amount
	// 21 = releasing branch
	
	int dashIndex = 0;
	private String corporateCode = "";
	private String corporationName = "";
	private String accountNumber = "";
	private String batchNumber = "";
	private String clientReferenceNumber = "";
	private String checkNumber = "";
	private String checkDate = "";
	private String payeeName = "";
	private String checkAmount = "";
	private String releasingBranch = "";
	private String releasingBranchCode = "";
	

	public String getCorporateCode() {
		return corporateCode;
	}
	public void setCorporateCode(String corporateCode) {
		this.corporateCode = corporateCode;
	}
	public String getCorporationName() {
		return corporationName;
	}
	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getClientReferenceNumber() {
		return clientReferenceNumber;
	}
	public void setClientReferenceNumber(String clientReferenceNumber) {
		this.clientReferenceNumber = clientReferenceNumber;
	}
	public String getCheckNumber() {
		return checkNumber;
	}
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getCheckAmount() {
		return checkAmount;
	}
	public void setCheckAmount(String checkAmount) {
		this.checkAmount = checkAmount;
	}
	public String getReleasingBranch() {
		return releasingBranch;
	}
	public void setReleasingBranch(String releasingBranch) {
		this.releasingBranch = releasingBranch;
	}
	
	public void setValues (int num, Cell cell) {
		
		 switch (num) {
		 	
		 	// excel column will start at index 0
			// 0 = check number
			// 1 = account number
			// 2 = corporate code
			// 3 = corp name
			// 4 = batch number
			// 5 = client ref num
			// 6 = check date
			// 7 = payee name
			// 8 = check amount
			// 9 = releasing branch code
		 	// 10 = releasing branch name
		 	
		 
     		case 2:
             setCorporateCode(cell.getStringCellValue());
             break;
            
     		case 3:
                setCorporationName(cell.getStringCellValue());
                break;
                
     		case 1:
                setAccountNumber(cell.getStringCellValue());
                break;
                
     		case 4:
                setBatchNumber(cell.getStringCellValue());
                break;
                
     		case 5:
                setClientReferenceNumber(cell.getStringCellValue());
                break;
                
     		case 0:
                setCheckNumber(cell.getStringCellValue());
                break;
                
     		case 6:
                setCheckDate(cell.getStringCellValue());
                break;
                
     		case 7:
                setPayeeName(cell.getStringCellValue());
                break;
                
     		case 8:
                setCheckAmount(cell.getStringCellValue());
                break;
            
     		case 9:
     			setReleasingBranchCode(cell.getStringCellValue());
     			break;
     		case 10:
     			setReleasingBranch(cell.getStringCellValue());
     			break;

		 
		 }
		
	}
	
	public String getReleasingBranchCode() {
		return releasingBranchCode;
	}
	public void setReleasingBranchCode(String releasingBranchCode) {
		this.releasingBranchCode = releasingBranchCode;
	}
	
	
}
