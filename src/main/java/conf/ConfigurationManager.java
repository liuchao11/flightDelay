package conf;

import java.io.InputStream;
import java.util.Properties;


public class ConfigurationManager {
    private static Properties properties = new Properties();

    static {
        try {
            InputStream in = ConfigurationManager.class.getClassLoader().getResourceAsStream("my.properties");
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取配置项
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取整数类型的配置项
     */
    public static Integer getInteger(String key) {
        String value = properties.getProperty(key);
        return Integer.valueOf(value);
    }

    /**
     * 获取布尔类型的
     */
    public static Boolean getBoolean(String key) {
        String value = getProperty(key);
        try {
            return Boolean.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
