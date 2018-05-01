package tw.com.lccnet.dkyang.lccapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ASUS on 2017/12/7.
 */

public class ParkAdapter extends BaseAdapter {

    private ArrayList<ListItem> listData;
    private LayoutInflater layoutInflater;
    Context context;


    public ParkAdapter(Context context,ArrayList<ListItem> listData){

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


        ParkAdapter.ViewHolder holder;

        if(convertView == null){

            convertView = layoutInflater.inflate(R.layout.park,null);
            holder = new ParkAdapter.ViewHolder();

            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.time = (TextView)convertView.findViewById(R.id.time);
            holder.pay = (TextView)convertView.findViewById(R.id.pay);
            holder.rest = (TextView)convertView.findViewById(R.id.rest);



            convertView.setTag(holder);

        }else{
            holder = (ParkAdapter.ViewHolder)convertView.getTag();
        }

        ListItem newItem = listData.get(position);
        holder.name.setText(newItem.getStation());
        holder.time.setText(newItem.getOpentime());
        holder.pay.setText(newItem.getTel());
        holder.rest.setText(newItem.getAddress());


        if(Integer.parseInt(newItem.getAddress()) == 0){
            convertView.setBackgroundColor(Color.RED);
        }else{
            convertView.setBackgroundColor(Color.GREEN);
        }


        return convertView;

    }



    static class ViewHolder{
        TextView name,rest,pay,time;

    }


}
