package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public     class PrefsFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);

    }
}
