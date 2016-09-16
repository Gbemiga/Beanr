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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    EditText username, password;
    Button login;
    String name = "" ;
    String pass = "" ;
    String result = "";
    boolean exists = false;
    ArrayList<User> userArray = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                        Intent mainIntent = new Intent(Login.this, MapsActivity.class);
                        Login.this.startActivity(mainIntent);
                        finish();
                    }
                }
            });

    }


    public class GetUser extends AsyncTask<Void, Void, Void> {

        // declare other objects as per your need
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            String url = "http://www.brewr.net/showUser.php";
            String line = "";
            InputStream is = null;
            System.out.println("Got into Asynccc");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", name));
            nameValuePairs.add(new BasicNameValuePair("password", pass));


			/*Connects to server*/
            try {


                HttpParams para = new BasicHttpParams();
                HttpProtocolParams.setVersion(para, HttpVersion.HTTP_1_1);
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
                if(!result.equals("less than 0{\"users\":[]}")) {
                    JSONObject obj = new JSONObject(result);
                    JSONArray users = obj.getJSONArray("users");
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        User user1 = new User(user.getString("username"), user.getString("email"), user.getString("password"), user.getString("gender"));
                        System.out.println(user.getString("username"));
                        userArray.add(user1);
                        System.out.println(userArray.size());
                        exists = true;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void rsult) {
            if(result.equals("less than 0{\"users\":[]}")) {
                Toast.makeText(Login.this, "Incorrect Username or Password.", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(Login.this, "Welcome "+userArray.get(0).getUsername(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
