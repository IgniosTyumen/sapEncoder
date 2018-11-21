package Reports;

import BasicClases.*;
import Controllers.UIController;
import Repositories.MissingShiftsRepo;
import StartApp.MainClass;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Reports {

    public static void saveBusySausage(File file, ObservableList<WorkerPOJO> print) throws IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("hh:mm");

        for (int i = 1; i <= MainClass.getDaysInMonth() ; i++) {
            HSSFSheet sheet = workbook.createSheet(i + " of "+UIController.getMonth());
            int rownum = 0;
            Cell cell;
            Row row;
            row = sheet.createRow(rownum);
            cell = row.createCell(0);
            cell.setCellValue("# хозяйки");
            cell = row.createCell(1);
            cell.setCellValue("Имя хозяйки");
            for (int j=0; j<73;j++){
                sheet.setColumnWidth(cell.getColumnIndex(),500);
                cell = row.createCell(j+2);
                LocalTime filler = LocalTime.of(6,0);
                filler = filler.plusMinutes(j*15);
                cell.setCellValue(filler.toString());

            }
//            for (WorkerPOJO workerPOJO: print){
//
//            }
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
    }

    public static void saveDayStatistic(File file, ObservableList<WorkerPOJO> print) throws IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("HoursStatistic");
        int rownum = 0;
        Cell cell;
        Row row;
        HSSFCellStyle styleError = workbook.createCellStyle();

        row = sheet.createRow(rownum);
        cell = row.createCell(0);
        cell.setCellValue("День");
        cell = row.createCell(1);
        cell.setCellValue("Всего часов по сатурну");
        cell = row.createCell(2);
        cell.setCellValue("Часов 6-8");
        cell = row.createCell(3);
        cell.setCellValue("Часов 8-12");
        cell = row.createCell(4);
        cell.setCellValue("Часов 12-15");
        cell = row.createCell(5);
        cell.setCellValue("Часов 15-17");
        cell = row.createCell(6);
        cell.setCellValue("Часов 17-22");
        cell = row.createCell(7);
        cell.setCellValue("Часов 22-24");
        for (int i = 6; i <25 ; i++) {
            cell = row.createCell(7+i-5);
            cell.setCellValue("Час "+i);
        }
        Integer minutes = 0;

    }

    public static void saveEncodedCodesToXLS(File file, ObservableList<WorkerPOJO> print) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("CodeShift");
        int rownum = 0;
        Cell cell;
        Row row;
        HSSFCellStyle styleError = workbook.createCellStyle();
        styleError.setFillBackgroundColor(HSSFColor.RED.index);

        row = sheet.createRow(rownum);
        cell = row.createCell(0);
        cell.setCellValue("# хозяйки");
        cell = row.createCell(1);
        cell.setCellValue("Имя хозяйки");
        cell = row.createCell(2);
        cell.setCellValue("Менеджер");
        cell = row.createCell(3);
        cell.setCellValue("Часов САП");
        cell = row.createCell(4);
        cell.setCellValue("Часов Сатурн");
        cell = row.createCell(5);
        cell.setCellValue("Часов нормы");
        for (int i = 1; i <= MainClass.getDaysInMonth() ; i++) {
            cell = row.createCell(i+5);
            cell.setCellValue(i + "/"+UIController.getMonth());
        }
        //необходимо добавить подгрузку имен хозяек
        for (WorkerPOJO o : print) {
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0);
            cell.setCellValue(o.getNumber());
            cell = row.createCell(1);
            cell.setCellValue(o.getSurname()+ " "+o.getName());
            cell = row.createCell(2);
            cell.setCellValue(o.getManagerName());
            cell = row.createCell(3);
            cell.setCellValue(o.getWorkedMinutesBySup());
            cell = row.createCell(4);
            cell.setCellValue(o.getWorkedMinutesBySaturne());
            cell = row.createCell(5);
            cell.setCellValue(o.getNormalHours());
            int day = 1;
            for (DaySchedule daySchedule: o.getWorker().getMonthSchedule().getDaySchedules()){
                cell = row.createCell(day+5);
                String out = daySchedule.getCode();
                cell.setCellValue(out);
                if (out.equals("ОШИБКА")) cell.setCellStyle(styleError);
                day++;
            }

        }
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
    }

    public static void saveAbsent(File file, ObservableList<WorkerPOJO> print) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Отсутствия");
        Integer rownum = 0;
        Cell cell;
        Row row;
        HSSFCellStyle styleError = workbook.createCellStyle();
        styleError.setFillBackgroundColor(HSSFColor.RED.index);

        row = sheet.createRow(rownum);
        cell = row.createCell(0);
        cell.setCellValue("# хозяйки");
        cell = row.createCell(1);
        cell.setCellValue("Имя хозяйки");
        cell = row.createCell(2);
        cell.setCellValue("Менеджер");
        cell = row.createCell(3);
        cell.setCellValue("Выходные дни");
        cell = row.createCell(4);
        cell.setCellValue("Отработано дней");
        cell = row.createCell(5);
        cell.setCellValue("Отпускные дни");
        for (int i = 1; i <= MainClass.getDaysInMonth() ; i++) {
            cell = row.createCell(i+5);
            cell.setCellValue(i+"/"+(UIController.datePicked.getMonth()));
        }
        //необходимо добавить подгрузку имен хозяек
        for (WorkerPOJO o : print) {
            rownum++;
            Integer rw = rownum+1;
            row = sheet.createRow(rownum);
            cell = row.createCell(0);
            cell.setCellValue(o.getNumber());
            cell = row.createCell(1);
            cell.setCellValue(o.getSurname()+" "+o.getName());
            cell = row.createCell(2);
            cell.setCellValue(o.getManagerName());
            cell = row.createCell(3);
            cell.setCellFormula("COUNTIF("
                    +getAlphabetColumnIndex(6)
                    +(rw).toString()+":"
                    +getAlphabetColumnIndex(MainClass.getDaysInMonth()+6)
                    +(rw).toString()+","
                    +" \"В\")");
            cell = row.createCell(4);
            cell.setCellFormula("COUNTIF("
                    +getAlphabetColumnIndex(6)
                    +(rw).toString()+":"
                    +getAlphabetColumnIndex(MainClass.getDaysInMonth()+6)
                    +(rw).toString()+","
                    +" \"x\")");
            cell = row.createCell(5);
            cell.setCellFormula("COUNTIF("
                    +getAlphabetColumnIndex(6)
                    +(rw).toString()+":"
                    +getAlphabetColumnIndex(MainClass.getDaysInMonth()+6)
                    +(rw).toString()+","
                    +" \"ОТ\")");cell.setCellValue(o.getOTdays());
            int day = 1;
            for (DaySchedule daySchedule: o.getWorker().getMonthSchedule().getDaySchedules()){
                cell = row.createCell(day+5);
                String out = daySchedule.getCode();
                if (out.equals("ОТ")) {
                    cell.setCellValue(out);
                }
                if (out.equals("В000")) {
                    cell.setCellValue("В");
                }
                if (out.equals("ОШИБКА"))
                    cell.setCellValue(out);
                day++;
            }

        }
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
    }

    /*   static CellStyle errorStyle(HSSFWorkbook workbook){
        {
            // Font
            HSSFFont font = workbook.createFont();
            font.setBoldweight((short)1);
            font.setItalic(true);


            // Font Color
            font.setColor(IndexedColors.RED.index);

            // Style
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);

            return style;
        }
    }

    static CellStyle offStyle(HSSFWorkbook workbook){
        {
            // Font
            HSSFFont font = workbook.createFont();
            font.setBoldweight((short)1);


            // Font Color
            font.setColor(IndexedColors.LIGHT_GREEN.index);


            // Style
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setFillBackgroundColor(IndexedColors.AQUA.index);
            return style;
        }
    }

    static CellStyle oTStyle(HSSFWorkbook workbook){
        {
            // Font
            HSSFFont font = workbook.createFont();
            font.setBoldweight((short)1);


            // Font Color
            font.setColor(IndexedColors.BLUE.index);

            // Style
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);

            return style;
        }
    }
*/
    public static void saveMissingShifts(File file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("MissingShifts");
        for (int i = 0; i < MainClass.getWeeksCount(); i++) {
            MissingShiftsRepo.getInstance().putMissingShifts(MainClass.getAllWeeksSchedules().get(i).getMissingShifts());

        }
        int rownum = 0;
        Cell cell;
        Row row;
        row = sheet.createRow(rownum);
        cell = row.createCell(0);
        cell.setCellValue("Начало смены");
        cell = row.createCell(1);
        cell.setCellValue("Окончание смены");
        cell = row.createCell(2);
        cell.setCellValue("Длительность перерывов");
        cell = row.createCell(3);
        cell.setCellValue("Количество повторений");

        for (MissingShift o : MissingShiftsRepo.getInstance().getRepoAcess()) {
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0);
            cell.setCellValue(o.getTimeBegin().toString());
            cell = row.createCell(1);
            cell.setCellValue(o.getTimeEnd().toString());
            cell = row.createCell(2);
            cell.setCellValue(o.getTimeRest().toString());
            cell = row.createCell(3);
            cell.setCellValue(o.getNumberOfRepeated());
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
    }

    static void saveShiftStatistic(ArrayList<ShiftMatrix> shiftMatrices) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("ShiftStatistic");
        int rownum = 0;
        Cell cell;
        Row row;
        for (ShiftMatrix o : shiftMatrices) {
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0);
            cell.setCellValue(o.getTimeCode());
            cell = row.createCell(1);
            cell.setCellValue(o.getSucsessEncodes());
        }
        File file = new File("ShiftStatistic.xls");
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
    }

