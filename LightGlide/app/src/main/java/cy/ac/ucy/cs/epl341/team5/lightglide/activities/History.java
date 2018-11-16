package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;

public class History extends ParentActivity {

    private ArrayList<Flight> flights = new ArrayList<>();
    private ListView flightList;

    @Override
    public String provideTitle(Intent ignored) {
        return getString(R.string.title_activity_history);
    }

    @Override
    public int provideLayout() {
        return R.layout.activity_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flightList = findViewById(R.id.flightList);

        Flight f = new Flight(0, "wanLink", new Timestamp(0L), 50, 590.34, new Timestamp(1L), 87 );
        for (int i=0; i<20; i++){
            flights.add(f);
        }

        CustomAdapter customAdapter = new CustomAdapter();
        flightList.setAdapter(customAdapter);

        flightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(getApplicationContext(), FlightRecord.class);
                intent.putExtra("flightID", flights.get(i).getId());
                startActivity(intent);
            }
        });
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
            Date date = new Date(flights.get(i).getStart().getTime() * 1000);
            textViewT.setText(new SimpleDateFormat("dd.MM.yyyy").format(date));

            return view;
        }
    }
}
