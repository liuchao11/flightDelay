<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>
<html>
<head>
    <title>热力图测试</title>
    <script language="javascript" type="text/javascript" src="${basePath}/js/fusioncharts.js"></script>
    <script language="javascript" type="text/javascript" src="${basePath}/js/fusioncharts.powercharts.js"></script>
    <script type="text/javascript" src="http://static.fusioncharts.com/code/latest/themes/fusioncharts.theme.fint.js?cacheBust=56"></script>
    <%--<script type="text/javascript" src="${basePath}/js/fusioncharts.theme.fint.js"></script>--%>
    <script src="${basePath}/js/jquery-1.12.3.min.js"></script>
    <script language="javascript" type="text/javascript" src="${basePath}/js/My97DatePicker/datepicker/WdatePicker.js"></script>
    <script type="text/javascript">
        var rows =<%=request.getAttribute("rows")%>;
        var columns =<%=request.getAttribute("columns")%>;
        var dataset =<%=request.getAttribute("dataset")%>;
        FusionCharts.ready(function () {
            var fusioncharts = new FusionCharts({
                    type: 'heatmap',
                    renderAt: 'chart-container',
                    width: '550',
                    height: '270',
                    dataFormat: 'json',
                    dataSource: {
                        "chart": {
                            "theme": "fint",
                            "caption": "航班延误等级预测热力图",
                            "subcaption": "",
                            "xAxisName": "延误情况",
                            "yAxisName": "航班号",
                            "canvasbgColor": "#1790e1",
                            "canvasbgAlpha": "10",
                            "canvasBorderThickness": "1",
                            "placeXAxisLabelsOnTop" : "1",
                            "showPlotBorder": "1"
                        },
                        "rows": rows,
                        "columns": columns,
                        "dataset": dataset,
                        "colorRange": {
                            "gradient": "1",
                            "minValue": "0",
                            "code": "#ffffff",
                            "startLabel": "准点",
                            "endLabel": "重度延误",
                            "color": [{
                                "code": "#0000CD",
                                "minValue": "0",
                                "maxValue": "1",
                                "label": "不延误"
                            },  {
                                "code": "#7FFFD4",
                                "minValue": "1",
                                "maxValue": "2",
                                "label": "轻度延误"
                            }, {
                                "code": "#8E8E38",
                                "minValue": "2",
                                "maxValue": "3",
                                "label": "中度延误"
                            }, {
                                "code": "#CDCD00",
                                "minValue": "3",
                                "maxValue": "4",
                                "label": "高度延误"
                            },{
                                "code": "#FF0000",
                                "minValue": "4",
                                "maxValue": "5",
                                "label": "重度延误"
                            }]
                        }
                    }
                }
            );
            fusioncharts.render();
        });
    </script>
</head>
<body>
<div>
    <form action="${basePath}/servlet/delayWarnServlet"  method="post">
        开始时间：
        <input type="text" name="startDate" id="startDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDate\')}'})" />
        结束时间：
        <input type="text" name="endDate" id="endDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startDate\')}'})" />
        <input type="submit" id="dosubmit" value="提交">
    </form>
</div>
<div id="chart-container" style="width:100%;height:100%;overflow:auto;">热力图</div>
</body>

</html>
