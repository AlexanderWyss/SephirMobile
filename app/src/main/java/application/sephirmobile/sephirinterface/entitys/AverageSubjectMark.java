package application.sephirmobile.sephirinterface.entitys;

public class AverageSubjectMark {
  private String subject;
  private double averageMark;
  private int testAmount;
  private double averageClassMark;

  public AverageSubjectMark() {
  }

  public AverageSubjectMark(String subject, double averageMark, int testAmount, double averageClassMark) {
    this.subject = subject;
    this.averageMark = averageMark;
    this.testAmount = testAmount;
    this.averageClassMark = averageClassMark;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public double getAverageMark() {
    return averageMark;
  }

  public void setAverageMark(double averageMark) {
    this.averageMark = averageMark;
  }

  public int getTestAmount() {
    return testAmount;
  }

  public void setTestAmount(int testAmount) {
    this.testAmount = testAmount;
  }

  public double getAverageClassMark() {
    return averageClassMark;
  }

  public void setAverageClassMark(double averageClassMark) {
    this.averageClassMark = averageClassMark;
  }

  @Override
  public String toString() {
    return "AverageSubjectMark [subject=" + subject + ", averageMark=" + averageMark + ", testAmount=" + testAmount
        + ", averageClassMark=" + averageClassMark + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(averageClassMark);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(averageMark);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((subject == null) ? 0 : subject.hashCode());
    result = prime * result + testAmount;
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
    AverageSubjectMark other = (AverageSubjectMark) obj;
    if (Double.doubleToLongBits(averageClassMark) != Double.doubleToLongBits(other.averageClassMark)) {
      return false;
    }
    if (Double.doubleToLongBits(averageMark) != Double.doubleToLongBits(other.averageMark)) {
      return false;
    }
    if (subject == null) {
      if (other.subject != null) {
        return false;
      }
    } else if (!subject.equals(other.subject)) {
      return false;
    }
    if (testAmount != other.testAmount) {
      return false;
    }
    return true;
  }
}
