package application.sephirmobile.sephirinterface.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.Urls;
import application.sephirmobile.sephirinterface.entitys.Semester;
import application.sephirmobile.sephirinterface.getters.Getter;

public class SemesterChangeForm extends Getter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SemesterChangeForm.class);
	private static final String URL = Urls.NOTEN + "/noten.cfm";

	public SemesterChangeForm(SephirInterface sephirInterface) {
		super(sephirInterface);
	}

	public void changeSemester(Semester semester) throws IOException {
		LOGGER.debug("Changing Semester to: {}", semester);
		Map<String, String> postMap = new HashMap<>();
		postMap.put("periodeid", semester.getId());
		getSephirInterface().post(URL, postMap);
	}
}
