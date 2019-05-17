package com.example.aizat.travelook_v1;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextUtils;
        import android.text.TextWatcher;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.FirebaseApp;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.URI;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private DatabaseReference databaseReference;
    String Uid;

    private EditText editTextName, editTextAddress, editTextPhoneNumber;
    private Button buttonAddPeople;
    private ProgressDialog progressDialog;
    private DataSnapshot dataSnapshot;
    private ImageView uploadImage;
    private Uri filePath;
    public StorageReference storageReference;
    private DatabaseError databaseError;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String image = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Uid = firebaseUser.getUid().toString();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        buttonAddPeople = (Button) findViewById(R.id.buttonAddPeople);
        uploadImage = (ImageView) findViewById(R.id.uploadImage);

        editTextName.addTextChangedListener(saveTextWatcher);
        editTextPhoneNumber.addTextChangedListener(saveTextWatcher);
        editTextAddress.addTextChangedListener(saveTextWatcher);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");



        buttonAddPeople.setOnClickListener(this);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileOpen();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == buttonAddPeople) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait..");
            progressDialog.show();

            //upload image
            if (filePath != null) {
                final StorageReference sR = storageReference.child("images/" + Uid.toString() + ".png");
                sR.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        sR.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image = uri.toString();

                                //with image
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child(Uid).exists()) {
                                            progressDialog.dismiss();
                                            //Toast.makeText(ProfileActivity.this, "Already in database", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            UserInformation userInformation = new UserInformation(editTextName.getText().toString(), editTextAddress.getText().toString(), editTextPhoneNumber.getText().toString(), image);
                                            databaseReference.child(Uid).setValue(userInformation);
                                            Toast.makeText(ProfileActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), Homepage.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                }
                                );
                            }
                        });
                    }
                });
            }else {
                //no image
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(Uid).exists()) {
                            progressDialog.dismiss();
                            //Toast.makeText(ProfileActivity.this, "Already in database", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            UserInformation userInformation = new UserInformation(editTextName.getText().toString(), editTextAddress.getText().toString(), editTextPhoneNumber.getText().toString(),image);
                            databaseReference.child(Uid).setValue(userInformation);
                            Toast.makeText(ProfileActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Homepage.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
                );
            }
        }
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

                uploadImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private TextWatcher saveTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String userName = editTextName.getText().toString().trim();
            String userAddress = editTextAddress.getText().toString().trim();
            String userPhone = editTextPhoneNumber.getText().toString().trim();

            buttonAddPeople.setEnabled(!userName.isEmpty() && !userAddress.isEmpty() && !userPhone.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
