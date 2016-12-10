package com.example.bemi.beanr;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bemi.beanr.entites.Business;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import android.widget.RatingBar.OnRatingBarChangeListener;
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

public class MapsActivity extends Fragment implements OnMapReadyCallback, OnInfoWindowClickListener {

    ArrayList<Business> businessArray = new ArrayList<Business>();
    String uri = "http://172.20.10.9:8080/restful-services/api/getAllBusinesses/";
    LocationListener listener;
    GoogleMap mMap;
    RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;

        if(mMap != null){
            mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
                                          @Override
                                          public View getInfoWindow(Marker marker) {
                                              return null;
                                          }

                                          @Override
                                          public View getInfoContents(final Marker marker) {
                                              View v = View.inflate(getActivity().getApplicationContext(),R.layout.map_location_info, null);

                                              ImageView imageView = (ImageView) v.findViewById(R.id.storePic);
                                              TextView storeName = (TextView) v.findViewById(R.id.storeName);
                                              TextView storeAddress = (TextView) v.findViewById(R.id.storeAddress);
                                              ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);

                                              storeName.setText(marker.getTitle().toString());
                                              storeAddress.setText(marker.getSnippet().toString());
                                              Picasso.with(getActivity()).load("https://maps.googleapis.com/maps/api/streetview?size=300x300&location="+marker.getPosition().latitude+",%20"+marker.getPosition().longitude+"&heading=151.78&pitch=-0.76&key=AIzaSyAwJtk1tfy9a6Bx3JygLldC2y8yO0K0Qfo").into(imageView);

                                              return v;
                                          }
                                      }
            );
            mMap.setOnInfoWindowClickListener(this);
        }

        GetBusinesses gb = new GetBusinesses();
        try {
            gb.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (locationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this.getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this.getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 16));
            mMap.setMyLocationEnabled(true);
        }

        mMap.setMyLocationEnabled(true);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 16));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        for(Business b: businessArray){
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bean_icon))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .title(b.getName())
                    .snippet(b.getAddress())
                    .position(new LatLng(b.getLatitude(), b.getLongitude())));
        }


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Bundle bundle = new Bundle();
        for(Business b: businessArray){
            if(marker.getTitle().equals(b.getName())){
                bundle.putSerializable("business", b);
            }
        }

        if(bundle!=null) {
            BusinessFragment nextFrag = new BusinessFragment();
            nextFrag.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, nextFrag, null)
                    .addToBackStack(null)
                    .commit();
        }
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
                InputStream in = new BufferedInputStream(conn.getInputStream());

                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    string.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                    businessArray.add(business1);
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
