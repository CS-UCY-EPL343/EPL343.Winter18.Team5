package cy.ac.ucy.cs.epl341.team5.lightglide.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;

import static cy.ac.ucy.cs.epl341.team5.lightglide.R.layout.fragment_general;


public class GeneralFragment extends Fragment{

    Flight flight;
    public GeneralFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        flight = new Flight(0, "Flight 00-01", new Timestamp(12000), 3000, 34.44, new Timestamp(500000), 4);
//        TextView tv=container.findViewById(R.id.textTitle);
//        tv.setText("Flight 00-01");
//
//        tv= container.findViewById(R.id.textDate);
//        Date date = new Date(flight.getStart().getTime() * 1000);
//        tv.setText("Date: " + new SimpleDateFormat("dd.MM.yyyy").format(date));
//
//        tv = container.findViewById(R.id.textStartTime);
//        String timeS = new SimpleDateFormat("hh:mm:ss").format(date);
//
//        tv = container.findViewById(R.id.textDistance);
//        tv.setText("Distance: " + (int) flight.getDistance() + "Km");
//
//        tv = container.findViewById(R.id.textDuration);
//        tv.setText("Duration: " + (int) (flight.getDuration()/60.0) + "min");
//        Inflate the layout for this fragment

        return inflater.inflate(fragment_general, container, false);
    }

}