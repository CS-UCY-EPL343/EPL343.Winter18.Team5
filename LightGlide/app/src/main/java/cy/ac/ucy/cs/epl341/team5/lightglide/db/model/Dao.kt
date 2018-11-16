package cy.ac.ucy.cs.epl341.team5.lightglide.db.model

import android.arch.persistence.room.*

@Dao
interface EnvironmentMetricsDao {

    /** Data Requests **/
    @Query("SELECT * FROM environmentmetrics")
    fun getAll(): List<EnvironmentMetrics>

    @Insert
    fun insertAll(vararg metrics: EnvironmentMetrics)

    @Delete
    fun delete(metrics: EnvironmentMetrics)

    @Update
    fun update(metrics: EnvironmentMetrics)

}

@Dao
interface MetricsDao {
    /** Data Requests **/
    @Query("SELECT * FROM Metrics")
    fun getAll(): List<Metrics>

    @Query("SELECT * FROM Metrics m where m.fpid = :fpid")
    fun getFor(fpid: Int): List<Metrics>

    @Insert
    fun insertAll(vararg metrics: Metrics)

    @Delete
    fun delete(metrics: Metrics)

    @Update
    fun update(metrics: Metrics)

}

@Dao
interface FlightPointDao {
    @Query("SELECT * FROM flightpoint")
    fun getAll(): List<FlightPoint>

    @Query("SELECT * FROM flightpoint fp WHERE fp.fid=:flightID")
    fun getFor(flightID: Int): List<FlightPoint>

    @Insert
    fun insertAll(vararg flights: FlightPoint)

    @Update
    fun update(fp:FlightPoint)
}

@Dao
interface FlightDao {

    @Query("SELECT * FROM flight")
    fun getAll(): List<Flight>

    @Query("SELECT * FROM FLIGHT WHERE FLIGHT.ID =:id ")
    fun get(id: Int): Flight

    @Query("SELECT ID, NAME, START FROM FLIGHT")
    fun getNamesAndStart(): List<NamedAndTimedFlight>

    @Insert
    fun insertAll(vararg flights: Flight)

    @Delete
    fun delete(flight: Flight)

    @Update
    fun udpate(flight: Flight)
}