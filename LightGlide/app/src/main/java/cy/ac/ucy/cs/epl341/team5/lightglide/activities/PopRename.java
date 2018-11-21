package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class PopRename extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_rename);
        getActionBar().hide();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels*0.75),(int)(dm.heightPixels*0.16));
    }
}
