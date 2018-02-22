package com.example.meizar.test;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Button addLocButton;
    private TextView mNameText;
    private TextView mLatText;
    private TextView mLongText;
    private TextView mTextView;
    private ValueEventListener mrefListener;
    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addLocButton = (Button) findViewById(R.id.addLocButton);
        mNameText = (TextView) findViewById(R.id.nameText);
        mLatText = (TextView) findViewById(R.id.latText);
        mLongText = (TextView) findViewById(R.id.longText);
        mTextView = (TextView) findViewById(R.id.textView);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mref = database.getReference("Haltes");
        mrefListener = mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mText = "";
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Halte mHalte = singleSnapshot.getValue(Halte.class);
                    mText = mText + " " + mHalte.getName();
                }
                mTextView.setText(mText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameText.getText().toString();
                double lat = Double.parseDouble(mLatText.getText().toString());
                double lng = Double.parseDouble(mLongText.getText().toString());
                Halte halte1 = new Halte(name, lat, lng);
                mref.push().setValue(halte1);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mref.removeEventListener(mrefListener);
    }
}
