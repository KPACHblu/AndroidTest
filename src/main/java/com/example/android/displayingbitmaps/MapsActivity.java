package com.example.android.displayingbitmaps;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.displayingbitmaps.ui.ImageGridFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String CURRENT_LOCATION_PARAM = "com.example.android.displayingbitmaps.CURRENT_LOCATION";
    private GoogleMap mMap;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mLastLocation = (Location) getIntent().getExtras().get(CURRENT_LOCATION_PARAM);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.use_location:
                final Intent i = new Intent();
                i.putExtra(ImageGridFragment.LOCATION_PARAM, mLastLocation);
                setResult(RESULT_OK, i);
                finish();
        }
        return super.onOptionsItemSelected(item);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        final Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
                mLastLocation.setLatitude(latLng.latitude);
                mLastLocation.setLongitude(latLng.longitude);
                Toast.makeText(getApplicationContext(), "Marker Location:"+latLng.latitude+";"+latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
