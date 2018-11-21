package Logic;

import BasicClases.*;
import Controllers.UIController;
import StartApp.MainClass;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.MINUTES;


public class PdfTranslate {
    private  ArrayList<ShiftMatrix> shiftMatrix = new ArrayList<>();
    private  static int cursor = 0;
    private  ArrayList<MissingShift> missingShifts = new ArrayList<>();
    private  ArrayList<RelocatedShift> relocatedShifts = new ArrayList<>();
    static WorkerSchedule current;
    int currentDay;
    static LocalDate dayOfMonday;
    File fileForDecoding;

    private static void setCursor(int cursors) {
        cursor = cursors;

    }

    private void uploadShiftMatrix(){
     shiftMatrix = MainClass.getShiftMatrix();
    }

    private void reinitialliseClass(){
        cursor=0;
        shiftMatrix = new ArrayList<>();
        missingShifts = new ArrayList<>();
        shiftMatrix = new ArrayList<>();
        currentDay = 0;
    }
    private static void  reinitialiseStaticClass(){
        current = null;
        cursor = 0;

    }

    public GeneratedSchedule translatePdf(File pdfFile, LocalDate dayOfMonday) throws IOException {
        reinitialliseClass();
        reinitialiseStaticClass();
        this.dayOfMonday = dayOfMonday;
        ArrayList<BasicClases.WorkerSchedule> workerSchedules = new ArrayList<>();
        GeneratedSchedule result = new GeneratedSchedule();
        uploadShiftMatrix();
        //try {
            PDDocument doc = PDDocument.load(pdfFile);
            PDFTextStripper s = new PDFTextStripper();
            String content = s.getText(doc);
            String[] split = content.split("\n");
            while (cursor < split.length) {
                String[] dataBlock = nextBlock(split, cursor);
                if (dataBlock != null) {
                    workerSchedules.add(translateParsedBlock(dataBlock));
                }
            }

            for (int i = 0; i < workerSchedules.size(); i++) {
                for (int j = 0; j < 7; j++) {
                    if (workerSchedules.get(i).getSheduleInDayOfWeek(j).getCode().equals("")) {
                        current = workerSchedules.get(i);
                        currentDay = j;
                        try {
                            LocalTime searchStart = workerSchedules.get(i).getSheduleInDayOfWeek(j).getBeginningTimeStr();
                            LocalTime searchFinish = workerSchedules.get(i).getSheduleInDayOfWeek(j).getEndTimeStr();
                            LocalTime searchRest = workerSchedules.get(i).getSheduleInDayOfWeek(j).getRestTime();
                            workerSchedules.get(i).getSheduleInDayOfWeek(j).setErrLog(
                                    codeShiftRest(searchStart, searchFinish
                                            , searchRest,
                                                                                        shiftMatrix));
                            if (searchRest == null || searchFinish==null || searchStart==null){
                                workerSchedules.get(i).getSheduleInDayOfWeek(j).setCode(
                                        "ОШИБКА",dayOfMonday.plusDays(j),shiftMatrix);
                            } else {
                                workerSchedules.get(i).getSheduleInDayOfWeek(j).setCode(
                                        workerSchedules.get(i).getSheduleInDayOfWeek(j).getErrLog()[0], dayOfMonday.plusDays(j),shiftMatrix);
                            }
                        } catch (Exception ex){
                            workerSchedules.get(i).getSheduleInDayOfWeek(j).setCode(
                                    "ОШИБКА", dayOfMonday.plusDays(j),shiftMatrix);
                            continue;
                        }
                    }
                }

            }
//        } catch (Exception ex) {
//        }
        result.setMissingShifts(missingShifts);
        result.setRelocatedShifts(relocatedShifts);
        result.setWorkerSchedules(workerSchedules);
doc.close();
        return result;

    }

