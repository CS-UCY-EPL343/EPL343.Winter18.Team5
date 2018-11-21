package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;

public abstract class ParentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final int NOT_APPLICABLE = -1;

    protected abstract String provideTitle(Intent intent);

    protected abstract boolean handleMenu(MenuItem item);

    protected abstract int provideLayout();

    protected abstract int optionsMenu();

    protected boolean usesHamburder;
    protected boolean usesActionbar=true;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (optionsMenu() != NOT_APPLICABLE)
            getMenuInflater().inflate(optionsMenu(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    String theme = null;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    private Flight mFlight;

    protected void onCreate(Bundle savedInstanceState) {
        updateTheme();

        super.onCreate(savedInstanceState);

        if (provideLayout() == NOT_APPLICABLE)
            return;

        setLayout(provideLayout());

        if(!usesActionbar)
            return;
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(provideTitle(getIntent()));


        if (usesHamburder) {
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


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (usesHamburder) mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.w("Who is it?",""+item.getItemId());

        if (usesHamburder && mDrawerToggle.onOptionsItemSelected(item)) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }

        if (item.getItemId() == android.R.id.home) finish();

        return handleMenu(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.navigation_about: {
                startActivity(new Intent(this, About.class));
            }
            break;
            case R.id.navigation_last_flight: {
                Intent intent = new Intent(this, FlightRecord.class);
                intent.putExtra("getFlight", mFlight == null ? 0 : mFlight.getId());
                startActivity(intent);
            }
            break;
            case R.id.navigation_settings: {
                startActivity(new Intent(this, Settings.class));
            }
            break;
            case R.id.navigation_history: {
                startActivity(new Intent(this, History.class));
            }
            break;
        }
        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (usesHamburder)
            mDrawerToggle.syncState();
    }

    private void setLayout(int layout) {
        if (layout != NOT_APPLICABLE) {
            setContentView(layout);
        }
    }


    protected void updateTheme() {
        try {
            theme = PreferenceManager.getDefaultSharedPreferences(this).getString("theme", "HighContrastLightOrange");
            assert theme != null;
            this.setTheme((int) R.style.class.getDeclaredField(theme).get(0));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String settingsTheme = PreferenceManager.getDefaultSharedPreferences(this).getString("theme", "HighContrastLightOrange");
        if (!this.theme.equals(settingsTheme)) {
            recreate();
        }
    }

}
