package BasicClases;

import Controllers.UIController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

public class Worker {
    private String name;
    private Integer number;
    private MonthSchedule monthSchedule;
    private String surname;
    private String managerName;
    private Integer contractHours;
    private Integer contractDays;
    private Integer dayOffs;
    private Integer workDays;
    private Integer abnormalDays;
    private Integer oTDays;
    boolean normalized;

    public boolean isNormalized() {
        return normalized;
    }
    public void setNormalizes(boolean normalizedStatusBoolean){
        this.normalized = normalizedStatusBoolean;
    }

    public void normaliseHours(){

    }

    public void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }

    public int getoTDays() {
        return oTDays;
    }

    public void setoTDays(int oTDays) {
        this.oTDays = oTDays;
    }

    public int getAbnormalDays() {
        return abnormalDays;
    }

    public void setAbnormalDays(int abnormalDays) {
        this.abnormalDays = abnormalDays;
    }

    private int sapMinutes;
    private int saturneMinutes;

    public Integer getSapMinutes() {
        return sapMinutes;
    }

    public void setSapMinutes(int sapMinutes) {
        this.sapMinutes = sapMinutes;
    }

    public int getSaturneMinutes() {
        return saturneMinutes;
    }

    public void setSaturneMinutes(int saturneMinutes) {
        this.saturneMinutes = saturneMinutes;
    }

    public Worker(){
        this.name = "";
        this.number = 0;
        this.surname ="";
        this.managerName = "";
        this.contractHours = 0;
        this.contractDays = 0;
        this.dayOffs = 0;
        this.workDays = 0;
        this.sapMinutes = 0;
        this.saturneMinutes = 0;
        this.oTDays = 0;
    this.abnormalDays =0;
    }

    private void incDayOffs(){
        this.dayOffs++;
    }

    private void  incOtDays(){
        this.oTDays++;
    }

    private void incWorkDays(){
        this.workDays++;
    }

    private void incAbnormalDays(){
        this.abnormalDays++;
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

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", monthSchedule=" + monthSchedule +
                ", surname='" + surname + '\'' +
                ", managerName='" + managerName + '\'' +
                ", contractHours=" + contractHours +
                ", contractDays=" + contractDays +
                ", dayOffs=" + dayOffs +
                ", workDays=" + workDays +
                '}';
    }

    public void setUpFromGeneratedSchedules (ArrayList<GeneratedSchedule> generatedSchedule, int startDay, int endDay, int numberOfWeeks){
        Iterator it = generatedSchedule.iterator();
        numberOfWeeks--;
        this.monthSchedule = new MonthSchedule();
        int numberOfWeek = 0;
        this.monthSchedule.daySchedules = new ArrayList<>();
        while (it.hasNext()){
            GeneratedSchedule activeSchedule = (GeneratedSchedule) it.next();
            ArrayList<WorkerSchedule> currentWeek = activeSchedule.getWorkerSchedules();
            WorkerSchedule searchableWeek = findWorkerSchedule(this.number,currentWeek);
            //this.monthSchedule.daySchedules = new ArrayList<>();
            if (numberOfWeek==numberOfWeeks){
                for (int i = 0; i < endDay; i++) {
                    this.monthSchedule.daySchedules.add(setDayFromSchedule(i,searchableWeek));
                    Integer worked = 0;
                    if (setDayFromSchedule(i,searchableWeek).workTime!=null)
                        worked = setDayFromSchedule(i,searchableWeek).workTime.getHour()*60 + setDayFromSchedule(i,searchableWeek).workTime.getMinute();
                    if (worked>0){
                        incWorkDays();
                    } else
                    if (setDayFromSchedule(i,searchableWeek).getCode().equals("В000")){
                        incDayOffs();
                    } else
                    if (setDayFromSchedule(i,searchableWeek).getCode().equals("ОТ")){
                        incOtDays();
                    }
                    else incAbnormalDays();
                 //   System.out.println(numberOfWeek);
                    continue;
                }
            } else if (numberOfWeek==0) {
                for (int i = startDay-1; i < 7; i++) {
                    this.monthSchedule.daySchedules.add(setDayFromSchedule(i,searchableWeek));
                    Integer worked = 0;
                    if (setDayFromSchedule(i,searchableWeek).workTime!=null)
                    worked = setDayFromSchedule(i,searchableWeek).workTime.getHour()*60 + setDayFromSchedule(i,searchableWeek).workTime.getMinute();
                    if (worked>0){
                        incWorkDays();
                    } else
                        if (setDayFromSchedule(i,searchableWeek).getCode().equals("В000")){
                        incDayOffs();
                        } else
                        if (setDayFromSchedule(i,searchableWeek).getCode().equals("ОТ")){
                            incOtDays();
                        } else
                            incAbnormalDays();
                }
                numberOfWeek++;
                System.out.println(numberOfWeek);

            }
             else if (numberOfWeek!=numberOfWeeks){
                for (int i = 0; i < 7; i++) {
                    this.monthSchedule.daySchedules.add(setDayFromSchedule(i,searchableWeek));
                    Integer worked = 0;
                    if (setDayFromSchedule(i,searchableWeek).workTime!=null)
                    worked = setDayFromSchedule(i,searchableWeek).workTime.getHour()*60 + setDayFromSchedule(i,searchableWeek).workTime.getMinute();
                    if (worked>0){
                        incWorkDays();
                    } else
                    if (setDayFromSchedule(i,searchableWeek).getCode().equals("В000")){
                        incDayOffs();
                    } else
                                         if (setDayFromSchedule(i,searchableWeek).getCode().equals("ОТ")){
                        incOtDays();
                    }else incAbnormalDays();

                }
                numberOfWeek++;
                System.out.println(numberOfWeek);
            }
        }

    }

    private static WorkerSchedule findWorkerSchedule(int numberOfHostess, ArrayList<WorkerSchedule> schedules){
        WorkerSchedule result = null;
        Iterator it = schedules.iterator();
        while (it.hasNext()){
            WorkerSchedule next = (WorkerSchedule) it.next();
            if (next.getNumber().equals(Integer.toString(numberOfHostess))) {
                result = next;
                return result;
            }
        }
        return result;
    }

    private DaySchedule setDayFromSchedule(int day, WorkerSchedule scheduleForSearch){
        DaySchedule result = null;
        try {
            result = scheduleForSearch.getSheduleInDayOfWeek(day);
        } catch (NullPointerException ex){
           result = new DaySchedule();
           LocalTime lt = LocalTime.of(0,0);
           result.setBeginningTimeStr(lt);
           result.setCode("NOT_IN_SCHEDULE");
           result.setFinished(true);
           result.setEndTimeStr(lt);
           result.setRestTime(lt);
           result.setWorkTimeSap(lt);
           result.setErrLog(new String[4]);
           result.setCanBeNormalized(false);
           result.setBeginningTimeSap(lt);
           result.setEndTimeSap(lt);
           result.setRestTime(lt);
        }
        return result;
    }

    public MonthSchedule getMonthSchedule() {
        return monthSchedule;
    }

    public void setMonthSchedule(MonthSchedule monthSchedule) {
        this.monthSchedule = monthSchedule;
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
}
