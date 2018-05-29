package application.sephirmobile.sephirinterface.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.Login;
import application.sephirmobile.sephirinterface.getters.Getter;

public class LoginForm extends Getter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginForm.class);
	private static final String URL = "login_action.cfm";

	public LoginForm(SephirInterface sephirInterface) {
		super(sephirInterface);
	}

	public String submit(Login login) {
		LOGGER.debug("Trying login with email: {}", login.getEmail());
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", login.getEmail());
		map.add("passwort", login.getPassword());
		return getSephirInterface().post(URL, map);
	}
}
