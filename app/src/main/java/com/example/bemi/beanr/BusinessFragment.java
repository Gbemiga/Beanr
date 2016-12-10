package com.example.bemi.beanr;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bemi.beanr.dbHandler.MyDBHandler;
import com.example.bemi.beanr.entites.Business;
import com.example.bemi.beanr.entites.FavouriteShop;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BusinessFragment extends Fragment {
    MyDBHandler myDBHandler;
    Business business;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDBHandler = new MyDBHandler(getActivity(), null, null ,1);
        business = (Business) getArguments().getSerializable("business");
        return inflater.inflate(R.layout.fragment_business, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView = (ImageView) view.findViewById(R.id.business_header_cover_image);
        TextView business_profile_name = (TextView) view.findViewById(R.id.business_profile_name);
        TextView business_profile_address = (TextView) view.findViewById(R.id.business_profile_address);
        TextView business_profile_email = (TextView) view.findViewById(R.id.business_profile_email);
        TextView business_profile_website = (TextView) view.findViewById(R.id.business_profile_website);
        RatingBar ratingBar=  (RatingBar) view.findViewById(R.id.ratingBarFavourite);

        business_profile_name.setText(business.getName());
        business_profile_address.setText(business.getAddress());
        business_profile_email.append(business.getEmail());
        business_profile_website.append(business.getWebsite());
        Picasso.with(view.getContext()).load("https://maps.googleapis.com/maps/api/streetview?size=600x300&location="+business.getLatitude()+",%20"+business.getLongitude()+"&heading=151.78&pitch=-0.76&key=AIzaSyAwJtk1tfy9a6Bx3JygLldC2y8yO0K0Qfo").fit().into(imageView);

        ArrayList<FavouriteShop> favouriteShops;
        favouriteShops = myDBHandler.getFavouriteShops();

        for(FavouriteShop f: favouriteShops){
                if(f.getCustomerName().equals(myDBHandler.getCustomer().getUsername())&& f.getBusinessName().equals(business.getName())){
                    ratingBar.setRating(1);
                }
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if(ratingBar.getRating()==1) {
                    myDBHandler.addFavouriteShop(myDBHandler.getCustomer(), business);
                }
            }});


    }
}
