package connection;

import java.sql.*;
import java.util.*;

import conf.ConfigurationManager;
import constants.Constants;

public class MySQLConnection {
    //第一步加载驱动

    /**
     * 静态代码块的特点：
     * 1、随着类的加载而只执行一次;
     * 静态变量：引用的时候直接用类名.成员变量，可以不用创建对象;
     * 作用：用于给类初始化;
     * 当java类被加载时，此时静态变量、静态方法 被执行；而java对象被加载时，构造函数才去执行
     */
    static {
        try {
            Class.forName(ConfigurationManager.getProperty(Constants.JDBC_DRIVER));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 第二步，实现JDBCHelper的单例化
    // 为什么要实现单例化呢？因为它的内部要封装一个简单的内部的数据库连接池
    // 为了保证数据库连接池有且仅有一份，所以就通过单例的方式
    // 保证JDBCHelper只有一个实例，实例中只有一份数据库连接池
    private static MySQLConnection instance = null;

    public static MySQLConnection getInstance() {
        if (instance == null)          //静态方法调用类里面的静态变量，所以instance已经是一个变量
        {
            synchronized (MySQLConnection.class) //synchronized同步块，限制一段代码的执行，表明同一时刻只能有一个形成执行该方法
            {
                if (instance == null) {
                    instance = new MySQLConnection();//调用JDBCHelper的构造函数
                }
            }
        }
        return instance;
    }

    private LinkedList<Connection> datasource = new LinkedList<Connection>();//创建数据库连接池
    //第三步，实现单类的过程中，创建唯一的数据库连接池

    /**
     * JDBCHelper在整个程序运行声明周期中，只会创建一次实例
     * 在这一次创建实例的过程中，就会调用JDBCHelper()构造方法
     * 此时，就可以在构造方法中，去创建自己唯一的一个数据库连接池
     */
    private MySQLConnection() {//构造函数私有化
        //连接数大小
        int datasourceSize = ConfigurationManager.getInteger(Constants.JDBC_DATASOURCE_SIZE);
        //创建指定数量的连接，并放入连接池中
        for (int i = 0; i < datasourceSize; i++) {
            String url = null;//mysql数据库的url地址
            String user = null;//连接mysql的用户名
            String password = null;//连接mysql的密码

            if (ConfigurationManager.getBoolean(Constants.SPARK_LOCAL)) {
                url = ConfigurationManager.getProperty(Constants.JDBC_URL);
                user = ConfigurationManager.getProperty(Constants.JDBC_USER);
                password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);
            } else {
                url = ConfigurationManager.getProperty(Constants.JDBC_URL_PROD);
                user = ConfigurationManager.getProperty(Constants.JDBC_USER_PROD);
                password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD_PROD);
            }
            try {
                Connection connection = DriverManager.getConnection(url, user, password);
                datasource.push(connection);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 第四步，提供获取数据库连接的方法
     * 有可能，你去获取的时候，这个时候，连接都被用光了，你暂时获取不到数据库连接
     * 所以我们要自己编码实现一个简单的等待机制，去等待获取到数据库连接
     */
    public Connection getConnection() {
        while (datasource.size() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return datasource.poll();
    }
    
    /*
     * 
     * 第五步：开发增删改查的方法
     * 1、执行增删改SQL语句的方法
     * 2、执行查询SQL语句的方法
     * 3批量执行SQL语句的方法
     */

    /**
     * 执行增删改语句
     */
    public int executeUpdate(String sql, Object[] params) {
        int rtn = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            rtn = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                datasource.push(connection);
            }
        }

        return rtn;
    }

    /**
     * 执行查询操作
     * QueryCallback是一个接口，里面有一个process()抽象方法,方便用户自己处理返回结果
     */
    public void executeQuery(String sql, Object[] params, QueryCallback callback) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
            callback.process(resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                datasource.push(connection);
            }
        }
    }

    /**
     * 只用于返回ResultSet
     *
     * @param sql
     * @param params
     * @return
     */
    public ResultSet executeQueryFullResult(String sql, Object[] params) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                datasource.push(connection);
            }
            return resultSet;
        }
    }

    public ArrayList executeQueryForFusionChart(String sql, Object[] params) throws SQLException {
        ResultSet resultSet = executeQueryFullResult(sql, params);
        ArrayList queryData = new ArrayList();
        while (resultSet.next()) {
            Map<String, String> lv = new HashMap<String, String>();
            lv.put("label", resultSet.getString(1));
            lv.put("value", resultSet.getString(2));
            String delay_reason = resultSet.getString(1);
            if (delay_reason.equals("天气原因")) {
                lv.put("isSliced", "1");
            }
            queryData.add(lv);
        }
        return queryData;


    }

    public static interface QueryCallback {
        //处理查询结果
        void process(ResultSet rs) throws Exception;
    }

    /**
     * 批量执行sql语句，通过PrepareStatement发送多条sql
     *
     * @return
     */
    public int[] executeBatch(String sql, List<Object[]> paramsList) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int[] resultSet = null;
        try {
            connection = getConnection();
            //第一步：取消自动提交
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(sql);
            //第二步：使用prepareStatement.addBatch方法加入批量sql参数
            for (Object[] params : paramsList) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
                preparedStatement.addBatch();
            }
            //第三步：使用prepareStatement.executeBatch()方法，执行批量的sql语句
            resultSet = preparedStatement.executeBatch();
            //最后一步，使用connection对象提交批量的sql语句
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                datasource.push(connection);
            }
        }
        return resultSet;
    }

}
