package com.example.bemi.beanr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bemi.beanr.entites.Business;

import java.util.ArrayList;

/**
 * Created by gbemigaadeosun on 11/10/2016.
 */


public class AdapterBusiness extends ArrayAdapter<Business> {
    private Activity activity;
    private ArrayList<Business> businesses;
    private static LayoutInflater inflater = null;

    public AdapterBusiness(Activity activity, int textViewResourceId, ArrayList<Business> _businesses) {
        super(activity, textViewResourceId, _businesses);
        try {
            this.activity = activity;
            this.businesses = _businesses;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return businesses.size();
    }

    public Business getItem(Business position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView business_favourite_name;
        public TextView business_favourite_address;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.model_favourite_listview, null);
                holder = new ViewHolder();

                holder.business_favourite_name = (TextView) vi.findViewById(R.id.business_favourite_name);
                holder.business_favourite_address = (TextView) vi.findViewById(R.id.business_favourite_address);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            holder.business_favourite_name.setText(businesses.get(position).getName());
            holder.business_favourite_address.setText(businesses.get(position).getAddress());


        } catch (Exception e) {


        }
        return vi;
    }
}