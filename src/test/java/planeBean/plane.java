package planeBean;

public class plane {
    private String lat = null;
    private String lng = null;
    private String landing_Airport = null;
    private String flight_Number = null;
    private String depature_Airport = null;
    private String altitude = null;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLanding_Airport() {
        return landing_Airport;
    }

    public void setLanding_Airport(String landing_Airport) {
        this.landing_Airport = landing_Airport;
    }

    public String getFlight_Number() {
        return flight_Number;
    }

    public void setFlight_Number(String flight_Number) {
        this.flight_Number = flight_Number;
    }

    public String getDepature_Airport() {
        return depature_Airport;
    }

    public void setDepature_Airport(String depature_Airport) {
        this.depature_Airport = depature_Airport;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public plane(String lat, String lng, String landing_Airport,
                 String flight_Number, String depature_Airport,
                 String altitude) {
        super();
        this.lat = lat;
        this.lng = lng;
        this.landing_Airport = landing_Airport;
        this.flight_Number = flight_Number;
        this.depature_Airport = depature_Airport;
        this.altitude = altitude;
    }


}
