package com.example.bemi.beanr;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bemi.beanr.dbHandler.MyDBHandler;

public class SignLoginActivity extends AppCompatActivity {

    MyDBHandler myDBHandler;
    Button login_button, reg_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_login);

        myDBHandler = new MyDBHandler(this, null, null ,1);


        if(myDBHandler.getCustomer().getEmail() != null) {
            Toast.makeText(SignLoginActivity.this, "Welcome "+ myDBHandler.getCustomer().getUsername(), Toast.LENGTH_LONG).show();
            Intent mainIntent = new Intent(SignLoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

        login_button = (Button) findViewById(R.id.login_button);

        reg_button = (Button) findViewById(R.id.reg_button);

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(SignLoginActivity.this, Register.class);
                startActivity(mainIntent);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(SignLoginActivity.this, Login.class);
                startActivity(mainIntent);
                finish();
            }
        });

    }
}
