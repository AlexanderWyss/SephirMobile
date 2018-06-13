package application.sephirmobile.sephirinterface.entitys;

import org.joda.time.LocalDate;

import java.io.Serializable;

/**
 * @author vmadmin
 */
public class AnnouncedTest implements Serializable{
    private LocalDate date;
    private String schoolClass;
    private String name;
    private String subject;
    private String text;

    public AnnouncedTest() {
    }

    public AnnouncedTest(LocalDate date, String schoolClass, String name, String subject, String text) {
        this.date = date;
        this.schoolClass = schoolClass;
        this.name = name;
        this.subject = subject;
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((schoolClass == null) ? 0 : schoolClass.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnnouncedTest other = (AnnouncedTest) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (schoolClass == null) {
            if (other.schoolClass != null)
                return false;
        } else if (!schoolClass.equals(other.schoolClass))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (text == null) {
            return other.text == null;
        } else return text.equals(other.text);
    }

    @Override
    public String toString() {
        return "AnnouncedTest [date=" + date + ", schoolClass=" + schoolClass + ", name=" + name + ", subject=" + subject
                + ", text=" + text + "]";
    }
}
