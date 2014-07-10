package me.sent1nel.criminalintent.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.*;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    public static final String EXTRA_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "date";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;


    @InjectView(R.id.crime_title)
    TextView crimeTitle;
    @InjectView(R.id.crime_date)
    Button crimeDateButton;
    @InjectView(R.id.crime_solved)
    CheckBox crimeSolved;
    @InjectView(R.id.crime_suspectButton)
    Button suspectButton;
    @InjectView(R.id.crime_reportButton)
    Button reportButton;

    private Crime crime;

    private Callbacks callbacks;

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks)activity;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public static Fragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        crime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.crime_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.inject(this, rootView);

        crimeTitle.setText(crime.getTitle());
        updateDate();
        crimeSolved.setChecked(crime.isSolved());

        if (crime.getSuspect() != null) {
            suspectButton.setText(crime.getSuspect());
        }

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            crime.setDate(date);
            callbacks.onCrimeUpdated(crime);
            updateDate();
        }
        if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for.
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // Perform your query - the contactUri is like a "where"
            // clause here
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            // Double-check that you actually got results
            if (c.getCount() == 0) {
                c.close();
                return;
            }
            // Pull out the first column of the first row of data -
            // that is your suspect's name.
            c.moveToFirst();
            String suspect = c.getString(0);
            crime.setSuspect(suspect);
            callbacks.onCrimeUpdated(crime);
            suspectButton.setText(suspect);
            c.close();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    private void updateDate() {
        try {
            crimeDateButton.setText(getPrettyDate(crime.getDate()));
        } catch (ParseException e) {
            crimeDateButton.setVisibility(View.GONE);
        }
    }

    private String getPrettyDate(Date date) throws ParseException {
        java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(getActivity().getApplicationContext());
        return dateFormat.format(date);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (crime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, crime.getDate()).toString();

        String suspect = crime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        return getString(R.string.crime_report, crime.getTitle(), dateString, solvedString, suspect);
    }

    @OnTextChanged(R.id.crime_title)
    void onTextChanged(CharSequence text) {
        crime.setTitle(text.toString());
        callbacks.onCrimeUpdated(crime);
    }

    @OnCheckedChanged(R.id.crime_solved)
    void onChecked(boolean checked) {
        crime.setSolved(checked);
        callbacks.onCrimeUpdated(crime);
    }

    @OnClick(R.id.crime_date)
    void showDatePicker() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
        dialog.show(fragmentManager, DIALOG_DATE);
    }

    @OnClick(R.id.crime_suspectButton) void selectSuspect() {
        Intent i = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, REQUEST_CONTACT);
    }

    @OnClick(R.id.crime_reportButton) void sendReport() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
        i.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.crime_report_subject));

        i = Intent.createChooser(i, getString(R.string.send_report));
        startActivity(i);
    }
}