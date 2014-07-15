package mx.unam.becapp.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentsFragment} factory method to
 * create an instance of this fragment.
 *
 */
public class PaymentsFragment extends Fragment {

    private Payments payments;
    private PaymentsTask pTask;

    private View mLoadingStatusView;
    private View mErrorButtonView;
    private GridView mPaymentsGridView;

    public PaymentsFragment(Payments payments) {
        this.payments = payments;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payments, container, false);

        mPaymentsGridView = (GridView) view.findViewById(R.id.payments_gridview);
        mLoadingStatusView = view.findViewById(R.id.loading_status);
        mErrorButtonView = view.findViewById(R.id.error_status);

        view.findViewById(R.id.reload_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptGetData();
                    }
                }
        );

        attemptGetData();
        return view;
    }

    private void attemptGetData() {
        visibleProgress();

        pTask = new PaymentsTask(payments);
        String[] params = {};
        pTask.execute(params);
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

    private void showPayments(final boolean show) {
        mPaymentsGridView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void visibleProgress() {
        showPayments(false);
        showError(false);

        showProgress(true);
    }

    private void visibleError() {
        showPayments(false);
        showProgress(false);

        showError(true);
    }

    private void visiblePayments() {
        mPaymentsGridView.setAdapter(new Adapter(getActivity()));

        showProgress(false);
        showError(false);

        showPayments(true);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class PaymentsTask extends AsyncTask<String, Void, String> {

        Payments payments ;

        public PaymentsTask(Payments payments) {
            this.payments = payments;
        }

        @Override
        protected String doInBackground(String[] params) {
            payments.getData();
            return payments.getStatus();
        }

        @Override
        protected void onPostExecute(final String status) {
            pTask = null;

            if (status.equals("200"))
                visiblePayments();
            else
                visibleError();
        }

        @Override
        protected void onCancelled() {
            pTask = null;
            visibleError();
        }
    }

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

    public class Adapter extends BaseAdapter {

        private Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            if (payments.success()) {
                return payments.calendar.size();
            } else {
                Log.d("PaymentsFragment","Aca paso 1");
                return 12;
            }
        }

        @Override
        public Object getItem(int i) {
            if (payments.success()) {
                return payments.calendar.get(i);
            } else {
                Log.d("PaymentsFragment","Aca paso 2");
                return 12;
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View row;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.grid, null);

            if (view == null) {
                Payments.Payment payment = payments.calendar.get(i);

                TextView mMonth = (TextView) row.findViewById(R.id.text_month);
                TextView mDay = (TextView) row.findViewById(R.id.text_payment_day);
                TextView mStatus = (TextView) row.findViewById(R.id.text_status);
                TextView mAmount = (TextView) row.findViewById(R.id.text_amount);

                mMonth.setText(payment.month);
                mDay.setText(payment.done);
                mStatus.setText(payment.status);
                mAmount.setText(payment.amount);

                return row;
            } else {
                return view;
            }


        }
    }
}
