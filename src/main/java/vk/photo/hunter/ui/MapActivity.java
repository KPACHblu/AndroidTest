package vk.photo.hunter.ui;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import vk.photo.hunter.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String CURRENT_LOCATION_PARAM = "vk.photo.hunter.CURRENT_LOCATION";
    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vk.photo.hunter.R.layout.map_fragment);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(vk.photo.hunter.R.id.map);
        mapFragment.getMapAsync(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mLastLocation = (Location) getIntent().getExtras().get(CURRENT_LOCATION_PARAM);
        Log.d(TAG, "onCreate activity with location: " + mLastLocation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(vk.photo.hunter.R.menu.maps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case vk.photo.hunter.R.id.use_location:
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
        LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        final Marker marker = mMap.addMarker(new MarkerOptions().position(currentLocation).title(getApplicationContext().getResources().getString(R.string.maps_marker_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
                marker.showInfoWindow();
                mLastLocation.setLatitude(latLng.latitude);
                mLastLocation.setLongitude(latLng.longitude);
            }
        });
    }

}
