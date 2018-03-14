package thread;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.hadoop.hbase.TableName;

import connection.HBaseConnection;

public class MyListener implements ServletContextListener {
    private MyThread myThread;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (myThread != null && myThread.isInterrupted()) {
            System.out.println("监听中断");
            myThread.interrupt();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 如果spark_local是本地，那么进行mysql测试，如果不是本地，那么进行hbase测试
//        if (!ConfigurationManager.getBoolean(Constants.SPARK_LOCAL))
//        {
        System.out.println("监听启动");
        String str = null;
        // 初始化tempRoutes表
        HBaseConnection HBaseConnection = new HBaseConnection();
        HBaseConnection.init();
        try {
            if (HBaseConnection.admin.tableExists(TableName.valueOf("tempRoutes"))) {
                if (!HBaseConnection.admin.isTableDisabled(TableName
                        .valueOf("tempRoutes"))) {
                    HBaseConnection.admin.disableTable(TableName
                            .valueOf("tempRoutes"));
                    System.out.println("tempRoutes已经被禁用");
                }
            } else {
                HBaseConnection.createTable("tempRoutes", new String[]{"conf"});
            }

            HBaseConnection.admin.truncateTable(
                    TableName.valueOf("tempRoutes"), false);
            if (HBaseConnection.admin.isTableDisabled(TableName
                    .valueOf("tempRoutes"))) {
                HBaseConnection.admin.enableTable(TableName
                        .valueOf("tempRoutes"));
                System.out.println("++++++++++tempRoutes已经被启用");
            } else {
                System.out.println("----------------tempRoutes已经被启用");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (str == null && myThread == null) {
            myThread = new MyThread();
            myThread.start();
            System.out.println("当前正在运行的线程名称是===========================================" +
                    "=====================================================" + Thread.currentThread().getName());
        }
    }
//        else {
//            String str = null;
//            if (str == null && myThread == null)
//            {
//                myThread = new MyThread();
//                myThread.start();
//            }
//        }
//    }
}
