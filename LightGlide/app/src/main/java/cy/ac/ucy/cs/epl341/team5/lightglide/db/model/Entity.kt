package cy.ac.ucy.cs.epl341.team5.lightglide.db.model

import android.arch.persistence.room.*
import java.sql.Timestamp


data class Velocity(val x: Double, val y: Double, val z: Double);

@Entity
data class Flight(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "name") var name: String?,
        @ColumnInfo(name = "start") val start: Timestamp,
        @ColumnInfo(name = "maxAltitude") val maxAltitude: Int,
        @ColumnInfo(name = "distance") val distance: Double,
        @ColumnInfo(name = "end") val end: Timestamp,
        @ColumnInfo(name = "duration") val duration: Int
)

@Entity(
        primaryKeys = ["id"],
        foreignKeys = [
            ForeignKey(
                    parentColumns = ["id"],
                    childColumns = ["fid"],
                    entity = Flight::class
            ),
            ForeignKey(
                    parentColumns = ["timestamp"],
                    childColumns = ["timestamp"],
                    entity = EnvironmentMetrics::class
            )
        ],
        indices = arrayOf(Index("timestamp"),Index("fid"))
)
data class FlightPoint(
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "timestamp") val timestamp: Timestamp,
        @ColumnInfo(name = "fid") val fid: Int
)

@Entity(indices = arrayOf(Index("fpid")))
data class EnvironmentMetrics(
        @ColumnInfo(name = "uv") val uv: Int,
        @PrimaryKey val timestamp: Timestamp,
        @ColumnInfo(name = "temperature") val temperature: Int,
        @ColumnInfo(name = "humidity") val humidity: Int,
        @ColumnInfo(name = "pressure") val pressure: Double,
        @ColumnInfo(name = "fpid") val fpid: Int
)

@Entity(
        primaryKeys = ["timestamp"],
        foreignKeys = [
            ForeignKey(
                    entity = FlightPoint::class,
                    parentColumns = ["id"],
                    childColumns = ["fpid"]
            )
        ],
        indices = arrayOf(Index("fpid"),Index("timestamp"))
)
data class Metrics(
        @ColumnInfo(name = "altitude") val altitude: Double,
        @ColumnInfo(name = "descend_rate") val descendRate: Double,
        @PrimaryKey val timestamp: Timestamp,
        val gpsCoordinates: GPSCoordinates,
        val acceleration: Acceleration,
        @ColumnInfo(name = "fpid") val fpid: Int
)

data class GPSCoordinates constructor(
        @ColumnInfo(name = "longtitude") val longtitude: Double,
        @ColumnInfo(name = "latitude") val latitude: Double,
        @ColumnInfo(name = "altitude") val altitude: Double
)

data class Acceleration(
        @ColumnInfo(name = "ax") val x: Double,
        @ColumnInfo(name = "ay") val y: Double,
        @ColumnInfo(name = "az") val z: Double
)


data class NamedAndTimedFlight(
        val name:String,
        val id:Int,
        val start: Timestamp
)