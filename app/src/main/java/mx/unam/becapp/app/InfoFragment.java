package mx.unam.becapp.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 */

public class InfoFragment extends TabFragment {

    public InfoFragment(Profile profile) {
        this.information = profile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        mInformationView = view.findViewById(R.id.list_info);
        mLoadingStatusView = view.findViewById(R.id.loading_status);
        mErrorButtonView = view.findViewById(R.id.error_status);

        Button button = (Button) view.findViewById(R.id.reload_button);

        button.
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
        showProgress(false);
        showError(false);

        TextView mStudentNumber = (TextView) mInformationView.findViewById(R.id.student_number_fill);
        TextView mStudentName = (TextView) mInformationView.findViewById(R.id.student_name_fill);
        TextView mCurp = (TextView) mInformationView.findViewById(R.id.curp_fill);
        TextView mUNAMEmail = (TextView) mInformationView.findViewById(R.id.unam_email_fill);
        TextView mComEmail = (TextView) mInformationView.findViewById(R.id.com_email_fill);
        TextView mSchool = (TextView) mInformationView.findViewById(R.id.school_name_fill);
        TextView mMajor = (TextView) mInformationView.findViewById(R.id.major_name_fill);
        TextView mScholarship = (TextView) mInformationView.findViewById(R.id.scholarship_name_fill);
        TextView mScholarshipStatus = (TextView) mInformationView.findViewById(R.id.scholarship_status_fill);
        TextView mCurrentCycle = (TextView) mInformationView.findViewById(R.id.scholarship_cycle_fill);

        Profile profile = (Profile) information;

        mStudentNumber.setText(profile.student_number);
        mStudentName.setText(profile.fullname);
        mCurp.setText(profile.curp);
        mUNAMEmail.setText(profile.unam_email);
        mComEmail.setText(profile.com_email);
        mSchool.setText(profile.school);
        mMajor.setText(profile.major);
        mScholarship.setText(profile.scholarship);
        mScholarshipStatus.setText(profile.scholarship_status);
        mCurrentCycle.setText(profile.current_cycle);

        showInformation(true);
    }
}
