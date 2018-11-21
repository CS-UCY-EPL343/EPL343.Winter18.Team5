package cy.ac.ucy.cs.epl341.team5.lightglide.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class MainActivity extends ParentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usesHamburder = true;
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here
                startActivity(new Intent(MainActivity.this,FlightMode.class));
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.g_map);
        mapFragment.getMapAsync(this);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        GraphView graph_elevation = (GraphView) findViewById(R.id.elevation_graph);
        LineGraphSeries<DataPoint> seriesEl = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 8)
        });
        if (graph_elevation!=null)
            graph_elevation.addSeries(seriesEl);
        adapter.addFragment(new GeneralFragment(), "GENERAL");
        adapter.addFragment(new SpeedFragment(), "SPEED");
        adapter.addFragment(new ElevationFragment(), "ELEVATION");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Larnaca coordinates
        LatLng larnaca = new LatLng(34.821983, 33.556684);
        mMap.addMarker(new MarkerOptions().position(larnaca).title("Seattle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(larnaca));
    }

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

}
