package application.sephirmobile.sephirinterface.htmlparser;

import org.jsoup.nodes.Element;

public class TableAmountReader {
    private TableAmountReader() {
    }

    public static String read(Element table) {
        System.out.print(table.selectFirst("tr:last-child").text());
        return table.selectFirst("tr:last-child").text();
    }
}
