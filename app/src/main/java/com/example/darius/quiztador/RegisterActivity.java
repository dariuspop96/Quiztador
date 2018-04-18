package com.example.darius.quiztador;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar loading;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnRegister = (Button) findViewById(R.id.btn_sign_up);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        loading = (ProgressBar) findViewById(R.id.loading);

        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                setCredentials();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnRegister.getWindowToken(), 0);

                new RegisterActivity.RegisterAsyncTask(this).execute();
                break;
            case R.id.tv_login:
                onBackPressed();
                break;
        }
    }

    public void setCredentials(){
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Auth> {
        private Context context;

        public RegisterAsyncTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Auth doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            String stringUri = "http://192.168.0.114/svcourse2018.1/Users";
            try {
                URL url = new URL(stringUri);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setConnectTimeout(5000);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                    System.out.println(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                httpURLConnection.setRequestProperty("Content-Type", "application/json");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes());
                outputStream.flush();
                outputStream.close();


                int responseCode = httpURLConnection.getResponseCode();
                Log.e("responseCode", responseCode + " ");

                Auth auth = new Auth();

                if ( responseCode >=200 && responseCode < 300) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";

                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    String responseString = stringBuilder.toString();
                    JSONObject responseJson = new JSONObject(responseString);

                    Log.e("response", responseString);

                    auth.setId(responseJson.getString("userId"));
                    auth.setToken(responseJson.getString("authorizationToken"));

                    return auth;
                }else{
                    InputStream inputStream = httpURLConnection.getErrorStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";

                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    String responseString = stringBuilder.toString();
                    JSONObject responseJson = new JSONObject(responseString);

                    auth.setMessage(responseJson.getString("errorMessage"));
                    Log.e("response", responseString);

                    return auth;

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Auth s) {
            super.onPostExecute(s);

            loading.setVisibility(View.GONE);

            if (s.getId() == null) {
                Toast.makeText(RegisterActivity.this,s.getMessage(),Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(RegisterActivity.this, "Log in successful", Toast.LENGTH_LONG).show();

            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            mainActivityIntent.putExtra("id", s.getId());
            mainActivityIntent.putExtra("token", s.getToken());
            startActivity(mainActivityIntent);
//            ((Activity)context).finish();

        }
    }
}
