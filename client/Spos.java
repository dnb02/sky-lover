import java.io.Serializable;
public class Spos implements Serializable{
    private double latitude;
    private double longitude;
    private double airPressure;
    private double elevation;
    private double temperature;
    private String sunpos;

    public Spos(double latitude, double longitude, double airPressure, double elevation, double temperature, String sunpos) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.airPressure = airPressure;
        this.elevation = elevation;
        this.temperature = temperature;
        this.sunpos = sunpos;
    }

    // Getters and setters for each field

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(double airPressure) {
        this.airPressure = airPressure;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getSunpos() {
        return sunpos;
    }

    public void setSunpos(String sunpos) {
        this.sunpos = sunpos;
    }

    public String toString() {
        return "Latitude: " + latitude + "\n" +
                "Longitude: " + longitude + "\n" +
                "Air Pressure: " + airPressure + " hPa\n" +
                "Elevation: " + elevation + " meters\n" +
                "Temperature: " + temperature + " °C\n" +
                "Sun Position: " + sunpos;
    }
}

