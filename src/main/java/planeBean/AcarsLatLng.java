package planeBean;

public class AcarsLatLng {
    private String latitute;
    private String longitute;

    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public AcarsLatLng(String latitute, String longitute) {
        super();
        this.latitute = latitute;
        this.longitute = longitute;
    }

    public AcarsLatLng() {
        super();
        // TODO Auto-generated constructor stub
    }


}
