package me.sent1nel.criminalintent.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnTextChanged;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    public static final String EXTRA_CRIME_ID = "crime_id";

    @InjectView(R.id.crime_title)
    TextView crimeTitle;
    @InjectView(R.id.crime_date)
    Button crimeDate;
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

        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        crime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.crime_fragment, container, false);

        ButterKnife.inject(this, rootView);

        crimeTitle.setText(crime.getTitle());
        configureCrimeDateEditText();
        crimeSolved.setChecked(crime.isSolved());

        return rootView;
    }

    private void configureCrimeDateEditText() {
        try {
            crimeDate.setText(getPrettyDate(crime.getDate()));
            crimeDate.setEnabled(false);
        } catch (ParseException e) {
            crimeDate.setVisibility(View.GONE);
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
}