package cy.ac.ucy.cs.epl341.team5.lightglide.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cy.ac.ucy.cs.epl341.team5.lightglide.History

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashScreen : AppCompatActivity() {
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, History::class.java))
        finish()
    }
}
