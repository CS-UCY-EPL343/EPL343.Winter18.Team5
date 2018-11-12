package cy.ac.ucy.cs.epl341.team5.lightglide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;

import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;

public class History extends AppCompatActivity {

    private ArrayList<Flight> flights = new ArrayList<Flight>();
    private ListView flightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        flightList = (ListView) findViewById(R.id.flightList);

        Flight f = new Flight("wanLink", new Timestamp(0L), 50, 590.34, new Timestamp(1L), 87 );
        for (int i=0; i<20; i++){
            flights.add(f);
        }
        CustomAdapter customAdapter = new CustomAdapter();
        flightList.setAdapter(customAdapter);


        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return flights.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.flight_info_view,null);

            TextView textViewN = view.findViewById(R.id.textViewN);
            TextView textViewT = view.findViewById(R.id.textViewT);

            textViewN.setText(flights.get(i).getName());
            textViewT.setText(flights.get(i).getStart().toString());

            return view;
        }
    }
}
