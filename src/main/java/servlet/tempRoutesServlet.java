package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.HBaseConnection;

/**
 * 类描述：页面定时访问此servlet
 *
 * @version 1.0
 * @author: cliu_yjs15
 * @date： 日期：2017年12月6日 时间：下午8:05:17
 */
public class tempRoutesServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 接收上次扫描最后一次的时间戳
        String lastTime = request.getParameter("id");
        HBaseConnection HBaseConnection = new HBaseConnection();
        String results = HBaseConnection.scanData("tempRoutes", Long.parseLong(lastTime));
        System.out.println("结果是结果是结果是" + results + "上次时间戳是" + lastTime);
        request.setAttribute("results", results);
        response.getWriter().print(results);

    }

}
