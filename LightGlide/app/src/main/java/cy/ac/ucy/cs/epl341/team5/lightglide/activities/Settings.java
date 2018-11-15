package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Optional;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    int themeID = R.style.HighContrastDarkOrange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateTheme(PreferenceManager.getDefaultSharedPreferences(this));
        super.onCreate(savedInstanceState);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addPreferencesFromResource(R.xml.preference);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "theme": {
                updateTheme(sharedPreferences);
                this.recreate();
                break;
            }
        }
    }

    private void updateTheme(SharedPreferences sharedPreferences) {
        try {
            String theme = Optional.ofNullable(
                    sharedPreferences.getString(
                            "theme", "HighContrastLightOrange"
                    )).orElse("HighContrastLightOrange");
            themeID = (int) R.style.class.getDeclaredField(theme).get(0);
            this.setTheme(themeID);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
