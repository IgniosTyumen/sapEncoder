package Repositories;

import BasicClases.MissingShift;

import java.util.*;

public class MissingShiftsRepo {
    static Set<MissingShift> reposSet;
    private static MissingShiftsRepo ourInstance = new MissingShiftsRepo();

    private MissingShiftsRepo() {
        reposSet = new TreeSet<>();
    }

    public static MissingShiftsRepo getInstance() {
        return ourInstance;
    }

    public void putMissingShifts(ArrayList<MissingShift> missingShift) {
        for (MissingShift o:missingShift
             ) {


            if (reposSet.contains(o)) {
                MissingShift ms = o;
                ms.incNumberOfRepeated();
                reposSet.remove(o);
                reposSet.add(ms);
            } else
                reposSet.add(o);
        }
    }

    public Set<MissingShift> getRepoAcess() {
        return reposSet;
    }
}
