package com.example.aizat.travelook_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Init extends AppCompatActivity implements View.OnClickListener {

    private Button signup;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == signup){
            startActivity(new Intent(this, MainActivity.class));
        }

        if (view == login){
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
