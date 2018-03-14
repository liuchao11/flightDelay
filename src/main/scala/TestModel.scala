package scala


import conf.ConfigurationManager
import  constants.Constants
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession


/**
  * Created by cliu_yjs15 on 2018/3/6.
  **/
object TestModel {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .enableHiveSupport()
      .appName("TestModel")
      .config("spark.sql.warehouse.dir", "hdfs://192.168.8.101:8020/user/hive/warehouse")
      //.config("spark.sql.warehouse.dir", "file:///")
      //.master("local")
      .getOrCreate()
    //远程模式
    var predictDataURL: String = null
    var modelURL: String = null
    if (ConfigurationManager.getBoolean(Constants.SPARK_LOCAL)) {
      predictDataURL = ConfigurationManager.getProperty(Constants.PREDICTDATA_URL)
      modelURL = ConfigurationManager.getProperty(Constants.MODEL_URL)
    } else {
      predictDataURL = ConfigurationManager.getProperty(Constants.PREDICTDATA_URL_PROD)
      modelURL = ConfigurationManager.getProperty(Constants.MODEL_URL_PROD)
    }
    val testData = spark.read.format("libsvm").load(predictDataURL)
    val samemodel = PipelineModel.load(modelURL)
    //val predicts = samemodel.transform(testData).select("predictedLabel", "label", "features")
    val predicts = samemodel.transform(testData).select("predictedLabel", "label")
    predicts.show(10, false)
    predicts.printSchema()
    predicts.createOrReplaceTempView("predictsTable")

//    spark.sqlContext.sql("insert overwrite table predictdata  select * from predictsTable")

    spark.sqlContext.sql("insert overwrite table 2016_12_Predict select a.Year,a.Quarter ,a.Month ," +
      "a.DayofMonth ,a.TailNumIndex ,a.FlightNumIndex ,a.AirlineID ,a.CarrierIndex ,a.OriginAirportID ," +
      "a.DestAirportID ,a.CRSDepTime ,a.CRSArrTime ,b.predictedLabel,b.label from 2016_12 a,predictsTable b")
    spark.stop()
  }

}
