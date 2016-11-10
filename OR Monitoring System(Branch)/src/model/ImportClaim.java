package model;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

public class ImportClaim {

	
	private String checkNum = "";
	private String checkAmount = "";
	private String orNum = "";
	private String claimedBy = "";
	private String claimedDate = "";
	private String claimedID = "";
	DecimalFormat formatter = new DecimalFormat("#,###.00");
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	Date cDate; 
	
	public String getCheckNum() {
		return checkNum;
	}
	
	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}
	
	public String getCheckAmount() {
		return checkAmount;
	}
	
	public void setCheckAmount(String checkAmount) {
		this.checkAmount = checkAmount;
	}
	
	
	public void setValues (int num, Cell cell) throws ParseException {
		
		 switch (num) {
		 	
		 	// excel column will start at index 0
			// 0 = check number
			// 1 = check amount
			// 2 - or num
			// 3 - id
			// 4 - claimed by
			// 5 - claimed date
		 
		 case 0:
			 setCheckNum(String.valueOf((long)cell.getNumericCellValue()));
			 break;
		 case 1:
			 setCheckAmount(String.valueOf(formatter.format(cell.getNumericCellValue())));
			 break;
		 case 2:
			 setOrNum(String.valueOf((int)cell.getNumericCellValue()));
			 break;
		 case 3:
			 setClaimedID(cell.getStringCellValue());
			 break;
		 case 4:
			 setClaimedBy(cell.getStringCellValue());
			 break;
		 case 5:
			 
			 String sDate = sdf.format(cell.getDateCellValue());
			 setClaimedDate(sDate);
			 break;
			 
		 }
		
	}

	public String getOrNum() {
		return orNum;
	}

	public void setOrNum(String orNum) {
		this.orNum = orNum;
	}

	public String getClaimedBy() {
		return claimedBy;
	}

	public void setClaimedBy(String claimedBy) {
		this.claimedBy = claimedBy;
	}

	public String getClaimedDate() {
		return claimedDate;
	}

	public void setClaimedDate(String claimedDate) {
		this.claimedDate = claimedDate;
	}

	public String getClaimedID() {
		return claimedID;
	}

	public void setClaimedID(String claimedID) {
		this.claimedID = claimedID;
	}
	
	
}
