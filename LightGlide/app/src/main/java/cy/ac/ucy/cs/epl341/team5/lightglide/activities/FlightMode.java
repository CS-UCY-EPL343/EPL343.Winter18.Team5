package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

public class FlightMode extends ParentActivity {
    private String[] names={"acceleration", "velocity", "temperature", "uv",  "humidity", "pressure"};
    private Boolean[] useThem= new Boolean[7];
    private Integer[] val=new Integer[7];
    @Override
    protected String provideTitle(Intent intent) {
        return "Flight Mode";
    }

    @Override
    protected boolean handleMenu(MenuItem item) {
        return false;
    }

    @Override
    protected int provideLayout() {
        return R.layout.activity_flight_mode;
    }

    @Override
    protected int optionsMenu() {
        return NOT_APPLICABLE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        for(int i=0; i<6; i++) {
            useThem[i] = prefs.getBoolean(names[i], true);
            val[i]=new Integer(0);
        }
        RelativeLayout relLayout =  (RelativeLayout) findViewById(R.id.Realtime);
        LinearLayout lL = new LinearLayout(this);
        lL.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lL.setLayoutParams(params);
        for (int i=0;i< 6;i++) {
            if(useThem[i]) {
                TextView tv = new TextView(this);
                tv.setId(i);
                String str = names[i].toUpperCase()+": ";
                tv.setText(str);
                //Ask how to get theme color
                //tv.setTextColor(Color.BLACK);
                tv.setPadding(80, 20, 80, 16);
                lL.addView(tv);


                TextView value = new TextView(this);
                value.setTextSize(36);
                value.setId(i + 69);
                str = val[i].toString();
                value.setText(str);
                value.setGravity(Gravity.CENTER);
                //Ask how to get theme color
                value.setPadding(80, 8, 80, 20);
                lL.addView(value);
                View v = new View(this);
                v.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        5
                ));
                v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                lL.addView(v);


            }
        }
        relLayout.addView(lL);
        final Button button = findViewById(R.id.terminate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
