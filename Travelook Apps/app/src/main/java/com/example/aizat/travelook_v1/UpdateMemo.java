package com.example.aizat.travelook_v1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aizat.travelook_v1.MemoModel.MemoAttr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class UpdateMemo extends AppCompatActivity {

    private EditText memoTitle, memoText;
    private Button buttonUpdate;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_memo);

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Memo");

        memoTitle = (EditText)findViewById(R.id.memoTitle);
        memoText = (EditText)findViewById(R.id.memoText);
        buttonUpdate = (Button)findViewById(R.id.buttonUpdate);

        Intent intent = getIntent();
        final String memoid = intent.getStringExtra("memoid");

        databaseReference = FirebaseDatabase.getInstance().getReference("Memo");

        query = databaseReference.orderByChild("memoid").equalTo(memoid);
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        final String date = snapshot.child("date").getValue().toString();
                        String title = snapshot.child("title").getValue().toString();
                        String text =snapshot.child("text").getValue().toString();
                        final String cuid = snapshot.child("cuid").getValue().toString();
                        memoTitle.setText(title);
                        memoText.setText(text);

                        buttonUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String title = memoTitle.getText().toString();
                                String text = memoText.getText().toString();

                                MemoAttr memoAttr = new MemoAttr(date,title,text,cuid,memoid);

                                databaseReference.child(memoid).setValue(memoAttr);

                                finish();

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
