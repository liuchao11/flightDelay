package test;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.LongWritable;

/**
 * Created by cliu_yjs15 on 2018/3/13.Hive自定义UDP用于实现自增id
 */
@Description(name="row_sequence",value="_FUNC_()-Returns a generated row sequence number starting from 1")
public class RowSequence extends UDF {
    private LongWritable result =  new LongWritable();
    public RowSequence(){
        result.set(0);
    }
    public LongWritable evaluate(){
        result.set(result.get()+1);
        return result;
    }
}
