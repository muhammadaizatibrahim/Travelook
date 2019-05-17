package com.example.aizat.travelook_v1.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aizat.travelook_v1.R;
import com.example.aizat.travelook_v1.UpdateBudget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewbudgetFrag extends Fragment {


    private TextView destination, date, tType,tCost,aType,aCost,fCost,acCost,totalBudget,totalCost;
    private ImageView transPic,accommPic;
    private Button update,delete;
    private DatabaseReference databaseReference;
    private Query query;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_viewbudget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        destination = (TextView) view.findViewById(R.id.destination);
        date = (TextView) view.findViewById(R.id.date);
        tType = (TextView) view.findViewById(R.id.transportation_type);
        tCost = (TextView) view.findViewById(R.id.transportation_cost);
        aType = (TextView) view.findViewById(R.id.accommodation_type);
        aCost = (TextView) view.findViewById(R.id.accommodation_cost);
        fCost = (TextView) view.findViewById(R.id.food);
        acCost = (TextView) view.findViewById(R.id.activities);
        totalBudget = (TextView) view.findViewById(R.id.totalBudget);
        totalCost = (TextView) view.findViewById(R.id.totalCost);
        transPic = (ImageView) view.findViewById(R.id.transportation_type_pic);
        accommPic = (ImageView) view.findViewById(R.id.accommodation_type_pic);
        update = (Button) view.findViewById(R.id.update);
        delete = (Button) view.findViewById(R.id.delete);


        Intent intent = getActivity().getIntent();

        final String budgetid = intent.getStringExtra("budgetid");

        databaseReference = FirebaseDatabase.getInstance().getReference("Budget");

        query = databaseReference.orderByChild("budgetid").equalTo(budgetid);
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        String dest = snapshot.child("destination").getValue().toString();
                        String bDate = snapshot.child("date").getValue().toString();
                        String btType = snapshot.child("transportationType").getValue().toString();
                        String btCost = snapshot.child("transportation").getValue().toString();
                        String baType = snapshot.child("accommodationType").getValue().toString();
                        String baCost = snapshot.child("accommodation").getValue().toString();
                        String bfCost = snapshot.child("food").getValue().toString();
                        String bacCost = snapshot.child("activities").getValue().toString();
                        String btotalBudget = snapshot.child("budget").getValue().toString();
                        String btotalCost = snapshot.child("totalCost").getValue().toString();


                        if(btType.equals("Car"))
                            transPic.setImageResource(R.drawable.budget_car);
                        else if (btType.equals("Bus"))
                            transPic.setImageResource(R.drawable.budget_bus);
                        else if (btType.equals("Flight"))
                            transPic.setImageResource(R.drawable.budget_aeroplane);
                        else if (btType.equals("Taxi"))
                            transPic.setImageResource(R.drawable.budget_taxi);
                        else if (btType.equals("Bike"))
                            transPic.setImageResource(R.drawable.budget_bike);
                        else
                            transPic.setImageResource(R.drawable.noimagea);

                        if (baType.equals("Hotel"))
                            accommPic.setImageResource(R.drawable.budget_hotel);
                        else if (baType.equals("Chalet"))
                            accommPic.setImageResource(R.drawable.budget_chalet);
                        else if (baType.equals("Resort"))
                            accommPic.setImageResource(R.drawable.budget_resort);
                        else if (baType.equals("Dorm"))
                            accommPic.setImageResource(R.drawable.budget_dorm);
                        else if (baType.equals("Homestay"))
                            accommPic.setImageResource(R.drawable.budget_homestay);
                        else
                            accommPic.setImageResource(R.drawable.noimagea);



                        destination.setText(dest);
                        date.setText(bDate);
                        tType.setText(btType);
                        tCost.setText(btCost);
                        aType.setText(baType);
                        aCost.setText(baCost);
                        fCost.setText(bfCost);
                        acCost.setText(bacCost);
                        totalBudget.setText(btotalBudget);
                        totalCost.setText(btotalCost);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),UpdateBudget.class);
                intent.putExtra("budgetid",budgetid);
                startActivity(intent);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBudget(budgetid);
            }
        });


    }

    private void deleteBudget(String budgetid) {
        DatabaseReference delBudget = FirebaseDatabase.getInstance().getReference("Budget").child(budgetid);
        delBudget.removeValue();

        Toast.makeText(getContext(), "Budget is deleted", Toast.LENGTH_LONG).show();

        getActivity().finish();

    }
}
