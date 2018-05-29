package application.sephirmobile.sephirinterface.getters;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import application.sephirmobile.sephirinterface.HtmlFromResourceReader;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;

public class SchoolClassGetterTest {
	@Test
	public void html_parse_correctSchoolClasses() throws Exception {
		String html = HtmlFromResourceReader.read("html/noten.txt");
		SchoolClassGetter schoolClassGetter = new SchoolClassGetter(null);

		List<SchoolClass> schoolClasses = schoolClassGetter.parse(html);

		List<SchoolClass> list = new ArrayList<>();
		list.add(new SchoolClass("11012", "S-BMLT16a BBZW-S"));
		list.add(new SchoolClass("11013", "S-INF16d BBZW-S"));
		assertThat(schoolClasses, is(list));
	}
}
