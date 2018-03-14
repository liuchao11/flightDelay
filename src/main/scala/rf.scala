package scala

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{RandomForestClassificationModel, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, VectorIndexer, StringIndexer}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, StructField, StructType}
import org.apache.spark.sql.{Row, DataFrame, SparkSession}
import org.apache.log4j.{Level, Logger}

/**
  * Created by jyli on 2017/4/9.
  */
object rf {
  def main(args: Array[String]) {
    val spark = SparkSession.builder().appName("rf").master("local[*]").config("spark.sql.warehouse.dir", "file:///").getOrCreate()
    //val spark = SparkSession.builder().appName("rf").config("spark.sql.warehouse.dir","file:///").getOrCreate()

    //屏蔽日志
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)

    val featuresDF0: DataFrame = spark.read.format("libsvm").load("D:\\学习\\航班延误分析\\美国交通运输部数据\\201608_60_3\\part-r-00000-887f8126-20d6-42e2-8c5e-f14dfebac830")
    //val featuresDF0:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/data/201608_60_3")
    /*val featuresDF0:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201601_45_3/201601_45_3")
    val featuresDF1:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201602_45_3/201602_45_3")
    val featuresDF2:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201603_45_3/201603_45_3")
    val featuresDF3:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201604_45_3/201604_45_3")
    val featuresDF4:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201605_45_3/201605_45_3")
    val featuresDF5:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201606_45_3/201606_45_3")
    val featuresDF6:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201607_45_3/201607_45_3")
    val featuresDF7:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201608_45_3/201608_45_3")
    val featuresDF8:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201609_45_3/201609_45_3")
    val featuresDF9:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201610_45_3/201610_45_3")
    val featuresDF10:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201611_45_3/201611_45_3")
    val featuresDF11:DataFrame=spark.read.format("libsvm").load("hdfs://192.168.8.101:8020/user/jyli/w_hourly_dest/201612_45_3/201612_45_3")
*/
    val featuresDF = featuresDF0
    /*.union(featuresDF5).union(featuresDF1).union(featuresDF3).union(featuresDF2).union(featuresDF4).union(featuresDF6)
          .union(featuresDF7).union(featuresDF8).union(featuresDF9).union(featuresDF10).union(featuresDF11)*/
    // val featuresDF=data.sample(true,0.5)
    println(featuresDF.count())
    featuresDF.cache()

    // Index labels, adding metadata to the label column.
    // Fit on whole dataset to include all labels in index.
    val labelIndexer = new StringIndexer()
      .setInputCol("label")
      .setOutputCol("indexedLabel")
      .fit(featuresDF)
    //.transform(featuresDF)
    //labelIndexer.limit(10).show()

    // Automatically identify categorical features, and index them.
    // Set maxCategories so features with > 4 distinct values are treated as continuous.
    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(4)
      .fit(featuresDF)
    // .transform(featuresDF)
    //featureIndexer.limit(10).show()


    // Split the data into training and test sets (30% held out for testing).
    val Array(trainingData, testData) = featuresDF.randomSplit(Array(0.7, 0.3))


    // Train a RandomForest model.
    val rf = new RandomForestClassifier()
      .setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures")
      .setNumTrees(10)
      .setMaxBins(128)
      .setMaxDepth(16)
    //      .setSubsamplingRate(0.6)


