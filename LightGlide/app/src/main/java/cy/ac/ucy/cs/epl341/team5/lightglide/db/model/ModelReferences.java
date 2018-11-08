package cy.ac.ucy.cs.epl341.team5.lightglide.db.model;

import android.support.annotation.NonNull;

public enum ModelReferences {
    ACCELERATION(Acceleration.class.getSimpleName()),
    GPS_COORDINATES(GPSCoordinates.class.getSimpleName()),
    VELOCITY(Velocity.class.getSimpleName()),
    TEMPERATURE("Temperature"),
    HUMIDITY("Humidity"),
    ALTITUDE("Altitude"),
    DESCENT_RATE("Descent_rate"),
    VOICE("Voice"),
    UV("UV"),
    PRESSURE("Pressure");

    private final String name;

    ModelReferences(String simpleName) {
        this.name = simpleName;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
