package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import java.util.stream.Collectors;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.AppDatabase;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;

public class History extends ParentActivity {

    private ArrayList<Flight> flights = new ArrayList<>();
    private ListView flightList;

    private CustomAdapter customAdapter;
    private final int reqCode = 0;
    private final int renameFlag = 1;
    private final int deleteFlag = -1;

    @Override
    public String provideTitle(Intent ignored) {
        return getString(R.string.title_activity_history);
    }

    @Override
    protected boolean handleMenu(MenuItem item) {
        return true;
    }

    @Override
    public int provideLayout() {
        return R.layout.activity_history;
    }

    @Override
    protected int optionsMenu() {
        return NOT_APPLICABLE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flightList = findViewById(R.id.flightList);
        for (int i=0; i<10; i++){
            Timestamp timestampStart = new Timestamp ((long) (Math.random() * 10000000) + (1532575640));
            Timestamp timestampEnd = new Timestamp ((long) ((Math.random() * 50800) + timestampStart.getTime()));
            System.out.println(timestampStart);
            System.out.println(timestampEnd.toString());
            int maxAltitude = (int) (Math.random() * 1000) + 50;
            double distance = (float) (Math.random() * 1000);
            int duration = (int) ((timestampEnd.getTime() - timestampStart.getTime()));
            flights.add(new Flight(i, "Flight Record - " + i, timestampStart, maxAltitude, distance, timestampEnd, duration ));
        }

        customAdapter = new CustomAdapter();
        flightList.setAdapter(customAdapter);

        flightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(getApplicationContext(), FlightRecord.class);
                intent.putExtra("flightID", flights.get(i).getId());
                intent.putExtra("flightName", flights.get(i).getName());
                intent.putExtra("flightStart", flights.get(i).getStart().getTime());
                intent.putExtra("flightMaxAlt", flights.get(i).getMaxAltitude());
                intent.putExtra("flightDistance", flights.get(i).getDistance());
                intent.putExtra("flightEnd", flights.get(i).getEnd().getTime());
                intent.putExtra("flightDuration", flights.get(i).getDuration());

                startActivityForResult(intent, reqCode);
            }
        });

        Log.d("DB:>",AppDatabase.Companion.getDB(this)==null?"DBNULL":"NOT NULL");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == deleteFlag){
            int id = data.getExtras().getInt("id");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                flights = flights.stream().filter((flight -> flight.getId()!=id)).collect(Collectors.toCollection(ArrayList::new));

            }else {
                int index = 0;
                for (; flights.get(index).getId() != id; index++) ;
                flights.remove(index);
            }
        }
        else if (resultCode == renameFlag){

        }

        customAdapter = new CustomAdapter();
        flightList.setAdapter(customAdapter);
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
