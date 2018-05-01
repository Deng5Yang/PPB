package tw.com.lccnet.dkyang.lccapp;

/**
 * Created by ASUS on 2017/11/14.
 */

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;


//做客製化

public class CustomerBusAdapter extends BaseAdapter{

    private ArrayList<ListItem> listData;
    private LayoutInflater layoutInflater;
    Context context;


    public CustomerBusAdapter(Context context,ArrayList<ListItem> listData){

        this.context = context;
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
        ButtonListener listener = null;
        if(convertView == null){
            listener = new ButtonListener(position);
            convertView = layoutInflater.inflate(R.layout.bus_item,null);
            holder = new ViewHolder();

            holder.station = (TextView)convertView.findViewById(R.id.txtname);
            holder.address = (TextView)convertView.findViewById(R.id.txtaddress);
            holder.tel = (TextView)convertView.findViewById(R.id.txttel);
            holder.opentime = (TextView)convertView.findViewById(R.id.txttime);

            holder.starts = (Button)convertView.findViewById(R.id.starts);
            holder.ends = (Button)convertView.findViewById(R.id.ends);


            holder.starts.setOnClickListener(listener);
            holder.ends.setOnClickListener(listener);

            convertView.setTag(holder);
            holder.starts.setTag(position);//要放進來才會做
            holder.ends.setTag(position);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }



        ListItem newItem = listData.get(position);
        holder.station.setText(newItem.getStation());
        holder.tel.setText("起點站 :" + newItem.getTel());
        holder.opentime.setText("目的地 :" + newItem.getOpentime());
        holder.starts.setTag(position);
        holder.ends.setTag(position);
        return convertView;


    }



    static class ViewHolder{
        TextView station,address,tel,opentime;
        Button starts,ends;
    }


    private class ButtonListener implements View.OnClickListener{

        int position;

        public ButtonListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            ListItem newItem = null;
            if(view.getId() == R.id.starts){
                int position = (Integer)view.getTag();
                newItem  = listData.get(position);
                Intent it = new Intent(context,BusStation.class);
                Bundle bundle = new Bundle();
                bundle.putString("RouteID",newItem.getID());
                bundle.putInt("goBack",1);
                it.putExtras(bundle);
                context.startActivity(it);
            }else{
                int position = (Integer)view.getTag();
                newItem  = listData.get(position);
                Intent it = new Intent(context,BusStation.class);
                Bundle bundle = new Bundle();
                bundle.putString("RouteID",newItem.getID());
                bundle.putInt("goBack",2);
                it.putExtras(bundle);
                context.startActivity(it);
            }
        }
    }




}
