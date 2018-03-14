/**
 * @BY刘超：将String类型的数组转换成Object类型的数组
 */
var strArrToObjArr = function (string1) {
    var string = string1.split("],[");
//第一位
    var arr = [];
    var one = string[0].split("[")[1].split(",");
    var oneson = [];
    var one0 = parseFloat(one[0]);
    var one1 = parseFloat(one[1]);
    oneson.push(one0);
    oneson.push(one1);
    arr.push(oneson);
    // alert("arr[0]"+arr[0]);
    //arr.push("["+oneson+"]");
//中间位
    for (var i = 1; i < string.length - 1; i++) {
        var son = [];
        var string0 = parseFloat(string[i].split(",")[0]);
        var string1 = parseFloat(string[i].split(",")[1]);
        son.push(string0);
        son.push(string1);
        arr.push(son);
        //alert("arr"+i+"="+arr[i]);

    }
    //最后一个
    var last = string[string.length - 1].split("]")[0].split(",");
    var lastson = [];
    var last0 = parseFloat(last[0]);
    var last1 = parseFloat(last[1]);
    lastson.push(last0);
    lastson.push(last1);
    arr.push(lastson);
    return arr;
}