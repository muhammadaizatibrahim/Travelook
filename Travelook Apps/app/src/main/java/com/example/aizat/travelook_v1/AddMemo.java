package com.example.aizat.travelook_v1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aizat.travelook_v1.MemoModel.MemoAttr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddMemo extends AppCompatActivity {

    private Button saveButton;
    private TextView memoDate;
    private EditText memoTitle, memoText;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        saveButton = (Button) findViewById(R.id.buttonSave);
        memoDate = (TextView) findViewById(R.id.memoDate);
        memoTitle = (EditText) findViewById(R.id.memoTitle);
        memoText = (EditText) findViewById(R.id.memoText);

        memoTitle.addTextChangedListener(saveTextWatcher);
        memoText.addTextChangedListener(saveTextWatcher);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        /*databaseReference = FirebaseDatabase.getInstance().getReference();*/
        Uid = firebaseUser.getUid().toString();

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Memo");


        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy, HH:mm");
        Date date = new Date();
        memoDate.setText(dateFormat.format(date));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = memoDate.getText().toString();
                String title = memoTitle.getText().toString();
                String text = memoText.getText().toString();
                String id = UUID.randomUUID().toString();
                String uid = Uid.toString();
                String memoid =id.toString();


                MemoAttr memoAttr = new MemoAttr(date,title,text,uid,memoid);

                databaseReference.child(id).setValue(memoAttr);

                finish();
            }
        });



    }

    private TextWatcher saveTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String mTitle = memoTitle.getText().toString().trim();
            String mText = memoText.getText().toString().trim();

            saveButton.setEnabled(!mTitle.isEmpty()&&!mText.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
