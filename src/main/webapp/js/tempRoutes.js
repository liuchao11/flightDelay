/**
 * @BY刘超：发送post请求，每隔几秒即向tempRoutes发送请求，回调数据。
 * duratime设定为服务端发射间隔的几倍，读取缓冲流中的数据，保证数据包的不丢失
 */
var url = "tempRoutesServlet",
    duratime = 2000,
    markerId = [],//存放所有的飞机国籍登记号
    plane,//存放单个飞机
    lat, lng, latitude, longitute,
    i = '0',
    j = 0;
//创建飞机图标
var myIcon = L.icon({
    iconUrl: 'images/plane.png',
    iconSize: [38, 45],
    iconAnchor: [22, 94],
    popupAnchor: [-3, -76],
    className: 'markss'
});
setInterval(function () {
    $.post(url, {id: i}, function (data) {
        var datas = JSON.parse(data);
        //console.log(datas+"--------------");
        for (var latlngs in datas) {
            if (latlngs == "lastTimes") {
                i = JSON.parse(datas[latlngs]).latitute;
            } else {
                //console.log(JSON.parse(datas[latlngs]));
                lat = JSON.parse(datas[latlngs]).latitute;
                lng = JSON.parse(datas[latlngs]).longitute;
                if (lat.substring(0, 1) == 'N') {
                    latitude = parseInt(lat.substring(1, 3)) + parseInt(lat.substring(3)) * 0.001;
                } else {
                    latitude = -(parseInt(lat.substring(1, 3)) + parseInt(lat.substring(3)) * 0.001);
                }
                if (lng.substring(0, 1) == 'E') {
                    longitute = parseInt(lng.substring(1, 4)) + parseInt(lng.substring(4)) * 0.001;
                } else {
                    longitute = -parseInt(lng.substring(1, 4)) - parseInt(lng.substring(4)) * 0.001;
                }
                //如果markerId数组里面没有这架飞机，则创建飞机，如果有飞机，则添加对应的点
                if (markerId.indexOf(latlngs) == -1) {
                    markerId.push(latlngs);
                    //plane = L.marker([latitude, longitute],{icon:planesIcon,clickable:true}).addTo(map);
                    var _iconurl = "images/plane.png";
                    var myIcon = L.divIcon({
                        className: 'my-div-icon',
                        html: "<img id=\"" + latlngs + "\"  src=\"" + _iconurl + "\"  width='38px'  height='45px'  />"
                    });
                    plane = L.Marker.movingMarker([[latitude, longitute]], [duratime * 5], {icon: myIcon}).addTo(map);
                    //plane =  L.Marker.movingMarker([[latitude, longitute]],[],{icon:myIcon}).addTo(map);
                    plane.options.icon.options.className = latlngs;
                    plane.addEventListener('click', guiji, false);
                    planes.push(plane);
//						   console.log("plane结果是"+planes[j]._latlng);
                    plane.addEventListener('click', planeItem, false);
                    plane.addEventListener('click', tubiao, false);
                }
                else {
                    for (var k in markerId) {
                        if (markerId[k] == latlngs) {
                            //console.log("经纬度为经纬度为"+planes[k]._latlng);
                            //degree用于方向的改变
                            var degree = 0;
                            if (longitute - planes[k]._latlng.lng > 0) {
                                degree = Math.atan((latitude - planes[k]._latlng.lat) / (longitute - planes[k]._latlng.lng)) / Math.PI * 180;
                            } else {
                                degree = -180 + Math.atan((latitude - planes[k]._latlng.lat) / (longitute - planes[k]._latlng.lng)) / Math.PI * 180;
                            }
                            ;
                            //判断经纬度有没有改变，degree可能是NaN类型
                            if ((latitude != planes[k]._latlng.lat) && (longitute != planes[k]._latlng.lng)) {
                                $("#" + latlngs).css({"transform": "rotate(" + degree * (-1) + "deg)"});
                                planes[k].moveTo([latitude, longitute], duratime * 30);
                                //console.log("转弯的方向是="+degree*(-1)+"+"+"latlngs="+latlngs);
                                //console.log(planes[k]._icon);
                            }

                            //var classNames = planes[k].options.icon.options.className;
                            //$(".classNames").css("transform","rotateZ(90deg)");

                        }//if(markerId[k] == latlngs)结束	
                    }//for(var k in markerId)结束
                }//if(markerId.indexOf(latlngs) == -1)的else结束	
            }//if(latlngs == "lastTimes")的else结束
        }//for(var latlngs in datas){循环请求结束
    });	//post请求结束   
}, duratime);//setIntetval结束


function guiji() {
    console.log("guiji方法开始运行");
    var url = "PathServelt";
    var id = this.options.icon.options.className;
    $.post(url, {id: id}, function (data) {
        var datas = JSON.parse(data);
        var latlngs = [];
        //console.log(datas);
        for (var i = 0; i < datas.length; i++) {
            //console.log(datas[i]);
            latlngs.push(L.latLng(parseFloat(datas[i].latitute), parseFloat(datas[i].longitute)));
        }
        var polyline = L.polyline(latlngs, {color: 'red'}).addTo(map);
    });
}
//planeItem()这个方法弹出对话框
function planeItem() {
    var url = "planeServlet";
    console.log(this.options.icon.options.className);
    var args = {"number": this.options.icon.options.className};
    $.post(url, args, function (data) {
        var planeItems = JSON.parse(data);
        var prof_content = document.getElementById("prof-content");
        prof_content.classList.add('move_up');
        prof_content.style.top = '250px';
        prof_content.style.left = '35px';
        $("#value-Aircraft-Registered").html(planeItems.flight_Number);
        $("#depature_Airport").html(planeItems.depature_Airport);
        $("#first_Airport").html(planeItems.landing_Airport);
        $("#alternate_Airport").html("(" + planeItems.landing_Airport + ")");
//			var time= new timeTransform(planeItems.expected_date_of_depature,planeItems.expected_time_of_depature,planeItems.estimated_time_of_flight);
//			$("#Time_of_Depature").html(time.getOffTime.getHours()+":"+time.getOffTime.getMinutes());
//			$("#Time_of_Arrive").html(time.arriveTime.getHours()+":"+time.arriveTime.getMinutes());
        var spanDiv = $("#particular-information>ul>li>span");
        spanDiv[0].innerText = planeItems.vacuum_Speed;
        spanDiv[1].innerText = planeItems.expected_Cruising_Altitude;
        spanDiv[2].innerText = planeItems.aircraft_Carrier_Oil;
        spanDiv[3].innerText = planeItems.number_of_Boarding;
        spanDiv[4].innerText = planeItems.maximum_Number_of_People;
        //alert(planeItems.the_Number_of_Aircraft_Registered);
        //改变飞机图片

        changePlaneImages(planeItems.aircraft_Type);
    })
}