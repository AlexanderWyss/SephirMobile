package application.sephirmobile.sephirinterface.htmlparser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.junit.Test;

import application.sephirmobile.sephirinterface.StringFromResourceReader;
import application.sephirmobile.sephirinterface.htmlparser.TableColumnParser.TableColumnParserBuilder;

public class TableParserTest {

  @Test
  public void htmlTable_parse_correctData() throws Exception {
    String html = StringFromResourceReader.read("html/table.txt");

    TableParser<Table> tableParser = new TableParser<>(getColumns(), Table::new);
    tableParser.parse(Jsoup.parse(html).selectFirst("#table"));

    List<Table> list = new ArrayList<>();
    list.add(new Table("Data 11", "Data 21", "Data 31"));
    list.add(new Table("Data 12", "Data 22", "Data 32"));
    assertThat(tableParser.getEntitys(), is(list));
  }

  private List<TableColumnParser<Table>> getColumns() {
    TableColumnParserBuilder<Table> builder = TableColumnParser.builder(Table.class);
    List<TableColumnParser<Table>> columns = new ArrayList<>();
    columns.add(builder.build(Table::setData1));
    columns.add(builder.build(Table::setData2));
    columns.add(builder.build(Table::setData3));
    return columns;
  }

  private class Table {
    private String data1;
    private String data2;
    private String data3;

    public Table() {
    }

    public Table(String data1, String data2, String data3) {
      this.data1 = data1;
      this.data2 = data2;
      this.data3 = data3;
    }

    public void setData1(String data1) {
      this.data1 = data1;
    }

    public void setData2(String data2) {
      this.data2 = data2;
    }

    public void setData3(String data3) {
      this.data3 = data3;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((data1 == null) ? 0 : data1.hashCode());
      result = prime * result + ((data2 == null) ? 0 : data2.hashCode());
      result = prime * result + ((data3 == null) ? 0 : data3.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Table other = (Table) obj;
      if (!getOuterType().equals(other.getOuterType()))
        return false;
      if (data1 == null) {
        if (other.data1 != null)
          return false;
      } else if (!data1.equals(other.data1))
        return false;
      if (data2 == null) {
        if (other.data2 != null)
          return false;
      } else if (!data2.equals(other.data2))
        return false;
      if (data3 == null) {
        if (other.data3 != null)
          return false;
      } else if (!data3.equals(other.data3))
        return false;
      return true;
    }

    private TableParserTest getOuterType() {
      return TableParserTest.this;
    }
  }
}
