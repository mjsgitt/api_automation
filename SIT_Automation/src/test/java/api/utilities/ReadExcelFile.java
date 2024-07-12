package api.utilities;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelFile {

    public static FileInputStream XSLFile;
    public static XSSFWorkbook workbook;
    public static XSSFRow rowObject;
    public static XSSFCell cell;
    public static XSSFSheet sheet;


    public static String GetCellValue(String fpath,String sheetname,int row,int col){

        try {
            XSLFile = new FileInputStream(fpath);
            workbook = new XSSFWorkbook(XSLFile);
            sheet = workbook.getSheet(sheetname);
            rowObject = sheet.getRow(row);
            cell = rowObject.getCell(col);

            return cell.getStringCellValue();

        }
        catch(Exception e) {
            return "";
        }
    }

    public static int rowCount(String fpath,String sheetname) {
        try {
            XSLFile = new FileInputStream(fpath);
            workbook = new XSSFWorkbook(XSLFile);
            sheet = workbook.getSheet(sheetname);

            int rowcnt= sheet.getLastRowNum() + 1;

            return rowcnt;

        }
        catch(Exception e) {
            return 0;
        }
    }

    public static int colCount(String fpath,String sheetname) {
        try {
            XSLFile = new FileInputStream(fpath);
            workbook = new XSSFWorkbook(XSLFile);
            sheet = workbook.getSheet(sheetname);

            int colcnt= sheet.getRow(0).getLastCellNum();
            return colcnt;

        }
        catch(Exception e) {
            return 0;
        }
    }
}
