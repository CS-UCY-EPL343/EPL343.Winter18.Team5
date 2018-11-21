package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;

public class FlightRecord extends ParentActivity {

    Flight flight;
    LinearLayout expandableEnvironmentLinearLayout;
    LinearLayout expandableFlightLinearLayout;

    @Override
    public String provideTitle(Intent intent) {
        int receivedFlightID = intent.getExtras()!=null?intent.getExtras().getInt("flightID",0):0;
        return intent.getStringExtra("flightName");
    }

    @Override
    protected boolean handleMenu(MenuItem item) {
        if (item.getItemId() == (R.id.editButton)){
            Intent intent = new Intent(this, PopRename.class);
            startActivity(intent);
            int id = flight.getId();
            intent.putExtra("id",id);
            setResult(1, intent);
            //finish();
        }
        else if (item.getItemId() == (R.id.deleteButton)){
            Intent intent = new Intent();
            int id = flight.getId();
            intent.putExtra("id",id);
            setResult(-1, intent);
            finish();
            }
        return true;
    }

    @Override
    public int provideLayout() {
        return R.layout.activity_ft;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expandableEnvironmentLinearLayout = findViewById(R.id.expandableEnvironmentLinearLayout);
        expandableEnvironmentLinearLayout.setVisibility(View.GONE);
        expandableFlightLinearLayout = findViewById(R.id.expandableFlightLinearLayout);
        expandableFlightLinearLayout.setVisibility(View.GONE);

        Intent intent = getIntent();
        flight = new Flight(intent.getExtras().getInt("flightID"),
                intent.getExtras().getString("flightName"),
                new Timestamp(intent.getExtras().getLong("flightStart")),
                intent.getExtras().getInt("maxAltitude"),
                intent.getExtras().getDouble("flightDistance"),
                new Timestamp(intent.getExtras().getLong("flightEnd")),
                intent.getExtras().getInt("flightDuration") );

        TextView tv = findViewById(R.id.dateTextView);

        Date date = new Date(flight.getStart().getTime() * 1000);
        tv.setText("Date: " + new SimpleDateFormat("dd.MM.yyyy").format(date));

        String timeS = new SimpleDateFormat("hh:mm:ss").format(date);
        date = new Date(flight.getEnd().getTime() * 1000);
        String timeE = new SimpleDateFormat("hh:mm:ss").format(date);
        tv = findViewById(R.id.timestampTextView);
        tv.setText("From: " + timeS + " - To: " + timeE);
        tv = findViewById(R.id.distanceTextView);
        tv.setText("Distance: " + (int) flight.getDistance() + "Km");
        tv = findViewById(R.id.durationTextView);
        tv.setText("Duration: " + (int) (flight.getDuration()/60.0) + "min");
    }

    public int optionsMenu(){
        return R.menu.edit_options;
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

    public void flightContents(View v){
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

    }