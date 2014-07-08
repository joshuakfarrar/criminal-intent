package me.sent1nel.criminalintent.app;

import android.content.Context;

public class CrimeLab {
    private static CrimeLab crimeLab;
    private Context context;

    private CrimeLab(Context appContext) {
        context = appContext;
    }

    public static CrimeLab get(Context c) {
        if (crimeLab == null) {
            crimeLab = new CrimeLab(c.getApplicationContext());
        }
        return crimeLab;
    }
}
