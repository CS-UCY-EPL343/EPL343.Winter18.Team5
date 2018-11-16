package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public abstract class ParentActivity extends AppCompatActivity {

    public abstract String provideTitle(Intent intent);
    public abstract int provideLayout();

    String theme = null;

    protected void onCreate(Bundle savedInstanceState) {
        updateTheme();
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(provideTitle(getIntent()));
        }
        setLayout(provideLayout());

    }

    private void setLayout(int layout) {
        if(layout != -1 ){
            setContentView(layout);
        }
    }


    protected void updateTheme() {
        try {
            theme = PreferenceManager.getDefaultSharedPreferences(this).getString("theme","HighContrastLightOrange");
            assert theme != null;
            this.setTheme((int) R.style.class.getDeclaredField(theme).get(0));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        String settingsTheme = PreferenceManager.getDefaultSharedPreferences(this).getString("theme","HighContrastLightOrange");
        if(!this.theme.equals(settingsTheme)){
            recreate();
        }
    }
}
