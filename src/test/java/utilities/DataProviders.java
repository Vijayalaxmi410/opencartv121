package utilities;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
 
/**
 * Central DataProvider class backed by an Excel file.
 *
 * Usage in a test class:
 *   @Test(dataProvider = "excelData", dataProviderClass = TestDataProvider.class)
 *   public void loginTest(String username, String password) { ... }
 *
 * "excelData" uses the TEST METHOD NAME as the sheet name
 * (so loginTest -> sheet "loginTest"). Use "loginData" for a fixed sheet.
 */

public class DataProviders {
 
    private static final String EXCEL_PATH = ".\\test-data\\data.xlsx";
 
    /** Generic provider: sheet name = test method name. */
    @DataProvider(name = "excelData")
    public static Object[][] excelData(Method method) {
        return new ExcelUtils(EXCEL_PATH).getSheetData(method.getName());
    }
 
    /** Fixed-sheet provider: reads the "Login" sheet. */
    @DataProvider(name = "loginData")
    public static Object[][] loginData() {
        return new ExcelUtils(EXCEL_PATH).getSheetData("Login");
    }
 
    /** Same as loginData but runs rows in parallel. */
    @DataProvider(name = "loginDataParallel", parallel = true)
    public static Object[][] loginDataParallel() {
        return new ExcelUtils(EXCEL_PATH).getSheetData("Login");
    }
}