package com.example.aizat.travelook_v1.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aizat.travelook_v1.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BudgetGraphFrag extends Fragment {

    private PieChart pieChart;
    private DatabaseReference databaseReference;
    private Query query;
    private TextView transAmount, accommAmount, foodAmount, accAmount, totalCost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_budget_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = (PieChart) view.findViewById(R.id.budgetChart);
        transAmount = (TextView) view.findViewById(R.id.transamount);
        accommAmount = (TextView) view.findViewById(R.id.accommAmount);
        foodAmount = (TextView) view.findViewById(R.id.foodAmount);
        accAmount = (TextView) view.findViewById(R.id.accAmount);
        totalCost = (TextView) view.findViewById(R.id.totalCost);



        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,5,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.85f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        Intent intent = getActivity().getIntent();

        final String budgetid = intent.getStringExtra("budgetid");

        databaseReference = FirebaseDatabase.getInstance().getReference("Budget");

        query = databaseReference.orderByChild("budgetid").equalTo(budgetid);
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        String transportation = snapshot.child("transPerc").getValue().toString();
                        String accommodation = snapshot.child("accommPerc").getValue().toString();
                        String food = snapshot.child("foodsPerc").getValue().toString();
                        String activities = snapshot.child("accPerc").getValue().toString();

                        float trans = Float.parseFloat(transportation);
                        float accomm = Float.parseFloat(accommodation);
                        float foods = Float.parseFloat(food);
                        float acc = Float.parseFloat(activities);


                        String transA = snapshot.child("transportation").getValue().toString();
                        String accomA = snapshot.child("accommodation").getValue().toString();
                        String foodA = snapshot.child("food").getValue().toString();
                        String accA = snapshot.child("activities").getValue().toString();
                        String totalCostA = snapshot.child("totalCost").getValue().toString();

                        transAmount.setText(transA);
                        accommAmount.setText(accomA);
                        foodAmount.setText(foodA);
                        accAmount.setText(accA);
                        totalCost.setText(totalCostA);


                        ArrayList<PieEntry> yValue = new ArrayList<>();

                        yValue.add(new PieEntry(trans, "Transportation"));
                        yValue.add(new PieEntry(accomm, "Accommodation"));
                        yValue.add(new PieEntry(foods, "Foods"));
                        yValue.add(new PieEntry(acc, "Activities"));

                        pieChart.animateY(2000, Easing.EasingOption.EaseInOutCubic);



                        PieDataSet dataSet = new PieDataSet(yValue,"Categories");

                        dataSet.setSliceSpace(3f);
                        dataSet.setSelectionShift(5f);
                        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

                        dataSet.setValueFormatter(new PercentFormatter());

                        pieChart.getLegend().setEnabled(false);


                        int colorBlack = Color.parseColor("#000000");
                        pieChart.setEntryLabelColor(colorBlack);

                        PieData data = new PieData(dataSet);
                        data.setValueTextSize(10f);
                        data.setValueTextColor(Color.BLACK);
                        pieChart.setData(data);


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
