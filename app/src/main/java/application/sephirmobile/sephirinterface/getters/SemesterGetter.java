package application.sephirmobile.sephirinterface.getters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.Urls;
import application.sephirmobile.sephirinterface.entitys.Semester;
import application.sephirmobile.sephirinterface.entitys.Semesters;
import application.sephirmobile.sephirinterface.htmlparser.SelectParser;

public class SemesterGetter extends Getter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SemesterGetter.class);
    private static final String URL = Urls.NOTEN + "/noten.cfm";

    public SemesterGetter(SephirInterface sephirInterface) {
        super(sephirInterface);
    }

    public Semesters get() throws IOException {
        LOGGER.debug("Loading Semesters");
        return parse(getSephirInterface().get(URL));
    }

    Semesters parse(String html) {
        Document document = Jsoup.parse(html);
        Element select = document.getElementsByAttributeValue("name", "periodeid").first();
        SelectParser<Semester> parser = new SelectParser<>(Semester::setId, Semester::setName, Semester::new);
        parser.parse(select);
        Semesters semesters = new Semesters(parser.getEntitys(), parser.getDefaultValue());
        LOGGER.debug("Loading successful: {}", semesters);
        return semesters;
    }
}
