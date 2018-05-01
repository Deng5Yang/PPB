package tw.com.lccnet.dkyang.lccapp;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.SerializedName;

//連線(第三方函數庫(LIBRARY)必須要IMPORT)
import com.squareup.okhttp.Callback;//東西回來
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;//塞URL
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class SpeedActivity extends AppCompatActivity {

    OkHttpClient client;
    ListView airlist;
    ArrayList<ListItem> listData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);

        airlist = (ListView) findViewById(R.id.airlist);
        client = new OkHttpClient();
        parseJSON();
    }
/*
    private class JsonData {
        @SerializedName("PM25")//這行一定要寫,這行是JSON裡面的KEY,會把抓到的KEY傳到你下面定義的值裡面
        private String location;
        @SerializedName("county")
        private String lng;
        @SerializedName("Site")
        private String lat;//名子不一定要跟它上面的一樣

        public String getLocation() {
            return location;
        }

        public String getLongitude() {
            return lng;
        }

        public String getLatitude() {
            return lat;
        }

    }
*/

    private void parseJSON() {

        //先給連線的位置---->JSON網址
        Request request = new Request.Builder()
                .url("https://pm25.lass-net.org/data/last-all-airbox.json")
                .build();

        //連線(異步)
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(final Response response) throws IOException {
                final String resStr = response.body().string();//得到網頁的原始碼

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listData = parse(resStr);
                        airlist.setAdapter(new AirAdapter(SpeedActivity.this,listData));

                        }


                });
            }
        });
    }


    private ArrayList<ListItem> parse(String str){
        ArrayList<ListItem> arrayList = new ArrayList<>();


        try {

            JSONObject oo = new JSONObject(str);
            String s = oo.getString("feeds");

            JSONArray arr = new JSONArray(s);

            for(int i=0;i<arr.length();i++){
                ListItem item = new ListItem();
                item.setStation(arr.getJSONObject(i).getString("SiteName"));
                item.setTel(arr.getJSONObject(i).getString("s_d0"));
                arrayList.add(item);
            }

        /*
        try{
            JSONArray arr = new JSONArray(str);

            for(int i=0;i<arr.length();i++){
                ListItem item = new ListItem();
                item.setStation(arr.getJSONObject(i).getString("Site"));
                item.setTel(arr.getJSONObject(i).getString("PM25"));
                arrayList.add(item);
            }

*/



        }catch(Exception e){}
        return arrayList;
    }



}
