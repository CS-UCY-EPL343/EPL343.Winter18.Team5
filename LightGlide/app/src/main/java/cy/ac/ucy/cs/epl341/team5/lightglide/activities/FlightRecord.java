package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class FlightRecord extends ParentActivity {

    LinearLayout expandableEnvironmentLinearLayout;
    LinearLayout expandableFlightLinearLayout;

    @Override
    public String provideTitle(Intent intent) {
        int receivedFlightID = intent.getExtras()!=null?intent.getExtras().getInt("flightID",0):0;
        return "<INSERT RECEIVED FLIGHT ID>";
    }

    @Override
    public int provideLayout() {
        return R.layout.activity_flight_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expandableEnvironmentLinearLayout = findViewById(R.id.expandableEnvironmentLinearLayout);
        expandableEnvironmentLinearLayout.setVisibility(View.GONE);
        expandableFlightLinearLayout = findViewById(R.id.expandableFlightLinearLayout);
        expandableFlightLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void environmentContents(View v){
        expandableEnvironmentLinearLayout.setVisibility(
                expandableEnvironmentLinearLayout.isShown() ? View.GONE : View.VISIBLE);

        ScrollView scrollview = findViewById(R.id.scrol);
        scrollview.post(() -> {
            Button env =findViewById(R.id.environmentExp);
            int env_Pos = env.getTop();
            scrollview.scrollTo(0,env_Pos);
        });
    }

    public void Flight_contents(View v){
        expandableFlightLinearLayout.setVisibility(
                expandableFlightLinearLayout.isShown() ? View.GONE : View.VISIBLE);

        ScrollView scrollview = findViewById(R.id.scrol);
        scrollview.post(
                () -> {
            Button env =findViewById(R.id.flightExp);
            int env_Pos = env.getTop();
            scrollview.scrollTo(0,env_Pos);
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    }