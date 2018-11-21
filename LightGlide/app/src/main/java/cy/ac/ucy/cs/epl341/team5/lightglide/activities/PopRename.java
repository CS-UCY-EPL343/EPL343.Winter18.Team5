package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class PopRename extends ParentActivity {

    @Override
    protected String provideTitle(Intent intent) {
        return "";
    }

    @Override
    protected boolean handleMenu(MenuItem item) {
        return false;
    }

    @Override
    protected int provideLayout() {
        return R.layout.activity_pop_rename;
    }

    @Override
    protected int optionsMenu() {
        return NOT_APPLICABLE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usesActionbar=false;
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pop_rename);
        //getActionBar().hide();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels*0.75),(int)(dm.heightPixels*0.16));
    }

    public void onClickCancelRename(View v){
        setResult(-1);
        finish();
    }
}
