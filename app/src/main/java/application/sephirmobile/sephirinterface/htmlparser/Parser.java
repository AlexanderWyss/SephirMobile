package application.sephirmobile.sephirinterface.htmlparser;

import org.jsoup.nodes.Element;

@FunctionalInterface
public interface Parser {

	String parse(Element element);
}
