package application.sephirmobile.sephirinterface.htmlparser;

import org.jsoup.Jsoup;
import org.junit.Test;

import application.sephirmobile.sephirinterface.StringFromResourceReader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TableAmountReaderTest {
    @Test
    public void tableElement_read_correctAmount() throws Exception {
        String html = StringFromResourceReader.read("html/noten.txt");

        String amount = TableAmountReader.read(Jsoup.parse(html).selectFirst("table:nth-of-type(4)"));
        System.out.print(amount);

        assertNotEquals(amount,"Es sind keine Prüfungen erfasst.");
    }}