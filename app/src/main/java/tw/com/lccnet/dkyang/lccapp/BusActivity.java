package tw.com.lccnet.dkyang.lccapp;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


//網路的部分
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.widget.ListView;

//IO的部分
import java.io.IOException;
import java.io.InputStream;//從網路抓進來是位元
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


//解析XML
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;//要實作一定要有FACTORY
import org.xmlpull.v1.XmlPullParserException;




public class BusActivity extends AppCompatActivity {

    OkHttpClient client;
    ListView buslist;
    ArrayList<ListItem> listData = null;//因為有順序性所以用ARRAYLIST泛型





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        buslist = (ListView)findViewById(R.id.buslist);
        client = new OkHttpClient();
        parseXML();

    }

    private void parseXML() {

        //先給連線的位置---->url網址
        Request request = new Request.Builder()
                .url("http://ibus.tbkc.gov.tw/xmlbus/StaticData/GetRoute.xml")
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
                        listData = parse(stream);
                        buslist.setAdapter(new CustomerBusAdapter(BusActivity.this,listData));
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
                        if(tagName.equals("Route") && findCount == 0){
                            findCount++;
                            ListItem newItem = new ListItem();
                            newItem.setID(parser.getAttributeValue(null,"ID"));
                            newItem.setStation(parser.getAttributeValue(null,"nameZh"));
                            newItem.setAddress(parser.getAttributeValue(null,"ddesc"));
                            newItem.setTel(parser.getAttributeValue(null,"departureZh"));
                            newItem.setOpentime(parser.getAttributeValue(null,"destinationZh"));
                            arrayList.add(newItem);
                        }
                        break;


                    //switch屬性
                    case XmlPullParser.TEXT:
                        break;



                    case XmlPullParser.END_TAG:
                        String trytagName = parser.getName();
                        if(tagName.equals("Route"))
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

}
