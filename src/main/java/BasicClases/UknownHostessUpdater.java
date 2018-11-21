package BasicClases;

import java.util.ArrayList;
import java.util.Iterator;

public class UknownHostessUpdater {
    public static ArrayList<Worker> updateHostessFromWorkerSchedule(ArrayList <Worker> knownWorkers, ArrayList <WorkerSchedule> report){
        ArrayList<Worker> result = knownWorkers;
        for (WorkerSchedule current:report) {
            boolean isKnown = false;
            for (Worker worker:knownWorkers) {
                String intP = "" +worker.getNumber();
                if (intP.equals
                        (current.getNumber())) {
                    isKnown = true;
                    break;
                }

            }
            if (!isKnown) {
                Worker newWorker = new Worker();
                newWorker.setName("НеизвестноеИмя");
                newWorker.setContractDays(5);
                newWorker.setContractHours(40);
                newWorker.setManagerName("НеопределенМенеджер");
                newWorker.setSurname("НеизвестнаяФамилия");
                newWorker.setNumber(Integer.parseInt(current.number));
                result.add(newWorker);
            }
        }
        return result;
    }
}
