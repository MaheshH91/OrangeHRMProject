package com.orangehrm.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;

public class ODSReaderUtility {

    public static Object[][] getTestData(String filePath, String sheetName) {
        List<String[]> dataList = getSheetData(filePath, sheetName);

        Object[][] dataArray = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = dataList.get(i);
        }
        return dataArray;
    }

    private static List<String[]> getSheetData(String filePath, String sheetName) {

        List<String[]> data = new ArrayList<>();

        try {
            OdfSpreadsheetDocument document = OdfSpreadsheetDocument.loadDocument(new File(filePath));
            OdfTable sheet = document.getTableByName(sheetName);

            if (sheet == null) {
                throw new IllegalArgumentException("Sheet " + sheetName + " doesn't exist");
            }

            int rowCount = sheet.getRowCount();
            int columnCount = sheet.getColumnCount();

            // Get header row (row 0)
            OdfTableRow headerRow = sheet.getRowByIndex(0);

            int actualColumnCount = 0;

            for (int i = 0; i < columnCount; i++) {
                OdfTableCell cell = headerRow.getCellByIndex(i);
                String value = cell.getDisplayText().trim();
                if (!value.isEmpty()) {
                    actualColumnCount++;
                }
            }

            // Start from row 1 (skip header)
            for (int i = 1; i < rowCount; i++) {

                OdfTableRow row = sheet.getRowByIndex(i);
                if (row == null)
                    continue;

                String[] rowData = new String[actualColumnCount];
                boolean isRowEmpty = true;

                for (int j = 0; j < actualColumnCount; j++) {
                    OdfTableCell cell = row.getCellByIndex(j);
                    String cellValue = cell.getDisplayText().trim();
                    rowData[j] = cellValue;

                    if (!cellValue.isEmpty()) {
                        isRowEmpty = false;
                    }
                }

                // Skip completely empty rows
                if (!isRowEmpty) {
                    data.add(rowData);
                }
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}