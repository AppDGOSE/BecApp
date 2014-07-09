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


import mx.unam.becapp.app.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class InfoFragment extends Fragment implements AbsListView.OnItemClickListener {
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private View mLoadingStatusView;
    private View mErrorButtonView;

    private Profile profile;
    private ProfileTask pTask;

    private boolean alreadyAttempt = false;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

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

        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item, container, false);

        mLoadingStatusView = view.findViewById(R.id.loading_status);

        mErrorButtonView = view.findViewById(R.id.error_status);

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

        //mListView = (AbsListView) view.findViewById(android.R.id.list);
        //((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);


        // Set the adapter


        // Set OnItemClickListener so we can be notified on item clicks
        //mListView.setOnItemClickListener(this);


        return view;
    }

    private void attemptGetData() {

        if (!alreadyAttempt) {
            showError(false);
            showProgress(true);

            pTask = new ProfileTask(profile);
            String[] params = {};
            pTask.execute(params);

            alreadyAttempt = true;
        } else if(!profile.success()) {
            showError(true);
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
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
    }

    /**
     *
     */
    private void showError(final boolean show) {
        mErrorButtonView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ProfileTask extends AsyncTask<String, Void, String> {

        Profile profile ;

        public ProfileTask(Profile profile) {
            this.profile = profile;
        }

        @Override
        protected String doInBackground(String[] params) {
            profile.getData();
            return profile.student_number;
        }

        @Override
        protected void onPostExecute(final String status) {
            pTask = null;

            if (status != null) {

            } else {
                showProgress(false);
                showError(true);
            }
        }

        @Override
        protected void onCancelled() {
            pTask = null;
            showProgress(false);
        }
    }
}
