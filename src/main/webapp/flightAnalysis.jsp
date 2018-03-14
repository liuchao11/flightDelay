<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="fusioncharts.CreateCondition" %>
<%@page import="fusioncharts.FusionCharts" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Map" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; chars1et=UTF-8">
    <title>甜饼圈图案</title>
    <meta chars1et="utf-8"/>
    <script src="js/fusioncharts.js"></script>
    <script src="js/themes/fusioncharts.theme.fint.js?cacheBust=56"></script>
    <script src="js/themes/fusioncharts.theme.carbon.js?cacheBust=56"></script>
    <script src="js/themes/fusioncharts.theme.ocean.js?cacheBust=56"></script>
    <script src="js/themes/fusioncharts.theme.zune.js?cacheBust=56"></script>
    <script src="js/jquery-1.12.3.min.js"></script>
    <link href="css/main.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<div class="top_menu" id="main" role="main">
    <ul class="menu">
        <li onclick="changeDisplay(this.id)" id="mainPage"><a href="#">航班延误介绍</a></li>
        <li onclick="changeDisplay(this.id)" id="delayReason"><a href="#">延误原因</a></li>
        <li class="active"><a href="#s2">延误机场</a>
            <ul class="submenu">
                <li onclick="changeDisplay(this.id)" id="delayAirport"><a href="#">准点率排名</a></li>
                <li onclick="changeDisplay(this.id)" id="delayDay"><a href="#">日均延误</a></li>
                <li onclick="changeDisplay(this.id)" id="delayHour"><a href="#">时均延误</a></li>
            </ul>
        </li>
        <li><a href="#">延误网络</a>
            <ul class="submenu">
                <li><a href="#">Submenu a</a></li>
                <li><a href="#">Submenu b</a></li>
                <li><a href="#">Submenu c</a></li>
                <li><a href="#">Submenu d</a></li>
                <li><a href="#">Submenu e</a></li>
                <li><a href="#">Submenu f</a></li>
                <li><a href="#">Submenu g</a></li>
                <li><a href="#">Submenu h</a></li>
            </ul>
        </li>
        <li><a href="map2.jsp">返回主页面</a></li>
        <li><a href="#">联系我们</a></li>
    </ul>
</div>
<div id="mainPage_id" class="close-item">
    <div style="margin:35px 60px;"><img src="images/plane_delay.jpg" style="width:1783px;height:550px;"></div>
    <div>
        <p>航班延误是指航班降落时间（航班实际到港挡轮挡时间）比计划降落时间（航班时刻表上的时间）延迟15分钟以上或航班取消的情况。</p>
        <p>据《全国民航航班运行效率报告》显示，2016年平均航班延误率高达25.96%，已经严重影响旅客的出行。</p>
    </div>
