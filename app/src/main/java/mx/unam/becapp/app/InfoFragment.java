package mx.unam.becapp.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class InfoFragment extends Fragment {
    /**
     * The fragment's ListView/GridView.
     */
    private View mLoadingStatusView;
    private View mErrorButtonView;
    private View mProfileView;

    private Profile profile;
    private ProfileTask pTask;

    private boolean alreadyAttempt = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InfoFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item, container, false);

        mLoadingStatusView = view.findViewById(R.id.loading_status);
        mErrorButtonView = view.findViewById(R.id.error_status);
        mProfileView = view.findViewById(R.id.list_info);

        
        view.findViewById(R.id.reload_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alreadyAttempt = false;
                        attemptGetData();
                    }
                }
        );

        attemptGetData();
        return view;
    }

    private void attemptGetData() {
        if (!alreadyAttempt) {
            visibleProgress();

            pTask = new ProfileTask(profile);
            String[] params = {};
            pTask.execute(params);

            alreadyAttempt = true;
        } else if(!profile.success()) {
            visibleError();
        } else {
            showData();
            visibleProfile();
        }
    }

    public void showData() {
        TextView mStudentNumber = (TextView) mProfileView.findViewById(R.id.student_number_fill);
        mStudentNumber.setText(profile.student_number);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(
                        android.R.integer.config_shortAnimTime);

                mLoadingStatusView.setVisibility(View.VISIBLE);
                mLoadingStatusView.animate().setDuration(shortAnimTime)
                        .alpha(show ? 1 : 0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mLoadingStatusView.setVisibility(show ? View.VISIBLE
                                        : View.GONE);
                            }
                        });

            } else {
                mLoadingStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        } catch(IllegalStateException e) {
        }
    }

    /**
     *
     */
    private void showError(final boolean show) {
        mErrorButtonView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showProfile(final boolean show) {
        mProfileView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void visibleProgress() {
        showProfile(false);
        showError(false);

        showProgress(true);
    }

    private void visibleError() {
        showProfile(false);
        showProgress(false);

        showError(true);
    }

    private void visibleProfile() {
        showProgress(false);
        showError(false);

        showProfile(true);
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ProfileTask extends AsyncTask<String, Void, String> {

        Profile profile;

        public ProfileTask(Profile profile) {
            this.profile = profile;
        }

        @Override
        protected String doInBackground(String[] params) {
            profile.getData();
            return profile.getStatus();
        }

        @Override
        protected void onPostExecute(final String status) {
            pTask = null;

            if (status.equals("200")) {
                showData();
                visibleProfile();
            } else {
                visibleError();
            }
        }

        @Override
        protected void onCancelled() {
            pTask = null;
            visibleError();
        }
    }
}
