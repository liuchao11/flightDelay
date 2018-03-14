package util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by cliu_yjs15 on 2018/3/12.
 */
@RunWith(Parameterized.class)
public class TimeUtilTest {
    private String para1;
    private String expected;

    @Parameterized.Parameters
    public static Collection prepareData(){  //必须为public static的 返回值必须是Collection类型的

        Object [][] object = {{"1245","12:45"},{"745","7:45"},{"1745","17:45"},{"1115","11:15"}};  //测试数据
        return Arrays.asList(object);
    }
    public TimeUtilTest(String para1 ,String expected){
        this.para1= para1;
        this.expected=expected;
    }
    @Test
    public void getHour() throws Exception {
         String  str = TimeUtil.getHour(para1);
         assertEquals(expected,str);

    }

}