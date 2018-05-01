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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BusStation extends AppCompatActivity {

    private OkHttpClient client;
    ListView buslist;
    String roundid;
    int goBack;
    ArrayList<ListItem> listData = null , gostationData = null , busLocation = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        //因為跟busActivity的佈局檔一樣所以直接拿來用而原本的可以刪掉


        Bundle bundle = getIntent().getExtras();
        roundid = bundle.getString("RouteID");
        goBack = bundle.getInt("goBack");
        buslist = (ListView)findViewById(R.id.buslist);
        client = new OkHttpClient();


        gostationXML();
        parseXML("http://ibus.tbkc.gov.tw/xmlbus/StaticData/GetStop.xml?routeIds=" + roundid,1);
        parseXML("http://ibus.tbkc.gov.tw/xmlbus/GetBusData.xml?routeIds=" + roundid,2);

    }

    private void gostationXML() {

        //先給連線的位置---->url網址
        final Request request = new Request.Builder()
                .url("http://ibus.tbkc.gov.tw/xmlbus/GetEstimateTime.xml?routeIds=" + roundid)
                .build();

        //連線(異步)
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request,final IOException e) {

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(final Response response) throws IOException {
                final String resStr = response.body().string();
                final InputStream stream = new ByteArrayInputStream(resStr.getBytes(StandardCharsets.UTF_8.name()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gostationData = parse(stream);
                        buslist.setAdapter(new CustomeListAdapter(BusStation.this,gostationData));
                        buslistener();
                    }
                });
            }
        });


    }



    private ArrayList<ListItem> parse(InputStream input){

        String tagName = null;
        ArrayList<ListItem> arrayList = new ArrayList<>();
        int findCount = 0;
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();//所有的XMLPARSE都要用到這個工廠

            //定義解析器
            XmlPullParser parser = factory.newPullParser();

            //把從XML抓來的拿出來解析
            parser.setInput(new InputStreamReader(input));

            int eventType = parser.getEventType();

            //不遇到檔尾就一直解析
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){

                    //switch起頭
                    case XmlPullParser.START_DOCUMENT:
                        break;


                    //switch標頭檔
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();

                        if(tagName.equals("EstimateTime") && findCount == 0) {

                            if(goBack == Integer.parseInt(parser.getAttributeValue(null,"GoBack"))){
                                findCount++;
                                ListItem newItem = new ListItem();
                                newItem.setStation(parser.getAttributeValue(null, "StopName"));
                                newItem.setAddress(parser.getAttributeValue(null, "comeTime"));
                                arrayList.add(newItem);
                            }
                        }


                        if(tagName.equals("Stop") && findCount == 0) {

                            if(goBack == Integer.parseInt(parser.getAttributeValue(null,"GoBack"))){
                                findCount++;
                                ListItem newItem = new ListItem();
                                newItem.setLat(parser.getAttributeValue(null, "latitude"));
                                newItem.setLng(parser.getAttributeValue(null, "longitude"));
                                arrayList.add(newItem);
                            }
                        }


                        if(tagName.equals("BusData") && findCount == 0) {

                            if(goBack == Integer.parseInt(parser.getAttributeValue(null,"GoBack"))){
                                findCount++;
                                ListItem newItem = new ListItem();
                                newItem.setLat(parser.getAttributeValue(null, "Latitude"));
                                newItem.setLng(parser.getAttributeValue(null, "Longitude"));
                                arrayList.add(newItem);
                            }
                        }



                        break;





                    //switch屬性
                    case XmlPullParser.TEXT:
                        break;



                    case XmlPullParser.END_TAG:
                        String trytagName = parser.getName();
                        if(trytagName.equals("EstimateTime") | trytagName.equals("Stop") | trytagName.equals("BusData"))
                            findCount = 0;
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        break;
                }

                eventType = parser.next();//繼續跑下一個,一定要做不然不能跑

            }

            return arrayList;

        }catch (Exception e){e.printStackTrace();}

        return arrayList;

    }


    private void parseXML(String url, final int pos) {

        //先給連線的位置---->url網址
        Request request = new Request.Builder()
                .url(url)
                .build();

        //連線(異步)
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request,final IOException e) {

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(final Response response) throws IOException {
                final String resStr = response.body().string();
                final InputStream stream = new ByteArrayInputStream(resStr.getBytes(StandardCharsets.UTF_8.name()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(pos ==1)
                            listData = parse(stream);
                        else
                            busLocation = parse(stream);

                    }
                });
            }
        });


    }



    private void buslistener(){

        buslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //各站的名稱還有經緯度
                Intent it = new Intent(BusStation.this,MapsActivity.class);
                String[] arrLat = new String[listData.size()];
                String[] arrLng = new String[listData.size()];
                String[] arrStop = new String[listData.size()];


                for(int i=0;i<arrLat.length;i++){
                    arrLat[i] = listData.get(i).getLat();
                    arrLng[i] = listData.get(i).getLng();
                    arrStop[i] = listData.get(i).getStation();
                }

                //現在公車的位置
                String[] buslat = new String[busLocation.size()];
                String[] buslng = new String[busLocation.size()];
                for(int i=0;i<buslat.length;i++){
                    buslat[i] = busLocation.get(i).getLat();
                    buslng[i] = busLocation.get(i).getLng();
                }


                Bundle bundle = new Bundle();
                bundle.putStringArray("lat",arrLat);
                bundle.putStringArray("lng",arrLng);
                bundle.putStringArray("buslat",buslat);
                bundle.putStringArray("buslng",buslng);
                bundle.putStringArray("busstop",arrStop);
                it.putExtras(bundle);
                startActivity(it);





            }
        });




    }




}