//    static void saveDayPerspective(ArrayList<WorkerSchedule> print) throws IOException {
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("ErrLog");
//        HSSFCellStyle style = workbook.createCellStyle();
//        style.setFillBackgroundColor((short)1);
//
//        int rownum = 0;
//        Cell cell;
//        Row row;
//        for (WorkerSchedule o : print) {
//            rownum++;
//            row = sheet.createRow(rownum);
//            cell = row.createCell(0);
//            cell.setCellValue(o.number);
//            cell = row.createCell(1);
//            cell.setCellValue(o.schedule[0].errLog[1]);
//            cell = row.createCell(2);
//            cell.setCellValue(o.schedule[1].errLog[1]);
//            cell = row.createCell(3);
//            cell.setCellValue(o.schedule[2].errLog[1]);
//            cell = row.createCell(4);
//            cell.setCellValue(o.schedule[3].errLog[1]);
//            cell = row.createCell(5);
//            cell.setCellValue(o.schedule[4].errLog[1]);
//            cell = row.createCell(6);
//            cell.setCellValue(o.schedule[5].errLog[1]);
//            cell = row.createCell(7);
//            cell.setCellValue(o.schedule[6].errLog[1]);
//
//        }
//        File file = new File("DayZ.xls");
//        FileOutputStream outputStream = new FileOutputStream(file);
//        workbook.write(outputStream);
//        outputStream.close();
//    }

    static void saveMovedShiftStatistic(ArrayList <RelocatedShift> relocatedShifts) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("MovedShifts");
        int rownum = 0;
        Cell cell;
        Row row;
        row = sheet.createRow(rownum);
        cell = row.createCell(0);
        cell.setCellValue("# хозяйки");
        cell = row.createCell(1);
        cell.setCellValue("День недели");
        cell = row.createCell(2);
        cell.setCellValue("Найденная смена");
        cell = row.createCell(3);
        cell.setCellValue("Начало смены");
        cell = row.createCell(4);
        cell.setCellValue("Начало найденной смены");
        cell = row.createCell(5);
        cell.setCellValue("Конец смены");
        cell = row.createCell(6);
        cell.setCellValue("Конец найденной смены");
        cell = row.createCell(7);
        cell.setCellValue("Перерывы");
        cell = row.createCell(8);
        cell.setCellValue("Перерывы найденной смены");
        for (RelocatedShift o : relocatedShifts) {
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0);
            cell.setCellValue(o.getNumberHostess());
            cell = row.createCell(1);
            switch (o.getDay()){
                case 0 : {
                    cell.setCellValue("Понедельник");
                    break;
                }
                case 1 : {
                    cell.setCellValue("Вторник");
                    break;
                }
                case 2 : {
                    cell.setCellValue("Среда");
                    break;
                }
                case 3 : {
                    cell.setCellValue("Четверг");
                    break;
                }
                case 4 : {
                    cell.setCellValue("Пятница");
                    break;
                }
                case 5 : {
                    cell.setCellValue("Суббота");
                    break;
                }
                case 6 : {
                    cell.setCellValue("Воскресенье");
                    break;
                }
                default: {
                    cell.setCellValue("ОШИБКА");
                    break;
                }
            }
            cell = row.createCell(2);
            cell.setCellValue(o.getCodeShift());
            cell = row.createCell(3);
            cell.setCellValue(o.getTimeStart1().toString());
            cell = row.createCell(4);
            cell.setCellValue(o.getTimeStart2().toString());
            cell = row.createCell(5);
            cell.setCellValue(o.getTimeEnd1().toString());
            cell = row.createCell(6);
            cell.setCellValue(o.getTimeEnd2().toString());
            cell = row.createCell(7);
            cell.setCellValue(o.getRestTime1().toString());
            cell = row.createCell(8);
            cell.setCellValue(o.getRestTime2().toString());
        }
        File file = new File("MovedShifts.xls");
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
    }

    static String getAlphabetColumnIndex(int index){
        String result= CellReference.convertNumToColString(index);
        return result;
    }
}
