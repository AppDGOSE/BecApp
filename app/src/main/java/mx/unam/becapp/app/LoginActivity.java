package mx.unam.becapp.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import android.content.Intent;

import android.content.Context;

import android.view.inputmethod.InputMethodManager;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for account number and password at the time of the login attempt.
	private String mAccountNumber;
	private String mPassword;

	// UI references.
	private EditText mAccountNumberView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private int ACCOUNT_NUMBER_SIZE = 9;

    private Session session;
    private static final String API_URL = "http://api-dgose.herokuapp.com";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        CookieHandler.setDefault(new CookieManager());

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mAccountNumberView = (EditText) findViewById(R.id.account_number);
		mAccountNumberView.setText(mAccountNumber);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
            .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id,
                    KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm;
                        imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        attemptLogin();
                    }
                });
	}

    public Session getSession() {
        return this.session;
    }

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
            return;
		}

		// Reset errors.
		mAccountNumberView.setError(null);
		mPasswordView.setError(null);
        mLoginStatusMessageView.setError(null);

		// Store values at the time of the login attempt.
		mAccountNumber = mAccountNumberView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid account number.
		if (TextUtils.isEmpty(mAccountNumber)) {
			mAccountNumberView.setError(getString(R.string.error_field_required));
			focusView = mAccountNumberView;
			cancel = true;
		} else if (mAccountNumber.length() < ACCOUNT_NUMBER_SIZE) {
			mAccountNumberView.setError(getString(R.string.error_invalid_account_number));
			focusView = mAccountNumberView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(this);
            String[] params = {mAccountNumber, mPassword};
			mAuthTask.execute(params);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<String, Void, String> {

        LoginActivity loginActivity;

        public UserLoginTask(LoginActivity loginActivity) {
            this.loginActivity = loginActivity;
        }

		@Override
		protected String doInBackground(String[] params) {
			session = new Session(API_URL);
			session.signIn(params[0], params[1]);

			return session.getStatus();
		}

		@Override
		protected void onPostExecute(final String status) {

            mAuthTask = null;
            CharSequence text;

            if (status.equals("200")) {

                Intent intent = new Intent(this.loginActivity, GeneralActivity.class);

                intent.putExtra("sessionObject", this.loginActivity.getSession());
                startActivity(intent);

                showProgress(false);

            } else if (status.equals("401")) {
                text = "Datos inválidos";
                showProgress(false);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            } else if (status.equals("500")){

                text = "Tenemos problemas con el servidor";
                showProgress(false);


                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();



            } else {

                text = "No hay conexión a internet";
                showProgress(false);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

	}
}
