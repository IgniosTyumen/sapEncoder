package BasicClases;

import Controllers.UIController;
import Logic.PdfTranslate;
import StartApp.MainClass;

import java.time.LocalTime;
import java.util.Iterator;

public class WorkerPOJO {
    private Worker worker;
    private String name;
    private Integer number;
    private String surname;
    private String managerName;
    private String contractVisible;
    private Integer contractHours;
    private Integer contractDays;
    private Integer dayOffs;
    private Integer workDays;
    private Integer OTdays;
    private Integer abnormalDays;
    private Double workedMinutesBySup;
    private Double workedMinutesBySaturne;
    private String manualHours;
    private Double normalHours;

    public WorkerPOJO(Worker worker){
        this.name = worker.getName();
        this.number = worker.getNumber();
        this.surname = worker.getSurname();
        this.managerName = worker.getManagerName();
        this.contractDays = worker.getContractDays();
        this.contractHours = worker.getContractHours();
        this.contractVisible = this.contractDays.toString() + "/"+ this.contractHours.toString();
        this.dayOffs = worker.getDayOffs();
        this.workDays = worker.getWorkDays();
        this.workedMinutesBySup = calculateSapMinutes(worker)/60.0;
        this.workedMinutesBySaturne = calculateSaturneMinutes(worker)/60.0;
        this.OTdays = worker.getoTDays();
        this.abnormalDays = worker.getAbnormalDays();
        this.worker = worker;
        this.manualHours ="1111";
        this.normalHours= calculateNormalDays(worker);
    }

    public Double getNormalHours() {
        return normalHours;
    }

    public String getManualHours() {
        return manualHours;
    }

    public void setManualHours(String manualHours) {
        this.manualHours = manualHours;
    }

    private void reloadWorkerPOJO(Worker worker){
        this.worker = worker;
        this.name = worker.getName();
        this.number = worker.getNumber();
        this.surname = worker.getSurname();
        this.managerName = worker.getManagerName();
        this.contractDays = worker.getWorkDays();
        this.contractHours = worker.getContractHours();
        this.contractVisible = this.contractDays.toString() + "/"+ this.contractHours.toString();
        this.dayOffs = worker.getDayOffs();
        this.workDays = worker.getWorkDays();
        this.workedMinutesBySup = calculateSapMinutes(this.worker)/60.0;
        this.workedMinutesBySaturne = calculateSaturneMinutes(this.worker)/60.0;
        this.OTdays = worker.getoTDays();

        this.abnormalDays = worker.getAbnormalDays();

    }

    private int calculateSapMinutes(Worker worker){
        Integer result;
        if (worker.getSapMinutes()==null) {result=0;} else
        result = worker.getSapMinutes();
        for (DaySchedule day:worker.getMonthSchedule().daySchedules             ) {
       //     System.out.println(worker.getName());
//            System.out.println(worker.getNumber()+" "+worker.getSurname()+" начало подсчета часов SAP за" +day.getShiftDate().toString());
            if (day.getWorkTime() != null && day.getWorkTimeSap()!=null && day!=null && result!=null && !day.getCode().equals("ОШИБКА") && !day.getCode().equals("NOT IN SHIFT")) {
                result = result + day.getWorkTimeSap().getMinute() + day.getWorkTimeSap().getHour()*60;
            }
        }
        return result;
}

    public Integer getOTdays() {
        return OTdays;
    }

    public Integer getAbnormalDays() {
        return abnormalDays;
    }

    public Double calculateNormalDays(Worker worker){
      Double result=0.0;
      int contract = 40;
      int ot = 0;
      Double avgHrs;
      try {
          avgHrs = ((UIController.normalFor40 / 40) - ((worker.getoTDays()+worker.getAbnormalDays()) / 7.0)) * worker.getContractHours();
          if (avgHrs<0) {avgHrs=0.0;}
      } catch (NullPointerException ex){
          avgHrs = UIController.normalFor40;
      }
      result = avgHrs;
  //      System.out.println(worker.getNumber()+" "+worker.getSurname()+" высчитана норма часов сотрудника " +result);
      return result;
    }


