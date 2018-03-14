package test;


import com.google.gson.Gson;

import java.sql.ResultSet;
import connection.HiveConnection;

/**
 * Created by cliu_yjs15 on 2018/3/3.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        HiveConnection hiveConnection = new HiveConnection();
        String sql = "SELECT * FROM `TopSmartphoneRatings` where 1=1";
        ResultSet resultSet = hiveConnection.getData(sql);
        //resultSet.close();
//        while (resultSet.next()){
//            System.out.println(resultSet.getString(1)+", "+resultSet.getFloat(2)
//                    +","+resultSet.getFloat(3)+","+resultSet.getFloat(4)+","+resultSet.getFloat(5));
//        }
//        for (int i=0;i<resultSet.getMetaData().getColumnCount();i++){
//            System.out.println("______________"+resultSet.getMetaData().getColumnName(i+1));
//        }
        Gson gson = new Gson();
//        System.out.println("resultSet================="+resultSet.next());
//        ResultSet resultSet1= resultSet;
//        ResultSet resultSet2= resultSet;
//        ResultSet resultSet3= resultSet;
//
//        HashMap<String, List<HashMap<String, String>>> columns = hiveConnection.setColumns(resultSet);
//        resultSet.beforeFirst();
//        System.out.println("columns=================" + gson.toJson(columns));
//        HashMap<String, HashMap<String, List<HashMap<String, String>>>> rows = hiveConnection.setRows(resultSet);
//        resultSet.beforeFirst();
//        System.out.println("resultSet2================="+resultSet2.next());
/*        HashMap<String, HashMap<String, List<HashMap<String, String>>>> datas = hiveConnection.setData(resultSet);
        resultSet.beforeFirst();
        System.out.println("resultSet3================="+resultSet.next());*/
/*        System.out.println(gson.toJson(columns));
        System.out.println(gson.toJson(rows));*/
//
//        System.out.println(gson.toJson(datas));

/*        List<HashMap<String, String>> lists = new ArrayList<>();
        while(resultSet.next()){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("id",resultSet.getString(1));
            hashMap.put("label",resultSet.getString(1).toUpperCase());
            lists.add(hashMap);
        }*/

/*

        System.out.println(gson.toJson(lists));
        HashMap<String, List<HashMap<String, String>>> rowHashMaps= new HashMap<>();
        rowHashMaps.put("row",lists);
        System.out.println(gson.toJson(rowHashMaps));
        HashMap<String,HashMap<String, List<HashMap<String, String>>>> rowsHashMap = new HashMap<>();
        rowsHashMap.put("rows",rowHashMaps);
        System.out.println(gson.toJson(rowsHashMap));


*/


/*
        System.out.println("==================");
        System.out.println(gson.toJson(hiveConnection.setColumns(resultSet)));
        System.out.println("==================");
        System.out.println(gson.toJson(hiveConnection.setData(resultSet)));
*/




/*

        HashMap<String, HashMap<String, List<HashMap<String, Integer>>>> hashMapAll= new HashMap<>();
        for (int k = 0 ;k<4;k++){
            List<HashMap<String,Integer>> lists = new ArrayList<>();
            for (int i =0;i<3;i++){
                HashMap<String,Integer> hashMap = new HashMap<>();
                hashMap.put("id"+i,i);
                lists.add(hashMap);
            }
            HashMap<String, List<HashMap<String, Integer>>> hashMap1= new HashMap<>();

            if (k == 0){
                hashMap1.put("row",lists);
                hashMapAll.put("rows",hashMap1);
            }else if(k==1){
                hashMap1.put("column",lists);
                hashMapAll.put("columns",hashMap1);
            }else if(k==2){
                hashMap1.put("data",lists);
                hashMapAll.put("dataset",hashMap1);
            }else if(k==3){
                hashMap1.put("color",lists);
                hashMapAll.put("colorRange",hashMap1);
            }


        }
        Gson gson = new Gson();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(gson.toJson(hashMapAll));
*/



    }


}
