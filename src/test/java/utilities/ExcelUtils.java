package utilities;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Excel utility for Selenium test data (Apache POI).
 *
 * Maven dependencies:
 *   org.apache.poi:poi:5.2.5
 *   org.apache.poi:poi-ooxml:5.2.5
 */
public class ExcelUtils {

    private final String filePath;
    private final DataFormatter formatter = new DataFormatter();

    public ExcelUtils(String filePath) {
        this.filePath = filePath;
    }

    /** Number of data rows (excluding header row). */
    public int getRowCount(String sheetName) {
        try (Workbook wb = open()) {
            Sheet sheet = wb.getSheet(sheetName);
            return sheet == null ? 0 : sheet.getLastRowNum();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read row count: " + e.getMessage(), e);
        }
    }

    /** Number of columns, based on the header row. */
    public int getColumnCount(String sheetName) {
        try (Workbook wb = open()) {
            Row header = wb.getSheet(sheetName).getRow(0);
            return header == null ? 0 : header.getLastCellNum();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read column count: " + e.getMessage(), e);
        }
    }

    /** Read a single cell as text (rowNum/colNum are 0-based). */
    public String getCellData(String sheetName, int rowNum, int colNum) {
        try (Workbook wb = open()) {
            Row row = wb.getSheet(sheetName).getRow(rowNum);
            if (row == null) return "";
            Cell cell = row.getCell(colNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            return cell == null ? "" : formatter.formatCellValue(cell, wb.getCreationHelper().createFormulaEvaluator());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read cell: " + e.getMessage(), e);
        }
    }

    /** Read a cell by column header name, e.g. getCellData("Login", 1, "username"). */
    public String getCellData(String sheetName, int rowNum, String columnName) {
        try (Workbook wb = open()) {
            Sheet sheet = wb.getSheet(sheetName);
            Row header = sheet.getRow(0);
            for (int c = 0; c < header.getLastCellNum(); c++) {
                if (formatter.formatCellValue(header.getCell(c)).trim().equalsIgnoreCase(columnName)) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null) return "";
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    return cell == null ? "" : formatter.formatCellValue(cell, wb.getCreationHelper().createFormulaEvaluator());
                }
            }
            throw new IllegalArgumentException("Column not found: " + columnName);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read cell: " + e.getMessage(), e);
        }
    }

    /** Whole sheet (minus header) as Object[][] -- ready for a TestNG @DataProvider. */
    public Object[][] getSheetData(String sheetName) {
        try (Workbook wb = open()) {
            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) throw new IllegalArgumentException("Sheet not found: " + sheetName);

            int rows = sheet.getLastRowNum();
            int cols = sheet.getRow(0).getLastCellNum();
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            Object[][] data = new Object[rows][cols];

            for (int r = 1; r <= rows; r++) {
                Row row = sheet.getRow(r);
                for (int c = 0; c < cols; c++) {
                    Cell cell = (row == null) ? null : row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    data[r - 1][c] = (cell == null) ? "" : formatter.formatCellValue(cell, evaluator);
                }
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read sheet: " + e.getMessage(), e);
        }
    }

    /** Write a value to a cell (creates sheet/row/cell if missing) and save the file. */
    public void setCellData(String sheetName, int rowNum, int colNum, String value) {
        try (Workbook wb = open()) {
            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) sheet = wb.createSheet(sheetName);
            Row row = sheet.getRow(rowNum);
            if (row == null) row = sheet.createRow(rowNum);
            Cell cell = row.getCell(colNum);
            if (cell == null) cell = row.createCell(colNum);
            cell.setCellValue(value);

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                wb.write(out);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to write cell: " + e.getMessage(), e);
        }
    }

    private Workbook open() throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return new XSSFWorkbook(fis);
        }
    }
}

/* ---------------------------------------------------------------
 * Example usage in a TestNG test:
 *
 * public class LoginTest extends BaseTest {
 *
 *     @DataProvider(name = "loginData")
 *     public Object[][] loginData() {
 *         return new ExcelUtils("src/test/resources/TestData.xlsx").getSheetData("Login");
 *     }
 *
 *     @Test(dataProvider = "loginData")
 *     public void loginTest(String username, String password) {
 *         driver.findElement(By.id("username")).sendKeys(username);
 *         driver.findElement(By.id("password")).sendKeys(password);
 *         driver.findElement(By.id("loginBtn")).click();
 *     }
 * }
 * --------------------------------------------------------------- */