package cy.ac.ucy.cs.epl341.team5.lightglide.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class MainActivity extends ParentActivity {


    @Override
    public String provideTitle(Intent ignored) {
        return getString(R.string.title_home);
    }

    @Override
    protected boolean handleMenu(MenuItem item) {
        return false;
    }

    @Override
    public int provideLayout(){
        return R.layout.activity_main;
    }

    @Override
    protected int optionsMenu() {
        return NOT_APPLICABLE;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usesHamburder = true;
        super.onCreate(savedInstanceState);
        
    }

}
