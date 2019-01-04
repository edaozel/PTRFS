package tr.edu.ege.bilmuh.ptrfs;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RouteDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Adapter2 adapter2;
    private RecyclerView recyclerView;
    private Route route;
    private List<String> stepList;
    private ArrayList<Station> stationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        route = (Route) getIntent().getSerializableExtra("route");
        stationList = (ArrayList<Station>) getIntent().getSerializableExtra("stationList");
        stepList = route.printDetailedSteps(stationList);
        //stepList = new ArrayList<>(Arrays.asList("rota1", "rota2"));
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter2 =new Adapter2(RouteDetailActivity.this,stepList);
        recyclerView.setAdapter(adapter2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        List<Station> lineStationList;
        PolylineOptions  polylineOptions = new PolylineOptions().width(20).color(Color.valueOf(171f, 71f, 188f).toArgb());
        for(Route.Step i : route.getStepList()) {
            if(i.getUsedPT().getID() == 2) {
                lineStationList = new ArrayList<>(Arrays.asList(i.getToStation(), i.getFromStation()));
            } else {
                lineStationList = i.getUsedLine().getStations();
                if(i.getUsedLine().getStations().indexOf(i.getFromStation()) > i.getUsedLine().getStations().indexOf(i.getToStation())) {
                    Collections.reverse(lineStationList);
                }
                lineStationList = lineStationList.subList(lineStationList.indexOf(i.getFromStation()),
                            lineStationList.indexOf(i.getToStation()) + 1);

            }

            for(Station s : lineStationList) {
                polylineOptions.add(s.getLocation());
                mMap.addMarker(new MarkerOptions().position(s.getLocation()).title(s.getName()));
            }
        }
        mMap.addPolyline(polylineOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.getStepList().get(0).getFromStation().getLocation(), 12));
    }
}
