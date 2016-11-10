/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import controller.queryData;
import controller.validation;



public class ReadExcel {
	
	int rowStart = 0;
	int validImport = 0;
	
    public void readExcelFileforImportingIssuedChecks(String excelFilePath, int colsExcel[], int startingRow) {
        
        try {
        		
        		int validData = 0;
	            Check check = new Check();
	            queryData cd = new queryData();

	            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
                Workbook workbook = getWorkbook(inputStream, excelFilePath);

                Sheet firstSheet = workbook.getSheetAt(0);

                rowStart = startingRow;
                int rowEnd = Math.max(rowStart, firstSheet.getLastRowNum());

                for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {

                    Row r1 = firstSheet.getRow(rowNum);
                    
                    for (int i = 0; i < colsExcel.length; i ++) {
	
                        check.setValues(colsExcel[i], r1.getCell(colsExcel[i], Row.RETURN_BLANK_AS_NULL));

                    }
                    
                    validData++;
                	cd.insertData(check);

                }

                if (validData > 1) JOptionPane.showMessageDialog(null, "There are " + rowEnd + " new issued checks for " + Global.getBranchName());
                else JOptionPane.showMessageDialog(null, "There is " + rowEnd + " new issued check for " + Global.getBranchName());
                
        }
        
        catch (Exception ex) {
            
        	
             JOptionPane.showMessageDialog(null,ex.toString() + rowStart);
              
        }
        
        
    }
    
   public void readExcelFileforImportingClaimedChecks(String excelFilePath, int colsExcel[], int startingRow) {
        
        try {
        		
        		validation vd = new validation();
	            ImportClaim importclaim = new ImportClaim();
	            queryData cd = new queryData();

	            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
                Workbook workbook = getWorkbook(inputStream, excelFilePath);

                Sheet firstSheet = workbook.getSheetAt(0);

                rowStart = startingRow;
                int rowEnd = Math.max(rowStart, firstSheet.getLastRowNum());

                for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {

                    Row r1 = firstSheet.getRow(rowNum);
                    
                    if (r1.getCell(colsExcel[3], Row.RETURN_BLANK_AS_NULL).getStringCellValue() != null) {
                    
	                    for (int i = 0; i < colsExcel.length; i ++) {
	                    	
	                    	importclaim.setValues(colsExcel[i], r1.getCell(colsExcel[i], Row.RETURN_BLANK_AS_NULL));
	
	                    }
	                    
	                    if (vd.validateClaim(importclaim.getCheckNum(), importclaim.getCheckAmount())) {
	                    
		                    validImport++;
		                	cd.updateClaimedfromImport(importclaim);
	                	
	                    }
	                	
                    } else {
                    	
                    	return;
                    }

                }
                

             
        }
        
        catch (Exception ex) {

            // JOptionPane.showMessageDialog(null,ex.toString() + rowStart);
              
        } finally {
			
        	if (validImport > 0) {
            	JOptionPane.showMessageDialog(null, "Successfully tagged " + validImport + " check numbers for claimed status..");           
            } else {
            	JOptionPane.showMessageDialog(null, "No data has been processed, please check your excel template..");  
            }
        	
		}
        
        
    }    
    
	   
   
public void readExcelFileforImportingReceivedByUCC(String excelFilePath, int colsExcel[], int startingRow) {
        
        try {
        		
        		int validData = 0;
	            ReceivedByUCC rb = new ReceivedByUCC();
	            queryData qd = new queryData();

	            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
                Workbook workbook = getWorkbook(inputStream, excelFilePath);

                Sheet firstSheet = workbook.getSheetAt(0);

                rowStart = startingRow;
                int rowEnd = Math.max(rowStart, firstSheet.getLastRowNum());

                for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {

                    Row r1 = firstSheet.getRow(rowNum);
                    
                    for (int i = 0; i < colsExcel.length; i ++) {
	
                    	rb.setValues(colsExcel[i], r1.getCell(colsExcel[i], Row.RETURN_BLANK_AS_NULL));

                    }
                    
                    
                	qd.updateStatusToReceivedByUCC(rb);
                	validData++;

                }
                
                if (validData > 1) JOptionPane.showMessageDialog(null, "UCC received " + rowEnd + " checks..");
                else JOptionPane.showMessageDialog(null, "UCC received " + rowEnd + " check..");
              
        }
        
        catch (Exception ex) {
            
        	
             JOptionPane.showMessageDialog(null,ex.toString() + rowStart);
              
        }
        
        
    }
    
    private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath)
            throws IOException {
    	
	        Workbook workbook = null;
	
	        
	        if (excelFilePath.endsWith("xlsx")) {
	            workbook = new XSSFWorkbook(inputStream);
	        } else if (excelFilePath.endsWith("xls")) {
	            workbook = new HSSFWorkbook(inputStream);
	        } else {
	            throw new IllegalArgumentException("The specified file is not an Excel file");
	        }
	     
	        return workbook;
	        
        }
    

    
    
}
