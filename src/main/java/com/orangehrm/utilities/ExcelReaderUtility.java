package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtility {

	public static Object[][] getTestData(String filePath, String sheetName) {
		List<String[]> dataList = getSheetData(filePath, sheetName);

		// Convert List<String[]> to Object[][] for TestNG DataProvider
		Object[][] dataArray = new Object[dataList.size()][];
		for (int i = 0; i < dataList.size(); i++) {
			dataArray[i] = dataList.get(i);
		}
		return dataArray;
	}

	private static List<String[]> getSheetData(String filePath, String sheetName) {
		List<String[]> data = new ArrayList<>();
		DataFormatter formatter = new DataFormatter();

		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Sheet " + sheetName + " doesn't exist");
			}

			int lastRowNum = sheet.getLastRowNum();

			Row headerRow = sheet.getRow(0);

			// Count only non-empty header cells
			int actualColumnCount = 0;
			for (int i = 0; i < headerRow.getLastCellNum(); i++) {
				Cell cell = headerRow.getCell(i);
				if (cell != null && !formatter.formatCellValue(cell).trim().isEmpty()) {
					actualColumnCount++;
				}
			}

//            for (int i = 1; i <= lastRowNum; i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                String[] rowData = new String[actualColumnCount];
//
//                for (int j = 0; j < actualColumnCount; j++) {
//                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                    rowData[j] = formatter.formatCellValue(cell);
//                }
//
//                data.add(rowData);
//            }
			for (int i = 1; i <= lastRowNum; i++) {

				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				String[] rowData = new String[actualColumnCount];
				boolean isRowEmpty = true;

				for (int j = 0; j < actualColumnCount; j++) {
					Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					String cellValue = formatter.formatCellValue(cell).trim();
					rowData[j] = cellValue;

					if (!cellValue.isEmpty()) {
						isRowEmpty = false;
					}
				}

				// 🚀 Skip completely empty rows
				if (!isRowEmpty) {
					data.add(rowData);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}
}
//    private static List<String[]> getSheetData(String filePath, String sheetName) {
//        List<String[]> data = new ArrayList<>();
//        DataFormatter formatter = new DataFormatter(); // Better for numeric/date formatting
//
//        try (FileInputStream fis = new FileInputStream(filePath); 
//             Workbook workbook = new XSSFWorkbook(fis)) {
//            
//            Sheet sheet = workbook.getSheet(sheetName);
//            if (sheet == null) {
//                throw new IllegalArgumentException("Sheet " + sheetName + " doesn't exist");
//            }
//
//            int lastRowNum = sheet.getLastRowNum();
//            int lastCellNum = sheet.getRow(0).getLastCellNum(); // Get column count from header
//
//            for (int i = 1; i <= lastRowNum; i++) { // Start from 1 to skip header
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                String[] rowData = new String[lastCellNum];
//                for (int j = 0; j < lastCellNum; j++) {
//                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                    rowData[j] = formatter.formatCellValue(cell); // Handles all types as String
//                }
//                data.add(rowData);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }
//}