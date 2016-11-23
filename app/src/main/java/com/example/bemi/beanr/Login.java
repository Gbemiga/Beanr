package com.example.bemi.beanr;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bemi.beanr.dbHandler.MyDBHandler;
import com.example.bemi.beanr.entites.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    EditText username, password;
    Button login;
    JSONObject cred;
    String name = "" ;
    String pass = "" ;
    String result = "";
    boolean exists = false;
    String uri = "http://172.20.10.9:8080/restful-services/api/getCustomer/";
    ArrayList<Customer> customersArrayList = new ArrayList<Customer>();
    MyDBHandler myDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDBHandler = new MyDBHandler(this, null, null ,1);

        if(myDBHandler.getCustomer().getEmail() != null) {
            Toast.makeText(Login.this, "Welcome "+ myDBHandler.getCustomer().getUsername(), Toast.LENGTH_LONG).show();
            Intent mainIntent = new Intent(Login.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

        login = (Button) findViewById(R.id.login_button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(Login.this, Register.class);
                Login.this.startActivity(mainIntent);
            }
        });


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        if (!username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()) {
                             name = username.getText().toString();
                             pass = password.getText().toString();
                            GetUser gu = new GetUser();
                            try {
                                gu.execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                       } else {
                            Toast.makeText(Login.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                       }


                    if(exists) {
                        myDBHandler.deleteAllCustomer();
                        myDBHandler.addCustomer(customersArrayList.get(0));
                        Intent mainIntent = new Intent(Login.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }
            });

    }


    class GetUser extends AsyncTask<String, String, String>{




        // declare other objects as per your need
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                cred = new JSONObject();
                try {
                    cred.put("username",name);
                    cred.put("password", pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 1. URL
                URL url = new URL(uri);

                // 2. Open connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // 3. Specify POST method
                conn.setRequestMethod("POST");

                // 4. Set the headers
                conn.setRequestProperty("Content-Type", "application/json");

                conn.setDoOutput(true);

                // 5. Add JSON data into POST request body

                //`5.1 Use Jackson object mapper to convert Contnet object into JSON
                ObjectMapper mapper = new ObjectMapper();

                // 5.2 Get connection output stream
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                // 5.3 Copy Content "JSON" into
                wr.writeBytes(cred.toString());

                // 5.4 Send the request
                wr.flush();

                // 5.5 close
                wr.close();

                // 6. Get the response
                int responseCode = conn.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 7. Print result
                System.out.println(response.toString());
                result = response.toString();

                if(!result.equals("[]")) {
                    exists = true;
                    JSONArray json = new JSONArray(result);
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject e = json.getJSONObject(i);
                        JSONObject object = e.getJSONObject("customer");
                        Customer customer = new Customer();
                        customer.setEmail(object.getString("email"));
                        customer.setGender(object.getString("gender"));
                        customer.setUsername(object.getString("username"));
                        customersArrayList.add(customer);
                    }
                    System.out.println("THIS IS THE ARRAY LIST" + customersArrayList.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("[]")) {
                Toast.makeText(Login.this, "Incorrect Username or Password.", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(Login.this, "Welcome "+ customersArrayList.get(0).getUsername(), Toast.LENGTH_LONG).show();
            }
        }

    }
}

