package cy.ac.ucy.cs.epl341.team5.lightglide.db.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import android.os.AsyncTask
import java.util.*

// File: AppDatabase.java
@Database(entities = arrayOf(EnvironmentMetrics::class, FlightPoint::class, Flight::class), version = 1)
@TypeConverters(DataTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun environmentMetricsDao(): EnvironmentMetricsDao
    abstract fun flightPointDao(): FlightPointDao
    abstract fun flightDao(): FlightDao

    companion object {
        private var optdb: Optional<AppDatabase> = Optional.ofNullable(null)
        fun getDB(context: Context): AppDatabase {
            optdb = if (optdb.isPresent) optdb
            else Optional.of(
                    Room.databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            "lightglide")
                            .build()
            )
            return optdb.get()
        }

        class DBInitializerTask: AsyncTask<Context, Int, AppDatabase>() {
            override fun doInBackground(vararg params: Context?): AppDatabase? {
                if (optdb.isPresent) return optdb.get()
                else {
                    if(params.size>0 && params[0]!=null){
                        return getDB(params[0]!!)
                    }
                    else{
                        return null;
                    }
                }
            }
        }
    }
}

