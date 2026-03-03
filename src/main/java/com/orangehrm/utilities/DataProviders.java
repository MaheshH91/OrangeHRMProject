package com.orangehrm.utilities;

import org.testng.annotations.DataProvider;

public class DataProviders {
    
    // Path to your Excel file
    private static final String FILE_PATH = System.getProperty("user.dir") + "/src/test/resources/testdata/TestData.xlsx";

    @DataProvider(name = "validLoginData")
    public static Object[][] validLoginData() {
        return getSheetData("validLoginData");
    }

    @DataProvider(name = "inValidLoginData")
    public static Object[][] inValidLoginData() {
        return getSheetData("inValidLoginData");
    }

    @DataProvider(name = "emplVerification")
    public static Object[][] emplVerification() {
        return getSheetData("emplVerification");
    }

//    /**
//     * Helper method to fetch data. 
//     * Uses the professional getTestData method from ExcelReaderUtility.
//     */
//    private static Object[][] getSheetData(String sheetName) {
//        Object[][] data = ExcelReaderUtility.getTestData(FILE_PATH, sheetName);
//        
//        // Safety check to prevent TestNG from crashing if Excel returns no data
//        if (data == null || data.length == 0) {
//            return new Object[0][0]; 
//        }
//        return data;
//    }
//    
    /**
     * Smart helper method:
     * Automatically detects file type (.xlsx or .ods)
     */
    private static Object[][] getSheetData(String sheetName) {

        Object[][] data = null;

        if (FILE_PATH.endsWith(".xlsx")) {
            data = ExcelReaderUtility.getTestData(FILE_PATH, sheetName);

        } else if (FILE_PATH.endsWith(".ods")) {
            data = ODSReaderUtility.getTestData(FILE_PATH, sheetName);

        } else {
            throw new IllegalArgumentException("Unsupported file format. Use .xlsx or .ods");
        }

        // Safety check to prevent TestNG crash if no data found
        if (data == null || data.length == 0) {
            return new Object[0][0];
        }

        return data;
    }
}