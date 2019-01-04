package tr.edu.ege.bilmuh.ptrfs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public class RouteActivity extends AppCompatActivity {

    private Button buttonStart;
    private Button buttonDestination;

    private LatLng startLoc;
    private String startAddress;
    private LatLng destinationLoc;
    private String destinationAddress;
    private List<PublicTransport> publicTransportList;
    private ArrayList<Station> stationList;
    private List<Line> lineList;
    private TreeMap<Double, Route> routeMap;
    private RecyclerView rv;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        routeMap = new TreeMap<>();

        // Initialize public transport data
        initializeData();

        rv = findViewById(R.id.rv);

        rv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(this, routeMap, stationList);// liste oluşturduktan sonra altına yaz
        rv.setAdapter(adapter);



        buttonStart = findViewById(R.id.buttonStart);
        buttonDestination = findViewById(R.id.buttonDestination);

        startLoc = getIntent().getParcelableExtra("startLoc");
        startAddress = getIntent().getStringExtra("startAddress").split(",")[0];
        destinationLoc = getIntent().getParcelableExtra("destinationLoc");
        destinationAddress = getIntent().getStringExtra("destinationAddress").split(",")[0];

        RouteFinder.CalculateRoute(startLoc, destinationLoc, publicTransportList, stationList, lineList, routeMap);
        adapter.notifyDataSetChanged();

        buttonStart.setText(startAddress);
        buttonDestination.setText(destinationAddress);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(new LatLngBounds(startLoc, startLoc));

                try {
                    startActivityForResult(builder.build(RouteActivity.this), 1);
                } catch (Exception e) {
                    // TODO
                }
            }
        });

        buttonDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(new LatLngBounds(destinationLoc, destinationLoc));

                try {
                    startActivityForResult(builder.build(RouteActivity.this), 2);
                } catch (Exception e) {
                    // TODO
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Place place;
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                startLoc = place.getLatLng();
                startAddress = place.getAddress().toString().split(",")[0];
                buttonStart.setText(startAddress);
                RouteFinder.CalculateRoute(startLoc, destinationLoc, publicTransportList, stationList, lineList, routeMap);
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                destinationLoc = place.getLatLng();
                destinationAddress = place.getAddress().toString().split(",")[0];
                buttonDestination.setText(destinationAddress);
                RouteFinder.CalculateRoute(startLoc, destinationLoc, publicTransportList, stationList, lineList, routeMap);
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void initializeData() {
        publicTransportList = new ArrayList<>();
        stationList = new ArrayList<>();
        lineList = new ArrayList<>();

        // Metro start
        List<Station> sL = new ArrayList<>();
        sL.add(new Station(0, "Fahrettin Altay",new LatLng(38.3968306, 27.0700999)));
        sL.add(new Station(1, "Poligon",new LatLng(38.3932771, 27.0852464)));
        sL.add(new Station(2, "Goztepe",new LatLng(38.3959700, 27.0946002)));
        sL.add(new Station(3, "Hatay",new LatLng(38.4016280, 27.1023609)));
        sL.add(new Station(4, "Izmirspor",new LatLng(38.4016377, 27.1102459)));
        sL.add(new Station(5, "Ucyol",new LatLng(38.4058954, 27.1213573)));
        sL.add(new Station(6, "Konak",new LatLng(38.4175249, 27.1283491)));
        sL.add(new Station(7, "Cankaya",new LatLng(38.4225384, 27.1369258)));
        sL.add(new Station(8, "Basmane",new LatLng(38.4224457, 27.1436552)));
        sL.add(new TransferStation(9, "Hilal", new LatLng(38.4267875, 27.1550975)));
        sL.add(new TransferStation(10, "Halkapinar", new LatLng(38.4350168, 27.1685568)));
        sL.add(new Station(11, "Stadyum",new LatLng(38.4424036, 27.1808500)));
        sL.add(new Station(12, "Sanayi",new LatLng(38.44880872, 27.1901397)));
        sL.add(new Station(13, "Bolge",new LatLng(38.4549939, 27.2013974)));
        sL.add(new Station(14, "Bornova",new LatLng(38.4582983, 27.2118205)));
        sL.add(new Station(15, "Ege Universitesi",new LatLng(38.4596354, 27.2289025)));
        sL.add(new Station(16, "Evka 3",new LatLng(38.4649212, 27.2286091)));

        LinkedHashMap<Station, Integer> stationHashMap = new LinkedHashMap<>();
        stationHashMap.put(sL.get(0), 0);
        stationHashMap.put(sL.get(1), 2);
        stationHashMap.put(sL.get(2), 3);
        stationHashMap.put(sL.get(3), 5);
        stationHashMap.put(sL.get(4), 6);
        stationHashMap.put(sL.get(5), 8);
        stationHashMap.put(sL.get(6), 11);
        stationHashMap.put(sL.get(7), 13);
        stationHashMap.put(sL.get(8), 14);
        stationHashMap.put(sL.get(9), 16);
        stationHashMap.put(sL.get(10), 19);
        stationHashMap.put(sL.get(11), 21);
        stationHashMap.put(sL.get(12), 23);
        stationHashMap.put(sL.get(13), 25);
        stationHashMap.put(sL.get(14), 27);
        stationHashMap.put(sL.get(15), 29);
        stationHashMap.put(sL.get(16), 30);

        List<LocalTime> runningTimes = new ArrayList<>();
        runningTimes.add(LocalTime.of(6, 0));
        runningTimes.add(LocalTime.of(0, 4));
        runningTimes.add(LocalTime.of(23, 59));

        Line tempLine = new Line(0, "Fahrettin Altay - Evka 3", sL, stationHashMap, null);
        tempLine.setRunningTimes(runningTimes);
        publicTransportList.add(new PublicTransport("Metro", new ArrayList<>(Arrays.asList(tempLine)), 0));
        tempLine.setPublicTransport(publicTransportList.get(0));
        ((TransferStation) sL.get(9)).addPublicTransport(publicTransportList.get(0));
        ((TransferStation) sL.get(9)).addLine(tempLine);
        ((TransferStation) sL.get(10)).addLine(tempLine);
        ((TransferStation) sL.get(10)).addPublicTransport(publicTransportList.get(0));
        stationList.addAll(sL);
        lineList.add(tempLine);
        // Metro end

        // 525 start
        sL.clear();
        sL.add(new Station(17, "Ege Universitesi Kampus Son Duragi",new LatLng(38.4600902, 27.2347309)));
        sL.add(new Station(18, "Bornova Metro",new LatLng(38.4585680, 27.2141429)));

        stationHashMap = new LinkedHashMap<>();
        stationHashMap.put(sL.get(0), 0);
        stationHashMap.put(sL.get(1), 5);

        runningTimes = new ArrayList<>();
        runningTimes.add(LocalTime.of(7, 50));
        runningTimes.add(LocalTime.of(8, 20));
        runningTimes.add(LocalTime.of(8, 50));
        runningTimes.add(LocalTime.of(14, 20));
        runningTimes.add(LocalTime.of(14, 50));
        runningTimes.add(LocalTime.of(15, 20));
        runningTimes.add(LocalTime.of(15, 50));


        tempLine = new Line(1, "525", sL, stationHashMap, null);
        tempLine.setRunningTimes(runningTimes);
        publicTransportList.add(new PublicTransport("Otobus", new ArrayList<>(Arrays.asList(tempLine)), 1));
        tempLine.setPublicTransport(publicTransportList.get(1));
        stationList.addAll(sL);
        lineList.add(tempLine);
        // 525 end

        // Walk start
        sL.clear();

        stationHashMap = new LinkedHashMap<>();

        runningTimes = new ArrayList<>();


        tempLine = new Line(2, "Yuruyus", sL, stationHashMap, null);
        tempLine.setRunningTimes(runningTimes);
        publicTransportList.add(new PublicTransport("Yuruyus", new ArrayList<>(Arrays.asList(tempLine)), 2));
        tempLine.setPublicTransport(publicTransportList.get(2));
        stationList.addAll(sL);
        lineList.add(tempLine);
        // Walk end

        // Izban start
        sL.clear();
        sL.add(new Station(19, "Aliaga", new LatLng(38.7888296, 26.9671122)));
        sL.add(new Station(20, "Bicerova", new LatLng(38.7497429, 26.9606779)));
        sL.add(new Station(21, "Hatundere", new LatLng(38.6896751, 27.0178676)));
        sL.add(new Station(22, "Menemen", new LatLng(38.6031803, 27.0764343)));
        sL.add(new Station(23, "Egekent 2", new LatLng(38.5603914, 27.0442069)));
        sL.add(new Station(24, "Ulukent", new LatLng(38.5467972, 27.0352071)));
        sL.add(new Station(25, "Egekent 1", new LatLng(38.5074627, 27.0459497)));
        sL.add(new Station(26, "Atasanayi", new LatLng(38.4990755, 27.0532516)));
        sL.add(new Station(27, "Cigli", new LatLng(38.4920971, 27.0632228)));
        sL.add(new Station(28, "Mavisehir", new LatLng(38.4822539, 27.0830366)));
        sL.add(new Station(29, "Semikler", new LatLng(38.4749161, 27.0897676)));
        sL.add(new Station(30, "Demirkopru", new LatLng(38.4678348, 27.0966903)));
        sL.add(new Station(31, "Nergiz", new LatLng(38.4594905, 27.1048020)));
        sL.add(new Station(32, "Karsiyaka", new LatLng(38.4578798, 27.1153719)));
        sL.add(new Station(33, "Alaybey", new LatLng(38.4608218, 27.1217546)));
        sL.add(new Station(34, "Naldoken", new LatLng(38.4648051, 27.1288396)));
        sL.add(new Station(35, "Turan", new LatLng(38.4669609, 27.1494175)));
        sL.add(new Station(36, "Bayrakli", new LatLng(38.4637910, 27.1644815)));
        sL.add(new Station(37, "Salhane", new LatLng(38.4509872, 27.1721811)));
        sL.add(stationList.stream().filter(t -> t.getID() == 10).findFirst().get());
        sL.add(new Station(38, "Alsancak", new LatLng(38.4390389, 27.1481583)));
        sL.add(stationList.stream().filter(t -> t.getID() == 9).findFirst().get());
        sL.add(new Station(39, "Kemer", new LatLng(38.4219802, 27.1562803)));
        sL.add(new Station(40, "Sirinyer", new LatLng(38.3919903, 27.1473073)));
        sL.add(new Station(41, "Kosu", new LatLng(38.3836880, 27.1474237)));
        sL.add(new Station(42, "Inkilap", new LatLng(38.3697105, 27.1419845)));
        sL.add(new Station(43, "Semt Garaji", new LatLng(38.3566934, 27.1366586)));
        sL.add(new Station(44, "Esbas", new LatLng(38.3369653, 27.1363609)));
        sL.add(new Station(45, "Gaziemir", new LatLng(38.3269347, 27.1399071)));
        sL.add(new Station(46, "Sarnic", new LatLng(38.3121933, 27.1445001)));
        sL.add(new Station(47, "Adnan Menderes Havalimani", new LatLng(38.2914013, 27.1478763)));
        sL.add(new Station(48, "Cumaovasi", new LatLng(38.2638468, 27.1629694)));

        stationHashMap = new LinkedHashMap<>();
        stationHashMap.put(sL.get(0), 0);
        stationHashMap.put(sL.get(1), 5);
        stationHashMap.put(sL.get(2), 11);
        stationHashMap.put(sL.get(3), 20);
        stationHashMap.put(sL.get(4), 25);
        stationHashMap.put(sL.get(5), 27);
        stationHashMap.put(sL.get(6), 31);
        stationHashMap.put(sL.get(7), 33);
        stationHashMap.put(sL.get(8), 35);
        stationHashMap.put(sL.get(9), 38);
        stationHashMap.put(sL.get(10), 40);
        stationHashMap.put(sL.get(11), 42);
        stationHashMap.put(sL.get(12), 44);
        stationHashMap.put(sL.get(13), 46);
        stationHashMap.put(sL.get(14), 47);
        stationHashMap.put(sL.get(15), 48);
        stationHashMap.put(sL.get(16), 50);
        stationHashMap.put(sL.get(17), 52);
        stationHashMap.put(sL.get(18), 54);
        stationHashMap.put(sL.get(19), 57);
        stationHashMap.put(sL.get(20), 62);
        stationHashMap.put(sL.get(21), 68);
        stationHashMap.put(sL.get(22), 71);
        stationHashMap.put(sL.get(23), 75);
        stationHashMap.put(sL.get(24), 77);
        stationHashMap.put(sL.get(25), 79);
        stationHashMap.put(sL.get(26), 81);
        stationHashMap.put(sL.get(27), 83);
        stationHashMap.put(sL.get(28), 85);
        stationHashMap.put(sL.get(29), 87);
        stationHashMap.put(sL.get(30), 91);
        stationHashMap.put(sL.get(31), 95);

        runningTimes = new ArrayList<>();
        runningTimes.add(LocalTime.of(5, 37));
        runningTimes.add(LocalTime.of(6, 3));
        runningTimes.add(LocalTime.of(6, 25));
        runningTimes.add(LocalTime.of(6, 49));
        runningTimes.add(LocalTime.of(7, 13));
        runningTimes.add(LocalTime.of(7, 37));
        runningTimes.add(LocalTime.of(8, 1));
        runningTimes.add(LocalTime.of(8, 25));
        runningTimes.add(LocalTime.of(8, 51));
        runningTimes.add(LocalTime.of(9, 13));
        runningTimes.add(LocalTime.of(9, 37));
        runningTimes.add(LocalTime.of(10, 1));
        runningTimes.add(LocalTime.of(10, 25));
        runningTimes.add(LocalTime.of(10, 49));
        runningTimes.add(LocalTime.of(11, 11));
        runningTimes.add(LocalTime.of(11, 37));
        runningTimes.add(LocalTime.of(12, 1));
        runningTimes.add(LocalTime.of(12, 25));
        runningTimes.add(LocalTime.of(12, 51));
        runningTimes.add(LocalTime.of(13, 13));
        runningTimes.add(LocalTime.of(13, 37));
        runningTimes.add(LocalTime.of(14, 0));
        runningTimes.add(LocalTime.of(14, 27));
        runningTimes.add(LocalTime.of(14, 49));
        runningTimes.add(LocalTime.of(15, 13));
        runningTimes.add(LocalTime.of(15, 37));
        runningTimes.add(LocalTime.of(16, 1));
        runningTimes.add(LocalTime.of(16, 25));
        runningTimes.add(LocalTime.of(16, 49));
        runningTimes.add(LocalTime.of(17, 14));
        runningTimes.add(LocalTime.of(17, 37));
        runningTimes.add(LocalTime.of(18, 3));
        runningTimes.add(LocalTime.of(18, 25));
        runningTimes.add(LocalTime.of(18, 49));
        runningTimes.add(LocalTime.of(19, 11));
        runningTimes.add(LocalTime.of(19, 37));
        runningTimes.add(LocalTime.of(20, 1));
        runningTimes.add(LocalTime.of(20, 25));
        runningTimes.add(LocalTime.of(20, 47));
        runningTimes.add(LocalTime.of(21, 10));
        runningTimes.add(LocalTime.of(21, 37));
        runningTimes.add(LocalTime.of(21, 59));
        runningTimes.add(LocalTime.of(22, 30));
        runningTimes.add(LocalTime.of(22, 47));
        runningTimes.add(LocalTime.of(23, 13));
        runningTimes.add(LocalTime.of(23, 43));
        runningTimes.add(LocalTime.of(23, 55));


        tempLine = new Line(3, "Aliaga - Cumaovasi", sL, stationHashMap, null);
        tempLine.setRunningTimes(runningTimes);
        publicTransportList.add(new PublicTransport("Izban", new ArrayList<>(Arrays.asList(tempLine)), 3));
        tempLine.setPublicTransport(publicTransportList.get(3));
        ((TransferStation) stationList.stream().filter(t -> t.getID() == 9).findFirst().get()).addLine(tempLine);
        ((TransferStation) stationList.stream().filter(t -> t.getID() == 10).findFirst().get()).addLine(tempLine);
        ((TransferStation) stationList.stream().filter(t -> t.getID() == 9).findFirst().get()).addPublicTransport(publicTransportList.get(3));
        ((TransferStation) stationList.stream().filter(t -> t.getID() == 10).findFirst().get()).addPublicTransport(publicTransportList.get(3));
        sL.remove(stationList.stream().filter(t -> t.getID() == 9).findFirst().get());
        sL.remove(stationList.stream().filter(t -> t.getID() == 10).findFirst().get());
        stationList.addAll(sL);
        lineList.add(tempLine);
        // Izban end
    }
}

