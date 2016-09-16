package com.example.bemi.beanr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Register extends AppCompatActivity {

    EditText username,email, password, confirm_password;
    Button signUp;
    RequestQueue requestQueue;
    RadioButton male, female;
    String emailtxt, usernametxt, passwordtxt;
    String result = "";
    String gendertxt = "M";
    String insertUrl = "http://www.brewr.net/insert_user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirmpassword);
        male = (RadioButton) findViewById(R.id.male_radioButton);
        female = (RadioButton) findViewById(R.id.female_radioButton);
        signUp = (Button) findViewById(R.id.reg_button);


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().isEmpty()) {
                    email.setError("Cannot be empty");
                }
                if (username.getText().toString().trim().isEmpty()) {
                    username.setError("Cannot be empty");
                }
                if (password.getText().toString().trim().isEmpty()) {
                    password.setError("Cannot be empty");
                }
                if (confirm_password.getText().toString().trim().isEmpty()) {
                    confirm_password.setError("Cannot be empty");
                }

                if (!email.getText().toString().trim().isEmpty() &&
                        !username.getText().toString().trim().isEmpty() &&
                        !password.getText().toString().trim().isEmpty() && !confirm_password.getText().toString().trim().isEmpty() ){
                    usernametxt = username.getText().toString();
                    emailtxt = email.getText().toString();
                    passwordtxt = password.getText().toString();

                    RegUser ru = new RegUser();
//                    StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    }) {
//
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> parameters = new HashMap<String, String>();
//                            parameters.put("email", email.getText().toString());
//                            parameters.put("username", username.getText().toString());
//                            parameters.put("password", password.getText().toString());
//                            parameters.put("gender", gender);
//                            System.out.println(parameters);
//                            return parameters;
//                        }
//                    };
//                    requestQueue.add(request);
//                    Intent mainIntent = new Intent(Register.this,MapsActivity.class);
//                    Register.this.startActivity(mainIntent);
                }
            }

        });

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (confirm_password.getText().toString().trim().isEmpty()) {
                    confirm_password.setError("Cannot be empty");
                } else if (!confirm_password.getText().toString().equals(password.getText().toString())) {
                    confirm_password.setError("Password Must Match");
                } else {
                    confirm_password.setError(null);
                }
            }

            public void afterTextChanged(Editable editable) {
                if (confirm_password.getText().toString().trim().isEmpty()) {
                    confirm_password.setError("Cannot be empty");
                } else if (!confirm_password.getText().toString().equals(password.getText().toString())) {
                    confirm_password.setError("Password Must Match");
                } else {
                    confirm_password.setError(null);
                }
            }
        });
    }
    public class RegUser extends AsyncTask<Void, Void, Void> {

        // declare other objects as per your need
        @Override
        protected void onPreExecute() {
            //Toast.makeText(MapsActivity.this, "This may take a while...", Toast.LENGTH_LONG).show();
        };

        @Override
        protected Void doInBackground(Void... params) {

            String url = "http://www.brewr.net/insert_user.php";
            String line = "";
            InputStream is = null;
            System.out.println("Got into Asynccc");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", usernametxt));
            nameValuePairs.add(new BasicNameValuePair("email", emailtxt));
            nameValuePairs.add(new BasicNameValuePair("password", passwordtxt));
            nameValuePairs.add(new BasicNameValuePair("gender", gendertxt));


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
                if(result.equals("Success")) {


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void rsult) {
//            System.out.println(userArray.get(0).getUsername());
            if(result.equals("Failure")) {
               // Toast.makeText(Login.this, "Incorrect Username or Password.", Toast.LENGTH_LONG).show();
            }else
            {
              //  Toast.makeText(Login.this, "Welcome "+userArray.get(0).getUsername(), Toast.LENGTH_LONG).show();
            }
        }

    }

}