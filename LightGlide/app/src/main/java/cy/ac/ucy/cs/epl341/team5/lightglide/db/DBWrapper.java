package cy.ac.ucy.cs.epl341.team5.lightglide.db;


import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.EnvironmentMetrics;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Flight;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Metrics;

public class DBWrapper {
    private static DBWrapper singleton;
    private static SQLiteDatabase db;
    private final static String DATABASE_NAME = "lightglide";
    private final static Supplier<SQLiteDatabase> dbinitializer =
            () -> SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, new LGCursorFactory());

    public DBWrapper newDBWrapper() {
        db = Optional.ofNullable(db).orElseGet(dbinitializer);
        singleton = Optional.ofNullable(singleton).orElseGet(() -> new DBWrapper(db));
        return singleton;
    }

    private DBWrapper(SQLiteDatabase db) {

    }


    public Collection<Flight> getFlights() {
        return null;
    }

    public Collection<EnvironmentMetrics> getEnvironmentMetrics() {
        return null;
    }

    public Collection<Metrics> getMetrics() {
        return null;
    }

    public Collection<Metrics> getMetrics(Flight f) {
        return null;
    }

    public Collection<EnvironmentMetrics> getEnvironmentMetrics(Flight f) {
        return null;
    }

    public boolean updateFlight(Flight f) {
        return false;
    }

    public boolean close() {
        try {
            db.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

class LGCursorFactory implements SQLiteDatabase.CursorFactory {

    @Override
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
        return null;
    }
}