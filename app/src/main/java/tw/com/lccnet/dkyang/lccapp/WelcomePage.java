package tw.com.lccnet.dkyang.lccapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomePage extends AppCompatActivity {


    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == 1111) {
                Intent it = new Intent(WelcomePage.this,MainActivity.class);
                startActivity(it);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceStategi
        setContentView(R.layout.activity_welcome_page);

        new Thread(new Runnable(){

            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (Exception e){}

                Message msg = new Message();
                //msg.what = 1111;
                handler.sendEmptyMessage(1111);

            }
        }).start();

    }
}
