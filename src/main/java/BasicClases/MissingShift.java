package BasicClases;

import java.time.LocalTime;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MINUTES;

public class MissingShift implements Comparable<MissingShift> {
    LocalTime timeBegin;
    LocalTime timeEnd;
    LocalTime timeRest;
    int numberOfRepeated;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MissingShift)) return false;
        MissingShift that = (MissingShift) o;
        return Objects.equals(getTimeBegin(), that.getTimeBegin()) &&
                Objects.equals(getTimeEnd(), that.getTimeEnd()) &&
                Objects.equals(getTimeRest(), that.getTimeRest());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getTimeBegin(), getTimeEnd(), getTimeRest());
    }

    public MissingShift(LocalTime timeBegin, LocalTime timeEnd, LocalTime timeRest) {
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
        this.timeRest = timeRest;
        this.numberOfRepeated = 1;
    }

    public void setNumberOfRepeated(int numberOfRepeated) {
        this.numberOfRepeated = numberOfRepeated;
    }

    @Override
    public int compareTo(MissingShift o) {
        return (int) MINUTES.between(this.timeBegin,o.timeBegin);
    }

    public LocalTime getTimeBegin() {
        return timeBegin;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public LocalTime getTimeRest() {
        return timeRest;
    }

    public int getNumberOfRepeated() {
        return numberOfRepeated;
    }

    public void setTimeBegin(LocalTime timeBegin) {
        this.timeBegin = timeBegin;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setTimeRest(LocalTime timeRest) {
        this.timeRest = timeRest;
    }

    public void incNumberOfRepeated() {
        this.numberOfRepeated++;
    }
}
