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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity2 extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener
{

    private GoogleMap mMap;
    DatabaseReference mRef, mRefBus;
    private EventListener mRefListener;
    private Bus mBus;
    private Location cameraLocation;
    private LocationManager locationManager;
    PlaceAutocompleteFragment placeAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("TJ-Way - Pilih Halte Tujuan");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Haltes");
        mRefBus = database.getReference("Buses");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        Criteria criteria = new Criteria();
        cameraLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Halte mHalte = singleSnapshot.getValue(Halte.class);
                    //Toast.makeText(getApplicationContext(), mHalte.getName(), Toast.LENGTH_SHORT).show();
                    LatLng loc = new LatLng(mHalte.getLatitude(), mHalte.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(loc)
                            .title(mHalte.getName())
                            .snippet("Halte")
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = null;
                try{
                    v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
                    TextView mHalteText = v.findViewById(R.id.halteTxt);
                    mHalteText.setText(marker.getTitle());
                }
                catch (Exception ex){
                    System.out.print(ex.getMessage());
                }
                return v;
            }
        });

        mMap.setOnInfoWindowClickListener(this);


        if(cameraLocation != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cameraLocation.getLatitude(), cameraLocation.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(cameraLocation.getLatitude(), cameraLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom level
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String halteTujuan = marker.getTitle();
        Intent intent = new Intent(this, PilihHalteActivity.class);
        intent.putExtra("halteTujuan", halteTujuan);
        startActivity(intent);
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
            startActivity(new Intent(MapsActivity2.this, ProfileActivity.class));
        }
        return true;
    }
}
