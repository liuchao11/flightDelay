package thread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import planeBean.Acars;
import planeBean.TempRoutes;
import conf.ConfigurationManager;
import constants.Constants;

public class MyThread extends Thread
{
    int SleepTime = ConfigurationManager.getInteger(Constants.SLEEP_TIME);
    int i = 0;

    // i用于第一次的时候必须清空数据库
    public void run()
    {
        DatagramSocket datagramSocket = null;// 定义接收数据报包的套接字
        try
        {
            datagramSocket = new DatagramSocket(ConfigurationManager.getInteger(Constants.TARGET_PORT));
        }
        catch (SocketException e1)
        {
            e1.printStackTrace();
        }
        while (!this.isInterrupted())
        { // 线程未中断执行
            while (true)
            {
                byte[] buf = new byte[1024];
                // 定义接收数据的数据报包
                DatagramPacket datagramPacket = new DatagramPacket(buf, 0,
                        buf.length);
                try
                {
                    System.out.println("线程开始运行！！！");
                    datagramSocket.receive(datagramPacket);
                    System.out.println("+++++++++" + datagramSocket);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                // 从接收数据包取出数据
                String data = new String(datagramPacket.getData(), 0,
                        datagramPacket.getLength());
                //System.out.println("++++++++++" + data);
                // 存进HBase数据库
                // 将路径存进tempRoutes数据库
                Acars acars = null;
                TempRoutes tempRoutes = null;
//                if (!ConfigurationManager.getBoolean(Constants.SPARK_LOCAL))//测试存进数据库的速度
//                {
                    try
                    {
                        acars = new Acars("Acars", "conf", data);
                        acars.putToTable();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                try
                    {
                        tempRoutes = new TempRoutes("tempRoutes", "conf", data);
                        tempRoutes.putToTable();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }// while结束

        }
    }
}
