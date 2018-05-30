package application.sephirmobile.sephirinterface.getters;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.Certification;

public class CertificationGetter extends Getter {
	private static final Logger LOGGER = LoggerFactory.getLogger(CertificationGetter.class);
	private static final String URL = "index.cfm";
	private Pattern pattern = Pattern.compile("cfid=(.+)&cftoken=(.+)");

	public CertificationGetter(SephirInterface sephirInterface) {
		super(sephirInterface);
	}

	public Certification get() throws IOException {
		LOGGER.debug("Loading Certification");
		SephirInterface sephirInterface = getSephirInterface();
		String html = sephirInterface.get(URL);
		return parse(html);
	}

	Certification parse(String html) {
		Document document = Jsoup.parse(html);
		Element mainFrame = document.getElementById("mainFrame");
		String src = mainFrame.attr("src");

		Matcher matcher = pattern.matcher(src);
		matcher.find();
		String cfid = matcher.group(1);
		String cftoken = matcher.group(2);
		Certification certification = new Certification(cfid, cftoken);
		LOGGER.debug("Loading successful: {}", certification);
		return certification;
	}
}
