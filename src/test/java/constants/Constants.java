package java.constants;

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
    String JDBC_URL_PROD = "jdbc.url.prod";
    String JDBC_USER_PROD = "jdbc.user.prod";
    String JDBC_PASSWORD_PROD = "jdbc.password.prod";
    String JDBC_DRIVER = "jdbc.driver";

    /**
     * C**发射客户端配置信息
     */
    String TARGET_PORT = "target.port";
    String SLEEP_TIME = "sleep.time";
}
