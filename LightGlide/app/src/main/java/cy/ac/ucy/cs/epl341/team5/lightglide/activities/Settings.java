package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.preference.PreferenceActivity;
import android.os.Bundle;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
