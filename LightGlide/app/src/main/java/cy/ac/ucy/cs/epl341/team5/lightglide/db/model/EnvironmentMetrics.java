package cy.ac.ucy.cs.epl341.team5.lightglide.db.model;

public final class EnvironmentMetrics {
    final int id;
    final int uv;
    final int temp;
    final double humidity;
    final double pressure;

    EnvironmentMetrics(int id, int uv, int temp, double humidity, double pressure) {
        this.id = id;
        this.uv = uv;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
    }
}
