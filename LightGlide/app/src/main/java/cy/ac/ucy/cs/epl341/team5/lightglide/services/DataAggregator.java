package cy.ac.ucy.cs.epl341.team5.lightglide.services;

import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Acceleration;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.GPSCoordinates;
import cy.ac.ucy.cs.epl341.team5.lightglide.db.model.Velocity;

public class DataAggregator {
    public DataAggregator(Bus.LocalBinder b) {
        registerFunctions(b.getService());
    }

    private void registerFunctions(Bus b) {
        for (InputReferences md:InputReferences.values()) {

        }
    }
    private void consumeAcceleration(Acceleration a){

    }
    private void consumeGPSCoords(GPSCoordinates gps){

    }
    private void consumeVelocity(Velocity v){

    }
    private void consumeTemperatore(Double temp){

    }
    private void consumeHumidity(Double humidity){

    }
    private void consumeAltitude(Double altitude){

    }
    private void consumeDescendRate(Double rate){

    }
    private void consumeUV(Double uv){

    }
    private void consumePressure(Double press){

    }
}

