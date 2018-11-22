package application.sephirmobile.sephirinterface.getters;

import android.graphics.Bitmap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;

public class TestChartGetter extends Getter {

    private static final String URL = "40_notentool/noten.cfm";
    private static final Logger LOGGER = LoggerFactory.getLogger(TestChartGetter.class);

    public TestChartGetter(SephirInterface sephirInterface) {
        super(sephirInterface);
    }


    public Bitmap get(SchoolTest test) throws IOException {
        LOGGER.debug("Loading Test Chart");
        Map<String, String> map = new HashMap<>();
        map.put("act", "pdet");
        map.put("pruefungID", test.getId());
        String html = getSephirInterface().get(URL, map);
        return parse(html);
    }

    Bitmap parse(String html) throws IOException {
        Document document = Jsoup.parse(html);
        Elements img = document.select("img.chart");
        String src = img.attr("src");
        return getSephirInterface().downloadImage(src);
    }
}
