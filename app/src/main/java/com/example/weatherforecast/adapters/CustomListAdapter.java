package com.example.weatherforecast.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherforecast.Utilities.DownloadImageTask;
import com.example.weatherforecast.activities.Weather;
import com.example.weatherforecast.activities.WeatherDetail;
import com.tlenclos.weatherforecast.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CustomListAdapter extends BaseAdapter {

    private ArrayList<Weather> listData;
    private Context context;

    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList<Weather> listData) {
        this.context=context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_day, null);
            holder = new ViewHolder();
            holder.dayView = (TextView) convertView.findViewById(R.id.day);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.infosView = (TextView) convertView.findViewById(R.id.infos);
            holder.iconView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Weather weather = listData.get(position);
        holder.dayView.setText(new SimpleDateFormat("EEEE dd", Locale.getDefault()).format(weather.day));
        holder.infosView.setText(String.format("%.1f�C / %d", weather.temperature, weather.pressure));
        holder.description.setText(weather.description);

        if (weather.iconUri != null)
            new DownloadImageTask(holder.iconView).execute(weather.iconUri);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, WeatherDetail.class);
                intent.putExtra("day", (String) holder.dayView.getText());
                intent.putExtra("infos", (String) holder.infosView.getText());
                intent.putExtra("description", (String) holder.description.getText());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView dayView;
        TextView description;
        TextView infosView;
        ImageView iconView;
    }

}