package application.sephirmobile.sephirinterface.getters;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import application.sephirmobile.sephirinterface.StringFromResourceReader;
import application.sephirmobile.sephirinterface.entitys.AverageSubjectMark;
import application.sephirmobile.sephirinterface.entitys.AverageSubjectMarks;

public class AverageSubjectMarkGetterTest {
  @Test
  public void html_parse_correctAverageSubjectMarks() throws Exception {
    String html = StringFromResourceReader.read("html/averageSubjectMark.txt");
    AverageSubjectMarkGetter averageSubjectMarkGetter = new AverageSubjectMarkGetter(null);

    AverageSubjectMarks averageMarks = averageSubjectMarkGetter.parse(html);

    AverageSubjectMarks averageSubjectMarksShould = new AverageSubjectMarks();
    averageSubjectMarksShould.add(new AverageSubjectMark("ENG", 0.0, 0, 4.67));
    averageSubjectMarksShould.add(new AverageSubjectMark("GES", 0.0, 0, 5.14));
    averageSubjectMarksShould.add(new AverageSubjectMark("INF120", 5.65, 1, 4.95));
    averageSubjectMarksShould.add(new AverageSubjectMark("INF213", 5.4, 2, 5.12));
    averageSubjectMarksShould.add(new AverageSubjectMark("INF226B", 6, 1, 4.72));
    averageSubjectMarksShould.add(new AverageSubjectMark("INF242", 5.45, 2, 4.94));
    averageSubjectMarksShould.add(new AverageSubjectMark("NWS", 0, 0, 3.94));
    averageSubjectMarksShould.add(new AverageSubjectMark("SPK", 0, 0, 4.86));
    averageSubjectMarksShould.add(new AverageSubjectMark("SPO", 0, 0, 4.88));
    averageSubjectMarksShould.add(new AverageSubjectMark("WUR", 0, 0, 4.61));
    averageSubjectMarksShould.setAverageMark(5.6);
    assertThat(averageMarks, CoreMatchers.is(averageSubjectMarksShould));
  }
}
