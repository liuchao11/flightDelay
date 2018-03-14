package planeBean;

/**
 * Created by cliu_yjs15 on 2018/3/11.
 */
public class DateBean {
    private  String startDay;
    private  String endDay;



    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public DateBean(String startYear, String startMoth, String startDay, String endYear, String endMoth, String endDay) {

        this.startDay = startDay;
        this.endDay = endDay;
    }

    public DateBean() {
        super();
    }
}
