package com.example.weatherforecast.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.tlenclos.weatherforecast.R;


/**
 * Created by Wittybrains on 5/6/2016.
 */
public class WeatherDetail  extends Activity{
    private String des,infos,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_detail);

        TextView textView=(TextView) findViewById(R.id.detail_tv);
        Bundle bundle=getIntent().getExtras();
        if(bundle !=null){
            des=bundle.getString("description");
            infos=bundle.getString("infos");
            day=bundle.getString("day");
        }
        textView.setTextSize(20);
        textView.setText(""+day+"\n"+infos+"\n"+"  "+des);
    }
}