    private static WorkerSchedule translateParsedBlock(String[] data) {
        String[] parsingMain = data[0].split(" ");
        String[] parsingTime = data[data.length - 1].split(" ");
        if (!parsingTime[0].contains("ч")) {
            parsingTime = data[data.length - 2].split(" ");
        }
        WorkerSchedule newHostess = new WorkerSchedule();
        int lines = data.length;
        //создаем очередь на время работ
        Queue<LocalTime> queue = new ArrayDeque<>();
        for (int i = 0; i < parsingTime.length - 1; i++) {
            String resolver = parsingTime[i].replace('ч', ':');
            resolver.replaceAll(" ", "");
            if (resolver.length() == 4) resolver = "0" + resolver;
            LocalTime time = null;
            String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
            Matcher m = Pattern.compile(regex).matcher(resolver);
            if (m.find()) time = LocalTime.parse(m.group(0));
            queue.add(time);
        }
        boolean opened = false;
        LocalTime search1 = null;
        LocalTime search2 = null;
        LocalTime search3 = null;
        LocalTime search4 = null;
        for (int i = 0; i < parsingMain.length; i++) {
            if (parsingMain[i].contains("[")) {
                newHostess.setNumber(parsingMain[0].substring(1, 4));
                continue;
            }
            if (parsingMain[i].equals("в")) {
                continue;
            }
            if (parsingMain[i].equals("[По")) {
                continue;
            }
            if (parsingMain[i].contains("Оплачиваемый")) {
                continue;
            }

            if (parsingMain[i].contains("Нерабоч")) {
                int day = freeday(newHostess);
                if (day >= 0) {
                    newHostess.getSheduleInDayOfWeek(day).setCode("В000", dayOfMonday.plusDays(day),MainClass.getShiftMatrix());
                    newHostess.getSheduleInDayOfWeek(day).setFinished(true);
                    continue;
                }
            }
            if (parsingMain[i].contains("беремен")) {
                int day = freeday(newHostess);
                if (day >= 0) {
                    newHostess.getSheduleInDayOfWeek(day).setCode("Р1", dayOfMonday.plusDays(day),MainClass.getShiftMatrix());
                    newHostess.getSheduleInDayOfWeek(day).setFinished(true);
                    continue;
                }
            }
            if (parsingMain[i].contains("болезни")) {
                int day = freeday(newHostess);
                if (day >= 0) {
                    newHostess.getSheduleInDayOfWeek(day).setCode("БЛ", dayOfMonday.plusDays(day),MainClass.getShiftMatrix());
                    newHostess.getSheduleInDayOfWeek(day).setFinished(true);
                    continue;
                }
            }
            if (parsingMain[i].contains("отпуск")) {
                int day = freeday(newHostess);
                if (day >= 0) {
                    newHostess.getSheduleInDayOfWeek(day).setCode("ОТ", dayOfMonday.plusDays(day),MainClass.getShiftMatrix());
                    newHostess.getSheduleInDayOfWeek(day).setFinished(true);
                    continue;
                }
            }
            if (parsingMain[i].contains("Отсутствует)")) {
                continue;
            }
            if (parsingMain[i].contains(":")) {
                int day = -1;
                if (!opened) day = unopenedDay(newHostess);
                else day = unclosedDay(newHostess);
                if (day >= 0) {
                    if (!opened) {
                        String resolver = parsingMain[i].replace('ч', ':');
                        resolver.replaceAll(" ", "");
                        if (resolver.length() == 4) resolver = "0" + resolver;
                        LocalTime time = null;
                        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
                        Matcher m = Pattern.compile(regex).matcher(resolver);
                        if (m.find()) time = LocalTime.parse(m.group(0));
                        if (newHostess.getSheduleInDayOfWeek(day).getEpisodeStart() == null) {
                            newHostess.getSheduleInDayOfWeek(day).setEpisodeStart(time);
                        }
                        if (newHostess.getSheduleInDayOfWeek(day).getBeginningTimeStr() == null) {
                            opened = true;
                            LocalTime polled = queue.poll();
                            search1 = time.plusHours(polled.getHour()).plusMinutes(polled.getMinute()).plusMinutes(BreakTime.getBreakTime());
                            search2 = time.plusHours(polled.getHour()).plusMinutes(polled.getMinute()).plusMinutes(BreakTime.getBreakTime()+15);
                            search3 = time.plusHours(polled.getHour()).plusMinutes(polled.getMinute()).plusMinutes(BreakTime.getBreakTime()-15);
                            search4 = time.plusHours(polled.getHour()).plusMinutes(polled.getMinute());
                            newHostess.getSheduleInDayOfWeek(day).setBeginningTimeStr(time);
                        }

                        continue;
                    }
                    String resolver = parsingMain[i].replace('ч', ':');
                    resolver.replaceAll(" ", "");
                    if (resolver.length() == 4) resolver = "0" + resolver;
                    LocalTime time = null;
                    String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
                    Matcher m = Pattern.compile(regex).matcher(resolver);
                    if (m.find()) time = LocalTime.parse(m.group(0));
                    if (newHostess.getSheduleInDayOfWeek(day).getEpisodeStart() == null) {
                        newHostess.getSheduleInDayOfWeek(day).setEpisodeStart(time);
                    } else if (newHostess.getSheduleInDayOfWeek(day).getEpisodeStart() != null) {
                        newHostess.getSheduleInDayOfWeek(day).setEpisodeEnd(time);
                        long workTime = MINUTES.between(
                                newHostess.getSheduleInDayOfWeek(day).getEpisodeStart(),
                                newHostess.getSheduleInDayOfWeek(day).getEpisodeEnd());
                        if (workTime < 180) {
                            newHostess.getSheduleInDayOfWeek(day).setRestTime(
                                    newHostess.getSheduleInDayOfWeek(day).getRestTime().plusMinutes(0));
                        }
                        if ((workTime >= 180) && (workTime < 360)) {
                            newHostess.getSheduleInDayOfWeek(day).setRestTime(
                                    newHostess.getSheduleInDayOfWeek(day).getRestTime().plusMinutes(15));
                        }
                        if ((workTime >= 360) && (workTime < 540)) {
                            newHostess.getSheduleInDayOfWeek(day).setRestTime(
                                    newHostess.getSheduleInDayOfWeek(day).getRestTime().plusMinutes(30));
                        }
                        if ((workTime >= 540)) {
                            newHostess.getSheduleInDayOfWeek(day).setRestTime(
                                    newHostess.getSheduleInDayOfWeek(day).getRestTime().plusMinutes(45));
                        }
                        newHostess.getSheduleInDayOfWeek(day).setEpisodeStart(null);
                        newHostess.getSheduleInDayOfWeek(day).setEpisodeEnd(null);
                    }
                    if (time.equals(search1)) {
                        opened = false;
                        newHostess.getSheduleInDayOfWeek(day).setEndTimeStr(time);
                        newHostess.getSheduleInDayOfWeek(day).setFinished(true);
                        newHostess.getSheduleInDayOfWeek(day).setRestTime(
                                newHostess.getSheduleInDayOfWeek(day).getRestTime().plusMinutes(BreakTime.getBreakTime()));

                        continue;
                    }
                    if (time.equals(search2)) {
                        opened = false;
                        newHostess.getSheduleInDayOfWeek(day).setEndTimeStr(time);
                        newHostess.getSheduleInDayOfWeek(day).setFinished(true);
                        newHostess.getSheduleInDayOfWeek(day).setRestTime(
                                newHostess.getSheduleInDayOfWeek(day).getRestTime().plusMinutes(BreakTime.getBreakTime()));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,"у хк " + newHostess.getNumber() +" "+  dayOfMonday);
                        alert.setHeaderText("Некорректно выставлен разрыв");
                        alert.show();
                        continue;
                    }
                    if (time.equals(search3)) {
                        opened = false;
                        newHostess.getSheduleInDayOfWeek(day).setEndTimeStr(time);
                        newHostess.getSheduleInDayOfWeek(day).setFinished(true);
                        newHostess.getSheduleInDayOfWeek(day).setRestTime(
                                newHostess.getSheduleInDayOfWeek(day).getRestTime().plusMinutes(BreakTime.getBreakTime()));
                       Alert alert = new Alert(Alert.AlertType.INFORMATION,"у хк " + newHostess.getNumber() +" "+  dayOfMonday);
                       alert.setHeaderText("Некорректно выставлен разрыв");
                        alert.show();
                        continue;
                    }
                    if (time.equals(search4)) {
                        opened = false;
                        newHostess.getSheduleInDayOfWeek(day).setEndTimeStr(time);
                        newHostess.getSheduleInDayOfWeek(day).setFinished(true);
                        continue;
                    }
                }
            }
        }
        return newHostess;
    }

    //парсим блок для определенной хозяйки
    private static String[] nextBlock(String[] alldata, int cursor) {
        ArrayList<String> result = new ArrayList<>();
        boolean flag = true;
        boolean inlineFlag = false;
        while (flag) {
            try {
                if (cursor > alldata.length - 1) {
                    setCursor(cursor);
                    return result.toArray(new String[0]);
                }
                if (alldata[cursor].contains("Назначение")) {
                    cursor++;
                    continue;
                }
                if (alldata[cursor].contains("Получатель")) {
                    cursor++;
                    continue;
                }
                if (alldata[cursor].contains("Страница")) {
                    cursor++;
                    continue;
                }
                if (alldata[cursor].contains("2018")) {
                    cursor++;
                    continue;
                }
                if (alldata[cursor].contains("Сумма")) {
                    cursor++;
                    continue;
                }


                if (alldata[cursor].contains("[") && (!inlineFlag)) {
                    result.add(alldata[cursor]);
                    inlineFlag = true;
                    cursor++;
                    continue;
                }
                if (!alldata[cursor].contains("[") && (inlineFlag)) {
                    result.add(alldata[cursor]);
                    cursor++;
                    continue;
                }
                if (alldata[cursor].contains("[") && (inlineFlag)) {
                    flag = false;
                    setCursor(cursor);
                }
                if (alldata[cursor].equals(-1)) {
                    flag = false;
                    setCursor(cursor);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                result.add(alldata[cursor]);
                setCursor(cursor);
                return null;
            }
        }
        return result.toArray(new String[0]);
    }

    //ищем свободный день
    private static int freeday(WorkerSchedule sch) {
        for (int i = 0; i < 7; i++) {
            if (!(sch.getSheduleInDayOfWeek(i).getFinished())) return i;
        }
        return -1;
    }

    //ищем незавершенный день
    private static int unclosedDay(WorkerSchedule sch) {
        for (int i = 0; i < 7; i++) {
            if (!(sch.getSheduleInDayOfWeek(i).getFinished()) && !(sch.getSheduleInDayOfWeek(i).getBeginningTimeStr() == null)) return i;
        }
        return -1;
    }

    private static int unopenedDay(WorkerSchedule sch) {
        for (int i = 0; i < 7; i++) {
            if (!(sch.getSheduleInDayOfWeek(i).getFinished()) && (sch.getSheduleInDayOfWeek(i).getBeginningTimeStr() == null)) return i;
        }
        return -1;
    }

       public String[] codeShiftRest(LocalTime timeStart, LocalTime endTime, LocalTime restTime, ArrayList<ShiftMatrix> stack) {
        String[] result = new String[4];
        result[0] = "ОШИБКА";
        result[1] = "TS-E";
        result[2] = "TF-E";
        result[3] = "RT-E";
        if ((timeStart == null) || (endTime == null) || (restTime == null)) {
            return result;
        }
        Iterator<ShiftMatrix> it = stack.iterator();
        while (it.hasNext()) {
            ShiftMatrix search = it.next();
            if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime) && search.getRestTime().equals(restTime)) {
                search.incSucsess();
                result[0] = search.getTimeCode();
                result[1] = "TS-OK";
                result[2] = "TF-OK";
                result[3] = "RT-OK";
                return result;
            }
        }

        missingShifts=checkMissingShift(missingShifts,new MissingShift(timeStart, endTime, restTime));
        for (int i = 1; i < 10; i++) {
            it = stack.iterator();
            while (it.hasNext()) {
                ShiftMatrix search = it.next();
                if (search.getTimeBegin().equals(timeStart.plusMinutes(i * 15)) && search.getTimeEnd().equals(endTime) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart.plusMinutes(i * 15), endTime, endTime, restTime, restTime, current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS+" + (i * 15);
                    result[2] = "TF-OK";
                    result[3] = "RT-OK";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime.plusMinutes(i * 15)) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart, endTime, endTime.plusMinutes(i * 15), restTime, restTime, current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS-OK";
                    result[2] = "TF+" + (i * 15);
                    result[3] = "RT-OK";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart.plusMinutes(i * 15)) && search.getTimeEnd().equals(endTime.plusMinutes(i * 15)) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart.plusMinutes(i * 15), endTime, endTime.plusMinutes(i * 15), restTime, restTime, current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS+" + (i * 15);
                    result[2] = "TF+" + (i * 15);
                    result[3] = "RT-OK";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart.minusMinutes(i * 15)) && search.getTimeEnd().equals(endTime) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart.minusMinutes(i * 15), endTime, endTime, restTime, restTime, current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS-" + (i * 15);
                    result[2] = "TF-OK";
                    result[3] = "RT-OK";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime.minusMinutes(i * 15)) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart, endTime, endTime.minusMinutes(i * 15), restTime, restTime, current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS-OK";
                    result[2] = "TF-" + (i * 15);
                    result[3] = "RT-OK";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart.minusMinutes(i * 15)) && search.getTimeEnd().equals(endTime.minusMinutes(i * 15)) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart.minusMinutes(i * 15), endTime, endTime.minusMinutes(i * 15), restTime, restTime, current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS-" + (i * 15);
                    result[2] = "TF-" + (i * 15);
                    result[3] = "RT-OK";
                    return result;
                }
            }
        }
        if (result[0].equals("ОШИБКА")) {
            result = codeShiftWithoutRest(timeStart, endTime, restTime, stack);
        }
        return result;
    }
    public String[] codeShiftRestPlus(LocalTime timeStart, LocalTime endTime, LocalTime restTime, ArrayList<ShiftMatrix> stack) {
        String[] result = new String[4];
        result[0] = "ОШИБКА";
        result[1] = "TS-E";
        result[2] = "TF-E";
        result[3] = "RT-E";
        if ((timeStart == null) || (endTime == null) || (restTime == null)) {
            return result;
        }
        Iterator<ShiftMatrix> it = stack.iterator();
        while (it.hasNext()) {
            ShiftMatrix search = it.next();
            if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime) && search.getRestTime().equals(restTime)) {
                search.incSucsess();
                result[0] = search.getTimeCode();
                result[1] = "TS-OK";
                result[2] = "TF-OK";
                result[3] = "RT-OK";
                return result;
            }
        }

        for (int i = 1; i < 10; i++) {
            it = stack.iterator();
            while (it.hasNext()) {
                ShiftMatrix search = it.next();
                if (search.getTimeBegin().equals(timeStart.plusMinutes(i * 15)) && search.getTimeEnd().equals(endTime) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    result[1] = "TS+" + (i * 15);
                    result[2] = "TF-OK";
                    result[3] = "RT-OK";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime.minusMinutes(i * 15)) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    result[1] = "TS-OK";
                    result[2] = "TF-" + (i * 15);
                    result[3] = "RT-OK";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart.plusMinutes(i * 15)) && search.getTimeEnd().equals(endTime.minusMinutes(i * 15)) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    result[1] = "TS+" + (i * 15);
                    result[2] = "TF-" + (i * 15);
                    result[3] = "RT-OK";
                    return result;
                }
            }
        }
        return result;
    }

    public String[] codeShiftRestMinus(LocalTime timeStart, LocalTime endTime, LocalTime restTime, ArrayList<ShiftMatrix> stack) {
        String[] result = new String[4];
        result[0] = "ОШИБКА";
        result[1] = "TS-E";
        result[2] = "TF-E";
        result[3] = "RT-E";
        if ((timeStart == null) || (endTime == null) || (restTime == null)) {
            return result;
        }
        Iterator<ShiftMatrix> it = stack.iterator();
        while (it.hasNext()) {
            ShiftMatrix search = it.next();
            if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime) && search.getRestTime().equals(restTime)) {
                search.incSucsess();
                result[0] = search.getTimeCode();
                result[1] = "TS-OK";
                result[2] = "TF-OK";
                result[3] = "RT-OK";
                return result;
            }
        }

        for (int i = 1; i < 10; i++) {
            it = stack.iterator();
            while (it.hasNext()) {
                ShiftMatrix search = it.next();
                if (search.getTimeBegin().equals(timeStart.minusMinutes(i * 15)) && search.getTimeEnd().equals(endTime) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    result[1] = "TS-" + (i * 15);
                    result[2] = "TF-OK";
                    result[3] = "RT-OK";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime.plusMinutes(i * 15)) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    result[1] = "TS-OK";
                    result[2] = "TF+" + (i * 15);
                    result[3] = "RT-OK";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart.minusMinutes(i * 15)) && search.getTimeEnd().equals(endTime.plusMinutes(i * 15)) && search.getRestTime().equals(restTime)) {
                    result[0] = search.getTimeCode();
                   search.incSucsess();
                    result[1] = "TS-" + (i * 15);
                    result[2] = "TF+" + (i * 15);
                    result[3] = "RT-OK";
                    return result;
                }
            }
        }
        return result;
    }

    private ArrayList<MissingShift> checkMissingShift(ArrayList <MissingShift> missingShifts, MissingShift missingShift){
        ArrayList <MissingShift> result = missingShifts;
        boolean sucsess = false;
        for (MissingShift ms:result) {
            if (missingShift.equals(ms)){
                ms.incNumberOfRepeated();
                return result;
            }
        }
        if (!sucsess) result.add(missingShift);
        return result;
    }
    private  String[] codeShiftWithoutRest(LocalTime timeStart, LocalTime endTime, LocalTime restTime, ArrayList<ShiftMatrix> stack) {
        String[] result = new String[4];
        result[0] = "ОШИБКА";
        result[1] = "TS-E";
        result[2] = "TF-E";
        result[3] = "RT-E";
        for (ShiftMatrix search : stack) {
            if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime)) {
                result[0] = search.getTimeCode();
                search.incSucsess();
                relocatedShifts.add(new RelocatedShift(timeStart, timeStart, endTime, endTime, restTime, search.getRestTime(), current.getNumber(), search.getTimeCode(), currentDay));
                result[1] = "TS-OK";
                result[2] = "TF-OK";
                result[3] = "RT-E";
                return result;
            }
        }
        for (int i = 1; i < 10; i++) {
            for (ShiftMatrix search : stack) {
                if (search.getTimeBegin().equals(timeStart.plusMinutes(i * 15)) && search.getTimeEnd().equals(endTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart.plusMinutes(i * 15), endTime, endTime, restTime, search.getRestTime(), current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS+" + (i * 15);
                    result[2] = "TF-OK";
                    result[3] = "RT-E";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime.plusMinutes(i * 15))) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart, endTime, endTime.plusMinutes(i * 15), restTime, search.getRestTime(), current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS-OK";
                    result[2] = "TF+" + (i * 15);
                    result[3] = "RT-E";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart.plusMinutes(i * 15)) && search.getTimeEnd().equals(endTime.plusMinutes(i * 15))) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart.plusMinutes(i * 15), endTime, endTime.plusMinutes(i * 15), restTime, search.getRestTime(), current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS+" + (i * 15);
                    result[2] = "TF+" + (i * 15);
                    result[3] = "RT-E";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart.minusMinutes(i * 15)) && search.getTimeEnd().equals(endTime)) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart.minusMinutes(i * 15), endTime, endTime, restTime, search.getRestTime(), current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS-" + (i * 15);
                    result[2] = "TF-OK";
                    result[3] = "RT-E";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart) && search.getTimeEnd().equals(endTime.minusMinutes(i * 15))) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart, endTime, endTime.minusMinutes(i * 15), restTime, search.getRestTime(), current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS-OK";
                    result[2] = "TF-" + (i * 15);
                    result[3] = "RT-E";
                    return result;
                }
                if (search.getTimeBegin().equals(timeStart.minusMinutes(i * 15)) && search.getTimeEnd().equals(endTime.minusMinutes(i * 15))) {
                    result[0] = search.getTimeCode();
                    search.incSucsess();
                    relocatedShifts.add(new RelocatedShift(timeStart, timeStart.minusMinutes(i * 15), endTime, endTime.minusMinutes(i * 15), restTime, search.getRestTime(), current.getNumber(), search.getTimeCode(), currentDay));
                    result[1] = "TS-" + (i * 15);
                    result[2] = "TF-" + (i * 15);
                    result[3] = "RT-E";
                    return result;
                }
            }
        }
        return result;
    }

}


