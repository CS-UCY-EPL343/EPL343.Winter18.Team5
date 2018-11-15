package cy.ac.ucy.cs.epl341.team5.lightglide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class FlightRecord extends AppCompatActivity {

    LinearLayout expandableEnvironmentLinearLayout;
    LinearLayout expandableFlightLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_record);

        Intent intent = getIntent();
        int receivedFlightID = (int) intent.getExtras().get("flightID");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("WanLink");//receivedFlight.getName());
        expandableEnvironmentLinearLayout = findViewById(R.id.expandableEnvironmentLinearLayout);
        expandableEnvironmentLinearLayout.setVisibility(View.GONE);
        expandableFlightLinearLayout = findViewById(R.id.expandableFlightLinearLayout);
        expandableFlightLinearLayout.setVisibility(View.GONE);
//        AppDatabase db = AppDatabase.Companion.getDB(this);
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
        scrollview.post(new Runnable(){

            @Override
            public void run() {
                Button env =findViewById(R.id.environmentExp);
                int env_Pos = env.getTop();
                scrollview.scrollTo(0,env_Pos);
            }
        });
    }

    public void Flight_contents(View v){
        expandableFlightLinearLayout.setVisibility(
                expandableFlightLinearLayout.isShown() ? View.GONE : View.VISIBLE);

        ScrollView scrollview = findViewById(R.id.scrol);
        scrollview.post(new Runnable(){

            @Override
            public void run() {
                Button env =findViewById(R.id.flightExp);
                int env_Pos = env.getTop();
                scrollview.scrollTo(0,env_Pos);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    }