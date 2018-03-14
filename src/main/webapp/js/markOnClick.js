/**
 * @BY刘超：单击飞机，从HBase调取该飞机的属性。
 * 并在页面显示
 */
for (var i = 0; i < planes.length; i++) {
    //planeItem()这个方法弹出对话框
    planes[i].addEventListener('click', planeItem, false);
    //tubiao()这个方法弹出提示框
    planes[i].addEventListener('click', tubiao, false);
    // 更改飞机的方向
    changePaths(planes[i]);
    //单机飞机时候，从数据库提取飞机的路径并回放路线
    planes[i].addEventListener('click', xianshi, false);
}
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
//				var time= new timeTransform(planeItems.expected_date_of_depature,planeItems.expected_time_of_depature,planeItems.estimated_time_of_flight);
//				$("#Time_of_Depature").html(time.getOffTime.getHours()+":"+time.getOffTime.getMinutes());
//				$("#Time_of_Arrive").html(time.arriveTime.getHours()+":"+time.arriveTime.getMinutes());
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

//单击弹出飞机的经纬度数据
function tubiao() {
    this.bindPopup("飞机" + this.options.icon.options.className + "的位置：" + "<br/>" + String(this.getLatLng(), {
            offset: (0, 0),
            autoPanPadding: (5, 5),
            autoPan: true,
            keepInview: true,
        }));
}

//将出发时间,转换"hours:minutes"形式的时间格式；同时计算到达时间，根据出发时间和预计飞行时间
function timeTransform(big, small, sum) {
    var year = "20".concat(big.slice(0, 2));
    var month = big.slice(2, 4);
    var day = big.slice(4, 6);
    var hour = small.slice(0, 2);
    var minute = small.slice(2, 4);
    var getOffTime = new Date(year + "-" + month + "-" + day + " " + hour + ":" + minute);
    var myDate1 = getOffTime.getTime() + parseInt(sum) * 60000;
    var arriveTime = new Date(myDate1);
    return {"getOffTime": getOffTime, "arriveTime": arriveTime};
}

//根据飞机类型更改飞机图片
function changePlaneImages(aircraft_Type) {
    var plane = null;
    switch (aircraft_Type) {
        case "C170":
            plane = "0.png";
            break;
        case "C171":
            plane = "1.jpg";
            break;
        case "C172":
            plane = "2.jpg";
            break;
        case "C173":
            plane = "3.jpg";
            break;
        case "C174":
            plane = "4.jpg";
            break;
        case "C175":
            plane = "5.jpg";
            break;
        case "C176":
            plane = "6.jpg";
            break;
        case "C177":
            plane = "7.jpg";
            break;
        case "C178":
            plane = "8.jpg";
            break;
        case "C179":
            plane = "9.jpg";
            break;
    }
    $("#list img").attr("src", "images/planes/" + plane);
}
//将传递进来的飞机根据斜率改变它的朝向
function changePaths(mark) {
    var lat = 0;
    var lng = 0;
    var i = 0;
    setInterval(function () {
        var latitute = mark.getLatLng().lat;
        var longitude = mark.getLatLng().lng;
        var deg = Math.atan2(latitute - lat, longitude - lng) * 180 / Math.PI;
        var className = mark.options.icon.options.className;
        $(".className").css("transform", "rotateZ(90deg)");
        i++;
        //mark.icon.css("transform","rotateZ("+deg+"deg)");
        //console.log(Math.atan2(latitute,longitude)*180/Math.PI);

        lat = latitute;
        lng = longitude;
        console.log(className);
    }, 1);
}
