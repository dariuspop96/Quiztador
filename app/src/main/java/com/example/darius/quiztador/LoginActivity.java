package com.example.darius.quiztador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView tvSignup;
    private Button bLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        tvSignup = (TextView) findViewById(R.id.signup);
        tvSignup.setOnClickListener(this);

        bLogin = (Button) findViewById(R.id.login);
        bLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:
                break;
            case R.id.signup:
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class); /** Class name here */
                startActivityForResult(myIntent, 0);
                break;
        }
    }

}
