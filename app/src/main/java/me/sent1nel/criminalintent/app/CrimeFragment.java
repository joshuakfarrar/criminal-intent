package me.sent1nel.criminalintent.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnTextChanged;

public class CrimeFragment extends Fragment {

    private Crime crime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crime = new Crime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.crime_fragment, container, false);
        return rootView;
    }

    @OnTextChanged(R.id.crime_title) void onTextChanged(CharSequence text) {
        crime.setTitle(text.toString());
    }
}