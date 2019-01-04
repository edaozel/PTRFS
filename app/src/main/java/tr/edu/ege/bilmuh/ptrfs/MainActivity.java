package tr.edu.ege.bilmuh.ptrfs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location loc;
    private PlaceDetectionClient placeDetectionClient;
    private Intent mainToDestinationIntent;
    private CardView durak;
    private CardView tasit;
    private CardView duyuru;
    private CardView ayarlar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        durak = findViewById(R.id.durak);
        tasit = findViewById(R.id.tasit);
        duyuru= findViewById(R.id.duyuru);
        ayarlar = findViewById(R.id.ayarlar);

        mainToDestinationIntent = new Intent(MainActivity.this, DestinationActivity.class);
        placeDetectionClient = Places.getPlaceDetectionClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button buttonNGE = findViewById(R.id.buttonNGE);
        buttonNGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainToDestinationIntent);
            }
        });

        durak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"BU USECASE GERÇEKLEŞTİRİLMEMİŞTİR",Toast.LENGTH_SHORT).show();
            }
        });
        tasit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"BU USECASE GERÇEKLEŞTİRİLMEMİŞTİR",Toast.LENGTH_SHORT).show();
            }
        });

        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"BU USECASE GERÇEKLEŞTİRİLMEMİŞTİR",Toast.LENGTH_SHORT).show();
            }
        });
        duyuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"BU USECASE GERÇEKLEŞTİRİLMEMİŞTİR",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            loc = locationManager.getLastKnownLocation("gps");
            mainToDestinationIntent.putExtra("startLoc", new LatLng(loc.getLatitude(), loc.getLongitude()));
            Task<PlaceLikelihoodBufferResponse> placeResult = placeDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    PlaceLikelihood mostLikelyPlace = null;
                    float likelyhood = 0;
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        if (placeLikelihood.getLikelihood() > likelyhood) {
                            mostLikelyPlace = placeLikelihood;
                            likelyhood = placeLikelihood.getLikelihood();
                        }
                    }
                    mainToDestinationIntent.putExtra("startAddress", mostLikelyPlace.getPlace().getAddress());
                    likelyPlaces.release();
                }
            });
        }

        LatLng currentLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLoc).title("Konumunuz"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
    }
}
