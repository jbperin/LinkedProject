package biz.perin.jibe.linkedproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText mUserPseudoView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserPseudoView = (AutoCompleteTextView) findViewById(R.id.pseudo);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        SharedPreferences UserPreferences = getSharedPreferences  ("UserPreferences", MODE_PRIVATE );
        if (UserPreferences.contains("login") && UserPreferences.contains("password") ){
            mUserPseudoView.setText(UserPreferences.getString("login", "defaultlogin"));
            mPasswordView.setText(UserPreferences.getString("password", "defaultpassword"));
        }else {
//            SharedPreferences.Editor editor = UserPreferences.edit();
//            editor.putInt(getString(R.string.saved_high_score), newHighScore);
//            editor.commit();
//
        }



        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUserPseudoView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String login = mUserPseudoView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(login)) {
            mUserPseudoView.setError(getString(R.string.error_field_required));
            focusView = mUserPseudoView;
            cancel = true;
        } else if (!isPseudoValid(login)) {
            mUserPseudoView.setError(getString(R.string.error_invalid_email));
            focusView = mUserPseudoView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            SharedPreferences UserPreferences = getSharedPreferences  ("UserPreferences", MODE_PRIVATE );
            SharedPreferences.Editor editor = UserPreferences.edit();
            editor.putString("login", login);
            editor.putString("password", password);
            editor.commit();
            WebHelper.getInstance().setAuthenticationInformation(
                    UserPreferences.getString("login", "defaultlogin")
                    , UserPreferences.getString("password", "defaultpassword"));
            Intent msgIntent = new Intent(this, SurferService.class);
            msgIntent.putExtra("RessourceType","LOGIN");
            startService(msgIntent);
            finish();

        }
    }

    private boolean isPseudoValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }


}

