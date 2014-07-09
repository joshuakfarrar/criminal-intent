package me.sent1nel.criminalintent.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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


    @InjectView(R.id.crime_title)
    TextView crimeTitle;
    @InjectView(R.id.crime_date)
    Button crimeDateButton;
    @InjectView(R.id.crime_solved)
    CheckBox crimeSolved;

    private Crime crime;

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

    @TargetApi(11)
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
            updateDate();
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

    @OnTextChanged(R.id.crime_title)
    void onTextChanged(CharSequence text) {
        crime.setTitle(text.toString());
    }

    @OnCheckedChanged(R.id.crime_solved)
    void onChecked(boolean checked) {
        crime.setSolved(checked);
    }

    @OnClick(R.id.crime_date)
    void showDatePicker() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
        dialog.show(fragmentManager, DIALOG_DATE);
    }
}