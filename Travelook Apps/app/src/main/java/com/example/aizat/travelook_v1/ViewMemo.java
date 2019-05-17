package com.example.aizat.travelook_v1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aizat.travelook_v1.MemoModel.MemoAttr;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class ViewMemo extends AppCompatActivity {

    private TextView memoTitle,memoDate,memoText;
    private LinearLayout memoEdit, memoDelete;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memo);

        memoTitle = (TextView) findViewById(R.id.memoTitle);
        memoDate = (TextView) findViewById(R.id.memoDate);
        memoText = (TextView) findViewById(R.id.memoText);
        memoEdit = (LinearLayout) findViewById(R.id.memoEdit);
        memoDelete = (LinearLayout) findViewById(R.id.memoDelete);
        


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

                        String date = snapshot.child("date").getValue().toString();
                        String title = snapshot.child("title").getValue().toString();
                        String text =snapshot.child("text").getValue().toString();
                        memoTitle.setText(title);
                        memoDate.setText(date);
                        memoText.setText(text);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        memoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewMemo.this, UpdateMemo.class);
                intent.putExtra("memoid",memoid);
                startActivity(intent);


            }
        });

        memoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMemo(memoid);
            }
        });

    }

    private void deleteMemo(String memoid) {
        DatabaseReference delMemo = FirebaseDatabase.getInstance().getReference("Memo").child(memoid);

        delMemo.removeValue();

        Toast.makeText(this,"Memo is deleted", Toast.LENGTH_LONG).show();

        finish();
    }
}
