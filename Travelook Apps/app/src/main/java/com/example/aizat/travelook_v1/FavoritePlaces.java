package com.example.aizat.travelook_v1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aizat.travelook_v1.FavouriteModel.FavouriteAttr;
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

public class FavoritePlaces extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FavoritePlacesAdapter favoritePlacesAdapter;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Query query;
    String Uid;

    List<FavouriteAttr> favouriteAttrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Uid = firebaseUser.getUid().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Favorite");
        databaseReference.keepSynced(true);

        query = databaseReference.orderByChild("name");

        favouriteAttrList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.favRecyclerView);
        recyclerView.setHasFixedSize(true);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouriteAttrList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        FavouriteAttr favouriteAttr = snapshot.getValue(FavouriteAttr.class);
                        if (favouriteAttr.getCuid().equals(Uid)){
                            favouriteAttrList.add(favouriteAttr);
                        }
                    }
                    Collections.reverse(favouriteAttrList);
                    favoritePlacesAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        layoutManager = new LinearLayoutManager(this);
        favoritePlacesAdapter = new FavoritePlacesAdapter(favouriteAttrList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(favoritePlacesAdapter);

        favoritePlacesAdapter.setOnItemClickListener(new FavoritePlacesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String placeid = favouriteAttrList.get(position).getPlaceId();
                String url = favouriteAttrList.get(position).getUrl();
                String name = favouriteAttrList.get(position).getName();
                String addr = favouriteAttrList.get(position).getAddr();
                String phoneNum = favouriteAttrList.get(position).getPhoneNum();
                String cuid = favouriteAttrList.get(position).getCuid();
                String favid = favouriteAttrList.get(position).getFavId();
                Intent intent = new Intent(FavoritePlaces.this, ViewFavPlaces.class);
                intent.putExtra("favId",favid);
                startActivity(intent);


            }
        });

    }
}
