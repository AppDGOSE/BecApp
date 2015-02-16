package mx.unam.becapp.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.content.ActivityNotFoundException;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentsFragment} factory method to
 * create an instance of this fragment.
 *
 */
public class EventsFragment extends TabFragment {

    public EventsFragment(Events events) {
        this.information = events;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        mInformationView = (GridView) view.findViewById(R.id.events_gridview);
        mLoadingStatusView = view.findViewById(R.id.loading_status);
        mErrorButtonView = view.findViewById(R.id.error_status);

        view.findViewById(R.id.reload_button).
            setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptGetData();
                    }
                });

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

        GridView mEventsGridView = (GridView) mInformationView;

        mEventsGridView.setAdapter(new Adapter(getActivity()));

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
            Events events = (Events) information;
            if (events.events != null)
                return events.events.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int i) {
            Events events = (Events) information;
            if (events.events != null)
                return events.events.get(i);
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Events events = (Events) information;

            if (events.events == null)
                return view;

            View row = view;
            ViewHolder holder;

            final Events.Event event = events.events.get(i);

            if (row == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.white_event, null);

                holder = new ViewHolder();

                holder.mTitle = (TextView) row.findViewById(R.id.title_event_fill);
                holder.mStartDate = (TextView) row.findViewById(R.id.start_date_fill);
                holder.mEndDate = (TextView) row.findViewById(R.id.end_date_fill);
                holder.mPlace = (TextView) row.findViewById(R.id.place_event_fill);

                holder.mURL = (Button) row.findViewById(R.id.url_button);

                row.setTag(holder);

            } else {
                holder = (ViewHolder) row.getTag();
            }

            holder.mTitle.setText(event.title);
            holder.mStartDate.setText(event.start);
            holder.mEndDate.setText(event.end);
            holder.mPlace.setText(event.place_name);

            holder.mURL.
                setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri;
                            String browser_url = "";

                            try {

                                if (!("http://").equals(event.url.substring(0, 7)))
                                    browser_url = "http://" + event.url;
                                else
                                    browser_url = event.url;

                                uri = Uri.parse(browser_url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                String text = "La URL está incorrecta";
                                showProgress(false);


                                Context context = getActivity().getApplicationContext();
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }
                    });

            return row;
        }

        class ViewHolder {
            TextView mTitle;
            TextView mStartDate;
            TextView mEndDate;
            TextView mPlace;

            Button mURL;
        }
    }
}
