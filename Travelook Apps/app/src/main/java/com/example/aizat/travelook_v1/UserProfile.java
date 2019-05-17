package com.example.aizat.travelook_v1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{

    private ImageView userImage;
    private TextView userName,userAddress,userPhoneNumber;
    private Button buttonEdit, buttonChangePassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseError databaseError;
    String Uid;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userImage = (ImageView)findViewById(R.id.userImage);
        userName = (TextView)findViewById(R.id.userName);
        userAddress = (TextView)findViewById(R.id.userAddress);
        userPhoneNumber = (TextView)findViewById(R.id.userPhoneNumber);
        buttonEdit = (Button)findViewById(R.id.buttonSave);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        buttonEdit.setOnClickListener(this);

        DatabaseReference databaseReference = firebaseDatabase.getReference("User").child(firebaseAuth.getUid());

        firebaseStorage.getReference().child("images/" + firebaseAuth.getUid()+ ".png").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(userImage);
                    }
                });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                userName.setText(userInformation.getName());
                userAddress.setText(userInformation.getAddress());
                userPhoneNumber.setText(userInformation.getPhoneNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==buttonEdit){
            finish();
            startActivity(new Intent(this, UpdateProfile.class));
        }

    }
}
