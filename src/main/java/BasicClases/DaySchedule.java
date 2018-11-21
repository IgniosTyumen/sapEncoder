package BasicClases;

import Logic.PdfTranslate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class DaySchedule {
    LocalDate shiftDate;
    LocalTime beginningTimeStr;
    LocalTime endTimeStr;
    LocalTime beginningTimeSap;
    LocalTime endTimeSap;
    LocalTime restTime;
    LocalTime restTimeSap;
    LocalTime episodeStart;
    LocalTime episodeEnd;
    String code;
    boolean finished;
    String[] errLog;
    LocalTime workTime;
    LocalTime workTimeSap;
    LocalTime PayedTime;
    boolean canBeNormalized;
    int [] workedMinutesInHour = new int[24];
  /*  LocalTime breakStart;
    LocalTime breakEnds;*/

    public boolean isCanBeNormalized() {
        return canBeNormalized;
    }

    public void setCanBeNormalized(boolean canBeNormalized) {
        this.canBeNormalized = canBeNormalized;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFinished() {
        return finished;
    }

    public LocalTime getWorkTime() {
        return workTime;
    }

    public void setWorkTime(LocalTime workTime) {
        this.workTime = workTime;
    }

    public LocalTime getWorkTimeSap() {
        return workTimeSap;
    }

    public void setWorkTimeSap(LocalTime workTimeSap) {
        this.workTimeSap = workTimeSap;
    }

    public DaySchedule() {
        this.beginningTimeStr= null;
        this.code="";
        this.finished=false;
        this.endTimeStr=null;
        restTime=LocalTime.of(0,0);
        shiftDate = null;
        errLog = new String[4];
        this.workTimeSap=null;
        canBeNormalized = true;
        this.beginningTimeSap = null;
        this.endTimeSap = null;
        this.restTimeSap = null;
        //this.breakStart = null;
        //this.breakEnds = null;
    }


    public String getCode() {
        return code;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public LocalTime getBeginningTimeStr() {
        return beginningTimeStr;
    }

    public void setBeginningTimeStr(LocalTime beginningTimeStr) {
        this.beginningTimeStr = beginningTimeStr;
    }

    public LocalTime getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(LocalTime endTimeStr) {

        this.endTimeStr = endTimeStr;
       /* for (int i=0;i<24;i++){
           LocalTime minutes = LocalTime.of(i,0);
           if (this.episodeEnd==null) {
            if (minutes.isAfter(this.beginningTimeStr)&&minutes.isBefore(this.endTimeStr)){
                Long fromBeginning= ChronoUnit.MINUTES.between(minutes,this.beginningTimeStr);
                Long fromEnd= ChronoUnit.MINUTES.between(this.endTimeStr,minutes);
                this.workedMinutesInHour[i]
            }
           }
            //this.workedMinutesInHour[i]=qwertjsdg;'qwertyuiop[]asdfghjkl;'\
        }*/
    }

    public LocalTime getRestTime() {
        return restTime;
    }

    public void setRestTime(LocalTime restTime) {
        this.restTime = restTime;
    }

    public LocalTime getEpisodeStart() {
        return episodeStart;
    }

    public void setEpisodeStart(LocalTime episodeStart) {
       // if (this.episodeStart!=null) {this.breakEnds=episodeStart;
       // this.breakStart=this.episodeEnd;}
        this.episodeStart = episodeStart;
    }

    public LocalTime getEpisodeEnd() {
        return episodeEnd;
    }

    public void setEpisodeEnd(LocalTime episodeEnd) {
        this.episodeEnd = episodeEnd;
    }

    public void setCode(String code, LocalDate date, ArrayList <ShiftMatrix> shifts) {
        if ((this.endTimeStr==null)||(this.beginningTimeStr==null) || (this.restTime==null)){
         this.workTime = LocalTime.of(0,0);
         this.restTime = LocalTime.of(0,0);
         this.workTimeSap = LocalTime.of(0,0);
        } else {
            int workMinutes = (int) ChronoUnit.MINUTES.between(this.beginningTimeStr, this.endTimeStr);
            int rest = this.restTime.getHour() * 60 + this.restTime.getMinute();
            //if (rest>45) rest=rest-45;
            int purework = workMinutes - rest;
            int hours = purework / 60;
            int minutes = purework - (hours*60);
            this.workTime = LocalTime.of(hours, minutes);
            for (ShiftMatrix matrix: shifts) {
                if (matrix.getTimeCode().equals(code)) {
                    this.workTimeSap = matrix.getTimeWork();
                    this.beginningTimeSap = matrix.timeBegin;
                    this.endTimeSap = matrix.timeEnd;
                    this.restTimeSap = matrix.restTime;
                    int restSup = restTimeSap.getHour()*60 + restTimeSap.getMinute();
//                    if (restSup>45) {
//                        restSup = restSup - 45;
//                        workTimeSap = workTimeSap.plusMinutes(restSup);
//                    }
                    break;
                }
            }
        }
        this.code = code;
        this.shiftDate = date;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String[] getErrLog() {
        return errLog;
    }

    public void setErrLog(String[] errLog) {
        this.errLog = errLog;
    }

    public LocalTime getBeginningTimeSap() {
        return beginningTimeSap;
    }

    public void setBeginningTimeSap(LocalTime beginningTimeSap) {
        this.beginningTimeSap = beginningTimeSap;
    }

    public LocalTime getEndTimeSap() {
        return endTimeSap;
    }

    public void setEndTimeSap(LocalTime endTimeSap) {
        this.endTimeSap = endTimeSap;
    }
}
