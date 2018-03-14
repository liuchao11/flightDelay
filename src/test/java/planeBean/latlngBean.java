package planeBean;

public class latlngBean {
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

    public latlngBean(String latitute, String longitute) {
        super();
        this.latitute = latitute;
        this.longitute = longitute;
    }


}
