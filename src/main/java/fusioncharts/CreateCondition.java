package fusioncharts;

import com.google.gson.Gson;
import connection.MySQLConnection;
import connection.HiveConnection;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by cliu_yjs15 on 2018/3/3.
 */
public class CreateCondition {
    /**
     * 封装从数据库获取的数据
     *
     * @param sql
     * @param object
     * @return
     * @throws SQLException
     */
    public ArrayList getData(String sql, Object[] object) throws Exception {
        ArrayList data = null;
        if (object != null) {
            MySQLConnection mySQLConnection = MySQLConnection.getInstance();
            data = mySQLConnection.executeQueryForFusionChart(sql, object);
        } else {
            HiveConnection hiveConnection = new HiveConnection();
            //data = hiveConnection.executeQueryForFusionChart(sql);
        }
        return data;

    }

    /**
     * 封装图的字段属性
     * 传过来的属性必须都以"-"作为分隔符
     */
    public Map<String, String> setAttribute(List<String> lists) {
        Map<String, String> chartObj = new HashMap<String, String>();
        for (String str : lists) {
            chartObj.put(str.split("-")[0], str.split("-")[1]);
        }
        return chartObj;
    }

    /**
     * 实例化FusionCharts
     *
     * @param attributes      封装的图表属性
     * @param delayReasonData 封装的数据
     * @param type            type of chart
     * @param chartID         type of chart
     * @param width
     * @param height
     * @param chartContainer  type of chart
     * @param dataFormat      type of chart
     * @return
     */
    public FusionCharts getFusionCharts(Map<String, String> attributes, ArrayList delayReasonData,
                                        String type, String chartID, String width, String height,
                                        String chartContainer, String dataFormat) {
        Gson gson = new Gson();
        Map<String, String> dataMap = new LinkedHashMap<String, String>();
        dataMap.put("chart", gson.toJson(attributes));
        dataMap.put("data", gson.toJson(delayReasonData));
        FusionCharts fusionChart = new FusionCharts(type, chartID, width, height, chartContainer, dataFormat, gson.toJson(dataMap));
        return fusionChart;

    }

}
