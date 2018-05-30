package application.sephirmobile.sephirinterface.getters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import application.sephirmobile.sephirinterface.HtmlFromResourceReader;
import application.sephirmobile.sephirinterface.entitys.Semester;
import application.sephirmobile.sephirinterface.entitys.Semesters;

public class SemesterGetterTest {
  @Test
  public void html_parse_correctSemesteres() throws Exception {
    String html = HtmlFromResourceReader.read("html/noten.txt");
    SemesterGetter semesterGetter = new SemesterGetter(null);

    Semesters semesteres = semesterGetter.parse(html);

    List<Semester> list = new ArrayList<>();
    list.add(new Semester("28", "SJ 2016/2017 - 2. Semester"));
    list.add(new Semester("29", "SJ 2017/2018 - 1. Semester"));
    Semester defaultValue = new Semester("30", "SJ 2017/2018 - 2. Semester");
    list.add(defaultValue);
    list.add(new Semester("31", "SJ 2018/2019 - 1. Semester"));
    assertThat(semesteres, is(new Semesters(list, defaultValue)));
  }
}
