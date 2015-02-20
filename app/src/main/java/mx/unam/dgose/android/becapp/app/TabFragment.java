package mx.unam.dgose.android.becapp.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import android.annotation.TargetApi;
import android.os.Build;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.view.ViewGroup;

public abstract class TabFragment extends Fragment {

    protected Information information;
    private InformationTask task;

    protected View mLoadingStatusView;
    protected View mErrorButtonView;
    protected View mInformationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void attemptGetData() {
        visibleProgress();

        task = new InformationTask(information, this);
        String[] params = {};
        task.execute(params);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show) {
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

    protected void showError(final boolean show) {
        mErrorButtonView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void showInformation(final boolean show) {
        mInformationView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void visibleProgress() {
        showInformation(false);
        showError(false);

        showProgress(true);
    }

    protected void visibleError() {
        task = null;

        showInformation(false);
        showProgress(false);

        showError(true);
    }

    protected abstract void visibleInformation();

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
