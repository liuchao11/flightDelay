<%@page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<!DOCTYPE html>


<html style="height: 80px;">
<head>
    <title style="text-align: center">航班延误分析系统</title>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Load Leaflet from CDN-->
    <link rel="stylesheet" href="css/leaflet.css"/>
    <script src="js/leaflet-src.js"></script>
    <!-- Esri Leaflet -->
    <script src="js/jquery-1.12.3.min.js"></script>
    <script src="js/esri-leaflet.js"></script>
    <!-- Esri Leaflet Geocoder -->
    <link rel="stylesheet" href="css/bootstrap.css">
    <link rel="stylesheet" href="css/sidebarDemo2.css">
    <link rel="stylesheet" href="css/esri-leaflet-geocoder.css">
    <script type="text/javascript" src="js/esri-leaflet-geocoder.js"></script>
    <script type="text/javascript" src="js/AnimatedMarker.js"></script>
    <script type="text/javascript" src="js/MovingMarker.js"></script>
    <script type="text/javascript" src="js/stringArrayToObjectArray.js"></script>


    <style>
        #map {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0px;
            right: 0px;
        }

        .information {
            font-size: large;
        }

        .one {
            width: 200px;
            height: 300px;
            color: red;
            background: red;
            z-index: 1500;
            float: right;
        }

        .plist {
            position: absolute;
            width: 50px;
            left: 100px;
            height: 50px;
            background-color: red;
            margin-top: 10px;
        }
    </style>

</head>
<body>
<div id="map"></div>
<%@ include file="./leftSideBar.jsp" %>
<script>

    var mbUrl = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
    var grayscale = L.tileLayer(mbUrl, {id: 'mapbox.light'}),
        streets = L.tileLayer(mbUrl, {id: 'mapbox.streets'});

    //设置地图的中心点以及放大级别
    var map = L.map('map', {
        center: [37.62373, 112.719],
        zoom: 5,
        layers: [streets]
    });
    var baseLayers = {
        "Streets": streets,
        "Grayscale": grayscale

    };
    L.control.layers(baseLayers).addTo(map);
    /*var tiles=L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw', {
     maxZoom: 18,
     id: 'mapbox.streets'
     }).addTo(map);
     */


    var arcgisOnline = L.esri.Geocoding.arcgisOnlineProvider();

    // create the geocoding control and add it to the map
    var searchControl = L.esri.Geocoding.geosearch({
        providers: [arcgisOnline],
        placeholder: 'Search locations',
        title: 'Search'
    }).addTo(map);

    /*搜索结果*/
    // create an empty layer group to store the results and add it to the map
    var results = L.layerGroup().addTo(map);
    // listen for the results event and add every result to the map
    searchControl.on("results", function (data) {
        results.clearLayers();
        for (var i = data.results.length - 1; i >= 0; i--) {
            results.addLayer(L.marker(data.results[i].latlng));
        }
    });

    /*弹出经纬度*/
    /*       var popup = L.popup();
     function onMapClick(e) {
     popup.setLatLng(e.latlng).setContent("你的位置是 " + e.latlng.toString()).openOn(map);
     }
     map.on('click', onMapClick);
     */

    /*飞机右上飞*/
    var planesIcon = L.icon({
        iconUrl: 'images/plane.png',//图片地址
        iconSize: [45, 45], // 图标大小
        //iconAnchor:   [1, 1], // point of the icon which will correspond to marker's location
        popupAnchor: [-3, -76] // point from which the popup should open relative to the iconAnchor
        //className:'plane'
    });
    //页面加载完成后使用AJAX技术从后台获取飞机的flight_Path，为飞机设定飞行路线
    var pathes = null;
    var line = new Array();
    var planes = [];


</script>

<script type="text/javascript" src="js/tempRoutes.js"></script>
<script type="text/javascript" src="js/markOnClick.js"></script>
<!-- <script type="text/javascript" src="js/addroutes.js"></script> -->


</body>
</html>
