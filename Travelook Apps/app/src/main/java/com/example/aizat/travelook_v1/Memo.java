package com.example.aizat.travelook_v1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.aizat.travelook_v1.MemoModel.MemoAttr;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Memo extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MemoAdapter adapter;
    FloatingActionButton floatingActionButton;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Query query;
    String Uid;



    List<MemoAttr> memoAttrList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Uid = firebaseUser.getUid().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Memo");
        databaseReference.keepSynced(true);

        query = databaseReference.orderByChild("date");


        floatingActionButton = (FloatingActionButton) findViewById(R.id.addMemo);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Memo.this,AddMemo.class));
            }
        });

        memoAttrList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.memoRecyclerView);
        recyclerView.setHasFixedSize(true);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memoAttrList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        MemoAttr memoAttr = snapshot.getValue(MemoAttr.class);
                        if (memoAttr.getCuid().equals(Uid)){
                            memoAttrList.add(memoAttr);
                        }
                    }
                    Collections.reverse(memoAttrList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        layoutManager = new LinearLayoutManager(this);
        adapter = new MemoAdapter(memoAttrList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String cuid = memoAttrList.get(position).getCuid();
                String title = memoAttrList.get(position).getTitle();
                String date = memoAttrList.get(position).getDate();
                String text = memoAttrList.get(position).getText();
                String memoid = memoAttrList.get(position).getMemoid();
                Intent intent = new Intent(Memo.this, ViewMemo.class);
                intent.putExtra("memoid",memoid);
                startActivity(intent);

            }
        });

    }
}
