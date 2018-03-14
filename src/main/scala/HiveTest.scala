package scala



import conf.ConfigurationManager

import org.apache.spark.sql.SparkSession
import constants.Constants

/**
  * Created by cliu_yjs15 on 2018/3/6.
  */
object HiveTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("TestHive")
      .master("local[*]")
      .config("spark.sql.warehouse.dir", "hdfs://192.168.8.101:8020/user/hive/warehouse")
      .enableHiveSupport()
      .getOrCreate()
    //    import spark.sql
    //    sql("show tables").show();
    //    spark.sql("show tables").show()
    //    spark.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)")
    //    spark.stop()
    var predictData: String = null
    if (ConfigurationManager.getBoolean(Constants.SPARK_LOCAL)) {
      predictData = ConfigurationManager.getProperty(Constants.PREDICTDATA_URL)

    } else {
      predictData = ConfigurationManager.getProperty(Constants.PREDICTDATA_URL_PROD)
    }
    println(predictData)


  }
}
