package planeBean;

public class AcarsBean {
    private String id;
    private String flight_Number;
    private String depature_Airport;
    private String landing_Airport;
    private String lat;
    private String lng;
    private String altitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLanding_Airport() {
        return landing_Airport;
    }

    public void setLanding_Airport(String landing_Airport) {
        this.landing_Airport = landing_Airport;
    }

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

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public AcarsBean(String id, String flight_Number, String depature_Airport,
                     String landing_Airport, String lat, String lng, String altitude) {
        super();
        this.id = id;
        this.flight_Number = flight_Number;
        this.depature_Airport = depature_Airport;
        this.landing_Airport = landing_Airport;
        this.lat = lat;
        this.lng = lng;
        this.altitude = altitude;
    }

    public AcarsBean() {

    }


}
