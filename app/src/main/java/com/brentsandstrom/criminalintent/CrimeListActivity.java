package com.brentsandstrom.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Brent on 2/4/2019.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
