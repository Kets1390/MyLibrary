package com.my_utils;

import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.my_utils_lib.M_utils;

public class MainActivity extends AppCompatActivity {
boolean isMsgShow=true;
boolean isCancelable=false;
  private  Handler handler;
    private String Date="2017-02-01 15:12:00";
    private String Convert_Format="dd/MM/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final M_utils m_utils=new M_utils(MainActivity.this);// lib for common utils
        Log.i("H",""+M_utils.Device_height);
        Log.i("W",""+M_utils.Device_width);

      //  m_utils.ShowToastMSG("Test msg",M_utils.tostLen_long);// Show toast msg

        m_utils.isConnectingToInternet(isMsgShow); // Check internet connection

        m_utils.mProgressDialog("Please wait...",isCancelable);// Generate Progress Dialog

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                m_utils.DismissmProgressDialog();//Dismiss Progress Dialog;
            }
        },5000);
        String newDate=m_utils.TimeFormatConverter(M_utils.DateFormat_YYYY_MM_DDHHmm,Convert_Format,Date);// Date Converter
        m_utils.ShowToastMSG(""+m_utils.Date_Different(M_utils.DateFormat_YYYY_MM_DDHHmm,"2017-01-01 12:12:12","2017-02-01 12:12:12"),M_utils.tostLen_long);

    }
}
