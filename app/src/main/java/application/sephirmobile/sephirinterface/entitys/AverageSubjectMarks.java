package application.sephirmobile.sephirinterface.entitys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AverageSubjectMarks implements Serializable {
  private Map<String, AverageSubjectMark> marks;
  private double averageMark;

  public AverageSubjectMarks() {
    this(new HashMap<>(), 0);
  }

  public AverageSubjectMarks(Map<String, AverageSubjectMark> marks, double averageMark) {
    this.marks = marks;
    this.averageMark = averageMark;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(averageMark);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((marks == null) ? 0 : marks.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AverageSubjectMarks other = (AverageSubjectMarks) obj;
    if (Double.doubleToLongBits(averageMark) != Double.doubleToLongBits(other.averageMark)) {
      return false;
    }
    if (marks == null) {
      if (other.marks != null) {
        return false;
      }
    } else if (!marks.equals(other.marks)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "AverageSubjectMarks [marks=" + marks + ", averageMark=" + averageMark + "]";
  }
}
