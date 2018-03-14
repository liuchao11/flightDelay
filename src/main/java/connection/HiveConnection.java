package connection;

import java.sql.*;
import java.util.*;


import conf.ConfigurationManager;
import constants.Constants;
import org.apache.hive.jdbc.HiveDriver;
import util.TimeUtil;


/**
 * 用于接近数据仓库
 *
 * @author cliu_yjs15
 */
public class HiveConnection {

    static Connection connection;
    static String url = ConfigurationManager.getProperty(Constants.HIVE_URL);
    static String user = ConfigurationManager.getProperty(Constants.HIVE_USER);
    static String password = ConfigurationManager.getProperty(Constants.HIVE_PASSWORD);

    static {
        try {
            new HiveDriver();
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 返回hive查询结果
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public ResultSet getData(String sql) throws Exception {
        ResultSet resultSet = null;
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.execute(sql);
        resultSet = statement.getResultSet();
        return resultSet;
    }

    /**
     * 设置纵坐标
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public HashMap<String, List<HashMap<String, String>>> setColumns(ResultSet resultSet) throws SQLException {
        //HashMap<String,HashMap<String, List<HashMap<String, String>>>> columnsHashMap = new HashMap<>();
        HashMap<String, List<HashMap<String, String>>> columnHashMap = new HashMap<>();
        List<HashMap<String, String>> lists = new ArrayList<>();
/*
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", resultSetMetaData.getColumnName(i + 1).split("\\.")[1]);
            hashMap.put("label", resultSetMetaData.getColumnName(i + 1).split("\\.")[1].toUpperCase());
            lists.add(hashMap);
        }
*/
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", "grade");
        hashMap.put("label", "延误等级");
        lists.add(hashMap);
        columnHashMap.put("column", lists);
        return columnHashMap;
    }

    /**
     * 设置横坐标
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public HashMap<String, List<HashMap<String, String>>> setRows(ResultSet resultSet) throws SQLException {
        HashMap<String, HashMap<String, List<HashMap<String, String>>>> rowsHashMap = new HashMap<>();
        HashMap<String, List<HashMap<String, String>>> rowHashMaps = new HashMap<>();
        List<HashMap<String, String>> lists = new ArrayList<>();
/*
        while (resultSet.next()) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", resultSet.getString(1));
            hashMap.put("label", resultSet.getString(1).toUpperCase());
            lists.add(hashMap);
        }
        */
        while (resultSet.next()){
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", resultSet.getString(3));
            hashMap.put("label", resultSet.getString(3).toUpperCase());
            lists.add(hashMap);
        }
        rowHashMaps.put("row", lists);
        //rowsHashMap.put("rows",rowHashMaps);
        return rowHashMaps;
    }

    /**
     * 设置数据
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */

    public HashMap<String, List<HashMap<String, String>>> setData(ResultSet resultSet) throws SQLException {
        HashMap<String, HashMap<String, List<HashMap<String, String>>>> datasHashMap = new HashMap<>();
        HashMap<String, List<HashMap<String, String>>> dataSetHashMaps = new HashMap<>();
        List<HashMap<String, String>> lists = new ArrayList<>();
        List<HashMap<String, String>> Tablelists = new ArrayList<>();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        while (resultSet.next()) {
/*
            String rowid = resultSet.getString(1);//第一列名称
            for (int i = 1; i < resultSetMetaData.getColumnCount(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                String columnid = resultSetMetaData.getColumnName(i + 1).split("\\.")[1];
                String value = String.valueOf(resultSet.getFloat(columnid));
                hashMap.put("rowid", rowid);
                hashMap.put("columnid", columnid);
                hashMap.put("value", value);
                lists.add(hashMap);
            }
           */
            HashMap<String, String> hashMap = new HashMap<>();
            String rowid = resultSet.getString(3);
            String columnid = "grade";
            String value = String.valueOf(resultSet.getString(10));
            hashMap.put("rowid", rowid);
            hashMap.put("columnid", columnid);
            hashMap.put("value", value);
            lists.add(hashMap);

            //右边表格属性
            HashMap<String, String> tableHashMap = new HashMap<>();;
            tableHashMap.put("OriginAirportID",resultSet.getString(6));
            tableHashMap.put("DestAirportID",resultSet.getString(7));
            tableHashMap.put("CRSDepTime", TimeUtil.getHour(resultSet.getString(8)));
            tableHashMap.put("CRSArrTime",TimeUtil.getHour(resultSet.getString(9)));
            tableHashMap.put("PREDICTArrTime",resultSet.getString(10));//估计到达时间
            Tablelists.add(tableHashMap);
        }
        dataSetHashMaps.put("data", lists);

        //datasHashMap.put("dataset",dataSetHashMaps);
        return dataSetHashMaps;
    }

    /**
     * 这是chart
     *
     * @param arrayLists
     * @return
     */
    public List<HashMap<String, String>> setChart(List<String> arrayLists) {
        HashMap<String, List<HashMap<String, String>>> chartHashMap = null;
        List<HashMap<String, String>> lists = new ArrayList<>();
        for (String str : arrayLists) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(str.split("-")[0], str.split("-")[1]);
            lists.add(hashMap);
        }
        //chartHashMap.put("chart",lists);
        return lists;
    }

    private void setColor(ResultSet resultSet, List<String> attrLists) {
        HashMap<String, List<HashMap<String, String>>> colorRangeHashMaps = new HashMap<>();
        List<HashMap<String, String>> lists = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            if (i == 0) {
                hashMap.put("code", "#e24b1a");
                hashMap.put("minValue", "1");
                hashMap.put("maxValue", "5");
                hashMap.put("label", "Bad");
                lists.add(hashMap);
            } else if (i == 1) {
                hashMap.put("code", "#f6bc33");
                hashMap.put("minValue", "5");
                hashMap.put("maxValue", "7");
                hashMap.put("label", "Average");
                lists.add(hashMap);
            } else if (i == 2) {
                hashMap.put("code", "#6da81e");
                hashMap.put("minValue", "7");
                hashMap.put("maxValue", "10");
                hashMap.put("label", "Good");
                lists.add(hashMap);
            }
        }
        colorRangeHashMaps.put("color", lists);
        List<HashMap<String, String>> listss = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            if (i == 0) {
                hashMap.put("gradient", "1");
                listss.add(hashMap);
            } else if (i == 1) {
                hashMap.put("minValue", "0");
                listss.add(hashMap);
            } else if (i == 2) {
                hashMap.put("code", "#6da81e");
                listss.add(hashMap);
            }
        }
    }

    /*public ArrayList executeQueryForFusionChart(String sql) throws Exception {
        Gson gson = new Gson();
        ResultSet resultSet = getData(sql);
        HashMap<String,List<HashMap<String,String>>>  charObj = setChart(Arrays.asList(
                "caption-航班延误原因统计饼图",
                "subCaption-五大原因",
                "numberPrefix-%",
                "showPercentValues-1",
                "decimals-3",
                "showPercentInTooltip-0",
                "decimals-3",
                "theme-fint"));

        HashMap<String, HashMap<String, List<HashMap<String, String>>>> rowObj = setRows(resultSet);
        resultSet.beforeFirst();
        HashMap<String,HashMap<String, List<HashMap<String, String>>>> columnObj = setColumns(resultSet);
        resultSet.beforeFirst();
        HashMap<String,HashMap<String, List<HashMap<String, String>>>> dataObj = setData(resultSet);
*/


//
//		return null;
//	}

}
