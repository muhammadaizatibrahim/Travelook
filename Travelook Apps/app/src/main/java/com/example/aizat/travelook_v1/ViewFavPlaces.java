package com.example.aizat.travelook_v1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewFavPlaces extends AppCompatActivity {

    private TextView favName,favAddress, favPhoneNum, favWebsite;
    private ImageView direction, call, openWebsite, remove;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fav_places);

        favName = (TextView) findViewById(R.id.favName);
        favAddress = (TextView) findViewById(R.id.favAddress);
        favPhoneNum = (TextView) findViewById(R.id.favPhoneNum);
        favWebsite = (TextView) findViewById(R.id.FavWebsite);
        direction = (ImageView) findViewById(R.id.direction);
        call = (ImageView) findViewById(R.id.call);
        openWebsite =(ImageView) findViewById(R.id.openWebsite);
        remove = (ImageView) findViewById(R.id.remove);

        final Intent intent = getIntent();

        final String favid = intent.getStringExtra("favId");

        databaseReference = FirebaseDatabase.getInstance().getReference("Favorite");

        query = databaseReference.orderByChild("favId").equalTo(favid);
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        String placeName = snapshot.child("name").getValue().toString();
                        String placeAddr = snapshot.child("addr").getValue().toString();
                        final String phoneNum = snapshot.child("phoneNum").getValue().toString();
                        final String gWebsite = snapshot.child("website").getValue().toString();
                        final String url = snapshot.child("url").getValue().toString();
                        favName.setText(placeName);
                        favAddress.setText(placeAddr);
                        favPhoneNum.setText(phoneNum);
                        favWebsite.setText(gWebsite);

                        direction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(mapIntent);
                            }
                        });

                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                if(phoneNum.equals("Phone number not available")){
                                    Toast.makeText(ViewFavPlaces.this,"Phone number not available",Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    callIntent.setData(Uri.parse("tel:" + phoneNum));
                                }

                                if (ActivityCompat.checkSelfPermission(ViewFavPlaces.this, Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED){
                                    return;
                                }
                                startActivity(callIntent);
                            }
                        });

                        openWebsite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (gWebsite.equals("Website not available")){
                                    Toast.makeText(ViewFavPlaces.this,"Website not available",Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gWebsite));
                                    startActivity(websiteIntent);
                                }


                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFav(favid);
            }
        });
    }

    private void removeFav(String favid) {
        DatabaseReference removeFavorite = FirebaseDatabase.getInstance().getReference("Favorite").child(favid);
        removeFavorite.removeValue();
        Toast.makeText(this,"Favorite place has been removed", Toast.LENGTH_LONG).show();

        finish();
    }
}
