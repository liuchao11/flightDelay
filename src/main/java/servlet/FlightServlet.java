package servlet;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import planeBean.FlightModel;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import page.PageBean;
import dao.FlightDaoHBase;

public class FlightServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(FlightServlet.class);
    private static final long serialVersionUID = 1L;

    //	FlightDao dao = new FlightDao();
    FlightDaoHBase dao = new FlightDaoHBase();


    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("++++++++++++++++++++++++");
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("--------------------------------------");
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        try {
            String action = request.getParameter("action"); //这个是静态的，永远不会变，只要提交了肯定会出现这个值
            if ("search".equals(action)) {// 查询
                search(request, response);
            } else { // 列表
                list(request, response);
            }
        } catch (Exception e) {
            logger.error("Servlet出错", e);
        }
    }

    private FlightModel getConditionModel(HttpServletRequest request) {
        FlightModel model = new FlightModel();
        String value = request.getParameter("flightDate");
        try {
            if (StringUtils.isNotBlank(value)) {
                model.setFlightDate(Date.valueOf(value));
            }
            value = request.getParameter("airlineId");
            if (StringUtils.isNotBlank(value)) {
                model.setAirlineId(Integer.parseInt(value));
            }
            value = request.getParameter("tailNum");
            if (StringUtils.isNotBlank(value)) {
                model.setTailNum(value);
            }
            value = request.getParameter("flightNum");
            if (StringUtils.isNotBlank(value)) {
                model.setFlightNum(Integer.parseInt(value));
            }
            value = request.getParameter("originAirport");
            if (StringUtils.isNotBlank(value)) {
                model.setOrgin(value);
            }
            value = request.getParameter("destAirport");
            if (StringUtils.isNotBlank(value)) {
                model.setDest(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FlightModel conditionModel = getConditionModel(request);
        request.getSession().setAttribute("ConditonModel", conditionModel);
        PageBean pageBean = null;
        try {

            pageBean = dao.getPageBean(PageBean.INIT_PAGE_NO, PageBean.INIT_PAGE_SIZE, conditionModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/flightDataQuey.jsp").forward(request, response);
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageNo = PageBean.INIT_PAGE_NO;
        int pageSize = PageBean.INIT_PAGE_SIZE;
        if (StringUtils.isNotBlank(request.getParameter("pageNo"))) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        } else {
            request.getSession().removeAttribute("ConditonModel");
        }
        if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
        }
        FlightModel conditionModel = null;
        if (request.getSession().getAttribute("ConditonModel") != null) {
            conditionModel = (FlightModel) request.getSession().getAttribute("ConditonModel");
        }
        PageBean pageBean = null;
        try {
            pageBean = dao.getPageBean(pageNo, pageSize, conditionModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/flightDataQuey.jsp").forward(request, response);
    }

}
