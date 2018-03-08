package com.example.meizar.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KonfirmasiActivity extends AppCompatActivity {

    String halteAsal;
    String halteTujuan;
    String busPilihan;
    String uid;
    String idHalteAsal;
    String idHalteTujuan;
    String idBus;
    DatabaseReference halteRef, busRef, orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        halteRef = database.getReference("Haltes");
        busRef = database.getReference("Buses");
        orderRef = database.getReference("Orders");

        halteAsal = getIntent().getStringExtra("halteAsal");
        halteTujuan = getIntent().getStringExtra("halteTujuan");
        busPilihan = getIntent().getStringExtra("busPilihan");

        halteRef.orderByChild("name").equalTo(halteAsal).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    idHalteAsal = singleSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        halteRef.orderByChild("name").equalTo(halteTujuan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    idHalteTujuan = singleSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        busRef.orderByChild("plat").equalTo(busPilihan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    idBus = singleSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();

        TextView halteAsalText = (TextView) findViewById(R.id.halteAsalText);
        TextView halteTujuanText = (TextView) findViewById(R.id.halteTujuanText);
        TextView platText = (TextView) findViewById(R.id.platText);

        halteAsalText.setText(halteAsal);
        halteTujuanText.setText(halteTujuan);
        platText.setText(busPilihan);


        Button bookingBtn = (Button) findViewById(R.id.bookingBtn);
        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order(uid, idHalteAsal, idHalteTujuan, idBus);
                String key = orderRef.push().getKey();
                orderRef.child(key).setValue(order);
                Intent intent = new Intent(KonfirmasiActivity.this, QRCodeActivity.class);
                intent.putExtra("halteTujuan", halteTujuan);
                intent.putExtra("halteAsal", halteAsal);
                intent.putExtra("busPilihan", busPilihan);
                intent.putExtra("orderKey", key);
                startActivity(intent);
            }
        });
    }
}
