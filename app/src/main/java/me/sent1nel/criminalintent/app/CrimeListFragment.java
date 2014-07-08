package me.sent1nel.criminalintent.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {

    private ArrayList<Crime> crimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.crimes_title);

        crimes = CrimeLab.get(getActivity().getApplicationContext()).getCrimes();

        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(),
                                                      android.R.layout.simple_list_item_1,
                                                      crimes);

        setListAdapter(adapter);
    }
}