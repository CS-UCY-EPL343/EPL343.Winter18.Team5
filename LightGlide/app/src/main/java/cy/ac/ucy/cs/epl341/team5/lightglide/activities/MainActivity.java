package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.AppDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppDatabase appdb = new AsyncTask<Context,Integer, AppDatabase>() {
            @Override
            protected AppDatabase doInBackground(Context... contexts) {
                return AppDatabase.Companion.getDB(contexts[0]);
            }
        }.doInBackground(getApplicationContext());
        Log.wtf("DB",""+appdb.toString());
    }
}