    // Convert indexed labels back to original labels.
    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labels)


    // Chain indexers and forest in a Pipeline.
    val pipeline = new Pipeline()
      .setStages(Array(labelIndexer, featureIndexer, rf, labelConverter))


    // 设置参数网格
    //impurity  不纯度
    //maxBins  离散化"连续特征"的最大划分数
    //maxDepth  树的最大深度
    //minInfoGain 一个节点分裂的最小信息增益，值为[0,1]
    //minInstancesPerNode 每个节点包含的最小样本数 >=1
    //numTrees 树的数量
    //featureSubsetStrategy // 在每个树节点处分割的特征数，参数值比较多，详细的请参考官方文档
    //SubsamplingRate(1.0)  给每棵树分配“学习数据”的比例，范围(0, 1]
    //maxMemoryInMB  如果太小，则每次迭代将拆分1个节点，其聚合可能超过此大小。
    //checkpointInterval  设置检查点间隔（> = 1）或禁用检查点（-1）。 例如 10意味着,每10次迭代,缓存将获得检查点。
    //cacheNodeIds  如果为false，则算法将树传递给执行器以将实例与节点匹配。 如果为true，算法将缓存每个实例的节点ID。 缓存可以加速更大深度的树的训练。 用户可以通过设置checkpointInterval来设置检查或禁用缓存的频率。(default = false)
    //seed 种子

    /*
    val paramGrid = new ParamGridBuilder()
      .addGrid(rf.impurity, Array("entropy", "gini"))
      .addGrid(rf.maxBins, Array(32, 5000))
      .addGrid(rf.maxDepth, Array(5, 7, 10))
      .addGrid(rf.minInfoGain, Array(0, 0.5, 1))
      //.addGrid(rf.minInstancesPerNode, Array(10, 20))
      .addGrid(rf.numTrees, Array(20, 50))
      .addGrid(rf.featureSubsetStrategy, Array("auto", "sqrt"))
      .addGrid(rf.subsamplingRate, Array(0.8, 1))
      .addGrid(rf.maxMemoryInMB, Array(256, 512))
      .addGrid(rf.checkpointInterval, Array(10, 20))
      .addGrid(rf.cacheNodeIds, Array(false, true))
      .addGrid(rf.seed, Array(123456L, 111L))
      .build()
      */

    // Train model. This also runs the indexers.
    val model = pipeline.fit(trainingData)

    // 输出随机森林模型的全部参数值
    model.stages(2).extractParamMap()

    // Make predictions.
    val predictions = model.transform(testData)

    // Select example rows to display.
    predictions.select("predictedLabel", "label", "features").show(10, false)

    /*
    spark.sqlContext.udf.register("recallTP",(s:Double,s1:Double) => {
      var countTP=0
      if (s==s1 & s==1) countTP += 1
      countTP
      })
    spark.sqlContext.udf.register("recallFN",(s:Double,s1:Double) => {
      var countFN=0
      if (s==0 & s1==1) countFN += 1
      countFN
    })*/


    predictions.createOrReplaceTempView("predictions")
    spark.sqlContext.udf.register("recall", new recalludaf)
    val recallDF = spark.sql("select recall(predictedLabel,label) as recallnum from predictions")
    println("recall =")
    recallDF.show()
    spark.sqlContext.udf.register("recallontime", new recallontimeudaf)
    val recallontimeDF = spark.sql("select recallontime(predictedLabel,label) as recallnum from predictions")
    println("recallontime =")
    recallontimeDF.show()
    spark.sqlContext.udf.register("TPR", new TPRudaf)
    val TPRDF = spark.sql("select TPR(predictedLabel,label) as TPRDFnum from predictions")
    println("TPR =")
    TPRDF.show()
    spark.sqlContext.udf.register("FPR", new FPRudaf)
    val FPRDF = spark.sql("select FPR(predictedLabel,label) as FPRDFnum from predictions")
    println("FPR =")
    FPRDF.show()


    // Select (prediction, true label) and compute test error.
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("indexedLabel")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println("Test Error = " + (1.0 - accuracy))


    val rfModel = model.stages(2).asInstanceOf[RandomForestClassificationModel]
    //println("Learned classification forest model:\n" + rfModel.toDebugString)
    //model.save("D:\\data\\model\\delaypredict")


    spark.stop()
  }

  //https://databricks.com/blog/2015/09/16/apache-spark-1-5-dataframe-api-highlights.html
  class recalludaf extends UserDefinedAggregateFunction {
    /**
      * 指定具体的输入数据的类型
      * 自段名称随意：Users can choose names to identify the input arguments - 这里可以是“name”，或者其他任意串
      */
    override def inputSchema: StructType = StructType(StructField("predictedLabel", DoubleType) :: StructField("label", DoubleType) :: Nil)

    /**
      * 在进行聚合操作的时候所要处理的数据的中间结果类型
      */
    override def bufferSchema: StructType = StructType(StructField("countTP", DoubleType) :: StructField("countFP", DoubleType) :: Nil)

    /**
      * 返回类型
      */
    override def dataType: DataType = DoubleType

    /**
      * whether given the same input,
      * always return the same output
      * true: yes
      */
    override def deterministic: Boolean = true

    /**
      * Initializes the given aggregation buffer
      */
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0.0
      buffer(1) = 0.0
    }

    /**
      * 在进行聚合的时候，每当有新的值进来，对分组后的聚合如何进行计算
      * 本地的聚合操作
      */
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      if (input.getAs[Double](0) == 1 & input.getAs[Double](1) == 1) {
        buffer(0) = buffer.getAs[Double](0) + 1.0
      }
      if (input.getAs[Double](0) == 0 & input.getAs[Double](1) == 1) {
        buffer(1) = buffer.getAs[Double](1) + 1.0
      }
    }

    /**
      * 最后在分布式节点进行local reduce完成后需要进行全局级别的merge操作
      */
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getAs[Double](0) + buffer2.getAs[Double](0)
      buffer1(1) = buffer1.getAs[Double](1) + buffer2.getAs[Double](1)
    }

    /**
      * 返回UDAF最后的计算结果
      */
    def evaluate(buffer: Row): Any = {
      buffer.getDouble(0) / (buffer.getDouble(0) + buffer.getDouble(1))
    }
  }

  class recallontimeudaf extends UserDefinedAggregateFunction {
    /**
      * 指定具体的输入数据的类型
      * 自段名称随意：Users can choose names to identify the input arguments - 这里可以是“name”，或者其他任意串
      */
    override def inputSchema: StructType = StructType(StructField("predictedLabel", DoubleType) :: StructField("label", DoubleType) :: Nil)

    /**
      * 在进行聚合操作的时候所要处理的数据的中间结果类型
      */
    override def bufferSchema: StructType = StructType(StructField("countTP", DoubleType) :: StructField("countFP", DoubleType) :: Nil)

    /**
      * 返回类型
      */
    override def dataType: DataType = DoubleType

    /**
      * whether given the same input,
      * always return the same output
      * true: yes
      */
    override def deterministic: Boolean = true

    /**
      * Initializes the given aggregation buffer
      */
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0.0
      buffer(1) = 0.0
    }

    /**
      * 在进行聚合的时候，每当有新的值进来，对分组后的聚合如何进行计算
      * 本地的聚合操作
      */
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      if (input.getAs[Double](0) == 0 & input.getAs[Double](1) == 0) {
        buffer(0) = buffer.getAs[Double](0) + 1.0
      }
      if (input.getAs[Double](0) == 1 & input.getAs[Double](1) == 0) {
        buffer(1) = buffer.getAs[Double](1) + 1.0
      }
    }

    /**
      * 最后在分布式节点进行local reduce完成后需要进行全局级别的merge操作
      */
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getAs[Double](0) + buffer2.getAs[Double](0)
      buffer1(1) = buffer1.getAs[Double](1) + buffer2.getAs[Double](1)
    }

    /**
      * 返回UDAF最后的计算结果
      */
    def evaluate(buffer: Row): Any = {
      buffer.getDouble(0) / (buffer.getDouble(0) + buffer.getDouble(1))
    }
  }


  class TPRudaf extends UserDefinedAggregateFunction {

    override def inputSchema: StructType = StructType(StructField("predictedLabel", DoubleType) :: StructField("label", DoubleType) :: Nil)

    override def bufferSchema: StructType = StructType(StructField("countTP", DoubleType) :: StructField("countFP", DoubleType) :: Nil)

    override def dataType: DataType = DoubleType

    override def deterministic: Boolean = true

    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0.0
      buffer(1) = 0.0
    }

    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      if (input.getAs[Double](0) == 1 & input.getAs[Double](1) == 1) {
        buffer(0) = buffer.getAs[Double](0) + 1.0
      } //prediction 1(yan)  yuan 1   TP
      if (input.getAs[Double](0) == 1 & input.getAs[Double](1) == 0) {
        buffer(1) = buffer.getAs[Double](1) + 1.0
      } //prediction  1    yuan0     Fn
    }

    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getAs[Double](0) + buffer2.getAs[Double](0)
      buffer1(1) = buffer1.getAs[Double](1) + buffer2.getAs[Double](1)
    }

    def evaluate(buffer: Row): Any = {
      buffer.getDouble(0) / (buffer.getDouble(0) + buffer.getDouble(1))
    }
  }

  class FPRudaf extends UserDefinedAggregateFunction {

    override def inputSchema: StructType = StructType(StructField("predictedLabel", DoubleType) :: StructField("label", DoubleType) :: Nil)

    override def bufferSchema: StructType = StructType(StructField("countTP", DoubleType) :: StructField("countFP", DoubleType) :: Nil)

    override def dataType: DataType = DoubleType

    override def deterministic: Boolean = true

    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0.0
      buffer(1) = 0.0
    }

    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      if (input.getAs[Double](0) == 0 & input.getAs[Double](1) == 1) {
        buffer(0) = buffer.getAs[Double](0) + 1.0
      } //prediction 0(yan)  yuan 1   FP
      if (input.getAs[Double](0) == 0 & input.getAs[Double](1) == 0) {
        buffer(1) = buffer.getAs[Double](1) + 1.0
      } //prediction 0     yuan0     tn
    }

    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getAs[Double](0) + buffer2.getAs[Double](0)
      buffer1(1) = buffer1.getAs[Double](1) + buffer2.getAs[Double](1)
    }

    def evaluate(buffer: Row): Any = {
      buffer.getDouble(0) / (buffer.getDouble(0) + buffer.getDouble(1))
    }
  }


}
