package tw.com.lccnet.dkyang.lccapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void busClick(View view){
        Intent it = new Intent(getApplicationContext(),BusActivity.class);
        startActivity(it);
    }

    public void foodClick(View view){
        Intent it = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(it);
    }

    public void speedClick(View view){
        Intent it = new Intent(getApplicationContext(),SpeedActivity.class);
        startActivity(it);
    }

    public void wcClick(View view){
        Intent it = new Intent(getApplicationContext(),WcActivity.class);
        startActivity(it);
    }




}
