package cy.ac.ucy.cs.epl341.team5.lightglide.db.model;

import java.sql.Timestamp;

public final class Flight {
    final int id;
    final String name;
    final Timestamp start;
    final Timestamp end;
    final int max_altitude;
    final double distnace;
    final double max_velocity;
    final int duration;


    Flight(int id, String name, Timestamp start, Timestamp end, int max_altitude, double distnace, double max_velocity, int duration) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.max_altitude = max_altitude;
        this.distnace = distnace;
        this.max_velocity = max_velocity;
        this.duration = duration;
    }
}
