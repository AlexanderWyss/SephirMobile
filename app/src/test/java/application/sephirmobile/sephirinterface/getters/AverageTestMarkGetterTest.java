package application.sephirmobile.sephirinterface.getters;

import org.junit.Test;

import application.sephirmobile.sephirinterface.HtmlFromResourceReader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AverageTestMarkGetterTest {
    @Test
    public void html_parse_correctAvgTestMark() throws Exception {
        String html = HtmlFromResourceReader.read("html/averageTestMark.txt");

        AverageTestMarkGetter averageTestMarkGetter = new AverageTestMarkGetter(null);
        double averageMark = averageTestMarkGetter.parseAverageMark(html);

        assertThat(averageMark, is(5.51));
    }
}
