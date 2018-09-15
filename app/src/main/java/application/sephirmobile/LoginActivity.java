package application.sephirmobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import application.sephirmobile.login.Login;
import application.sephirmobile.login.LoginUtils;
import application.sephirmobile.sephirinterface.SephirInterface;

@SuppressLint("StaticFieldLeak")
public class LoginActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mMessageView;

    private final SephirInterface sephirInterface = SephirInterface.newSephirInterface();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mMessageView = findViewById(R.id.message);

        mPasswordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Login login = LoginUtils.load();
                    if (login != null && sephirInterface.login(login)) {
                        nextActivity();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                showProgress(false);
            }

            @Override
            protected void onPreExecute() {
                showProgress(true);
            }
        }.execute();
    }


    private void attemptLogin() {
        Login login = new Login(mEmailField.getText().toString(), mPasswordField.getText().toString());
        if (isInputValid(login)) {
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    boolean success = false;
                    try {
                        success = sephirInterface.login(login);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        return success;
                    }
                }

                @Override
                protected void onPostExecute(Boolean success) {
                    if (success) {
                        LoginUtils.save(login);
                        nextActivity();
                    } else {
                        mMessageView.setText(R.string.login_failed);
                    }
                    showProgress(false);
                }

                @Override
                protected void onPreExecute() {
                    showProgress(true);
                }
            }.execute();
        }
    }

    private void nextActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private boolean isInputValid(Login login) {
        mEmailField.setError(null);
        mPasswordField.setError(null);
        boolean isValid = true;
        if (TextUtils.isEmpty(login.getPassword())) {
            mPasswordField.setError(getString(R.string.error_field_required));
            mPasswordField.requestFocus();
            isValid = false;
        }

        if (TextUtils.isEmpty(login.getEmail())) {
            mEmailField.setError(getString(R.string.error_field_required));
            mEmailField.requestFocus();
            isValid = false;
        } else if (!isEmailValid(login.getEmail())) {
            mEmailField.setError(getString(R.string.error_invalid_email));
            mEmailField.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private boolean isEmailValid(String email) {
        return email.endsWith("@sluz.ch");
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

