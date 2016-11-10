package model;

import org.apache.poi.ss.usermodel.Cell;

public class ReceivedByUCC extends Check {

	private String receivedByUCCDateAndTimestamp = "";

	public String getReceivedByUCCDateAndTimestamp() {
		return receivedByUCCDateAndTimestamp;
	}

	public void setReceivedByUCCDateAndTimestamp(
			String receivedByUCCDateAndTimestamp) {
		this.receivedByUCCDateAndTimestamp = receivedByUCCDateAndTimestamp;
	}
	
	public void setValues (int num, Cell cell) {
		
		 switch (num) {
		 	
		 	// excel column will start at index 0
			// 0 = check number
			// 1 = payee name
			// 9 = corporate code
		 	
		 	case 0:
             setCheckNumber(cell.getStringCellValue());
             break;
             
		 	case 1:
		 		setPayeeName(cell.getStringCellValue());
		 		break;
		 	
		 	case 9:
		 		setReceivedByUCCDateAndTimestamp(cell.getStringCellValue());
		 		break;

		 
		 }
		
	}
	
	
}
