package constants;

/**
 * 常用变量集合
 */
public interface Constants {
    /**
     * HBase配置项
     */
    String HBASE_ZOOKEEPER_QUORUM = "quorum";
    String JDBC_DATASOURCE_SIZE = "jdbc.datasource.size";
    String SPARK_LOCAL = "spark.local";
    String JDBC_URL = "jdbc.url";
    String JDBC_USER = "jdbc.user";
    String JDBC_PASSWORD = "jdbc.password";
    String JDBC_URL_PROD = "jdbc.url.prod";    //用于集群跑
    String JDBC_USER_PROD = "jdbc.user.prod";
    String JDBC_PASSWORD_PROD = "jdbc.password.prod";
    String JDBC_DRIVER = "jdbc.driver";

    /**
     * C**发射客户端配置信息
     */
    String TARGET_PORT = "target.port";
    String SLEEP_TIME = "sleep.time";

    /**
     * solr信息
     */
    String ZKHOST_PORD = "zkHost";
    String COLLECTION_PORD = "collection";
    /**
     * hive配置信息
     */
    String HIVE_URL = "hive.url";
    String HIVE_USER = "hive.user";
    String HIVE_PASSWORD = "hive.password";


    /**
     * 算法模型url和数据url
     */
    String PREDICTDATA_URL = "predictData.url";
    String MODEL_URL = "model.url";
    String PREDICTDATA_URL_PROD = "predictData.url.prod";
    String MODEL_URL_PROD = "model.url.prod";

    /**
     * 航班延误预测热力图sql
     */
    String FPOREST_DATA_SQL = "forest.data.sql";
}
