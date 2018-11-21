package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.sql.Timestamp;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;

public class FlightRecord extends ParentActivity {

    Flight flight;
    LinearLayout expandableEnvironmentLinearLayout;
    LinearLayout expandableFlightLinearLayout;

    @Override
    public String provideTitle(Intent intent) {
        int receivedFlightID = intent.getExtras()!=null?intent.getExtras().getInt("flightID",0):0;
        return "<INSERT RECEIVED FLIGHT ID>";
    }

    @Override
    protected boolean handleMenu(MenuItem item) {
        if (item.getItemId() == (R.id.editButton)){
            Intent intent = new Intent(this, PopRename.class);
            startActivity(intent);
            int id = flight.getId();
            intent.putExtra("id",id);
            setResult(1, intent);
            finish();
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

        flight = new Flight(0, "wanLink", new Timestamp(0L), 50, 590.34, new Timestamp(1L), 87 );

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