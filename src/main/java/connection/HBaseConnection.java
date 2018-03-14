package connection;

import conf.ConfigurationManager;
import constants.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planeBean.latlngBean;
import planeBean.plane;
import util.JsonUtil;



/**
 * Created by xuemin on 15/9/28.
 */
public class HBaseConnection {

    public static Configuration configuration;
    public static Connection connection;
    public static Admin admin;

//     public static void main(String[] args) throws IOException {
//     createTable("t22",new String[]{"cf1","cf2"});
    // insterRow("t2", "rw1", "cf1", "q1", "val1");
    // queryByRowkey("t2", "rw1");
    // scanData("t2", "rw1", "rw2");
    // deleRow("t2","rw1","cf1","q1");
    // deleteTable("routes");
//         listTables();
//     }

    // 初始化链接
    public static void init() {

        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", ConfigurationManager.getProperty(Constants.HBASE_ZOOKEEPER_QUORUM));
        configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        // configuration.set("hbase.zookeeper.property.clientPort","2181");
        //configuration.set("zookeeper.znode.parent","/hbase");

        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 关闭连接
    public static void close() {
        try {
            if (null != admin)
                admin.close();
            if (null != connection)
                connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 建表
    public static void createTable(String tableNmae, String[] cols)
            throws IOException {

        init();
        TableName tableName = TableName.valueOf(tableNmae);

        if (admin.tableExists(tableName)) {
            System.out.println("talbe is exists!");
        } else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            for (String col : cols) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(col);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            System.out.println("建表成功");
            admin.createTable(hTableDescriptor);
        }
        close();
    }

    // 删表
    public static void deleteTable(String tableName) throws IOException {
        init();
        TableName tn = TableName.valueOf(tableName);
        if (admin.tableExists(tn)) {
            admin.disableTable(tn);
            admin.deleteTable(tn);
        }
        System.out.println("删除" + tableName + "成功");
        close();
    }

    // 查看已有表
    public static void listTables() throws IOException {
        init();
        HTableDescriptor hTableDescriptors[] = admin.listTables();
        for (HTableDescriptor hTableDescriptor : hTableDescriptors) {
            System.out.println(hTableDescriptor.getNameAsString());
        }
        close();
    }

    // 插入数据
    public static void insterRow(String tableName, String rowkey,
                                 String colFamily, String col, String val) throws IOException {
        init();
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(col),
                Bytes.toBytes(val));
        table.put(put);

        // 批量插入
        /*
         * List<Put> putList = new ArrayList<Put>(); puts.add(put);
         * table.put(putList);
         */
        table.close();
        close();
    }

    /**
     * 批量插入,使用泛型方法，针对List<Put>或者单个Put度可以自动插入
     *
     * @param tableName
     * @param puts
     * @throws IOException
     */
//     public static void insertPatch(String tableName,List<Put> puts) throws IOException{
//         init();
//         Table table = connection.getTable(TableName.valueOf(tableName));
//         table.put(puts);
//         table.close();
//         close();
//     }
    public static <T> void insertPatch(String tableName, T puts) throws IOException, ClassNotFoundException {
        init();
        Table table = connection.getTable(TableName.valueOf(tableName));
        if (puts.getClass() == Class.forName("java.util.ArrayList")) {
            table.put((List<Put>) puts);
        } else {
            table.put((Put) puts);
        }
        table.close();
        close();
    }

    // 删除数据
    public static void deleRow(String tableName, String rowkey,
                               String colFamily, String col) throws IOException {
        init();
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowkey));
        // 删除指定列族
        // delete.addFamily(Bytes.toBytes(colFamily));
        // 删除指定列
        // delete.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(col));
        table.delete(delete);
        // 批量删除
        /*
         * List<Delete> deleteList = new ArrayList<Delete>();
         * deleteList.add(delete); table.delete(deleteList);
         */
        table.close();
        close();
    }

    // 根据rowkey查找数据
    public static plane queryByRowkey(String tableName, String rowkey)
            throws IOException {
        String altitude = null;
        String depature_Airport = null;
        String flight_Number = null;
        String landing_Airport = null;
        String lat = null;
        String lng = null;
        init();
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowkey));
        // 获取指定列族数据
        // get.addFamily(Bytes.toBytes(colFamily));
        // 获取指定列数据
        // get.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(col));
        Result results = table.get(get);
        for (Cell cell : results.rawCells()) {
            String qualifier = new String(CellUtil.cloneQualifier(cell));
            switch (qualifier) {
                case "altitude":
                    altitude = Bytes.toString(CellUtil.cloneValue(cell));
                    break;
                case "depature_Airport":
                    depature_Airport = Bytes.toString(CellUtil.cloneValue(cell));
                    break;
                case "flight_Number":
                    flight_Number = Bytes.toString(CellUtil.cloneValue(cell));
                    break;
                case "landing_Airport":
                    landing_Airport = Bytes.toString(CellUtil.cloneValue(cell));
                    break;
                case "lat":
                    lat = Bytes.toString(CellUtil.cloneValue(cell));
                    break;
                case "lng":
                    lng = Bytes.toString(CellUtil.cloneValue(cell));
                    break;
            }
        }
        plane planeItems = new plane(lat, lng, altitude, depature_Airport,
                flight_Number, landing_Airport);
        System.out.println("+++++++++++" + planeItems);
        table.close();
        close();
        return planeItems;
    }

    // 格式化输出
    public static void showCell(Result result) {
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            // new String 和Bytes.toString效果一样
            System.out.println("测试:" + Bytes.toString(CellUtil.cloneRow(cell)));
            System.out.println("RowName:" + new String(CellUtil.cloneRow(cell))
                    + " ");
            System.out.println("Timetamp:" + cell.getTimestamp() + " ");
            System.out.println("column Family:"
                    + new String(CellUtil.cloneFamily(cell)) + " ");
            System.out.println("row Name:"
                    + new String(CellUtil.cloneQualifier(cell)) + " ");
            System.out.println("value:" + new String(CellUtil.cloneValue(cell))
                    + " ");
        }
    }

    // 批量查找数据，查找完成之后删除对应的数据，减少数据存储的占用空间
    public String scanData(String tableName, long time) throws IOException {
        init();
        long beginningTime = time;
        long lastTimes;
        List<String> rowKeyList = new ArrayList<String>();
        HashMap results = new HashMap();
        JsonUtil jsonUtil = new JsonUtil();
        String latitute = null, longitute = null;
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
//        lastTimes = System.currentTimeMillis() + 268000;
        lastTimes = System.currentTimeMillis();
        // 将最后的时间戳返回给前台，方便下次调用的时候从前一次开始
        results.put("lastTimes", jsonUtil.bean2json(new latlngBean(String
                .valueOf(lastTimes), String.valueOf(lastTimes))));
        //System.out.println("beginningTime+++"+beginningTime+"lastTimes+++"+lastTimes);
        scan.setTimeRange(beginningTime, lastTimes);
        ResultScanner resultScanner = table.getScanner(scan);
        // 获取所有的rorkey值
        for (Result result : resultScanner) {
            String key = Bytes.toString(result.getRow());
            rowKeyList.add(key);
        }
        // 获取每个rowkey可以对应列值
        for (int j = 0; j < rowKeyList.size(); j++) {
            Get get = new Get(Bytes.toBytes(rowKeyList.get(j)));
            Result rowKeyResult = table.get(get);
            for (Cell cell : rowKeyResult.rawCells()) {
                String qualifier = new String(CellUtil.cloneQualifier(cell));
                switch (qualifier) {
                    case "lat":
                        latitute = Bytes.toString(CellUtil.cloneValue(cell));
                        break;
                    case "lng":
                        longitute = Bytes.toString(CellUtil.cloneValue(cell));
                        break;
                }
                results.put(rowKeyList.get(j),
                        jsonUtil.bean2json(new latlngBean(latitute, longitute)));
            }// cell 的循环结束
        }// rowkey的循环结束

        //删除对应的数据
        List<Delete> deleteList = new ArrayList<>();
        for (String key : rowKeyList) {
            Delete delete = new Delete(Bytes.toBytes(key));
            deleteList.add(delete);
        }
        table.delete(deleteList);
        table.close();
        close();
        return jsonUtil.map2json(results);
    }

    // 查看表是否是存在
    public boolean tableExists(final TableName tablename) throws IOException {
        init();
        return admin.tableExists(tablename);
    }

    // 获取每一个rowkey的某一列值
    public HashMap<String, String> getOneQualifier(String tableName,
                                               String qualifier) throws IOException {
        HashMap hashMap = new HashMap();
        init();
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        // scan.addColumn(Bytes.toBytes("conf"), Bytes.toBytes("qualifier"));
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                String rowkey = new String(CellUtil.cloneRow(cell));
                String qua = Bytes.toString(CellUtil.cloneValue(cell));
                // System.out.println(qua.indexOf(qualifier));
                if (qua.startsWith("[")) {
                    // System.out.println(qua);
                    hashMap.put(rowkey, qua);
                }
            }

        }
        table.close();
        close();
        return hashMap;
    }
}
