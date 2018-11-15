package cy.ac.ucy.cs.epl341.team5.lightglide;

import android.app.Application;
import android.app.Instrumentation;
import android.arch.persistence.room.Room;
import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Timestamp;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.AppDatabase;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

