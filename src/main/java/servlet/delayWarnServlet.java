package servlet;

import com.google.gson.Gson;
import conf.ConfigurationManager;
import constants.Constants;
import planeBean.DateBean;
import connection.HiveConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cliu_yjs15 on 2018/3/7.
 */
public class delayWarnServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            process(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            process(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html,charset=utf-8");

        DateBean dateBean = mappingModel(request.getParameter("startDate"),request.getParameter("endDate"));
        String sql = getCondition(dateBean);
        HiveConnection hiveConnection = new HiveConnection();
        System.out.println("sql查询语句="+ConfigurationManager.getProperty(Constants.FPOREST_DATA_SQL)+sql);
        ResultSet resultSet = hiveConnection.getData(ConfigurationManager.getProperty(Constants.FPOREST_DATA_SQL)+sql);
        HashMap<String, List<HashMap<String, String>>> rows = hiveConnection.setRows(resultSet);
        resultSet.beforeFirst();
        HashMap<String, List<HashMap<String, String>>> columns = hiveConnection.setColumns(resultSet);
        resultSet.beforeFirst();
        HashMap<String, List<HashMap<String, String>>> dataset = hiveConnection.setData(resultSet);
        Gson gson = new Gson();
        request.setAttribute("rows", gson.toJson(rows));
        request.setAttribute("columns", gson.toJson(columns));
        request.setAttribute("dataset", gson.toJson(dataset));
        request.getRequestDispatcher("/delayWarn.jsp").forward(request, response);
    }

    private String getCondition(DateBean dateBean) {
        if (dateBean == null) return "";
        StringBuilder condition = new StringBuilder();
        if (dateBean.getStartDay() != null){
            condition.append(" and day>='"+dateBean.getStartDay()+"'");
        }
        if (dateBean.getEndDay() != null){
            condition.append(" and day<='"+dateBean.getEndDay()+"'");
        }
        return  condition.toString();
    }


    private DateBean mappingModel(String startDate, String endDate){
        DateBean dateBean = new DateBean();
        if (startDate.length() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            String[] start = startDate.split("-");
            stringBuilder.append(start[0]).append(start[1]).append(start[2]);
            dateBean.setStartDay(stringBuilder.toString());
        }
        if (endDate.length() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            String[] end = endDate.split("-");
            stringBuilder.append(end[0]).append(end[1]).append(end[2]);
            dateBean.setEndDay(stringBuilder.toString());
        }
        return  dateBean;
    }



}
