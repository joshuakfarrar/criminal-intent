package me.sent1nel.criminalintent.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.*;
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
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.crimes_title);

        crimes = CrimeLab.get(getActivity().getApplicationContext()).getCrimes();

        ArrayAdapter<Crime> adapter = new CrimeAdapter(crimes);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime crime = ((CrimeAdapter) getListAdapter()).getItem(position);

        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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