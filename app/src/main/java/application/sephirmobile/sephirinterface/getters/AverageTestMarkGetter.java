package application.sephirmobile.sephirinterface.getters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.Urls;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;

public class AverageTestMarkGetter extends Getter {
    private static final String URL = Urls.NOTEN + "/noten.cfm";
    private static final Logger LOGGER = LoggerFactory.getLogger(AverageTestMarkGetter.class);

    public AverageTestMarkGetter(SephirInterface sephirInterface) {
        super(sephirInterface);
    }

    public double get(SchoolTest test) throws IOException {
        LOGGER.debug("Loading Average Test Mark");
        Map<String, String> map = new HashMap<>();
        map.put("act", "pdet");
        map.put("pruefungID", test.getId());
        String html = getSephirInterface().get(URL, map);
        return parseAverageMark(html);
    }

    double parseAverageMark(String html) {
        Document document = Jsoup.parse(html);
        Elements data = document.select("body table:nth-of-type(3) tr:last-of-type td:nth-of-type(2)");
        if (data.isEmpty()) {
            return 0;
        }
        double averageMark = Double.parseDouble(data.text());
        LOGGER.debug("Loading successful: {}", averageMark);
        return averageMark;
    }
}
