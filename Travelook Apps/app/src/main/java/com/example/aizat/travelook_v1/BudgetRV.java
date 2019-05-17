package com.example.aizat.travelook_v1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.aizat.travelook_v1.BudgetModel.BudgetAttr;
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

public class BudgetRV extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton floatingActionButton;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Query query;
    String Uid;
    BudgetRVAdapter adapter;

    List<BudgetAttr> budgetAttrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_rv);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Uid = firebaseUser.getUid().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Budget");
        databaseReference.keepSynced(true);

        query = databaseReference.orderByChild("date");


        floatingActionButton = (FloatingActionButton) findViewById(R.id.addBudget);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BudgetRV.this, Budget.class));
            }
        });

        budgetAttrList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.budgetRecyclerView);
        recyclerView.setHasFixedSize(true);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                budgetAttrList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        BudgetAttr budgetAttr = snapshot.getValue(BudgetAttr.class);
                        if (budgetAttr.getCuid().equals(Uid)){
                            budgetAttrList.add(budgetAttr);
                        }
                    }
                    Collections.reverse(budgetAttrList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        layoutManager = new LinearLayoutManager(this);
        adapter = new BudgetRVAdapter(budgetAttrList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BudgetRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String destination = budgetAttrList.get(position).getDestination();
                String date = budgetAttrList.get(position).getDate();
                String budget = budgetAttrList.get(position).getBudget();
                String transportation = budgetAttrList.get(position).getTransportation();
                String tType = budgetAttrList.get(position).getTransportationType();
                String accommodation = budgetAttrList.get(position).getAccommodation();
                String aType = budgetAttrList.get(position).getAccommodationType();
                String food = budgetAttrList.get(position).getFood();
                String activities = budgetAttrList.get(position).getActivities();
                String cuid = budgetAttrList.get(position).getCuid();
                String budgetid = budgetAttrList.get(position).getBudgetid();
                String totalcost = budgetAttrList.get(position).getTotalCost();
                Intent intent = new Intent(BudgetRV.this, ViewBudget.class);
                intent.putExtra("budgetid",budgetid);
                startActivity(intent);
            }
        });
    }
}
