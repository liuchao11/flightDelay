package servlet;

import java.io.IOException;

import connection.HBaseConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.JsonUtil;
import planeBean.plane;


/**
 * servlet用于接收飞机id，调用数据库DAO;
 *
 * @author LiuChao
 */
public class planeServlet extends HttpServlet {
    HBaseConnection hbaseConnection = new HBaseConnection();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String rowkey = request.getParameter("number");
        String tableName = "tempRoutes";
        plane planes = hbaseConnection.queryByRowkey(tableName, rowkey);
        JsonUtil jsonUtil = new JsonUtil();
        String planeItems = jsonUtil.bean2json(planes);
        System.out.println(planeItems);
        request.setAttribute("planeItems", planeItems);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(planeItems);
        //request.getRequestDispatcher("/map.jsp").forward(request, response);
    }

}


