package BasicClases;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class ShiftMatrix implements Comparable<ShiftMatrix>{
    LocalTime timeBegin;
    LocalTime timeEnd;
    String timeCode;
    LocalTime restTime;
    LocalTime timeWork;
    int sucsess = 0;

//    public BasicClases.ShiftMatrix(LocalTime timeBegin, LocalTime timeEnd, String timeCode) {
//        this.timeBegin = timeBegin;
//        this.timeEnd = timeEnd;
//        this.timeCode = timeCode;
//    }


    public LocalTime getTimeWork() {
        return timeWork;
    }

    public void setTimeWork(LocalTime timeWork) {
        this.timeWork = timeWork;
    }

    public int getSucsess() {
        return sucsess;
    }

    public void setSucsess(int sucsess) {
        this.sucsess = sucsess;
    }

    public void incSucsess(){
        this.sucsess++;
        int workMinutes = (int) ChronoUnit.MINUTES.between(this.timeBegin, this.timeEnd);
        int rest = this.restTime.getHour() * 60 + this.restTime.getMinute();
        int purework = workMinutes - rest;
        int hours = purework / 60;
        int minutes = purework - (hours*60);
        if (hours>=0 && minutes>=0){
        this.timeWork = LocalTime.of(hours, minutes);}
    }

    @Override
    public int compareTo(ShiftMatrix o) {
        return o.sucsess-this.sucsess;
    }

    public LocalTime getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(LocalTime timeBegin) {
        this.timeBegin = timeBegin;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

    public LocalTime getRestTime() {
        return restTime;
    }

    public void setRestTime(LocalTime restTime) {
        this.restTime = restTime;
    }

    public int getSucsessEncodes() {
        return sucsess;
    }

}
