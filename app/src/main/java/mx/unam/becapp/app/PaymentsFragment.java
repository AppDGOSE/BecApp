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

    private OnFragmentInteractionListener mListener;

    private GridView paymentsGridView;

    private Payments payments;
    private PaymentsTask pTask;

    private View mLoadingStatusView;
    private View mErrorButtonView;

    private boolean alreadyAttempt = false;

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

        paymentsGridView = (GridView) view.findViewById(R.id.gridview);
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

        return view;
    }

    private void attemptGetData() {

        if (!alreadyAttempt) {
            showError(false);
            showProgress(true);

            pTask = new PaymentsTask(payments);
            String[] params = {};
            pTask.execute(params);

            alreadyAttempt = true;
        } else if(!payments.success()) {
            showError(false);
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
            if (payments.calendar == null)
                return null;
            else
                return "";
        }

        @Override
        protected void onPostExecute(final String status) {
            pTask = null;

            if (status != null) {
                paymentsGridView.setAdapter(new Adapter(getActivity()));
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
            if (payments.success())
                return payments.calendar.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int i) {
            if (payments.success())
                return payments.calendar.get(i);
            else
                return 0;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder;
            View row = view;

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.grid, viewGroup, false);

                holder = new ViewHolder(row);
                row.setTag(holder);


            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (payments.success()) {
                Payments.Payment payment = payments.calendar.get(i);

                holder.month.setText(payment.month);
                holder.day.setText(payment.done);
                holder.status.setText(payment.status);
                holder.amount.setText(payment.amount);
            }
            return row;
        }
    }

    public class ViewHolder {
        TextView month;
        TextView day;
        TextView amount;
        TextView status;

        public ViewHolder(View view) {
            this.month = (TextView) view.findViewById(R.id.text_month);
            this.day = (TextView) view.findViewById(R.id.text_payment_day);
            this.amount = (TextView) view.findViewById(R.id.text_amount);
            this.status = (TextView) view.findViewById(R.id.text_status);
        }
    }
}
