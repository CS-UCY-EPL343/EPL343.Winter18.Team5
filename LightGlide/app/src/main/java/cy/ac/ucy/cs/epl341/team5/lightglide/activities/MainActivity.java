package cy.ac.ucy.cs.epl341.team5.lightglide.activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;

public class MainActivity extends ParentActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private Flight mFlight;

    @Override
    public String provideTitle(Intent ignored) {
        return getString(R.string.title_home);
    }

    @Override
    public int provideLayout(){
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mNavigationView = findViewById(R.id.nav_view);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.inflateMenu(R.menu.nav_menu);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        mDrawerLayout.openDrawer(GravityCompat.START);
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        switch (menuItem.getItemId()){
            case R.id.navigation_about:{
                startActivity(new Intent(this,About.class));
            }break;
            case R.id.navigation_last_flight:{
                Intent intent = new Intent(this, FlightRecord.class);
                intent.putExtra("getFlight", mFlight==null?0:mFlight.getId());
                startActivity(intent);
            }break;
            case R.id.navigation_settings:{
                startActivity(new Intent(this,Settings.class));
            }break;
            case R.id.navigation_history:{
                startActivity(new Intent(this,History.class));
            }break;
        }
        return true;
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

}
