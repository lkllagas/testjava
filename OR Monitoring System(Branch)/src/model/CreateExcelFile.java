package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/*
* Here we will learn how to create Excel file and header for the same.
*/
public class CreateExcelFile {

	public CreateExcelFile(Map<Integer, Object[]> data, int startingRow, String sheetName, String excelPWD, String excelFileName, String location, boolean withPWD) {
		
		//Create a new Workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
		
      //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet(sheetName);
        
      //Iterate over data and write it to the sheet
        Set<Integer> keyset = data.keySet();
        int rownum = startingRow;
        for (Integer key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
                Cell cell = row.createCell(cellnum++);
                

                if(obj instanceof String)
                    cell.setCellValue((String)obj);	
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);  
                
                sheet.autoSizeColumn(cellnum);
            }
        }
        
        //Save the excel sheet
        try{
        	if (withPWD) sheet.protectSheet(excelPWD);

            FileOutputStream out = new FileOutputStream(new File(location + excelFileName + ".xlsx"));
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
	
	
	

}
