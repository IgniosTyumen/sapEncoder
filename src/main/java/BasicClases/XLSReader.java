package BasicClases;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XLSReader {

    public static ArrayList<ShiftMatrix> uploadShiftsFromXls(File xls) throws IOException {

        ArrayList<ShiftMatrix> result = new ArrayList<>();

        InputStream inp = null;
        HSSFWorkbook wb = null;

        inp = new FileInputStream(xls);
        wb = new HSSFWorkbook(inp);

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            ShiftMatrix matrix = new ShiftMatrix();
            int cou = 0;
            while (cells.hasNext()) {
                Cell cell = cells.next();
                if (cou == 1) {
                    String resolver = cell.getStringCellValue();
                    resolver.replaceAll(" ", "");
                    if (resolver.length() == 4) resolver = "0" + resolver;
                    LocalTime time = null;
                    String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
                    Matcher m = Pattern.compile(regex).matcher(resolver);
                    if (m.find()) time = LocalTime.parse(m.group(0));
                    matrix.setTimeBegin(time);
                    cou++;
                    continue;
                }
                if (cou == 0) {
                    String resolver = cell.getStringCellValue();
                    matrix.setTimeCode(resolver);
                    cou++;
                    continue;
                }
                if (cou == 2) {
                    String resolver = cell.getStringCellValue();
                    //noinspection ResultOfMethodCallIgnored
                    resolver.replaceAll(" ", "");
                    if (resolver.length() == 4) resolver = "0" + resolver;
                    LocalTime time = null;
                    String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
                    Matcher m = Pattern.compile(regex).matcher(resolver);
                    if (m.find()) time = LocalTime.parse(m.group(0));
                    matrix.setTimeEnd(time);
                    cou++;
                    continue;
                }
                if (cou == 3) {
                    String resolver = cell.getStringCellValue();
                    resolver.replaceAll(" ", "");
                    if (resolver.length() == 4) resolver = "0" + resolver;
                    LocalTime time = null;
                    String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
                    Matcher m = Pattern.compile(regex).matcher(resolver);
                    if (m.find()) time = LocalTime.parse(m.group(0));
                    matrix.setRestTime(time);
                    cou = 0;
                    result.add(matrix);
                    continue;
                }
            }
        }
        return result;
    }

    public static ArrayList<Worker> uploadWorkersFromXLS(File xls){
        ArrayList <Worker> result = new ArrayList<>();
        InputStream inp = null;
        HSSFWorkbook wb = null;
        try {
            inp = new FileInputStream(xls);
            wb = new HSSFWorkbook(inp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        if (it.hasNext()) it.next();
        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            Worker worker = new Worker();
            int cou = 0;
            while (cells.hasNext()) {
                Cell cell = cells.next();
                if (cou == 0) {
                    Double resolver = cell.getNumericCellValue();
                    if (worker.getNumber()==0) {
                        worker.setNumber(resolver.intValue());
                        cou++;
                    }
                    continue;
                }
                if (cou == 1) {
                    String resolver = cell.getStringCellValue();
                    worker.setSurname(resolver);
                    cou++;
                    continue;
                }
                if (cou == 2) {
                    String resolver = cell.getStringCellValue();
                    worker.setName(resolver);
                    cou++;
                    continue;
                }
                if (cou==3 ){
                    String resolver = cell.getStringCellValue();
                    worker.setManagerName(resolver);
                    cou++;
                    continue;
                }
                if (cou == 4) {
                    Double resolver = cell.getNumericCellValue();
                    worker.setContractHours(resolver.intValue());
                    cou++;
                    continue;
                }
                if (cou == 5) {
                    Double resolver = cell.getNumericCellValue();
                    worker.setContractDays(resolver.intValue());
                    cou=0;
                    result.add(worker);
                    continue;
                }
            }
        }
        return result;
    }


}
