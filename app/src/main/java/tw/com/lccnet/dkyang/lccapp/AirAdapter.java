package tw.com.lccnet.dkyang.lccapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ASUS on 2017/12/5.
 */

public class AirAdapter extends BaseAdapter {

    private ArrayList<ListItem> listData;
    private LayoutInflater layoutInflater;
    Context context;


    public AirAdapter(Context context,ArrayList<ListItem> listData){

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

        if(convertView == null){

            convertView = layoutInflater.inflate(R.layout.pm25_item,null);
            holder = new ViewHolder();

            holder.site = (TextView)convertView.findViewById(R.id.site);
            holder.pm25 = (TextView)convertView.findViewById(R.id.pm25);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        ListItem newItem = listData.get(position);
        holder.site.setText(newItem.getStation());
        holder.pm25.setText("PM2.5 : " + newItem.getTel());


        int pm = Integer.parseInt(newItem.getTel());
        if(pm>0) {

            if (pm < 15) {
                convertView.setBackgroundColor(Color.GREEN);
            } else if (pm < 30) {
                convertView.setBackgroundColor(Color.YELLOW);
            } else {
                convertView.setBackgroundColor(Color.RED);
            }
        }

        return convertView;

    }



    static class ViewHolder{
        TextView site,pm25;

    }

}
