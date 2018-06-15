package application.sephirmobile.sephirinterface.getters;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.sephirinterface.StringFromResourceReader;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SchoolTestGetterTest {
    @Test
    public void html_parse_correctSchoolTests() throws Exception {
        String html = StringFromResourceReader.read("html/noten.txt");
        SchoolTestGetter schoolTestGetter = new SchoolTestGetter(null);

        List<SchoolTest> schoolTests = schoolTestGetter.parse(html);

        List<SchoolTest> list = new ArrayList<>();
        list.add(new SchoolTest(LocalDate.parse("2018-6-21"), "MA", "Transformationen von Funktionen", "Angek.",
                "Definitiv", 1.0, 0.0, "219015"));
        list.add(new SchoolTest(LocalDate.parse("2018-6-21"), "DE", "Kompaktwissen S. 113-127", "Angek.", "Definitiv", 0.5,
                0.0, "222217"));
        list.add(new SchoolTest(LocalDate.parse("2018-5-24"), "NW", "Gesamtrepetition", "Durchg.", "Definitiv", 1.0, 4.0,
                "212624"));
        assertThat(schoolTests, is(list));
    }

    @Test
    public void test_filter_correctSchoolTests() {
        SchoolTestGetter schoolTestGetter = new SchoolTestGetter(null);

        SchoolTest test1 = new SchoolTest(LocalDate.parse("2000-6-21"), "MA", "Transformationen von Funktionen", "Angek.",
                "Definitiv", 1.0, 5.0, "219015");
        SchoolTest test2 = new SchoolTest(LocalDate.parse("2000-6-21"), "DE", "Kompaktwissen S. 113-127", "Angek.", "Definitiv", 0.5,
                0.0, "222217");
        SchoolTest test3 = new SchoolTest(LocalDate.parse("2000-6-21"), "NW", "Gesamtrepetition", "Durchg.", "Definitiv", 1.0, 4.0,
                "212624");

        List<SchoolTest> allTests = new ArrayList<>();
        allTests.add(test1);
        allTests.add(test2);
        allTests.add(test3);

        List<SchoolTest> filteredTests = new ArrayList<>();
        filteredTests.add(test1);
        filteredTests.add(test3);
        assertThat(schoolTestGetter.filterToOnlyPastTests(allTests), is(filteredTests));
    }
}
