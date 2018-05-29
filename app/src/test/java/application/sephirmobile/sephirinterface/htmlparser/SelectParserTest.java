package application.sephirmobile.sephirinterface.htmlparser;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.junit.Test;

import static org.junit.Assert.assertThat;

import application.sephirmobile.sephirinterface.HtmlFromResourceReader;

public class SelectParserTest {
	@Test
	public void htmlSelect_parse_correctlyMapped() throws Exception {
		String select = HtmlFromResourceReader.read("html/select.txt");

		SelectParser<Hello> selectParser = new SelectParser<>(Hello::setLanguage, Hello::setText, Hello::new);
		selectParser.parse(Jsoup.parse(select).selectFirst("#select"));

		Hello selected = new Hello("en", "Hello");
		List<Hello> hellos = new ArrayList<>();
		hellos.add(new Hello("de", "Hallo"));
		hellos.add(selected);
		hellos.add(new Hello("fr", "Bonjour"));
		assertThat(selectParser.getEntitys(), is(hellos));
		assertThat(selectParser.getDefaultValue(), is(selected));
	}

	private class Hello {
		private String language;
		private String text;

		public Hello() {
		}

		public Hello(String language, String text) {
			this.language = language;
			this.text = text;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public void setText(String text) {
			this.text = text;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((language == null) ? 0 : language.hashCode());
			result = prime * result + ((text == null) ? 0 : text.hashCode());
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
			Hello other = (Hello) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (language == null) {
				if (other.language != null)
					return false;
			} else if (!language.equals(other.language))
				return false;
			if (text == null) {
				if (other.text != null)
					return false;
			} else if (!text.equals(other.text))
				return false;
			return true;
		}

		private SelectParserTest getOuterType() {
			return SelectParserTest.this;
		}
	}
}
