package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class Settings extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    boolean changed = false;
    Intent call = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateTheme(PreferenceManager.getDefaultSharedPreferences(this));
        super.onCreate(savedInstanceState);
        call = getIntent();
        addPreferencesFromResource(R.xml.preference);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "theme": {
                updateTheme(sharedPreferences);
                this.recreate();
                changed = true;
                break;
            }
        }
    }

    void updateTheme(SharedPreferences sharedPreferences) {
        try {
            String theme = sharedPreferences.getString("theme", "HighContrastLightOrange");
            this.setTheme((int) R.style.class.getDeclaredField(theme).get(0));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void finish() {
        setResult(changed?0:1,  call);
        super.finish();
    }
}
