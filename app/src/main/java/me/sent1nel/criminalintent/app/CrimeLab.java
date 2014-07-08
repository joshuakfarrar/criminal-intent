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

        // generate some crimes!
        for (int i = 0; i < 10; i++) {
            Crime c = new Crime();
            c.setTitle("Crime #" + i);
            c.setSolved(i % 2 == 0); // Every other one
            crimes.add(c);
        }

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

    private class CrimeNotFoundException extends RuntimeException {
    }
}
