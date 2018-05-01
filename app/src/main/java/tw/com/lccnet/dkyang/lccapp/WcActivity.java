package tw.com.lccnet.dkyang.lccapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class WcActivity extends AppCompatActivity {

    OkHttpClient client;
    ListView parklist;
    ArrayList<ListItem> listData = null;

    String url;
    int status = 0;
    TreeMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wc);

        parklist = (ListView) findViewById(R.id.parklist);
        client = new OkHttpClient();
        parseJSON();

    }


    private void parseJSON() {

        //網址另外宣告,把程式做成迴圈這樣就不重複打了
        if(status == 0)
            url = "http://data.ntpc.gov.tw/od/data/api/E09B35A5-A738-48CC-B0F5-570B67AD9C78?$format=json";
        else
            url = "http://data.ntpc.gov.tw/od/data/api/B1464EF0-9C7C-4A6F-ABF7-6BDF32847E68?$format=json";



        //先給連線的位置---->JSON網址

        Request request = new Request.Builder().url(url).build();


        //連線(異步)
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(final Response response) throws IOException {
                final String resStr = response.body().string();//得到JSON的內容

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(status == 0){
                            addCar(resStr);
                            status = 1;
                            parseJSON();
                            //抓完第一個網頁資料後讓parseJSON()再跑一次
                        }else {
                            listData = parse(resStr);//把抓下來的資料塞進陣列
                            parklist.setAdapter(new ParkAdapter(WcActivity.this,listData));
                            addlistListener();
                        }
                    }


                });
            }
        });

    }


    private ArrayList<ListItem> parse(String str){
        ArrayList<ListItem> arrayList = new ArrayList<>();
        TMParameter tmp = new TMParameter(250000.00, 0.0, Math.toRadians(121.0), 0.9999, 6378137.0, 6356752.3141, 0.0818201799960599);
        double[] lat;

        try {

            //把抓到的字串丟進arr
            JSONArray arr = new JSONArray(str);

            for(int i=0;i<arr.length();i++){
                ListItem item = new ListItem();
                item.setID(arr.getJSONObject(i).getString("ID"));
                item.setStation(arr.getJSONObject(i).getString("NAME"));
                item.setTel(arr.getJSONObject(i).getString("PAYEX"));
                item.setOpentime(arr.getJSONObject(i).getString("SERVICETIME"));


                lat = TMToLatLon.convert(tmp,Double.parseDouble(arr.getJSONObject(i).getString("TW97X"))
                        ,Double.parseDouble(arr.getJSONObject(i).getString("TW97Y")));
                //(Double.parseDouble(string))轉DOUBLE

                item.setLat(String.valueOf(lat[0]));
                item.setLng(String.valueOf(lat[1]));




                item.setAddress(map.get(item.getID()));
                //如果MAP沒有宣告泛型就自己轉字串
                //map.get(item.getID()).toString()
                //(String)map.get(item.getID())
                arrayList.add(item);

            }

        }catch(Exception e){}
        return arrayList;
    }


    private void addCar(String str){
        try {

            map = new TreeMap<>();
            //把抓到的字串丟進arr
            JSONArray arr = new JSONArray(str);

            for(int i=0;i<arr.length();i++){
                map.put(arr.getJSONObject(i).getString("ID")
                        ,arr.getJSONObject(i).getString("AVAILABLECAR"));
            }
        }catch(Exception e){}
    }

    private void addlistListener(){
        parklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getApplicationContext(),ParkMap.class);
                ListItem item = listData.get(position);
                Bundle bundle = new Bundle();

                String[] arrLat = new String[listData.size()];
                String[] arrLng = new String[listData.size()];
                String[] arrRest = new String[listData.size()];
                String[] arrName = new String[listData.size()];


                for(int i=0;i<arrLat.length;i++){
                    arrLat[i] = listData.get(i).getLat();
                    arrLng[i] = listData.get(i).getLng();
                    arrRest[i] = listData.get(i).getAddress();
                    arrName[i] = listData.get(i).getStation();
                }


                bundle.putString("lat",item.getLat());
                bundle.putString("lng",item.getLng());
                bundle.putString("name",item.getStation());
                bundle.putString("rest",item.getAddress());
                bundle.putStringArray("arrLat",arrLat);
                bundle.putStringArray("arrLng",arrLng);
                bundle.putStringArray("arrRest",arrRest);
                bundle.putStringArray("arrName",arrName);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
    }

}
