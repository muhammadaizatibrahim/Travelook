package com.example.aizat.travelook_v1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UpdateProfile extends AppCompatActivity {

    private ImageView userUpdateImage;
    private EditText editName,editAddress,editPhoneNumber;
    private Button buttonSave,buttonPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private StorageReference storageReference;
    private String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        userUpdateImage = (ImageView) findViewById(R.id.userUpdateImage);
        editName = (EditText)findViewById(R.id.editName);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        buttonSave = (Button) findViewById(R.id.buttonSave);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        final DatabaseReference databaseReference = firebaseDatabase.getReference("User").child(firebaseAuth.getUid());

        firebaseStorage.getReference().child("images/" + firebaseAuth.getUid()+ ".png").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(userUpdateImage);
                    }
                });

        userUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileOpen();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                editName.setText(userInformation.getName());
                editAddress.setText(userInformation.getAddress());
                editPhoneNumber.setText(userInformation.getPhoneNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (filePath != null) {
                    final StorageReference sR = storageReference.child("images/" + firebaseAuth.getUid() + ".png");
                    sR.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            sR.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image = uri.toString();

                                    String name = editName.getText().toString();
                                    String address = editAddress.getText().toString();
                                    String phoneNumber = editPhoneNumber.getText().toString();

                                    UserInformation userInformation = new UserInformation(name, address, phoneNumber, image);

                                    databaseReference.setValue(userInformation);
                                }
                            });
                        }
                    });
                }else {

                    String name = editName.getText().toString();
                    String address = editAddress.getText().toString();
                    String phoneNumber = editPhoneNumber.getText().toString();

                    UserInformation userInformation = new UserInformation(name, address, phoneNumber, image);

                    databaseReference.setValue(userInformation);
                }




                finish();
            }
        });


    }
    public void fileOpen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Log.d(TAG, String.valueOf(bitmap));

                userUpdateImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
