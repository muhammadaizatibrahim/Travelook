package com.example.aizat.travelook_v1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    private TextView ownerEmail;
    private LinearLayout instagram,twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ownerEmail = (TextView) findViewById(R.id.ownerEmail);
        instagram = (LinearLayout) findViewById(R.id.instagram);
        twitter = (LinearLayout) findViewById(R.id.twitter);

        ownerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gmailIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.gmail.com"));
                startActivity(gmailIntent);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent instaIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/aizatfaizz/"));
                startActivity(instaIntent);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent twitterIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/aizatfaizz"));
                startActivity(twitterIntent);
            }
        });
    }
}
