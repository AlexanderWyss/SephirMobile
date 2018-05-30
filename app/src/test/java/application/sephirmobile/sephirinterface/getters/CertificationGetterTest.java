package application.sephirmobile.sephirinterface.getters;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import application.sephirmobile.sephirinterface.StringFromResourceReader;
import application.sephirmobile.sephirinterface.entitys.Certification;

public class CertificationGetterTest {
	@Test
	public void html_parse_correctCertification() throws Exception {
		String html = StringFromResourceReader.read("html/index.txt");
		CertificationGetter certificationGetter = new CertificationGetter(null);

		Certification certification = certificationGetter.parse(html);

		assertThat(certification,
				is(new Certification("10816264", "18432d5de40d354e-16E665A3-5056-B23F-B5D8862457DF0470")));
	}
}
