package com.example.aizat.travelook_v1;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aizat.travelook_v1.BudgetModel.BudgetAttr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class Budget extends AppCompatActivity {

    private Spinner transportationSpinner, accommodationSpinner;
    private Button budgetCalculate, budgetSave;
    private TextView budgetTotal,budgetDate, budgetUnusedMoney;
    private LinearLayout view_date;
    private EditText budgetDest, budgetAmount, budgetTransportation, budgetAccommodation,budgetFood,budgetActivities;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    String Uid;
    float totalCost, unUsedCost, tPerc,aPerc,fPerc,accPerc,uMPerc;
    int initCost,tCost,aCost,fCost,acCost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        transportationSpinner = (Spinner) findViewById(R.id.transportationSpinner);
        String [] transportation = {"Car","Bus","Flight", "Bike","Taxi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,transportation);
        transportationSpinner.setAdapter(adapter);
        int position = adapter.getPosition("Taxi");
        transportationSpinner.setSelection(position);

        accommodationSpinner = (Spinner) findViewById(R.id.accommodationSpinner);
        String [] accommodation = {"Hotel", "Chalet", "Resort", "Dorm", "Homestay"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, accommodation);
        accommodationSpinner.setAdapter(adapter1);
        int position1 = adapter1.getPosition("Homestay");
        accommodationSpinner.setSelection(position1);


        budgetTotal = (TextView) findViewById(R.id.budgetTotal);
        budgetDest = (EditText) findViewById(R.id.budgetDest);
        budgetAmount = (EditText) findViewById(R.id.budgetAmount);
        budgetTransportation = (EditText) findViewById(R.id.budgetTransportation);
        budgetAccommodation = (EditText) findViewById(R.id.budgetAccomodation);
        budgetFood = (EditText) findViewById(R.id.budgetFood);
        budgetActivities = (EditText) findViewById(R.id.budgetActivities);
        budgetDate = (TextView) findViewById(R.id.budgetDate);
        budgetTotal = (TextView) findViewById(R.id.budgetTotal);
        budgetUnusedMoney = (TextView) findViewById(R.id.unUsedMoney);
        view_date = (LinearLayout) findViewById(R.id.view_date);
        budgetCalculate = (Button) findViewById(R.id.budgetCalculate);
        budgetSave = (Button) findViewById(R.id.budgetSave);


        budgetAmount.addTextChangedListener(calculateTextWatcher);
        budgetTransportation.addTextChangedListener(calculateTextWatcher);
        budgetAccommodation.addTextChangedListener(calculateTextWatcher);
        budgetFood.addTextChangedListener(calculateTextWatcher);
        budgetActivities.addTextChangedListener(calculateTextWatcher);



        budgetCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                initCost = Integer.parseInt(budgetAmount.getText().toString());
                tCost = Integer.parseInt(budgetTransportation.getText().toString());
                aCost = Integer.parseInt(budgetAccommodation.getText().toString());
                fCost = Integer.parseInt(budgetFood.getText().toString());
                acCost = Integer.parseInt(budgetActivities.getText().toString());


                totalCost = tCost + aCost + fCost + acCost;
                unUsedCost = initCost - totalCost;
                budgetTotal.setText(String.valueOf(totalCost));
                budgetUnusedMoney.setText(String.valueOf(unUsedCost));

                tPerc = (( tCost* 100) / totalCost);
                aPerc = (( aCost* 100) / totalCost);
                fPerc = (( fCost* 100) / totalCost);
                accPerc = (( acCost* 100) / totalCost);
            }
        });




        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Uid = firebaseUser.getUid().toString();

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Budget");

        view_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Budget.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDataSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                budgetDate.setText(date);
            }
        };

        budgetSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String destination = budgetDest.getText().toString();
                if(TextUtils.isEmpty(destination)){
                    Toast.makeText(Budget.this," Please enter your destination", Toast.LENGTH_SHORT).show();
                    return;
                }
                String date = budgetDate.getText().toString();
                if (TextUtils.isEmpty(date)){
                    Toast.makeText(Budget.this, "Please enter date", Toast.LENGTH_SHORT).show();
                    return;
                }
                String budget = budgetAmount.getText().toString();
                String transportation = budgetTransportation.getText().toString();
                String transpotationtype = String.valueOf(transportationSpinner.getSelectedItem());
                String accommodation = budgetAccommodation.getText().toString();
                String accommodationType = String.valueOf(accommodationSpinner.getSelectedItem());
                String food = budgetFood.getText().toString();
                String activities = budgetActivities.getText().toString();
                String totalCost = budgetTotal.getText().toString();
                String unusedCost = budgetUnusedMoney.getText().toString();
                String id = UUID.randomUUID().toString();
                String uid = Uid.toString();
                String budgetid = id.toString();
                String transPercent = Float.toString(tPerc);
                String accommPercent = Float.toString(aPerc);
                String foodPercent = Float.toString(fPerc);
                String actiPercent = Float.toString(accPerc);


                BudgetAttr budgetAttr = new BudgetAttr(destination,date,budget,transportation,transpotationtype,accommodation,
                        accommodationType,food,activities,uid,budgetid,totalCost,unusedCost,transPercent,accommPercent,foodPercent,
                        actiPercent);

                databaseReference.child(id).setValue(budgetAttr);

                finish();

            }
        });



    }

    private TextWatcher calculateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String atInput = budgetAmount.getText().toString().trim();
            String tInput = budgetTransportation.getText().toString().trim();
            String aInput = budgetAccommodation.getText().toString().trim();
            String fInput = budgetFood.getText().toString().trim();
            String acInput = budgetActivities.getText().toString().trim();

            budgetCalculate.setEnabled(!atInput.isEmpty() && !tInput.isEmpty() && !aInput.isEmpty() && !fInput.isEmpty() && !acInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


}
