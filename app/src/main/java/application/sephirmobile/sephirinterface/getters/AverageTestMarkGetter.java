package application.sephirmobile.sephirinterface.getters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.htmlparser.TableColumnParser;
import application.sephirmobile.sephirinterface.htmlparser.TableColumnParser.TableColumnParserBuilder;
import application.sephirmobile.sephirinterface.htmlparser.TableParser;

public class AverageTestMarkGetter extends Getter {
    public static final String URL = "40_notentool/noten.cfm";
    private static final Logger LOGGER = LoggerFactory.getLogger(AverageTestMarkGetter.class);
    private String html;

    public AverageTestMarkGetter(SephirInterface sephirInterface) {
        super(sephirInterface);
    }

    public double get(SchoolTest test) throws IOException {
        LOGGER.debug("Loading Average Test Mark");
        Map<String, String> map = new HashMap<>();
        map.put("act", "pdet");
        map.put("pruefungID", test.getId());
        html = getSephirInterface().get(URL, map);
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

    List<MarkAssignement> parseMarks(String html) {
        Document document = Jsoup.parse(html);
        List<TableColumnParser<MarkAssignement>> columns = new ArrayList<>();
        TableColumnParserBuilder<MarkAssignement> builder = TableColumnParser.builder(MarkAssignement.class);
        columns.add(builder.build(MarkAssignement::setName));
        columns.add(builder.build(MarkAssignement::setCompany));
        columns.add(builder.build(MarkAssignement::setBm));
        columns.add(builder.build((entity, value) -> entity.setMark(Double.parseDouble(value))));
        TableParser<MarkAssignement> tableParser = new TableParser<>(columns, MarkAssignement::new, "tr:nth-last-of-type(n+3)",
                "td");
        tableParser.parse(document.selectFirst("body table:nth-of-type(3)"));
        return tableParser.getEntitys();
    }

    public List<MarkAssignement> getMarks() {
        return parseMarks(html);
    }

    public class MarkAssignement {
        private String name;
        private String company;
        private String bm;
        private double mark;

        public MarkAssignement() {
        }

        public MarkAssignement(String name, String company, String bm, double mark) {
            this.name = name;
            this.company = company;
            this.bm = bm;
            this.mark = mark;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getBm() {
            return bm;
        }

        public void setBm(String bm) {
            this.bm = bm;
        }

        public double getMark() {
            return mark;
        }

        public void setMark(double mark) {
            this.mark = mark;
        }

        @Override
        public String toString() {
            return "Mark [name=" + name + ", company=" + company + ", bm=" + bm + ", mark=" + mark + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((bm == null) ? 0 : bm.hashCode());
            result = prime * result + ((company == null) ? 0 : company.hashCode());
            long temp;
            temp = Double.doubleToLongBits(mark);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            MarkAssignement other = (MarkAssignement) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (bm == null) {
                if (other.bm != null) {
                    return false;
                }
            } else if (!bm.equals(other.bm)) {
                return false;
            }
            if (company == null) {
                if (other.company != null) {
                    return false;
                }
            } else if (!company.equals(other.company)) {
                return false;
            }
            if (Double.doubleToLongBits(mark) != Double.doubleToLongBits(other.mark)) {
                return false;
            }
            if (name == null) {
                return other.name == null;
            } else return name.equals(other.name);
        }

        private AverageTestMarkGetter getOuterType() {
            return AverageTestMarkGetter.this;
        }
    }
}
