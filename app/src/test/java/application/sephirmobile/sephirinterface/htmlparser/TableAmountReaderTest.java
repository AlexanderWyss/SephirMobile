package application.sephirmobile.sephirinterface.htmlparser;

import org.jsoup.Jsoup;
import org.junit.Test;

import application.sephirmobile.sephirinterface.HtmlFromResourceReader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TableAmountReaderTest {
    @Test
    public void tableElement_read_correctAmount() throws Exception {
        String html = HtmlFromResourceReader.read("html/noten.txt");

        int amount = TableAmountReader.read(Jsoup.parse(html).selectFirst("table:nth-of-type(4)"));

        assertThat(amount, is(5));
    }
}
