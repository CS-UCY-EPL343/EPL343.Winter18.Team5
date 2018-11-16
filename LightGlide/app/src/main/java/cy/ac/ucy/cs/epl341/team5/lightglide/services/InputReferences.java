package cy.ac.ucy.cs.epl341.team5.lightglide.services;

import android.support.annotation.NonNull;

import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Acceleration;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.GPSCoordinates;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Velocity;

public enum InputReferences {
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

    InputReferences(String simpleName) {
        this.name = simpleName;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
