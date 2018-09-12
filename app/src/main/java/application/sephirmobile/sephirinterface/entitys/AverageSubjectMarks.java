package application.sephirmobile.sephirinterface.entitys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AverageSubjectMarks implements Serializable {
    private final Map<String, AverageSubjectMark> marks;
    private SchoolClass schoolClass;
    private double averageMark;

    public AverageSubjectMarks() {
        this(new HashMap<>(), null);
    }

    private AverageSubjectMarks(Map<String, AverageSubjectMark> marks, SchoolClass schoolClass) {
        this.marks = marks;
        this.schoolClass = schoolClass;
    }

    public AverageSubjectMark get(String subject) {
        return marks.get(subject);
    }

    public void add(AverageSubjectMark mark) {
        marks.put(mark.getSubject(), mark);
    }

    public List<AverageSubjectMark> getAsList() {
        return new ArrayList<>(marks.values());
    }

    public Map<String, AverageSubjectMark> getAsMap() {
        return marks;
    }

    public double getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(double averageMark) {
        this.averageMark = averageMark;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AverageSubjectMarks that = (AverageSubjectMarks) o;
        return Double.compare(that.averageMark, averageMark) == 0 &&
                Objects.equals(marks, that.marks) &&
                Objects.equals(schoolClass, that.schoolClass);
    }

    @Override
    public int hashCode() {

        return Objects.hash(marks, schoolClass, averageMark);
    }

    @Override
    public String toString() {
        return "AverageSubjectMarks{" +
                "marks=" + marks +
                ", schoolClass=" + schoolClass +
                ", averageMark=" + averageMark +
                '}';
    }
}
