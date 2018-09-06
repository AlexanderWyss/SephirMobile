package application.sephirmobile.sephirinterface.entitys;

import java.io.Serializable;
import java.util.List;

public class Semesters implements Serializable {
  private List<Semester> semesters;
  private Semester defaultSelected;

  public Semesters(List<Semester> semesters, Semester defaultSelected) {
    this.semesters = semesters;
    this.defaultSelected = defaultSelected;
  }

  public List<Semester> getSemesters() {
    return semesters;
  }

  /** When the Semester never got changed this is the current Semester */
  public Semester getDefaultSelected() {
    return defaultSelected;
  }

  @Override
  public String toString() {
    return "Semesters [semesters=" + semesters + ", defaultSelected=" + defaultSelected + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((defaultSelected == null) ? 0 : defaultSelected.hashCode());
    result = prime * result + ((semesters == null) ? 0 : semesters.hashCode());
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
    Semesters other = (Semesters) obj;
    if (defaultSelected == null) {
      if (other.defaultSelected != null) {
        return false;
      }
    } else if (!defaultSelected.equals(other.defaultSelected)) {
      return false;
    }
    if (semesters == null) {
        return other.semesters == null;
    } else return semesters.equals(other.semesters);
  }
}
