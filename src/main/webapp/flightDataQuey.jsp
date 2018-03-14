<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    System.out.println("path=" + path + "    basePath=" + basePath);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>Fligth Report</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
</head>
<style type="text/css">
    table.imagetable {
        font-family: verdana, arial, sans-serif;
        font-size: 11px;
        color: #333333;
        border-width: 1px;
        border-color: #999999;
        border-collapse: collapse;
    }

    table.imagetable th {
        background: #b5cfd2 url('images/cell-blue.jpg');
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #999999;
    }

    table.imagetable td {
        background: #dcddc0 url('images/cell-grey.jpg');
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        text-align: center;
        border-color: #999999;
    }

    body {
        font-family: verdana, arial, sans-serif;
        font-size: 15px;
    }
</style>
<script type="text/javascript">
    function goPage(pageNo) {
        window.location.replace("<%=path%>/servlet/FlightServlet?pageNo=" + pageNo);
    }
</script>
<script language="javascript" type="text/javascript" src="js/My97DatePicker/datepicker/WdatePicker.js"></script>


<body>
<form action="servlet/FlightServlet" method="post" name="selForm">
    <input type="hidden" name="action" value="search">
    飞行日期：
    <input type="text" name="flightDate" value="" readonly="readonly" ;size="16" class="Wdate"
           onfocus="WdatePicker({lang:'zh-cn',skin:'whyGreen'})"/>

    航空公司号：
    <input type="text" name="airlineId" value="">

    尾流号：&nbsp;&nbsp;
    <input type="text" name="tailNum" value="">
    <br/>
    航班号：&nbsp;&nbsp;&nbsp;
    <input type="text" name="flightNum" value="">
    <!--
    起飞机场号：
    <input type="text" name="originAirportId" value="">
     -->
    起飞机场：&nbsp;&nbsp;
    <input type="text" name="originAirport" value="">

    <!--
    目的机场号
    <input type="text" name="destAirportId" value="">
     -->
    目的机场：
    <input type="text" name="destAirport" value="">
    <input type="submit" value="查询">
</form>


<table width="100%" class="imagetable">
    <tr>
        <th class="td">年</th>
        <th class="td">季度</th>
        <th class="td">月</th>
        <th class="td">日</th>
        <th class="td">星期</th>
        <th class="td">飞行日期</th>
        <th class="td">航空编码</th>
        <th class="td">航空公司编号</th>
        <th class="td">尾流号</th>
        <th class="td">航班号</th>
        <th class="td">起飞机场</th>
        <th class="td">起飞城市</th>
        <th class="td">目的机场</th>
        <th class="td">目的城市</th>
    </tr>

    <c:forEach var="flt" items="${pageBean.list}">
        <tr>
            <td class="td">${flt.year}</td>
            <td class="td">${flt.season}</td>
            <td class="td">${flt.month}</td>
            <td class="td">${flt.day}</td>
            <td class="td">${flt.dayOfWeek}</td>
            <td class="td">${flt.flightDate}</td>
            <td class="td">${flt.carrrier}</td>
            <td class="td">${flt.airlineId}</td>
            <td class="td">${flt.tailNum}</td>
            <td class="td">${flt.flightNum}</td>
            <td class="td">${flt.orgin}</td>
            <td class="td">${flt.orginCityName}</td>
            <td class="td">${flt.dest}</td>
            <td class="td">${flt.destCityName}</td>
        </tr>
    </c:forEach>
</table>
<TABLE width="98%">
    <TR>
        <TD>
            <STRONG>
                共有<FONT color="#ff6600">${pageBean.totalCount}</FONT>条记录&nbsp;
                当前第<FONT color="#ff6600">${pageBean.pageNo}/${pageBean.pageCount}页</FONT></STRONG>&nbsp;
        </TD>
        <TD align="right">&nbsp;
            <a href="servlet/FlightServlet?pageNo=1">首页</a>&nbsp;&nbsp;
            <a href="servlet/FlightServlet?pageNo=${pageBean.pageNo-1}">上一页</a>&nbsp;&nbsp;
            <a href="servlet/FlightServlet?pageNo=${pageBean.pageNo+1}">下一页</a>&nbsp;&nbsp;
            <a href="servlet/FlightServlet?pageNo=${pageBean.pageCount}">末页</a>&nbsp;&nbsp;

            跳到：第&nbsp;<INPUT onkeydown="if(event.keyCode==13)event.returnValue=false"
                             style="WIDTH: 40px; HEIGHT: 18px"
                             onchange="if(/\D/.test(this.value)){alert('只能在跳转目标页框内输入整数！');this.value='1';}"
                             value="${pageBean.pageNo}" name="SkipPage">&nbsp;页
            <INPUT class="button" style="WIDTH: 25px; HEIGHT: 18px" onclick='goPage(SkipPage.value)' type="button"
                   value="GO" name="submitSkip">
        </TD>
    </TR>
</TABLE>
</body>


