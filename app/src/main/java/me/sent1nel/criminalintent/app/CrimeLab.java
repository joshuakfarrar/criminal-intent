package me.sent1nel.criminalintent.app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private Context context;
    private static CrimeLab crimeLab;
    private static CriminalIntentJSONSerializer serializer;

    private static ArrayList<Crime> crimes;

    private CrimeLab(Context appContext) {
        context = appContext;
        serializer = new CriminalIntentJSONSerializer(context, FILENAME);

        loadCrimes();
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
        return new Crime();
    }

    public void addCrime(Crime crime) {
        crimes.add(crime);
    }

    public boolean saveCrimes() {
        try {
            serializer.saveCrimes(crimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            Toast.makeText(context, "Failed to save crimes", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void loadCrimes() {
        try {
            crimes = serializer.loadCrimes();
        } catch (Exception e) {
            crimes = new ArrayList<Crime>();
            Log.e(TAG, "Error saving crimes: ", e);
            Toast.makeText(context, "Failed to load crimes", Toast.LENGTH_SHORT).show();
        }
    }
}
