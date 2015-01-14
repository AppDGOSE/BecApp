package mx.unam.becapp.app;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.Window;


/**
 * A simple {@link DialogFragment} subclass.
 *
 */
public class AboutDialogFragment extends DialogFragment {

    private final static String UNAMPAGE    = "http://www.unam.mx";
    private final static String UNAMENLINEA = "http://www.unamenlinea.unam.mx";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view =  inflater.inflate(R.layout.fragment_about_dialog, container, false);

        view.findViewById(R.id.left_unam).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Uri uri;
                            uri = Uri.parse(UNAMPAGE);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {

                            Context context = getActivity().getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, e.getMessage(), duration);
                            toast.show();
                        }
                    }
                }
        );

        view.findViewById(R.id.right_unam).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Uri uri;
                            uri = Uri.parse(UNAMENLINEA);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {

                            Context context = getActivity().getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, e.getMessage(), duration);
                            toast.show();
                        }
                    }
                }
        );

        return view;

    }
}