    private int calculateSaturneMinutes(Worker worker){
        int result = worker.getSaturneMinutes();
        for (DaySchedule day:worker.getMonthSchedule().daySchedules             ) {
            if (day.getWorkTime()!=null)
            result = result + day.getWorkTime().getMinute() + day.getWorkTime().getHour()*60;
        }
    //    System.out.println(worker.getNumber()+" "+worker.getSurname()+" расчет минут сатурна " +result);
        return result;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void normalise(NormaliseMethod method) {
            Double normaliseParam = 0.0;
        if (method.equals(NormaliseMethod.NORMALIZE_BY_SATURNE)){
            normaliseParam = this.workedMinutesBySaturne;
        } else
            normaliseParam = this.normalHours;
        PdfTranslate pdfTranslate = new PdfTranslate();
        int normilizeTries = 0;
        if (!this.worker.isNormalized() && normaliseParam!=null) {
                if (normaliseParam.equals(workedMinutesBySup)) {
                    worker.setNormalized(true);
                } else {
                while (true) {
                    if (this.worker.isNormalized()) break;
                    normilizeTries++;
                    if (normilizeTries>10000){
                        this.worker.setNormalized(true);
                        break;
                    }
                    Integer difference = Math.round((float) (normaliseParam - this.workedMinutesBySup) * 60);
                    if (difference > 0) {
                        IndexOfDaySchedule inp = findHigherDay(this.worker.getMonthSchedule());
                        DaySchedule day = inp.daySchedule;
                        if (day == null) {
                            this.worker.setNormalized(true);
                            continue;
                        }
                        LocalTime searchStart = day.getBeginningTimeSap();
                        LocalTime searchEnd = day.getEndTimeSap();
                        LocalTime searchRest = day.restTimeSap;
                        if (searchStart == null || searchEnd == null || searchRest == null) {
                            this.worker.setNormalized(true);
                            continue;
                        }
                        String[] code = pdfTranslate.codeShiftRestMinus(searchStart.minusMinutes(15),searchEnd, searchRest, MainClass.getShiftMatrix());
                        if (code[0].equals("ОШИБКА") || code[0].equals(day.code)) {
                            code = pdfTranslate.codeShiftRestMinus(searchStart,searchEnd.plusMinutes(15), searchRest, MainClass.getShiftMatrix());
                        }
                        if (code[0].equals("ОШИБКА") || code[0].equals(day.code) && difference >= 60) {
                            code = pdfTranslate.codeShiftRestMinus(searchStart.minusMinutes(60), searchEnd, searchRest, MainClass.getShiftMatrix());
                            if (code[0].equals("ОШИБКА") || code[0].equals(day.code)) {
                                code = pdfTranslate.codeShiftRestMinus(searchStart, searchEnd.plusMinutes(60), searchRest, MainClass.getShiftMatrix());
                            }
                            day.canBeNormalized = false;
                        }
                        if (code[0].equals("ОШИБКА") || code[0].equals(day.code) && difference >= 45) {
                            code = pdfTranslate.codeShiftRestMinus(searchStart.minusMinutes(45), searchEnd, searchRest, MainClass.getShiftMatrix());
                            if (code[0].equals("ОШИБКА") || code[0].equals(day.code)) {
                                code = pdfTranslate.codeShiftRestMinus(searchStart, searchEnd.plusMinutes(45), searchRest, MainClass.getShiftMatrix());
                            }
                            day.canBeNormalized = false;
                        }
                        if (code[0].equals("ОШИБКА") || code[0].equals(day.code) && difference >= 30) {
                            code = pdfTranslate.codeShiftRestMinus(searchStart.minusMinutes(30), searchEnd, searchRest, MainClass.getShiftMatrix());
                            if (code[0].equals("ОШИБКА") || code[0].equals(day.code)) {
                                code = pdfTranslate.codeShiftRestMinus(searchStart, searchEnd.plusMinutes(30), searchRest, MainClass.getShiftMatrix());
                            }
                            day.canBeNormalized = false;
                        }
                        if (!code[0].equals("ОШИБКА")) {
                            day.setCode(code[0], day.shiftDate, MainClass.getShiftMatrix());
                            this.worker.getMonthSchedule().daySchedules.set(inp.index, day);
                            this.reloadWorkerPOJO(this.worker);
                        } else day.canBeNormalized = false;
                    }
                    if (difference < 0) {
                        IndexOfDaySchedule inp = findLowerDay(this.worker.getMonthSchedule());
                        DaySchedule day = inp.daySchedule;
                        if (day == null) {
                            this.worker.setNormalized(true);
                            continue;
                        }
                        LocalTime searchStart = day.getBeginningTimeSap();
                        LocalTime searchEnd = day.getEndTimeSap();
                        LocalTime searchRest = day.restTimeSap;
                        if (searchStart == null || searchEnd == null || searchRest == null) {
                            this.worker.setNormalized(true);
                            continue;
                        }
                        String[] code = pdfTranslate.codeShiftRestPlus(searchStart.plusMinutes(15), searchEnd, searchRest, MainClass.getShiftMatrix());
                        if (code[0].equals("ОШИБКА") || code[0].equals(day.code)) {
                            code = pdfTranslate.codeShiftRestPlus(searchStart, searchEnd.minusMinutes(15), searchRest, MainClass.getShiftMatrix());
                        }
                        if (code[0].equals("ОШИБКА") && difference <= -30 || code[0].equals(day.code)) {
                            code = pdfTranslate.codeShiftRestPlus(searchStart.plusMinutes(30), searchEnd, searchRest, MainClass.getShiftMatrix());
                            if (code[0].equals("ОШИБКА") || code[0].equals(day.code)) {
                                code = pdfTranslate.codeShiftRestPlus(searchStart, searchEnd.minusMinutes(30), searchRest, MainClass.getShiftMatrix());
                            }

                            day.canBeNormalized = false;
                        }
                        if (!code[0].equals("ОШИБКА")) {
                            day.setCode(code[0], day.shiftDate, MainClass.getShiftMatrix());
                            this.worker.getMonthSchedule().daySchedules.set(inp.index, day);
                            this.reloadWorkerPOJO(this.worker);
                        } else day.canBeNormalized = false;

                    }
                    workedMinutesBySup = null;
                    workedMinutesBySup = calculateSapMinutes(this.worker) / 60.0;
                    if (normaliseParam.equals(this.workedMinutesBySup)) {
                        worker.setNormalized(true);
                    }
                }
            }
        }
    }

    private IndexOfDaySchedule findLowerDay(MonthSchedule schedule){
     IndexOfDaySchedule result = new IndexOfDaySchedule();
     result.daySchedule = new DaySchedule();
     result.index = 0;
     int lower=9000;
        for (DaySchedule day:schedule.daySchedules
             ) {
            if (day.workTime==null || day.workTime.getHour()*60+day.workTime.getMinute()==0 || !day.canBeNormalized){
                day.canBeNormalized = false;
            } else {
                Integer worktime = day.workTime.getHour() * 60 + day.workTime.getMinute();
                 if (worktime<lower){
                    lower = worktime;
                    result.daySchedule = day;
                    result.index = schedule.daySchedules.indexOf(day);
                 }
            }

        }
     return  result;
    }

    private IndexOfDaySchedule findHigherDay(MonthSchedule schedule){
        IndexOfDaySchedule result = new IndexOfDaySchedule();
        result.daySchedule = new DaySchedule();
        result.index = 0;
        int lower=1;
        for (DaySchedule day:schedule.daySchedules
                ) {
            if (day.workTime==null || day.workTime.getHour()*60+day.workTime.getMinute()==0 || !day.canBeNormalized){
                day.canBeNormalized = false;
            } else {
                Integer worktime = day.workTime.getHour() * 60 + day.workTime.getMinute();
                if (worktime>lower){
                    lower = worktime;
                    result.daySchedule = day;
                    result.index = schedule.daySchedules.indexOf(day);
                }
            }

        }
        return  result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public int getContractHours() {
        return contractHours;
    }

    public void setContractHours(int contractHours) {
        this.contractHours = contractHours;
    }

    public int getContractDays() {
        return contractDays;
    }

    public void setContractDays(int contractDays) {
        this.contractDays = contractDays;
    }

    public int getDayOffs() {
        return dayOffs;
    }

    public void setDayOffs(int dayOffs) {
        this.dayOffs = dayOffs;
    }

    public int getWorkDays() {
        return workDays;
    }

    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }

    public Double getWorkedMinutesBySup() {
        return workedMinutesBySup;
    }

    public void setWorkedMinutesBySup(Double workedMinutesBySup) {
        this.workedMinutesBySup = workedMinutesBySup;
    }

    public Double getWorkedMinutesBySaturne() {
        return workedMinutesBySaturne;
    }

    public String getContractVisible() {
        return contractVisible;
    }

    public void setWorkedMinutesBySaturne(Double workedMinutesBySaturne) {
        this.workedMinutesBySaturne = workedMinutesBySaturne;
    }

    public void setNormalHours(Double normalHours) {
        this.normalHours = normalHours;
    }
}
