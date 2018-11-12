package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cy.ac.ucy.cs.epl341.team5.lightglide.History;
import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class MainActivity extends AppCompatActivity {
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backButton = (Button) findViewById(R.id.button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onHistoryClick();

            }
        });
    }
    public void onHistoryClick(){
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }

}
