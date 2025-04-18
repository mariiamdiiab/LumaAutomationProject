package data;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelReader {
    static FileInputStream fis = null;
    private final String filePath = System.getProperty("user.dir") + "/src/test/java/data/TestData.xlsx";

    public FileInputStream getFileInputStream() {
        File srcFile = new File(filePath);  // Now uses the class-level variable
        try {
            fis = new FileInputStream(srcFile);
        } catch (FileNotFoundException e) {
            System.out.println("Test Data File not found !!");
            System.exit(0);
        }
        return fis;
    }

    public Object[][] getExcelDataForRegistration() throws IOException {
        fis = getFileInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        // Get row count (skip header)
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;
        int colCount = 6; // firstName, lastName, email, password

        String[][] testData = new String[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) { // Start from 1 to skip header
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                // Handle null cells safely
                testData[i - 1][j] = row.getCell(j) != null ?
                        row.getCell(j).toString() : "";
            }
        }

        workbook.close();
        fis.close();
        return testData;
    }


    public Object[][] getExcelDataForSignIn() throws IOException {
        fis = getFileInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(1);

        // Get row count (skip header)
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;
        int colCount = 4; // email, password

        String[][] testData = new String[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) { // Start from 1 to skip header
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                // Handle null cells safely
                testData[i - 1][j] = row.getCell(j) != null ?
                        row.getCell(j).toString() : "";
            }
        }

        workbook.close();
        fis.close();
        return testData;
    }


    public Object[][] getExcelDataForSearchProduct() throws IOException {
        fis = getFileInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(2);

        // Get row count (skip header)
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;
        int colCount = 1; // firstName, lastName, email, password

        String[][] testData = new String[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) { // Start from 1 to skip header
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                // Handle null cells safely
                testData[i - 1][j] = row.getCell(j) != null ?
                        row.getCell(j).toString() : "";
            }
        }

        workbook.close();
        fis.close();
        return testData;
    }


    public Object[][] getExcelDataForAddingReview() throws IOException {
        fis = getFileInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(3); // Verify the correct sheet index

        int rowCount = sheet.getPhysicalNumberOfRows() - 1; // Exclude header
        int colCount = 6; // Must match the number of columns your test needs

        Object[][] testData = new Object[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                testData[i - 1][j] = (row != null && row.getCell(j) != null)
                        ? row.getCell(j).toString()
                        : "";
            }
        }

        workbook.close();
        fis.close();
        return testData;
    }


    public Object[][] getExcelDataForCompare() throws IOException {
        fis = getFileInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(4);

        // Get row count (skip header)
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;
        int colCount = 2; // firstName, lastName, email, password

        String[][] testData = new String[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) { // Start from 1 to skip header
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                // Handle null cells safely
                testData[i - 1][j] = row.getCell(j) != null ?
                        row.getCell(j).toString() : "";
            }
        }

        workbook.close();
        fis.close();
        return testData;
    }


    public Object[][] getExcelDataForNewAddress() throws IOException {
        fis = getFileInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(5);

        int rowCount = sheet.getPhysicalNumberOfRows() - 1;
        int colCount = 8;

        String[][] testData = new String[rowCount][colCount];
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                testData[i - 1][j] = (row != null && row.getCell(j) != null)
                        ? formatter.formatCellValue(row.getCell(j))
                        : "";
            }
        }

        workbook.close();
        fis.close();
        return testData;
    }


    public void writeNewOrderId(String orderId) throws IOException {
        String sheetName = "Orders";
        fis = getFileInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // Get or create a sheet
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            // Create header row if new sheet
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Order IDs");
        }

        // Find the next available row
        int nextRow = sheet.getLastRowNum() + 1;

        // Create row and write order ID
        Row newRow = sheet.createRow(nextRow);
        newRow.createCell(0).setCellValue(orderId);

        // Save changes
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);

        // Close resources
        workbook.close();
        fis.close();
        fos.close();
    }


    public String getLastOrderId() throws IOException {
        fis = getFileInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(6); // or getSheet("Orders")

        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum < 0) return ""; // Empty sheet

        Row lastRow = sheet.getRow(lastRowNum);
        Cell idCell = lastRow.getCell(0); // Assuming ID is in the first column

        String orderId = (idCell != null)
                ? new DataFormatter().formatCellValue(idCell)
                : "";

        workbook.close();
        fis.close();
        return orderId;
    }
}