package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    static int themeID=R.style.AppTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(themeID);
        addPreferencesFromResource(R.xml.preference);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("theme")) {
            String aham=sharedPreferences.getString("theme", "AppTheme");
            if(aham.equals("MyLightTheme"))
                themeID=R.style.MyLightTheme;
            else if(aham.equals("MyDarkTheme"))
                themeID=R.style.MyDarkTheme;
            else
                themeID=R.style.AppTheme;
            this.recreate();
        }
    }
}
