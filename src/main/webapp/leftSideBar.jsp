<%@page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<div id="leftSideBar">
    <div id="sidebar">
        <div id="wrap">
            <div id="prof" class="item">
			                <span>
			                    <i class="glyphicon glyphicon-plane"></i>
			                </span>
                <div>
                    航班信息
                </div>
            </div>
            <div id="asset" class="item">
			                <span>
			                    <i class="glyphicon glyphicon-font"></i>
			                </span>
                <div>
                    <a style="color:#333" onmouseover="this.style.cssText='text-decoration:none';"
                       onmouseout="this.style.cssText='color:#333';" href="servlet/FlightServlet">航班数据</a>
                </div>
            </div>
            <div id="brand" class="item">
			                <span>
			                    <i class="glyphicon glyphicon-cloud"></i>
			                </span>
                <div>
                    天气
                </div>
            </div>
            <div id="broadcast" class="item">
			                <span>
			                    <i class="glyphicon glyphicon-bell"></i>
			                </span>
                <div>
                    <a style="color:#333" onmouseover="this.style.cssText='text-decoration:none';"
                       onmouseout="this.style.cssText='color:#333';" href="delayWarn.jsp">延误警示</a>
                </div>
            </div>
            <div id="foot" class="item">
			                <span>
			                    <i class="glyphicon glyphicon-eye-open"></i>
			                </span>
                <div>
                    <a style="color:#333" onmouseover="this.style.cssText='text-decoration:none';"
                       onmouseout="this.style.cssText='color:#333';" href="flightAnalysis.jsp">航班分析</a>
                </div>
            </div>
            <div id="calendar" class="item">
			                <span>
			                    <i class="glyphicon glyphicon-earphone"></i>
			                </span>
                <div>
                    联系我们
                </div>
            </div>
        </div>
        <div id="closeBar">
            <div>
                <i class="glyphicon glyphicon-remove"></i>
            </div>
        </div>
    </div>
    <!-- end -->
    <div id="prof-content" class="nav-content">
        <div class="nav-con-close">
            <i class="glyphicon glyphicon-chevron-left"></i>
        </div>
        <div id="list">
            <div style="color: #1914e4;font-size: large;font-family: STHeiti ;font-weight:bold"> 航班信息</div>
        </div>
        <div id="airline">
            <img src="images/school.png" alt="飞机">
            <div class="father">
                <div id="Aircraft-Registered">航班号:</div>
                <span id="value-Aircraft-Registered">B1765</span>
            </div>
        </div> <!-- id=airline结束 -->
        <!-- jsp多行注释快捷键 ctrl+shift+c -->
        <div id="depature_Airport">天津滨海国际机场</div>
        <div id="loadbar">
            <img id="bar" src="images/bar.png">
        </div>
        <div id="landing_Airport">天河机场</div>
        <hr style="border: 1px dotted  #c7cdd2;margin:0px;">
        <div id="arriveTakeofTime" class="dividing-line">
            <ul>
                <li><span>SCH: </span>12:58</li><!--模拟左边航班信息的飞机图标的移动  -->
                <li><span>SDT: </span>3:18</li>
                <li><span>ACH: </span>1:00</li>
                <li><span>EDT: </span>3:30</li>
            </ul>
        </div>
        <hr style="border: 1px dotted  #c7cdd2;margin:0px;">
        <div id="Aircraft-information">
            <ul class="firstLine">
                <li>航班号</li>
                <li>CZ5225</li>
            </ul>
            <hr style="border: 1px dotted  #c7cdd2;margin:0px;">
            <ul class="secondLine">
                <li>机型</li>
                <li>机龄</li>
                <li>座位</li>
                <li>737</li>
                <li>7</li>
                <li>135</li>
            </ul>
            <hr style="border: 1px dotted  #c7cdd2;margin:0px;clear:both">
            <ul class="firstLine">
                <li>航空公司</li>
                <li>海南航空</li>
            </ul>
            <hr style="border: 1px dotted  #c7cdd2;margin:0px;clear:both">
            <ul class="secondLine">
                <li>机型</li>
                <li>机龄</li>
                <li>Airline</li>
                <li>737</li>
                <li>7</li>
                <li>海航</li>
            </ul>
            <hr style="border: 1px dotted  #c7cdd2;margin:0px;clear:both">
        </div>

    </div><!-- class是nav-content的类对应六个弹出框，注释了两个 -->
    <!-- 航班数据这一栏右边对话框设置 -->
    <!-- 			    <div id="asset-content" class="nav-content"> -->
    <!-- 			        <div class="nav-con-close"> -->
    <!-- 			            <i class="glyphicon glyphicon-chevron-left"></i> -->
    <!-- 			        </div> -->
    <!-- 			        <div> -->
    <!-- 			            航班数据 -->
    <!-- 			        </div> -->
    <!-- 			        <div style="font-size: 23px;color:red"><a href="servlet/FlightServlet">航班数据展示</a></div> -->
    <!-- 			    </div> -->
    <div id="brand-content" class="nav-content">
        <div class="nav-con-close">
            <i class="glyphicon glyphicon-chevron-left"></i>
        </div>
        <div style="color: #1914e4;font-size: large;font-family: STHeiti ;font-weight:bold">
            天气
        </div>
    </div>
    <%--<div id="broadcast-content" class="nav-content">--%>
    <%--<div class="nav-con-close">--%>
    <%--<i class="glyphicon glyphicon-chevron-left"></i>--%>
    <%--</div>--%>
    <%--<div style="color: #1914e4;font-size: large;font-family: STHeiti ;font-weight:bold">--%>
    <%--延误警示--%>
    <%--</div>--%>
    <%--</div>--%>
    <!-- 设置航班分析右边弹出菜单栏 -->
    <!-- 			    <div id="foot-content" class="nav-content"> -->
    <!-- 			        <div class="nav-con-close"> -->
    <!-- 			            <i class="glyphicon glyphicon-chevron-left"></i> -->
    <!-- 			        </div> -->
    <!-- 			        <div> -->
    <!-- 			            航班分析 -->
    <!-- 			        </div> -->
    <!-- 			    </div> -->
    <div id="calendar-content" class="nav-content">
        <div class="nav-con-close">
            <i class="glyphicon glyphicon-chevron-left"></i>
        </div>
        <div style="color: #1914e4;font-size: large;font-family: STHeiti ;font-weight:bold">
            联系我们
        </div>
    </div>
</div>
<script src="js/sidebarDemo2.js"></script>

<!--模拟左边航班信息的飞机图标沿直线从起飞机场往目的机场移动 -->
<script>
    var k = 0;
    var barTop = parseInt($("#bar").css("top").split("p")[0]);
    var showbars = setInterval(setbar, 1000);
    function setbar() {
        k += 5;
        if (k >= 200) {
            clearInterval(showbars);
        }
        barTop += 5;
        $("#bar").css("top", barTop + "px");
        //console.log("高度="+$("#bar").css("top"));
    }
</script>



