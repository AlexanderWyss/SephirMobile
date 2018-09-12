package application.sephirmobile.login;

import de.adorsys.android.securestoragelibrary.SecurePreferences;

public class LoginUtils {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "pw";

    private LoginUtils(){}

    public static void save(Login login) {
        if(login == null) {
            login = new Login("", "");
        }
        SecurePreferences.setValue(EMAIL, login.getEmail());
        SecurePreferences.setValue(PASSWORD, login.getPassword());
    }
    public static Login load() {
        String email = SecurePreferences.getStringValue(EMAIL, "");
        String password = SecurePreferences.getStringValue(PASSWORD, "");
        if( email.isEmpty() || password.isEmpty()) {
            return null;
        }
        return new Login(email, password);
    }
}
