package application.sephirmobile.sephirinterface.htmlparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableParser<T> {
  public static final String DEFAULT_DATA_DATA_SELECTOR = "td";
  public static final String DEFAULT_DATA_ROW_SELECTOR = "tr:not(:nth-child(1))";

  private List<T> entitys;

  private List<TableColumnParser<T>> columns;
  private String dataRowSelector;
  private String dataDataSelector;
  private EntityFactory<T> factory;

  public TableParser(List<TableColumnParser<T>> columns, EntityFactory<T> factory, String dataRowSelector,
      String dataDataSelector) {
    this.columns = columns;
    this.dataRowSelector = dataRowSelector;
    this.dataDataSelector = dataDataSelector;
    this.factory = factory;
  }

  public TableParser(List<TableColumnParser<T>> columns, EntityFactory<T> factory) {
    this(columns, factory, DEFAULT_DATA_ROW_SELECTOR, DEFAULT_DATA_DATA_SELECTOR);
  }

  public void parse(Element table) {
    parseData(table);
  }

  private void parseData(Element table) {
    entitys = new ArrayList<>();

    Iterator<Element> dataRows = table.select(dataRowSelector).iterator();
    while (dataRows.hasNext()) {
      Element dataRow = dataRows.next();
      Elements datas = dataRow.select(dataDataSelector);
      T entity = factory.build();
      for (int i = 0; i < datas.size(); i++) {
        Element data = datas.get(i);
        TableColumnParser<T> parser = columns.get(i);
        String value = parser.parse(data);
        parser.map(entity, value);
      }
      entitys.add(entity);
    }
  }

  public List<T> getEntitys() {
    return entitys;
  }

  public void setEntitys(List<T> entitys) {
    this.entitys = entitys;
  }
}
