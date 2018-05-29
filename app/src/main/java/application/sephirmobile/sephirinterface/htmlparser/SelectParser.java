package application.sephirmobile.sephirinterface.htmlparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Element;

public class SelectParser<T> {

	private SimpleMapper<T> valueMapper;
	private SimpleMapper<T> textMapper;
	private EntityFactory<T> factory;
	private T defaultValue;
	private List<T> entitys;

	public SelectParser(SimpleMapper<T> valueMapper, SimpleMapper<T> textMapper, EntityFactory<T> factory) {
		this.valueMapper = valueMapper;
		this.textMapper = textMapper;
		this.factory = factory;
	}

	public void parse(Element select) {
		entitys = new ArrayList<>();
		Iterator<Element> options = select.select("option").iterator();
		while (options.hasNext()) {
			Element option = options.next();
			T entity = factory.build();
			valueMapper.map(entity, option.attr("value"));
			textMapper.map(entity, option.text());
			if (option.hasAttr("selected")) {
				defaultValue = entity;
			}
			entitys.add(entity);
		}
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public List<T> getEntitys() {
		return entitys;
	}
}