</div>
<div id="delayReason_id" style="display: none" class="close-item">
    <div class="chart-container">
        <div id="chart-container1">
            <%
                CreateCondition delayReasonPie = new CreateCondition();
                Map<String, String> pieobj = delayReasonPie.setAttribute(Arrays.asList(
                        "caption-航班延误原因统计饼图",
                        "subCaption-五大原因",
                        "numberPrefix-%",
                        "showPercentValues-1",
                        "decimals-3",
                        "showPercentInTooltip-0",
                        "decimals-3",
                        "theme-fint"));
                FusionCharts pieChart = null;
                String sql = "SELECT * FROM `delay_cause` where 1=?";
                Object[] params = new Object[]{1};
                try {
                    pieChart = delayReasonPie.getFusionCharts(
                            pieobj,
                            delayReasonPie.getData(sql, params),
                            "pie2d",
                            "pie2d",
                            "650",
                            "300",
                            "chart-container1",
                            "json"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
            <%=pieChart.render()%>
        </div>
        <div id="chart-container2">
            <%
                CreateCondition delayReasonDoughnut = new CreateCondition();
                Map<String, String> doughnutobj = delayReasonDoughnut.setAttribute(Arrays.asList(
                        "caption-航班延误原因统计甜甜圈图",
                        "subCaption-五大原因",
                        "numberPrefix-%",
                        "startingAngle-20",
                        "showPercentValues-1",
                        "showPercentInTooltip-0",
                        "decimals-3",
                        "useDataPlotColorForLabels-1",
                        "enableSmartLabels-1",
                        "enableMultiSlicing-1",
                        "theme-zune"
                ));
                FusionCharts DoughnutChart = null;
                try {
                    DoughnutChart = delayReasonDoughnut.getFusionCharts(
                            doughnutobj,
                            delayReasonDoughnut.getData(sql, params),
                            "doughnut3d",
                            "chart2",
                            "650",
                            "300",
                            "chart-container2",
                            "json"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
            <%=DoughnutChart.render()%>
        </div>
    </div>
    <div class="char-explain">
        <div class="char-explain-left">
            从统计图与甜甜圈图可以看出，航班到达机场时已延误、航空公司自己因素以及航空管制等是导致航班延误的主要原因。
        </div>
        <div class="char-explain-right">
            从统计图与甜甜圈图可以看出，航班到达机场时已延误、航空公司自己因素以及航空管制等是导致航班延误的主要原因。
        </div>
    </div>
</div>
<div id="delayAirport_id" class="close-item" style="display: none">
    <div class="chart-container">
        <div id="chart-container3">
            <%
                CreateCondition delayAirport = new CreateCondition();
                Map<String, String> Top8Airportobj = delayAirport.setAttribute(Arrays.asList(
                        "caption-美国8大延误机场延误率",
                        "subCaption-2015年",
                        "yAxisName-延误率",
                        "numberPrefix-%",
                        "paletteColors-#0005c2",
                        "bgColor-#ffffff",
                        "showCanvasBorder-0",
                        "useDataPlotColorForLabels-1",
                        "plotBorderAlpha-10",
                        "valueFontColor-#ffffff",
                        "showAxisLines-1",
                        "axisLineAlpha-25",
                        "divLineAlpha-10",
                        "alignCaptionWithCanvas-0",
                        "showAlternateVGridColor-0",
                        "captionFontSize-14",
                        "subcaptionFontSize-14",
                        "subcaptionFontBold-0",
                        "toolTipColor-#ffffff",
                        "toolTipBorderThickness-0",
                        "toolTipBgColor-#000000",
                        "toolTipBgAlpha-80",
                        "toolTipBorderRadius-2",
                        "toolTipPadding-5",
                        "decimals-3",
                        "theme-fint"
                ));
                FusionCharts top8DelayAirportsChart = null;
                sql = "SELECT * FROM `top8_delay_airports` where 1=?";
                params = new Object[]{1};
                try {
                    top8DelayAirportsChart = delayAirport.getFusionCharts(
                            Top8Airportobj,
                            delayAirport.getData(sql, params),
                            "bar3d",
                            "chart3",
                            "650",
                            "300",
                            "chart-container3",
                            "json"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
            <%=top8DelayAirportsChart.render() %>
        </div>
    </div>
    <div class="char-explain">
        <div class="char-explain-left">
            2015年美国八大延误机场的延误概率统计图，最易延误机场排名依次为旧金山国际机场、迈阿密国际机场、劳德代尔堡-好莱坞国际机场、达拉斯-沃斯堡国际机场、
            丹佛国际机场、拉瓜迪亚机场、洛杉矶国际机场、奥兰多国际机场。
        </div>
    </div>
</div>
<div id="delayDay_id" class="close-item" style="display: none">
    <div class="chart-container">
        <div id="chart-container5">
            <%
                CreateCondition delayDay = new CreateCondition();
                Map<String, String> Delayobj = delayDay.setAttribute(Arrays.asList(
                        "showValues-1",
                        "theme-zune",
                        "xAxisName-天",
                        "yAxisName-延误时长（分钟）",
                        "baseFontSize-20"
                ));
                FusionCharts delayDayChart = null;
                sql = "SELECT * FROM `JFK_JANURARY_DELAY` WHERE 1=?";
                params = new Object[]{1};
                try {
                    delayDayChart = delayDay.getFusionCharts(
                            Delayobj,
                            delayDay.getData(sql, params),
                            "column3d",
                            "chart5",
                            "800",
                            "300",
                            "chart-container5",
                            "json"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
            <%=delayDayChart.render() %>
        </div>
        <div class="char-explain">
            <div class="char-explain-one">
                2015年美国肯尼迪国际机场1月份每天平均延误时长统计,从图中可以看出月初与月末最容易发生航班延误，而且延误的时长也相对比较长。
            </div>
        </div>
    </div>
</div>
<div id="delayHour_id" class="close-item" style="display: none">
    <div class="chart-container">
        <div id="chart-container4">
            <%
                CreateCondition delayHour = new CreateCondition();
                Map<String, String> DelayHourobj = delayHour.setAttribute(Arrays.asList(
                        "theme-zune",
                        "caption-24个小时延误概率统计分析曲线",
                        "subCaption-2015年1月份",
                        "xAxisName-时间区间",
                        "yAxisName-延误航班数量",
                        "showValues-1"
                ));
                FusionCharts delayHourChart = null;
                sql = "select * from 24everyhourdelaycounts where 1=?";
                params = new Object[]{1};
                try {
                    delayHourChart = delayHour.getFusionCharts(
                            DelayHourobj,
                            delayHour.getData(sql, params),
                            "line",
                            "chart4",
                            "750",
                            "300",
                            "chart-container4",
                            "json"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
            <%=delayHourChart.render() %>
        </div>
    </div>
    <div class="char-explain">
        <div class="char-explain-right">
            从折线图可以看出，航班延误发生时间段主要是在上午10:00到晚上10:00这个时间段，下午4点到6点延误易达到峰值。
        </div>
    </div>
</div>
<script>
    function changeDisplay(id) {
        $(".close-item").css("display", "none");
        $("#" + id + "_id").css("display", "block");
    }
</script>

</body>
</html>