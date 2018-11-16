package cy.ac.ucy.cs.epl341.team5.lightglide.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.AppDatabase


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashScreen : ParentActivity() {
    override fun optionsMenu(): Int {
        return NOT_APPLICABLE;
    }

    override fun handleMenu(item: MenuItem?): Boolean {
        return false
    }

    override fun provideLayout() = NOT_APPLICABLE;

    override fun provideTitle(intent: Intent?) = "Light Gliding"

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this,MainActivity::class.java))
        AppDatabase.Companion.DBInitializerTask().execute(applicationContext)
        finish()
    }
}
