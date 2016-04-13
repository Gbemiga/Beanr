package com.example.bemi.beanr;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String showUrl = "http://www.brewr.net/showbusiness.php";
    RequestQueue requestQueue;
    ArrayList<Business> businessArray = new ArrayList<Business>();
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        System.out.println("Got here");

//        requestQueue = Volley.newRequestQueue(getApplicationContext());
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
//                showUrl, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray businesses = response.getJSONArray("Businesses");
//                    for(int i = 0; i<businesses.length(); i++){
//                        JSONObject business = businesses.getJSONObject(i);
//                        Business business1 = new Business(business.getString("name"),business.getString("address"),Double.parseDouble(business.getString("latitude")),Double.parseDouble(business.getString("longitude")));
//                        System.out.println(business.getString("name"));
//                        businessArray.add(business1);
//                        System.out.println(businessArray.size());
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        requestQueue.add(jsonObjectRequest);
//        System.out.println(businessArray.size());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        GetBusinesses gb = new GetBusinesses();
        try {
            gb.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        /*mMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(41.889, -87.622), 16));
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker in Sydney"));

        if (locationManager != null) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            }
        }
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bean_icon))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(41.889, -87.622)));*/
       map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (locationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null){
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                   new LatLng(location.getLatitude(), location.getLongitude()), 16));
           // map.setMyLocationEnabled(true);

        }

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        for(Business b: businessArray){
            map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bean_icon))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .title(b.getName())
                    .position(new LatLng(b.getLatitude(), b.getLongitude())));
        }
    }

    public class GetBusinesses extends AsyncTask<Void, Void, Void> {

        // declare other objects as per your need
        @Override
        protected void onPreExecute() {
            Toast.makeText(MapsActivity.this, "This may take a while...", Toast.LENGTH_LONG).show();
        };

        @Override
        protected Void doInBackground(Void... params) {
            String result = "";
            String url = "http://www.brewr.net/showbusiness.php";
            String line = "";
            InputStream is = null;
            System.out.println("Got into Asynccc");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			/*Connects to server*/
            try {

                HttpParams para = new BasicHttpParams();
                //this how tiny it might seems, is actually absoluty needed. otherwise http client lags for 2sec.
                HttpProtocolParams.setVersion(para, HttpVersion.HTTP_1_1);
                //HttpClient httpClient = new DefaultHttpClient(params);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "Connection success ");

            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
            }

			/*Reads the results. */
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                result = sb.toString();

                Log.e("pass 2", "Result: " + result);
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }

			/*Convert string to JSON*/
            try {

                JSONObject obj = new JSONObject(result);
                JSONArray businesses = obj.getJSONArray("Businesses");
                    for(int i = 0; i<businesses.length(); i++) {
                        JSONObject business = businesses.getJSONObject(i);
                        Business business1 = new Business(business.getString("name"), business.getString("address"), Double.parseDouble(business.getString("latitude")), Double.parseDouble(business.getString("longitude")));
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
        protected void onPostExecute(Void result) {
            Toast.makeText(MapsActivity.this, "All done.", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
