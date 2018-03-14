package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import conf.ConfigurationManager;
import constants.Constants;
import planeBean.FlightModel;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import page.PageBean;
import util.DateUtils;

public class FlightDaoHBase {
    //    public static Configuration configuration;
//    public static Connection connection;
//    public static Admin admin;
//    
//     
//     static {
//        configuration = HBaseConfiguration.create();
//        
//        
//        try
//        {
//            connection = ConnectionFactory.createConnection();
//            admin = connection.getAdmin();
//            
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    //pageNo当前页码，pageSize每页数目
    public PageBean getPageBean(int pageNo, int pageSize, FlightModel conditionModel) throws SolrServerException, IOException {
        Connection connection = ConnectionFactory.createConnection();
        PageBean bean = new PageBean(pageNo, pageSize);
        List<FlightModel> resultList = new ArrayList<FlightModel>();
        CloudSolrServer cloudSolrServer = new CloudSolrServer(ConfigurationManager.getProperty(Constants.ZKHOST_PORD));
        cloudSolrServer.setZkClientTimeout(1800000);
        cloudSolrServer.setZkConnectTimeout(1800000);
        cloudSolrServer.setDefaultCollection(ConfigurationManager.getProperty(Constants.COLLECTION_PORD));
        String str = getCondition(conditionModel);//组合查询条件
        SolrQuery query = new SolrQuery();
        query.setStart(bean.getStart());//起始页码
        query.set("rows", Integer.MAX_VALUE);
//        query.setRows(bean.getPageSize());//页数
        if (str.equals("") || str.equals(null)) {
            str = "*:*";
        }
        query.setQuery(str);
        QueryResponse response = cloudSolrServer.query(query);
        SolrDocumentList allResults = response.getResults();
        int totalCount = allResults.size();//总共有多少条数据
        bean.setTotalCount(totalCount);
        SolrDocumentList results = new SolrDocumentList();
        for (int i = 0; i < bean.getPageSize(); i++) {
            results.add(allResults.get(i));
        }

        List<Get> RowKeylists = new ArrayList<Get>();//存放所有符合条件的rorkey值
        for (int i = 0; i < results.size(); i++) {
            SolrDocument document = results.get(i);
            Get get = new Get(Bytes.toBytes(document.get("id").toString()));
            RowKeylists.add(get);
        }
        //根据RowKeylists扫描数据库，获取符合条件的数据
        System.out.println("查询结果条数=" + RowKeylists.size());
        Table table = connection.getTable(TableName.valueOf("flight_info"));
        Result[] results2 = table.get(RowKeylists);
        System.out.println("返回结果的条目数=" + results2.length);
        for (Result result : results2) {
            resultList.add(mappingModel(result));
        }
        bean.setList(resultList);

        return bean;
    }

    /**
     * 获取查询条件拼接的字符串
     *
     * @param conditionModel
     * @return
     */
    private String getCondition(FlightModel conditionModel) {
        if (conditionModel == null) return "";
        StringBuilder condition = new StringBuilder("*:* ");
        if (conditionModel.getFlightDate() != null) {
            condition.append(" AND FLIGHTDATE:" + DateUtils.formatUniqueDate(conditionModel.getFlightDate()) + " ");
        }
        if (conditionModel.getAirlineId() != null) {
            condition.append(" AND  AIRLINEID:" + conditionModel.getAirlineId() + " ");
        }
        if (conditionModel.getTailNum() != null) {
            condition.append(" AND  TAILNUM:" + conditionModel.getTailNum() + " ");
        }
        if (conditionModel.getFlightNum() != null) {
            condition.append(" AND  FLIGHTNUM:" + conditionModel.getFlightNum() + " ");
        }
        if (conditionModel.getOrgin() != null) {
            condition.append(" AND  ORIGIN:" + conditionModel.getOrgin() + " ");
        }
        if (conditionModel.getDest() != null) {
            condition.append(" AND  DEST:" + conditionModel.getDest());
        }
        return condition.toString();
    }

    private FlightModel mappingModel(Result result) {
        FlightModel model = new FlightModel();

        for (Cell cell : result.rawCells()) {
            String name = Bytes.toString(CellUtil.cloneQualifier(cell));
            byte[] value = CellUtil.cloneValue(cell);
            switch (name) {
                case "YEAR":
                    model.setYear(Integer.valueOf(Bytes.toString(value)));
                    break;
                case "MONTH":
                    model.setMonth(Integer.valueOf(Bytes.toString(value)));
                    break;
                case "QUARTER":
                    model.setSeason(Integer.valueOf(Bytes.toString(value)));
                    break;
                case "DAYOFMONTH":
                    model.setDay(Integer.valueOf(Bytes.toString(value)));
                    break;
                case "DAYOFWEEK":
                    model.setDayOfWeek(Integer.valueOf(Bytes.toString(value)));//改过
                    break;
                case "FLIGHTDATE":
                    model.setFlightDate(DateUtils.parseTime(Bytes.toString(value)));
                    break;
                case "CARRIER":
                    model.setCarrrier(Bytes.toString(value));
                    break;
                case "AIRLINEID":
                    model.setAirlineId(Integer.valueOf(Bytes.toString(value)));
                    break;
                case "TAILNUM":
                    model.setTailNum(Bytes.toString(value));
                    break;
                case "FLIGHTNUM":
                    model.setFlightNum(Integer.valueOf(Bytes.toString(value)));
                    break;
                case "ORIGIN":
                    model.setOrgin(Bytes.toString(value));
                    break;
                case "ORIGINCITYNAME":
                    model.setOrginCityName(Bytes.toString(value));
                    break;
                case "DEST":
                    model.setDest(Bytes.toString(value));
                    break;
                case "DESTCITYNAME":
                    model.setDestCityName(Bytes.toString(value));
                    break;
                default:
                    break;
            }
        }
        return model;
    }


}
