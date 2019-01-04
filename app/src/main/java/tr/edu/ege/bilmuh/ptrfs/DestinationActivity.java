package tr.edu.ege.bilmuh.ptrfs;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class DestinationActivity extends FragmentActivity implements OnMapReadyCallback {

    private Place place;
    private Intent destinationToRouteIntent;
    private LatLng startLoc;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        startLoc = getIntent().getParcelableExtra("startLoc");

        destinationToRouteIntent = new Intent(DestinationActivity.this, RouteActivity.class);
        destinationToRouteIntent.putExtra("startLoc", startLoc);
        destinationToRouteIntent.putExtra("startAddress", getIntent().getStringExtra("startAddress"));

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(new LatLngBounds(startLoc, startLoc));

        try {
            startActivityForResult(builder.build(DestinationActivity.this), 1);
        } catch (Exception e) {
            // TODO
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button button_ileri = findViewById(R.id.button_ileri);
        button_ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(destinationToRouteIntent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(startLoc).title("Start location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 15));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                destinationToRouteIntent.putExtra("destinationLoc", place.getLatLng());
                destinationToRouteIntent.putExtra("destinationAddress", place.getAddress());
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Destination location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
            }
        }
    }
}
