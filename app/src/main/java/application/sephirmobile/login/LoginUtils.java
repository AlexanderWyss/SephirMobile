package application.sephirmobile.login;

import android.content.Context;

import java.time.Instant;

import de.adorsys.android.securestoragelibrary.SecurePreferences;

public class LoginUtils {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "pw";

    private LoginUtils(){}

    public static void save(Login login) {
        SecurePreferences.setValue(EMAIL, login.getEmail());
        SecurePreferences.setValue(PASSWORD, login.getPassword());
    }
    public static Login load() {
        String email = SecurePreferences.getStringValue(EMAIL, null);
        String password = SecurePreferences.getStringValue(PASSWORD, null);
        if(email == null || password == null) {
            return null;
        }
        return new Login(email, password);
    }
}
