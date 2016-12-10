package com.example.bemi.beanr;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bemi.beanr.dbHandler.MyDBHandler;
import com.example.bemi.beanr.entites.Business;
import com.example.bemi.beanr.entites.FavouriteShop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FavouriteFragment extends Fragment {
    MyDBHandler myDBHandler;
    Business business;
    FavouriteShop favouriteShop;
    ArrayList<Business> businessArray = new ArrayList<Business>();
    ArrayList<Business> favouriteBusinessArray = new ArrayList<Business>();
    String uri = "http://172.20.10.9:8080/restful-services/api/getAllBusinesses/";
    AdapterBusiness adapterBusiness;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDBHandler = new MyDBHandler(getActivity(), null, null ,1);
        return inflater.inflate(R.layout.favourite_listview_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView)view.findViewById(R.id.list);
        ArrayList<FavouriteShop> favouriteShops;

        favouriteShops = myDBHandler.getFavouriteShops();

        GetBusinesses gb = new GetBusinesses();
        try {
            gb.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(FavouriteShop f: favouriteShops){
            for (Business b: businessArray){
                if(f.getCustomerName().equals(myDBHandler.getCustomer().getUsername()) && f.getBusinessName().equals(b.getName())){
                    favouriteBusinessArray.add(b);
                }
            }
        }
        myDBHandler.deleteAllFavourite();
        adapterBusiness = new AdapterBusiness (getActivity(), 0, favouriteBusinessArray);

        listView.setAdapter(adapterBusiness);
    }

    public class GetBusinesses extends AsyncTask<Void, Void, String> {

        // declare other objects as per your need
        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity().getApplicationContext(), "This may take a while...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            System.out.println("Got into Asynccc");
            StringBuffer string = new StringBuffer("");
            URL url = null;
            try {
                url = new URL(uri);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            // read the response
            try {
                System.out.println("Response Code: " + conn.getResponseCode());
                InputStream in = new BufferedInputStream(conn.getInputStream());

                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    string.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("RESPONSE JSON: "+string.toString());
            result = string.toString();
			/*Convert string to JSON*/
            try {

                JSONArray json = new JSONArray(result);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject e = json.getJSONObject(i);
                    JSONObject business = e.getJSONObject("business");
                    String email = "No email address available";
                    String website = "Website unavailable";
                    if(business.has("email")){
                        email = business.getString("email");
                    }
                    if(business.has("website")){
                        website = business.getString("website");
                    }
                    Business business1 = new Business(business.getString("name"), business.getString("address"), Double.parseDouble(business.getString("latitude")), Double.parseDouble(business.getString("longitude")), email,website);
                    System.out.println(business.getString("name"));
                    businessArray.add(business1);
                    System.out.println(businessArray.size());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity().getApplicationContext(), "All done.", Toast.LENGTH_LONG).show();

        }

    }
}
