package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FlightMode extends AppCompatActivity {
    private String[] names={"acceleration", "velocity", "temperature", "uv", "gps", "humidity", "pressure"};
    private Boolean[] useThem= new Boolean[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_mode);
        getSupportActionBar().setTitle("Flight Mode");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        for(int i=0; i<7; i++)
            useThem[i]=prefs.getBoolean(names[i], true);
        final Button button = findViewById(R.id.terminate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
