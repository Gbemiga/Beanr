package com.example.bemi.beanr;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bemi.beanr.dbHandler.MyDBHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Activity {

    EditText username,email, password, confirm_password;
    Button signUp;
    String emailtxt, usernametxt, passwordtxt;
    String uri = "http://172.20.10.9:8080/restful-services/api/createCustomer/";
    String gendertxt = "M";
    JSONObject cred;
    String result = "";
    MyDBHandler myDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDBHandler = new MyDBHandler(this, null, null ,1);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirmpassword);
        signUp = (Button) findViewById(R.id.reg_button);

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

            @Override
            public void afterTextChanged(Editable s) {
                if (confirm_password.getText().toString().trim().isEmpty()) {
                    confirm_password.setError("Cannot be empty");
                } else if (!confirm_password.getText().toString().equals(password.getText().toString())) {
                    confirm_password.setError("Password Must Match");
                } else {
                    confirm_password.setError(null);
                }
            }

        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().isEmpty()) {
                    email.setError("Cannot be empty");
                }
                if(isValidEmail(email.getText().toString().trim())==false) {
                    email.setError("Enter valid email");
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
                if(!confirm_password.getText().toString().trim().equals(password.getText().toString().trim())){
                    confirm_password.setError("Password must match");
                }

                if (!email.getText().toString().trim().isEmpty() &&
                        !username.getText().toString().trim().isEmpty() &&
                        !password.getText().toString().trim().isEmpty() &&
                        !confirm_password.getText().toString().trim().isEmpty() &&
                        confirm_password.getText().toString().trim().equals(password.getText().toString().trim()) &&
                        isValidEmail(email.getText().toString().trim()) != false) {
                    usernametxt = username.getText().toString();
                    emailtxt = email.getText().toString();
                    passwordtxt = password.getText().toString();

                    cred = new JSONObject();
                    try {
                        cred.put("username",username.getText().toString().trim());
                        cred.put("password", password.getText().toString().trim());
                        cred.put("email", email.getText().toString().trim());
                        cred.put("gender", gendertxt);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new RegUser().execute();
                    try {
                        //to allow the other Thread to set the value
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    class RegUser extends AsyncTask<String, String, String>{

        // declare other objects as per your need
        @Override
        protected void onPreExecute() {
            Toast.makeText(Register.this, "Signing you up, please wait...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
        }
    }
}