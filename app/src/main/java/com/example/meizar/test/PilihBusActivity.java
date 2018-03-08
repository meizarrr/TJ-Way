package com.example.meizar.test;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PilihBusActivity extends AppCompatActivity {

    DatabaseReference busRef;
    private ArrayList<Bus> busList = new ArrayList<Bus>();
    ListView busListView;
    private LocationManager locationManager;
    private Location myLocation;
    String halteAsal;
    String halteTujuan;
    String busPilihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_bus);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        busListView = (ListView) findViewById(R.id.busListView);

        halteAsal = getIntent().getStringExtra("halteAsal");
        halteTujuan = getIntent().getStringExtra("halteTujuan");

        Criteria criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final BusListAdapter busListAdapter = new BusListAdapter(this, busList);
        busListView.setAdapter(busListAdapter);

        busRef = database.getReference("Buses");
        busRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Bus mBus = singleSnapshot.getValue(Bus.class);
                    double jarak = measure(myLocation.getLatitude(), myLocation.getLongitude(),mBus.getLatitude(), mBus.getLongitude());
                    mBus.setJarak(jarak);
                    busList.add(mBus);
                }
                Collections.sort(busList, new Comparator<Bus>() {
                    @Override
                    public int compare(Bus o1, Bus o2) {
                        if(o1.getJarak() > o2.getJarak()) return 1;
                        else return -1;
                    }
                });
                busListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bus bus = (Bus) busListAdapter.getItem(position);
            busPilihan = bus.getPlat();
            Intent intent = new Intent(PilihBusActivity.this, KonfirmasiActivity.class);
            intent.putExtra("halteTujuan", halteTujuan);
            intent.putExtra("halteAsal", halteAsal);
            intent.putExtra("busPilihan", busPilihan);
            startActivity(intent);
            //startActivity(new Intent(PilihBusActivity.this, KonfirmasiActivity.class));
            }
        });
    }

    private double measure(double lat1, double lon1, double lat2, double lon2){  // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(PilihBusActivity.this, ProfileActivity.class));
        }
        return true;
    }
}