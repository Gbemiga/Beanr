package com.example.bemi.beanr;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bemi.beanr.dbHandler.MyDBHandler;

public class ProfileFragment extends Fragment {
    MyDBHandler myDBHandler;
    TextView username, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDBHandler = new MyDBHandler(getActivity(), null, null ,1);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = (TextView)getView().findViewById(R.id.user_profile_name);
        email = (TextView)getView().findViewById(R.id.user_profile_email);
        username.append(myDBHandler.getCustomer().getUsername());
        email.append(myDBHandler.getCustomer().getEmail());
    }
}
