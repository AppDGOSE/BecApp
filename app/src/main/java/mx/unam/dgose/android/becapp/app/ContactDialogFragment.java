package mx.unam.dgose.android.becapp.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.Window;

import mx.unam.dgose.android.becapp.R;


/**
 * A simple {@link DialogFragment} subclass.
 *
 */
public class ContactDialogFragment extends DialogFragment {

    private final static String FBPAGE = "https://www.facebook.com/pages/Becarios-UNAM/275733789169485";
    private final static String WEB = "http://www.becarios.unam.mx/portal/";
    private final static String EMAIL = "becarios@unam.mx";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view =  inflater.inflate(R.layout.fragment_help_dialog, container, false);

        ImageView contactFb = (ImageView) view.findViewById(R.id.contact_fb);
        ImageView contactWeb = (ImageView) view.findViewById(R.id.contact_web);
        ImageView contactMail = (ImageView) view.findViewById(R.id.contact_mail);

        contactFb.
            setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Uri uri;
                            uri = Uri.parse(FBPAGE);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {

                            Context context = getActivity().getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, e.getMessage(), duration);
                            toast.show();
                        }
                    }
                });

        contactWeb.
            setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Uri uri;
                            uri = Uri.parse(WEB);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Context context = getActivity().getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, e.getMessage(), duration);
                            toast.show();
                        }
                    }
                });

        contactMail.
            setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SENDTO,
                                                       Uri.fromParts("mailto",EMAIL, null));
                            
                            startActivity(Intent.createChooser(intent, "Send email..."));

                        } catch (ActivityNotFoundException e) {
                            Context context = getActivity().getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, e.getMessage(), duration);
                            toast.show();
                        }
                    }
                });


        return view;

    }
}
