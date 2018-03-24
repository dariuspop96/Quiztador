package com.example.darius.quiztador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvLogin;
    private Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        tvLogin = (TextView) findViewById(R.id.login);
        tvLogin.setOnClickListener(this);

        bRegister = (Button) findViewById(R.id.register);
        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register:
                break;
            case R.id.login:
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class); /** Class name here */
                startActivityForResult(myIntent, 0);
                break;
        }
    }
}
