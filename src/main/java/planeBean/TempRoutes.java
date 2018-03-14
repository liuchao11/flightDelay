package planeBean;


import java.io.IOException;

import connection.HBaseConnection;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Acars表是作为数据总表，TempRoutes作为临时表，可以只存储飞机唯一标示和经纬度信息
 */
public class TempRoutes {
    static String tableName, colFamilyName, values;
    HBaseConnection hBaseConnection = new HBaseConnection();

    public TempRoutes(String tableName1, String colFamilyName1, String value1) throws IOException {
        this.tableName = tableName1;
        this.colFamilyName = colFamilyName1;
        this.values = value1;
        if (!hBaseConnection.tableExists(TableName.valueOf(tableName1))) {
            hBaseConnection.createTable(tableName1, new String[]{colFamilyName1});
        }
    }

    public void putToTable() throws IOException, ClassNotFoundException {
        //解析数据
        String[] value = values.split("\040");
        String id = value[2];//飞机国籍登记号
        String flight_Number = value[3].concat(value[4]);//航班号
        String depature_Airport = value[7];//起飞机场
        String landing_Airport = value[8];//降落机场
        String lat = value[11];
        String lng = value[12];
        String altitude = value[13];
        //插入一行
        Put put = new Put(Bytes.toBytes(id));
        put.addColumn(Bytes.toBytes(colFamilyName), Bytes.toBytes("flight_Number"), Bytes.toBytes(flight_Number));
        put.addColumn(Bytes.toBytes(colFamilyName), Bytes.toBytes("depature_Airport"), Bytes.toBytes(depature_Airport));
        put.addColumn(Bytes.toBytes(colFamilyName), Bytes.toBytes("landing_Airport"), Bytes.toBytes(landing_Airport));
        put.addColumn(Bytes.toBytes(colFamilyName), Bytes.toBytes("lat"), Bytes.toBytes(lat));
        put.addColumn(Bytes.toBytes(colFamilyName), Bytes.toBytes("lng"), Bytes.toBytes(lng));
        put.addColumn(Bytes.toBytes(colFamilyName), Bytes.toBytes("altitude"), Bytes.toBytes(altitude));
        hBaseConnection.insertPatch(tableName, put);

    }

}

