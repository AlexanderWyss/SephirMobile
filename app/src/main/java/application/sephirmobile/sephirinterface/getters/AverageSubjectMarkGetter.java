package application.sephirmobile.sephirinterface.getters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.AverageSubjectMark;
import application.sephirmobile.sephirinterface.entitys.AverageSubjectMarks;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;

public class AverageSubjectMarkGetter extends Getter {
  private static final Logger LOGGER = LoggerFactory.getLogger(AverageSubjectMarkGetter.class);
  private static final String URL = "40_notentool/noten.cfm";

  public AverageSubjectMarkGetter(SephirInterface sephirInterface) {
    super(sephirInterface);
  }

  public AverageSubjectMarks get(SchoolClass schoolClass) throws IOException {
    LOGGER.debug("Loading Average Subject Mark");
    Map<String, String> postMap = new HashMap<>();
    postMap.put("seltyp", "klasse");
    postMap.put("klasseid", schoolClass.getId());
    getSephirInterface().post(URL, postMap);

    Map<String, String> getMap = new HashMap<>();
    getMap.put("act", "kdet");
    String html = getSephirInterface().get(URL, getMap);
    return parse(html);
  }

  // TODO Rewrite disgusting Code
  AverageSubjectMarks parse(String html) {
    Document document = Jsoup.parse(html);
    Element table = document.select("table:nth-of-type(3)").first();
    int size = table.select("tr:nth-of-type(4) td:not(:last-of-type)").size();
    AverageSubjectMarks averageSubjectMarks = new AverageSubjectMarks();
    for (int i = 2; i <= size; i++) {
      Element subject = table.selectFirst(getCssQuery(1, i));
      Element mark = table.selectFirst(getCssQuery(3, i));
      Element classMark = table.selectFirst(getCssQuery(4, i));
      AverageSubjectMark averageSubjectMark = new AverageSubjectMark();
      averageSubjectMark.setSubject(subject.text());
      Matcher matcher = Pattern.compile("(\\d\\.\\d{2}) \\((\\d+)\\)").matcher(mark.text());
      if (matcher.matches()) {
        averageSubjectMark.setAverageMark(Double.parseDouble(matcher.group(1)));
        averageSubjectMark.setTestAmount(Integer.parseInt(matcher.group(2)));
      } else {
        averageSubjectMark.setAverageMark(0);
        averageSubjectMark.setTestAmount(0);
      }
      averageSubjectMark.setAverageClassMark(Double.parseDouble(classMark.text()));
      averageSubjectMarks.add(averageSubjectMark);
    }
    Element averageMark = table.selectFirst("tr:nth-of-type(3) td:last-of-type");
    averageSubjectMarks.setAverageMark(Double.parseDouble(averageMark.text()));
    LOGGER.debug("Loading successful: {}", averageSubjectMarks);
    return averageSubjectMarks;
  }

  private String getCssQuery(int index, int i) {
    return "tr:nth-of-type(" + index + ") td:nth-of-type(" + i + ")";
  }
}
