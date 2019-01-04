package tr.edu.ege.bilmuh.ptrfs;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public class RouteFinderTest {
    private List<PublicTransport> publicTransportList;
    private ArrayList<Station> stationList;
    private List<Line> lineList;

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionCalculateRoute() {
        RouteFinder.CalculateRoute(null, null, null, null, null, null);
    }

    @Test
    public void successCalculateRoute() {
        initializeData();

        assertNotNull(RouteFinder.CalculateRoute(new LatLng(38.4016940, 27.1019545),
                new LatLng(38.4600723, 27.2298547),
                publicTransportList,
                stationList,
                lineList,
                new TreeMap<>()));
    }

    @Test (expected = NullPointerException.class)
    public void nullPointerExceptionFindClosestStations() {
        RouteFinder.findClosestStations(null, null);
    }

    @Test
    public void successFindClosestStations() {
        initializeData();
        assertNotNull(RouteFinder.findClosestStations(new LatLng(38.4016940, 27.1019545), stationList));
    }

    private void initializeData(){
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
        sL.add(new TransferStation(9, "Hilal",new LatLng(38.4267875, 27.1550975)));
        sL.add(new TransferStation(10, "Halkapinar",new LatLng(38.4350168, 27.1685568)));
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
        publicTransportList.add(new PublicTransport("Metro", new ArrayList<Line>(Arrays.asList(tempLine)), 0));
        tempLine.setPublicTransport(publicTransportList.get(0));
        ((TransferStation) sL.get(9)).addPublicTransport(publicTransportList.get(0));
        ((TransferStation) sL.get(9)).addLine(tempLine);
        ((TransferStation) sL.get(10)).addLine(tempLine);
        ((TransferStation) sL.get(10)).addPublicTransport(publicTransportList.get(0));
        stationList.addAll(sL);
        lineList.add(tempLine);
        // Metro end

        // 525 start
        sL = new ArrayList<>();
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
        publicTransportList.add(new PublicTransport("Otobus", new ArrayList<Line>(Arrays.asList(tempLine)), 1));
        tempLine.setPublicTransport(publicTransportList.get(1));
        stationList.addAll(sL);
        lineList.add(tempLine);
        // 525 end

        // Walk start
        sL = new ArrayList<>();

        stationHashMap = new LinkedHashMap<>();

        runningTimes = new ArrayList<>();


        tempLine = new Line(2, "Yuruyus", sL, stationHashMap, null);
        tempLine.setRunningTimes(runningTimes);
        publicTransportList.add(new PublicTransport("Yuruyus", new ArrayList<Line>(Arrays.asList(tempLine)), 2));
        tempLine.setPublicTransport(publicTransportList.get(2));
        stationList.addAll(sL);
        lineList.add(tempLine);
        // Walk end
    }
}
