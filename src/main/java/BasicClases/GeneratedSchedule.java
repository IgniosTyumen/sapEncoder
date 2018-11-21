package BasicClases;

import java.util.ArrayList;
import java.util.Map;

public class GeneratedSchedule {
    private  ArrayList<MissingShift> missingShifts = new ArrayList<MissingShift>();
    private  ArrayList<RelocatedShift> relocatedShifts = new ArrayList<RelocatedShift>();
    private ArrayList<WorkerSchedule> workerSchedules = new ArrayList<WorkerSchedule>();

    public ArrayList<WorkerSchedule> getWorkerSchedules() {
        return workerSchedules;
    }

    public void setWorkerSchedules(ArrayList<WorkerSchedule> workerSchedules) {
        this.workerSchedules = workerSchedules;
    }

    public ArrayList<MissingShift> getMissingShifts() {
        return missingShifts;
    }

    public void setMissingShifts(ArrayList<MissingShift> missingShifts) {
        this.missingShifts = missingShifts;
    }

    public ArrayList<RelocatedShift> getRelocatedShifts() {
        return relocatedShifts;
    }

    public void setRelocatedShifts(ArrayList<RelocatedShift> relocatedShifts) {
        this.relocatedShifts = relocatedShifts;
    }
}
