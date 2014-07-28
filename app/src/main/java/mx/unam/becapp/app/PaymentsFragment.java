package mx.unam.becapp.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
public class PaymentsFragment extends TabFragment {

    public PaymentsFragment(Payments payments) {
        this.information = payments;
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

        mInformationView = (GridView) view.findViewById(R.id.payments_gridview);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void visibleInformation() {

        GridView mPaymentsGridView = (GridView) mInformationView;

        mPaymentsGridView.setAdapter(new Adapter(getActivity()));

        showProgress(false);
        showError(false);

        showInformation(true);
    }

    public class Adapter extends BaseAdapter {

        private Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            Payments payments = (Payments) information;
            if (payments.calendar != null)
                return payments.calendar.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int i) {
            Payments payments = (Payments) information;
            if (payments.calendar != null)
                return payments.calendar.get(i);
            else
                return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Payments payments = (Payments) information;

            if (payments.calendar == null)
                return view;

            View row = view;
            ViewHolder holder;

            Payments.Payment payment = payments.calendar.get(i);

            if (row == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.white_payment, null);

                holder = new ViewHolder();

                holder.mMonth = (TextView) row.findViewById(R.id.text_month);
                holder.mDay = (TextView) row.findViewById(R.id.text_payment_day);
                holder.mStatus = (TextView) row.findViewById(R.id.text_status);
                holder.mAmount = (TextView) row.findViewById(R.id.text_amount);

                holder.mBackground = row;

                row.setTag(holder);

            } else {
                holder = (ViewHolder) row.getTag();
            }

            holder.mMonth.setText(payment.month);
            holder.mDay.setText(payment.done);
            holder.mStatus.setText(payment.status);
            holder.mAmount.setText("$" + payment.amount);

            /**

            Drawable background;

            if (isYellow(i))
                background = getResources().getDrawable(R.drawable.yellow_background);
            else
                background = getResources().getDrawable(R.drawable.white_background);

            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.mBackground.setBackgroundDrawable(background);
            } else {
                holder.mBackground.setBackground(background);
            }
            */

            return row;
        }

        public boolean isYellow(int i) {

            return (i == 1 || i == 2 || i == 5 || i == 6 || i == 9 || i == 10);
        }

        class ViewHolder {
            TextView mMonth;
            TextView mDay;
            TextView mStatus;
            TextView mAmount;

            View mBackground;
        }
    }
}
