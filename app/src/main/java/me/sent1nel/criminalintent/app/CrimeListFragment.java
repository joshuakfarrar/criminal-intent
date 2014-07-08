package me.sent1nel.criminalintent.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class CrimeListFragment extends ListFragment {

    private ArrayList<Crime> crimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.crimes_title);

        crimes = CrimeLab.get(getActivity().getApplicationContext()).getCrimes();

        ArrayAdapter<Crime> adapter = new CrimeAdapter(crimes);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public class CrimeAdapter extends ArrayAdapter<Crime> {

        @InjectView(R.id.crime_list_item_titleTextView)
        TextView title;
        @InjectView(R.id.crime_list_item_dateTextView)
        TextView date;
        @InjectView(R.id.crime_list_item_solvedCheckBox)
        CheckBox solved;

        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), android.R.layout.simple_list_item_1, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.crime_list_item, parent, false);
            }

            ButterKnife.inject(this, convertView);

            Crime crime = getItem(position);

            title.setText(crime.getTitle());
            try {
                date.setText(getPrettyDate(crime.getDate()));
            } catch (ParseException e) {
                date.setVisibility(View.GONE);
            }
            solved.setChecked(crime.isSolved());

            return convertView;
        }

        private String getPrettyDate(Date date) throws ParseException {
            java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(getActivity().getApplicationContext());
            return dateFormat.format(date);
        }
    }
}