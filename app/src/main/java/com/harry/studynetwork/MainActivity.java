package com.harry.studynetwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        requestSubThread();
        TestUtil.testGet();
//        TestUtil.testPost();
    }

    public void requestSubThread(){
        new Thread(){
            @Override
            public void run() {
//                try {
//                    Test_HttpURLConnection.channel();
//                } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }


//                Test_OkHttp3.read();
//                Test_OkHttp3.read1();
                Test_OkHttp3.read2();
            }
        }.start();
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}