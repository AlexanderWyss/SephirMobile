package application.sephirmobile.sephirinterface.htmlparser;

import org.jsoup.nodes.Element;

public class TableColumnParser<T> {

  private Parser parser;
  private Mapper<T> mapper;

  public TableColumnParser(Parser parser, Mapper<T> mapper) {
    this.parser = parser;
    this.mapper = mapper;
  }

  public String parse(Element value) {
    return parser.parse(value);
  }

  public void map(T t, String value) {
    mapper.map(t, value);
  }

  public static <T> TableColumnParserBuilder<T> builder(Class<T> clazz) {
    return new TableColumnParserBuilder<>();
  }

  public static class TableColumnParserBuilder<T> {
    public static final Parser DEFAULT_PARSER = Element::text;

    private Parser parser;
    private Mapper<T> mapper;

    private TableColumnParserBuilder() {
      defaults();
    }

    public TableColumnParserBuilder<T> defaults() {
      parser = DEFAULT_PARSER;
      mapper = (t, v) -> {
      };
      return this;
    }

    public TableColumnParserBuilder<T> parser(Parser parser) {
      this.parser = parser;
      return this;
    }

    public TableColumnParserBuilder<T> mapper(Mapper<T> mapper) {
      this.mapper = mapper;
      return this;
    }

    public TableColumnParser<T> build(Mapper<T> mapper) {
      return new TableColumnParser<>(parser, mapper);
    }

    public TableColumnParser<T> build() {
      return build(mapper);
    }
  }
}
