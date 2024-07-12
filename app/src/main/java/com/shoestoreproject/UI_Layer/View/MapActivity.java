package com.shoestoreproject.UI_Layer.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.shoestoreproject.R;
import com.shoestoreproject.databinding.ActivityMapBinding;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    private SearchView mapSearchView;
    private ActivityMapBinding binding;
    private static final int DEFAULT_ZOOM = 15;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng mainLocation = new LatLng(10.79340, 106.73946);
    private final LatLng branchLocation = new LatLng(10.792503587827955, 106.736553853411);
    private final LatLng branchLocation1 = new LatLng(10.797827283528587, 106.7432081803936);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        setupViews();
        getLastLocation();
        setButtonActions();
    }

    private void setupViews() {
        mapSearchView = binding.mapSearch;
        ImageButton btnCurrentLocation = binding.btnCurrentLocation;
        ImageButton btnNearestBranch = binding.btnNearestBranch;

        // SearchView setup
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // ImageButton setup
        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocation != null) {
                    moveCameraToLocation(currentLocation);
                }
            }
        });

        btnNearestBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with logic to find and navigate to the nearest branch
                LatLng nearestBranch = findNearestBranch(currentLocation);
                if (nearestBranch != null) {
                    moveCameraToLocation(nearestBranch);
                }
            }
        });
    }

    private void setButtonActions() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        if (currentLocation != null) {
            moveCameraToLocation(currentLocation);
        }
        addMarkers();
    }

    private void moveCameraToLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        myMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
    }

    private void moveCameraToLocation(LatLng latLng) {
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    private void addMarkers() {
        myMap.addMarker(new MarkerOptions()
                .position(mainLocation)
                .title("Shoe Store")
                .snippet("Main Store"));

        myMap.addMarker(new MarkerOptions()
                .position(branchLocation)
                .title("Shoe Store Nguyễn Hiền")
                .snippet("Store 1"));

        myMap.addMarker(new MarkerOptions()
                .position(branchLocation1)
                .title("Shoe Store Thủ Thiêm")
                .snippet("Store 2"));
    }

    private void searchLocation(String locationName) {
        Geocoder geocoder = new Geocoder(MapActivity.this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                myMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
                moveCameraToLocation(latLng);
            } else {
                Toast.makeText(this, "Location not found: " + locationName, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LatLng findNearestBranch(Location location) {
        return branchLocation;
    }
}
