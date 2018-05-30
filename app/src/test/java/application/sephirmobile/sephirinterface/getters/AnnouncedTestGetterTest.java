package application.sephirmobile.sephirinterface.getters;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.sephirinterface.StringFromResourceReader;
import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AnnouncedTestGetterTest {
    @Test
    public void html_parse_correctAnnouncedTests() throws Exception {
        String html = StringFromResourceReader.read("html/announcedTests.txt");

        AnnouncedTestGetter announcedTestGetter = new AnnouncedTestGetter(null);
        List<AnnouncedTest> announcedTests = announcedTestGetter.parse(html);

        List<AnnouncedTest> list = new ArrayList<>();
        list.add(new AnnouncedTest(LocalDate.parse("2018-06-01"), "S-INF16d", "Programmierung (Block 3)", "INF242",
                "Geprüft werden die im Unterrichtsblock 3 erarbeiteten und angewendeten Grundlagen der Mikroprozessorprogrammierung!"));
        list.add(new AnnouncedTest(LocalDate.parse("2018-06-07"), "S-INF16d", "Presentations: Trevor Noah - Born a crime",
                "ENG", ""));
        list.add(new AnnouncedTest(LocalDate.parse("2018-06-07"), "S-INF16d", "Wärmelehre (Block 3)", "NWS",
                "Geprüft werden die im Unterrichtsblock 3 erarbeiteten und angewendeten Grundlagen der Wärmelehre!"));
        assertThat(announcedTests, is(list));
    }
}
