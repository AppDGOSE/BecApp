package mx.unam.becapp.app;

import android.os.AsyncTask;

public class InformationTask extends AsyncTask<String, Void, String> {

    Information information;
    TabFragment fragment;

    public InformationTask(Information information, TabFragment fragment) {
        this.information = information;
        this.fragment = fragment;
    }

    @Override
    protected String doInBackground(String[] params) {
        information.getData();
        return information.getStatus();
    }

    @Override
    protected void onPostExecute(final String status) {
        if (status.equals("200"))
            fragment.visibleInformation();
        else
            fragment.visibleError();
    }

    @Override
    protected void onCancelled() {
        fragment.visibleError();
    }
}
