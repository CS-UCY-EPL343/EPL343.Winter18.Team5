package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class About extends ParentActivity {

    @Override
    public String provideTitle(Intent ignored) {
        return getString(R.string.title_about);
    }

    @Override
    protected boolean handleMenu(MenuItem item) {
        return false;
    }

    @Override
    public int provideLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected int optionsMenu() {
        return NOT_APPLICABLE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
