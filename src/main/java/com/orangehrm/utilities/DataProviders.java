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

    /**
     * Helper method to fetch data. 
     * Uses the professional getTestData method from ExcelReaderUtility.
     */
    private static Object[][] getSheetData(String sheetName) {
        Object[][] data = ExcelReaderUtility.getTestData(FILE_PATH, sheetName);
        
        // Safety check to prevent TestNG from crashing if Excel returns no data
        if (data == null || data.length == 0) {
            return new Object[0][0]; 
        }
        return data;
    }
}