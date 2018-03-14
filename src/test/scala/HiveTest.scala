package scala

import org.apache.spark.sql.SparkSession

/**
  * Created by cliu_yjs15 on 2018/3/6.
  */
object HiveTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      //.master("local")
      .appName("TestHive")
      .config("spark.sql.warehouse.dir", "hdfs://192.168.8.101:8020/user/hive/warehouse")
      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._
    import spark.sql
    sql("show tables").show();
    spark.sql("show tables").show()
    spark.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)")
    spark.stop()


  }
}
