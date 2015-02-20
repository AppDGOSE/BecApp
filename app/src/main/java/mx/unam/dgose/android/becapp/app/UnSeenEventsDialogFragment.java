package mx.unam.dgose.android.becapp.app;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import mx.unam.dgose.android.becapp.R;

/**
 * Created by emilianork on 2/20/15.
 */
public class UnSeenEventsDialogFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_unseen_events_dialog, container, false);

        view.findViewById(R.id.ok_button).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

        return view;

    }

}
