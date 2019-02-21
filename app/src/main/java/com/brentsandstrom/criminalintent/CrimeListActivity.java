package com.brentsandstrom.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Brent on 2/4/2019.
 */

// The main activity of the application. Creates new CrimeListFragment.
public class CrimeListActivity extends SingleFragmentActivity {

    //SingleFragmentActivity requires this in order to run onCreate() like a normal activity.
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
