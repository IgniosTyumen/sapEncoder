package BasicClases;

import BasicClases.DaySchedule;

public class WorkerSchedule {
    String number;
    DaySchedule[] schedule= {new DaySchedule(),new DaySchedule(),new DaySchedule(),new DaySchedule(),new DaySchedule(),new DaySchedule(),new DaySchedule()};

    public String getNumber() {
        return number;
    }
    public DaySchedule getSheduleInDayOfWeek(int dayOfWeek){
     DaySchedule result;
        try {
         result = this.schedule[dayOfWeek];
     } catch (ArrayIndexOutOfBoundsException ex){
            return null;
        }
      return result;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public DaySchedule[] getSchedule() {
        return schedule;
    }

    public void setSchedule(DaySchedule[] schedule) {
        this.schedule = schedule;
    }
}
