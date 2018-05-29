package application.sephirmobile.sephirinterface.getters;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.htmlparser.SelectParser;

public class SchoolClassGetter extends Getter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SchoolClassGetter.class);
	public static final String URL = "40_notentool/noten.cfm";

	public SchoolClassGetter(SephirInterface sephirInterface) {
		super(sephirInterface);
	}

	public List<SchoolClass> get() {
		LOGGER.debug("Loading Classes");
		return parse(getSephirInterface().get(URL));
	}

	List<SchoolClass> parse(String html) {
		Document document = Jsoup.parse(html);
		Element select = document.getElementsByAttributeValue("name", "klasseid").first();
		SelectParser<SchoolClass> parser = new SelectParser<>(SchoolClass::setId, SchoolClass::setName, SchoolClass::new);
		parser.parse(select);
		List<SchoolClass> classes = parser.getEntitys();
		LOGGER.debug("Loading successful: {}", classes);
		return classes;
	}
}
