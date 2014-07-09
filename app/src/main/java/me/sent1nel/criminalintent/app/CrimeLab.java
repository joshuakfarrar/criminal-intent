package me.sent1nel.criminalintent.app;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab crimeLab;
    private Context context;
    private static ArrayList<Crime> crimes;

    private CrimeLab(Context appContext) {
        context = appContext;
        crimes = new ArrayList<Crime>();
    }

    public static CrimeLab get(Context c) {
        if (crimeLab == null) {
            crimeLab = new CrimeLab(c.getApplicationContext());
        }
        return crimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return crimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : crimes)
            if (crime.getId().equals(id))
                return crime;
        throw new CrimeNotFoundException();
    }

    public void addCrime(Crime crime) {
        crimes.add(crime);
    }

    private class CrimeNotFoundException extends RuntimeException {
    }
}
