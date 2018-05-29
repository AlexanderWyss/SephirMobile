package application.sephirmobile.sephirinterface.htmlparser;

import org.jsoup.nodes.Element;

public class TableAmountReader {
    private TableAmountReader() {
    }

    public static int read(Element table) {
        return Integer.parseInt(table.previousElementSibling().selectFirst("div:last-child div:last-child").text());
    }
}
