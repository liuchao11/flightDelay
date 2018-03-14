package util;


import org.junit.Test;

/**
 * Created by cliu_yjs15 on 2018/3/12.
 */
public class TimeUtil {

    public static String getHour(String string){
        String str = null;
        if (string.length() == 3){
            str = string.charAt(0)+":"+string.substring(1,3);
        }else if (string.length() == 4){
            str = string.substring(0,2)+":"+string.substring(2,4);
        }
        return  str;
    }
}
