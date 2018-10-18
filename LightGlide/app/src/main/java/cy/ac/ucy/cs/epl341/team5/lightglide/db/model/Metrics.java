package cy.ac.ucy.cs.epl341.team5.lightglide.db.model;

import java.util.List;

public class Metrics {
    final int elevation;
    final int altitude;
    final List<Double> acceleration;
    final List<Double> gpsCoords;
    final double climb_descend_rate;

    Metrics(int elevation, int altitude, List<Double> acceleration, List<Double> gpsCoords, double climb_descend_rate) {
        this.elevation = elevation;
        this.altitude = altitude;
        this.acceleration = acceleration;
        this.gpsCoords = gpsCoords;
        this.climb_descend_rate = climb_descend_rate;
    }
}
