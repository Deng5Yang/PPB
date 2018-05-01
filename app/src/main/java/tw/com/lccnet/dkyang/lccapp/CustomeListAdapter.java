package tw.com.lccnet.dkyang.lccapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ASUS on 2017/11/21.
 */

public class CustomeListAdapter extends BaseAdapter{

    private ArrayList<ListItem> listData;
    private LayoutInflater layoutInflater;



    public CustomeListAdapter(Context context,ArrayList<ListItem> listData){

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;

        if(convertView == null){

            convertView = layoutInflater.inflate(R.layout.businfo,null);
            holder = new ViewHolder();

            holder.busstation = (TextView)convertView.findViewById(R.id.busname);
            holder.bustime = (TextView)convertView.findViewById(R.id.bustime);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        ListItem newItem = listData.get(position);
        holder.busstation.setText(newItem.getStation());
        holder.bustime.setText(newItem.getAddress());

        return convertView;


    }



    static class ViewHolder{
        TextView busstation,bustime;
    }



}
