package com.example.meizar.test;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference mRef, mRefBus;
    private EventListener mRefListener;
    private Bus mBus;
    private Location cameraLocation;
    private Map<String, Marker> busMarkerList;
    private LocationManager locationManager;
    PlaceAutocompleteFragment placeAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Haltes");
        mRefBus = database.getReference("Buses");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        busMarkerList = new HashMap<String, Marker>();

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
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRefBus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot valueSnapshot : dataSnapshot.getChildren()) {
                    Bus mBus = valueSnapshot.getValue(Bus.class);
                    LatLng busLoc = new LatLng(mBus.getLatitude(), mBus.getLongitude());
                    Marker busMarker = busMarkerList.get(mBus.getPlat());
                    if(busMarker == null){
                        busMarker = mMap.addMarker(new MarkerOptions()
                                .position(busLoc)
                                .title(mBus.getPlat())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .snippet("Penumpang : " + mBus.getPassenger_count())
                        );
                        busMarkerList.put(mBus.getPlat(), busMarker);
                    }
                    else{
                        busMarker.setPosition(busLoc);
                    }
                }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
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
                }
                catch (Exception ex){
                    System.out.print(ex.getMessage());
                }
                return v;
            }
        });
        */

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
}
