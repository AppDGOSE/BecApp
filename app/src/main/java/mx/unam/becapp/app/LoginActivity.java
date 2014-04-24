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

import java.io.IOException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;


import android.view.inputmethod.InputMethodManager;
import android.content.Context;
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
	private int PASSWORD_MIN_SIZE = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
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

        // Check the network connection
        // TODO: There's no place to display error.
        /*
        ConnectivityManager connMgr = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        TextView t = (TextView) findViewById(R.id.network_status_message);
        if (networkInfo == null || !networkInfo.isConnected()) {
            // No network connection
			mLoginStatusMessageView.setText(getString(R.string.error_no_network));
			focusView = mLoginStatusMessageView;
            cancel = true;
        }
        */

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask();
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
		@Override
		protected String doInBackground(String... params) {
            String result = null;

			try {
                result =  sendUsernamePassword(params[0], params[1]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
			}/* catch (InterruptedException e) {
				return e.getMessage();
			}*/

			return result;
		}

		@Override
		protected void onPostExecute(final String response) {
			mAuthTask = null;
			//showProgress(false);

			mLoginStatusMessageView.setText(response);
            /*
			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
            */
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

        /**
         * Login.
         * TODO: Move to somwhere else.
         * */
        private String sendUsernamePassword(String username, String password)
            throws IOException {
            InputStream is = null;
            String url = "http://api-dgose.herokuapp.com/users/sign_in";
            String result = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                // FIX: I mean, just look at it :-\
                JSONObject jsonObject = new JSONObject(
                        "{\"user\":{\"account_number\":\""+username+"\",\"password\":\""+password+"\"}}");
                //result = jsonObject.toString();

                StringEntity se = new StringEntity(jsonObject.toString());

                httpPost.setEntity(se);

                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpclient.execute(httpPost);

                is = httpResponse.getEntity().getContent();

                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                result = e.getMessage();

            } finally {
                if (is != null) {
                    is.close();
                } 
            }

            return result;
        }
	}

    /**
     * Only to convert string, remove when no longer used.
     */
    private static String convertInputStreamToString(InputStream inputStream)
        throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    } 
}
