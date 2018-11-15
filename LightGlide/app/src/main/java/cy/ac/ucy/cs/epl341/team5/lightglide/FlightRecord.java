package cy.ac.ucy.cs.epl341.team5.lightglide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class FlightRecord extends AppCompatActivity {

    LinearLayout expandableEnvironmentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_record);

        Intent intent = getIntent();
        String receivedName = intent.getStringExtra("name");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(receivedName);

        expandableEnvironmentLinearLayout = findViewById(R.id.expandableEnvironmentLinearLayout);
        expandableEnvironmentLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void toggle_contents(View v){
        expandableEnvironmentLinearLayout.setVisibility(
                expandableEnvironmentLinearLayout.isShown() ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
