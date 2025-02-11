package application.sephirmobile.sephirinterface.getters;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.Urls;
import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;
import application.sephirmobile.sephirinterface.htmlparser.TableAmountReader;
import application.sephirmobile.sephirinterface.htmlparser.TableColumnParser;
import application.sephirmobile.sephirinterface.htmlparser.TableColumnParser.TableColumnParserBuilder;
import application.sephirmobile.sephirinterface.htmlparser.TableParser;

public class AnnouncedTestGetter extends Getter {
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendDayOfMonth(2)
            .appendLiteral('.').appendMonthOfYear(2).appendLiteral('.').appendYear(4, 4).toFormatter();
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncedTestGetter.class);
    private static final String URL = Urls.NOTEN + "/angekuendigt.cfm";

    public AnnouncedTestGetter(SephirInterface sephirInterface) {
        super(sephirInterface);
    }

    public List<AnnouncedTest> get() throws IOException {
        LOGGER.debug("Loading Announced Tests");
        Map<String, String> getMap = new HashMap<>();
        getMap.put("group", "dat");
        String html = getSephirInterface().get(URL, getMap);
        return parse(html);
    }

    List<AnnouncedTest> parse(String html) {
        Document document = Jsoup.parse(html);
        Element table = document.select(".listtab_rot").first();
        if (!"Es sind keine Prüfungen erfasst.".equals(TableAmountReader.read(table))) {
            TableParser<AnnouncedTest> parser = new TableParser<>(getColumns(), AnnouncedTest::new);
            parser.parse(table);
            List<AnnouncedTest> tests = parser.getEntitys();
            LOGGER.debug("Loading successful: {}", tests);
            return tests;
        }
        LOGGER.debug("No Announced Tests");
        return new ArrayList<>();
    }

    private List<TableColumnParser<AnnouncedTest>> getColumns() {
        List<TableColumnParser<AnnouncedTest>> columns = new ArrayList<>();
        TableColumnParserBuilder<AnnouncedTest> builder = TableColumnParser.builder(AnnouncedTest.class);
        columns.add(builder
                .build((test, text) -> test.setDate(LocalDate.parse(text, FORMATTER))));
        columns.add(builder.build(AnnouncedTest::setSchoolClass));
        columns.add(builder.build(AnnouncedTest::setName));
        columns.add(builder.build(AnnouncedTest::setSubject));
        columns.add(builder.build((entity, text) -> entity.setText(text.substring(text.indexOf(':') + 1).trim())));
        return columns;
    }
}
