package application.sephirmobile.sephirinterface.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.Login;
import application.sephirmobile.sephirinterface.getters.Getter;

public class LoginForm extends Getter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginForm.class);
	private static final String URL = "login_action.cfm";

	public LoginForm(SephirInterface sephirInterface) {
		super(sephirInterface);
	}

	public boolean submit(Login login) throws IOException {
		LOGGER.debug("Trying login with email: {}", login.getEmail());
		Map<String, String> map = new HashMap<>();
		map.put("email", login.getEmail());
		map.put("passwort", login.getPassword());
		return getSephirInterface().postForResponse(URL, map, new HashMap<>()).isSuccessful();
	}
}
