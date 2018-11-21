package cy.ac.ucy.cs.epl341.team5.lightglide.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import cy.ac.ucy.cs.epl341.team5.lightglide.R;

public class PopDelete extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_delete);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels*0.75),(int)(dm.heightPixels*0.16));
    }

    public void onClickOk(View v){
        setResult(1);
        finish();
    }

    public void onClickCancel(View v){
        setResult(-1);
        finish();
    }
}
