package application.sephirmobile.sephirinterface.htmlparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Element;

public class SelectParser<T> {

	private final Mapper<T> valueMapper;
	private final Mapper<T> textMapper;
	private final EntityFactory<T> factory;
	private T defaultValue;
	private List<T> entitys;

	public SelectParser(Mapper<T> valueMapper, Mapper<T> textMapper, EntityFactory<T> factory) {
		this.valueMapper = valueMapper;
		this.textMapper = textMapper;
		this.factory = factory;
	}

	public void parse(Element select) {
		entitys = new ArrayList<>();
		for (Element option : select.select("option")) {
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
