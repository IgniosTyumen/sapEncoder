package BasicClases;

import java.time.LocalTime;

public class RelocatedShift {

    LocalTime timeStart1;
    LocalTime timeStart2;
    LocalTime timeEnd1;
    LocalTime timeEnd2;
    LocalTime restTime1;
    LocalTime restTime2;
    String numberHostess;
    String codeShift;
    int day;

    public RelocatedShift(LocalTime timeStart1, LocalTime timeStart2, LocalTime timeEnd1, LocalTime timeEnd2, LocalTime restTime1, LocalTime restTime2, String numberHostess, String codeShift, int day) {
        this.timeStart1 = timeStart1;
        this.timeStart2 = timeStart2;
        this.timeEnd1 = timeEnd1;
        this.timeEnd2 = timeEnd2;
        this.restTime1 = restTime1;
        this.restTime2 = restTime2;
        this.numberHostess = numberHostess;
        this.codeShift = codeShift;
        this.day = day;
    }

    public LocalTime getTimeStart1() {
        return timeStart1;
    }

    public void setTimeStart1(LocalTime timeStart1) {
        this.timeStart1 = timeStart1;
    }

    public LocalTime getTimeStart2() {
        return timeStart2;
    }

    public void setTimeStart2(LocalTime timeStart2) {
        this.timeStart2 = timeStart2;
    }

    public LocalTime getTimeEnd1() {
        return timeEnd1;
    }

    public void setTimeEnd1(LocalTime timeEnd1) {
        this.timeEnd1 = timeEnd1;
    }

    public LocalTime getTimeEnd2() {
        return timeEnd2;
    }

    public void setTimeEnd2(LocalTime timeEnd2) {
        this.timeEnd2 = timeEnd2;
    }

    public LocalTime getRestTime1() {
        return restTime1;
    }

    public void setRestTime1(LocalTime restTime1) {
        this.restTime1 = restTime1;
    }

    public LocalTime getRestTime2() {
        return restTime2;
    }

    public void setRestTime2(LocalTime restTime2) {
        this.restTime2 = restTime2;
    }

    public String getNumberHostess() {
        return numberHostess;
    }

    public void setNumberHostess(String numberHostess) {
        this.numberHostess = numberHostess;
    }

    public String getCodeShift() {
        return codeShift;
    }

    public void setCodeShift(String codeShift) {
        this.codeShift = codeShift;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
