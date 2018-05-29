package application.sephirmobile.sephirinterface.getters;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.htmlparser.TableAmountReader;
import application.sephirmobile.sephirinterface.htmlparser.TableColumnParser;
import application.sephirmobile.sephirinterface.htmlparser.TableColumnParser.TableColumnParserBuilder;
import application.sephirmobile.sephirinterface.htmlparser.TableParser;

public class SchoolTestGetter extends Getter {
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendDayOfMonth(2)
            .appendLiteral('.').appendMonthOfYear(2).appendLiteral('.').appendYear(4, 4).toFormatter();
    private static final Logger LOGGER = LoggerFactory.getLogger(SchoolTestGetter.class);
    private static final String URL = "40_notentool/noten.cfm";

    public SchoolTestGetter(SephirInterface sephirInterface) {
        super(sephirInterface);
    }

    public List<SchoolTest> get(SchoolClass schoolClass) {
        LOGGER.debug("Loading Tests");
        MultiValueMap<String, String> postMap = new LinkedMultiValueMap<>();
        postMap.add("seltyp", "klasse");
        postMap.add("klasseid", schoolClass.getId());
        MultiValueMap<String, String> getMap = new LinkedMultiValueMap<>();
        getMap.add("nogroup", "pruef");
        String html = getSephirInterface().post(URL, postMap, getMap);
        return parse(html);
    }

    List<SchoolTest> parse(String html) {
        Document document = Jsoup.parse(html);
        Element table = document.select(".listtab_rot").first();
        if (TableAmountReader.read(table) != 0) {
            TableParser<SchoolTest> parser = new TableParser<>(getColumns(), SchoolTest::new);
            parser.parse(table);
            List<SchoolTest> tests = parser.getEntitys();
            LOGGER.debug("Loading successful: {}", tests);
            return tests;
        }
        LOGGER.debug("No Tests");
        return new ArrayList<>();
    }

    private List<TableColumnParser<SchoolTest>> getColumns() {
        List<TableColumnParser<SchoolTest>> columns = new ArrayList<>();
        TableColumnParserBuilder<SchoolTest> builder = TableColumnParser.builder(SchoolTest.class);
        columns.add(builder
                .build((test, text) -> test.setDate(LocalDate.parse(text, FORMATTER))));
        columns.add(builder.build(SchoolTest::setSubject));
        columns.add(builder.build(SchoolTest::setName));
        columns.add(builder.build(SchoolTest::setState));
        columns.add(builder.build(SchoolTest::setType));
        columns.add(builder.build((test, text) -> test.setWeight(toDouble(text))));
        columns.add(builder.build((test, text) -> test.setMark(toDouble(text))));
        columns.add(builder.parser(element -> {
            String href = element.select("a").first().attr("href");
            return href.substring(href.indexOf('(') + 1, href.indexOf(')'));
        }).build(SchoolTest::setId));
        return columns;
    }

    private double toDouble(String text) {
        return "--".equals(text) ? 0 : Double.parseDouble(text);
    }
}
