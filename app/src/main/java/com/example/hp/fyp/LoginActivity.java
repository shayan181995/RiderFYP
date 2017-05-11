package com.example.hp.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        Intent i = new Intent(this,Main.class);
        startActivity(i);
    }

    public void signup(View view){
        Intent i =  new Intent(this,SignUp.class);
        startActivity(i);
    }
}
