package tr.edu.ege.bilmuh.ptrfs;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class RouteFinder {
    private static final double MAX_WALKING_DISTANCE = 1000;
    private static final double MAX_WALKING_DISTANCE_TO_STATIONS = 1000;

    public static TreeMap<Double, Route> CalculateRoute(LatLng start,
                                                        LatLng destination,
                                                        List<PublicTransport> publicTransportList,
                                                        ArrayList<Station> stationList,
                                                        List<Line> lineList,
                                                        TreeMap<Double, Route> routeMap) {
        routeMap.clear();
        TreeMap<Double, Route> resultMap = routeMap;
       Route tempRoute;
       Set<Map.Entry<Double, Station>> closestToStart = findClosestStations(start, stationList).subMap(0d, MAX_WALKING_DISTANCE_TO_STATIONS).entrySet();
       Set<Map.Entry<Double, Station>> closestToDestination = findClosestStations(destination, stationList).subMap(0d, MAX_WALKING_DISTANCE_TO_STATIONS).entrySet();


        // Case 1: Two points within walking distance
        if(SphericalUtil.computeDistanceBetween(start, destination) <= MAX_WALKING_DISTANCE) {
            tempRoute = new Route();
            double duration = SphericalUtil.computeDistanceBetween(start, destination) / (1.4 * 60);
            tempRoute.addStep(publicTransportList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                    null,
                    lineList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                    new Station("Baslangic", start),
                    new Station("hedef", destination),
                    duration,
                    duration * 1
                    );
            resultMap.put(duration * 1, tempRoute);
            tempRoute = null;
        }
        // Case 1 end

        // Case 2: Two stations on the same line
        for(Map.Entry<Double, Station> i : closestToStart) {
            for(Map.Entry<Double, Station> j : closestToDestination) {
                for(Line k : lineList) {
                    if(k.containsStations(i.getValue(), j.getValue())
                            && LocalTime.now().isBefore(k.getRunningTimes( /* TODO */).get(k.getRunningTimes( /* TODO */).size() - 1))
                            && LocalTime.now().isAfter(k.getRunningTimes( /* TODO */).get(0))) {
                        tempRoute = new Route();
                        int waitTime = k.getNearestTime(LocalTime.now());
                        double durations = SphericalUtil.computeDistanceBetween(start, i.getValue().getLocation()) / (1.4 * 60);
                        tempRoute.addStep(publicTransportList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                null,
                                lineList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                new Station("Baslangic", start),
                                i.getValue(),
                                durations,
                                durations * 1
                        );
                        double duration = Math.abs(waitTime + k.getTimetable().get(j.getValue()) - k.getTimetable().get(i.getValue()));
                        tempRoute.addStep(k.getPublicTransport(),
                                null,
                                k,
                                i.getValue(),
                                j.getValue(),
                                duration,
                                duration * 1);
                        double durationw = SphericalUtil.computeDistanceBetween(j.getValue().getLocation(), destination) / (1.4 * 60);
                        tempRoute.addStep(publicTransportList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                null,
                                lineList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                j.getValue(),
                                new Station("hedef", destination),
                                durationw,
                                durationw * 1
                        );
                        resultMap.put((durations * 1) + (duration * 1) + (durationw * 1), tempRoute);
                        tempRoute = null;
                    }
                }
            }
        }
        // Case 2 end

        // Case 3: Using two different lines through a transfer station
        for(Map.Entry<Double, Station> i : closestToStart) {
            for(Map.Entry<Double, Station> j : closestToDestination) {
                HashMap<Line, List<Station>> startLines = new HashMap<>();
                HashMap<Line, List<Station>> destinationLines = new HashMap<>();
                List<Station> tempList;
                for(Line k : lineList) {
                    tempList = k.getStations().stream()
                            .filter(t -> t instanceof TransferStation)
                            .collect(Collectors.toList());
                    boolean containsTransferStation = k.getStations().stream().anyMatch(t -> t instanceof TransferStation);
                    if(containsTransferStation
                            && k.getStations().contains(i.getValue())
                            && !k.getStations().contains(j.getValue())
                            && LocalTime.now().isBefore(k.getRunningTimes( /* TODO */).get(k.getRunningTimes( /* TODO */).size() - 1))
                            && LocalTime.now().isAfter(k.getRunningTimes( /* TODO */).get(0))) {
                        startLines.put(k, tempList);
                    }
                    else if(containsTransferStation
                            && !k.getStations().contains(i.getValue())
                            && k.getStations().contains(j.getValue())
                            && LocalTime.now().isBefore(k.getRunningTimes( /* TODO */).get(k.getRunningTimes( /* TODO */).size() - 1))
                            && LocalTime.now().isAfter(k.getRunningTimes( /* TODO */).get(0))) {
                        destinationLines.put(k, tempList);
                    }
                }
                for(Map.Entry<Line, List<Station>> k : startLines.entrySet()) {
                    for(Map.Entry<Line, List<Station>> l: destinationLines.entrySet()) {
                        List<Station> commonTransferStations = k.getValue();
                        commonTransferStations.retainAll(l.getValue());
                        for(Station s : commonTransferStations) {
                            tempRoute = new Route();
                            double duration0 = Math.abs(k.getKey().getTimetable().get(s) - k.getKey().getTimetable().get(i.getValue()));
                            double duration1 = Math.abs(l.getKey().getTimetable().get(j.getValue()) - l.getKey().getTimetable().get(s));

                            double durations = SphericalUtil.computeDistanceBetween(start, i.getValue().getLocation()) / (1.4 * 60);
                            tempRoute.addStep(publicTransportList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                    null,
                                    lineList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                    new Station("Baslangic", start),
                                    i.getValue(),
                                    durations,
                                    durations * 1
                            );

                            tempRoute.addStep(k.getKey().getPublicTransport(),
                                    null,
                                    k.getKey(),
                                    i.getValue(),
                                    s,
                                    duration0,
                                    duration0 * 1);
                            tempRoute.addStep(k.getKey().getPublicTransport(),
                                    l.getKey().getPublicTransport(),
                                    k.getKey(),
                                    s,
                                    s,
                                    0,
                                    0 * 1);
                            tempRoute.addStep(l.getKey().getPublicTransport(),
                                    null,
                                    l.getKey(),
                                    s,
                                    j.getValue(),
                                    duration1,
                                    duration1 * 1);

                            double durationw = SphericalUtil.computeDistanceBetween(j.getValue().getLocation(), destination) / (1.4 * 60);
                            tempRoute.addStep(publicTransportList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                    null,
                                    lineList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                    j.getValue(),
                                    new Station("hedef", destination),
                                    durationw,
                                    durationw * 1
                            );

                            resultMap.put((durations * 1) + (duration0 * 1) + (duration1 * 1) + (durationw * 1), tempRoute);
                            tempRoute = null;
                        }
                    }
                }
            }
        }
        // Case 3 end

        // Case 4: Using two different lines, transferring through close (within MAX_WALKING_DISTANCE) stations
        for(Map.Entry<Double, Station> i : closestToStart) {
            for (Map.Entry<Double, Station> j : closestToDestination) {
                List<Line> startLines = lineList.stream()
                                                .filter(t -> t.getStations().contains(i.getValue())
                                                             && !t.getStations().contains(j.getValue())
                                                             && LocalTime.now().isBefore(t.getRunningTimes( /* TODO */).get(t.getRunningTimes( /* TODO */).size() - 1))
                                                             && LocalTime.now().isAfter(t.getRunningTimes( /* TODO */).get(0)))
                                                .collect(Collectors.toList());
                List<Line> destinationLines = lineList.stream()
                                                      .filter(t -> !t.getStations().contains(i.getValue())
                                                              && t.getStations().contains(j.getValue())
                                                              && LocalTime.now().isBefore(t.getRunningTimes( /* TODO */).get(t.getRunningTimes( /* TODO */).size() - 1))
                                                              && LocalTime.now().isAfter(t.getRunningTimes( /* TODO */).get(0)))
                                                      .collect(Collectors.toList());
                for(Line s : startLines) {
                    for(Line d : destinationLines) {
                        for(Station ss : s.getStations()) {
                            for(Station ds : d.getStations()) {
                                if(!s.getStations().contains(ds) &&
                                        !d.getStations().contains(ss) &&
                                        SphericalUtil.computeDistanceBetween(ss.getLocation(), ds.getLocation()) <= MAX_WALKING_DISTANCE) {
                                    tempRoute = new Route();
                                    double duration0 = Math.abs(s.getTimetable().get(ss) - s.getTimetable().get(i.getValue()));

                                    double durations = SphericalUtil.computeDistanceBetween(start, i.getValue().getLocation()) / (1.4 * 60);
                                    tempRoute.addStep(publicTransportList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                            null,
                                            lineList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                            new Station("Baslangic", start),
                                            i.getValue(),
                                            durations,
                                            durations * 1
                                    );

                                    tempRoute.addStep(s.getPublicTransport(),
                                            null,
                                            s,
                                            i.getValue(),
                                            ss,
                                            duration0,
                                            duration0 * 1);
                                    double duration1 = SphericalUtil.computeDistanceBetween(ss.getLocation(), ds.getLocation()) / (1.4 * 60);
                                    tempRoute.addStep(publicTransportList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                            null,
                                            lineList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                            ss,
                                            ds,
                                            duration1,
                                            duration1 * 1
                                    );
                                    double duration2 = Math.abs(d.getTimetable().get(j.getValue()) - d.getTimetable().get(ds));
                                    tempRoute.addStep(d.getPublicTransport(),
                                            null,
                                            d,
                                            ds,
                                            j.getValue(),
                                            duration2,
                                            duration2 * 1);

                                    double durationw = SphericalUtil.computeDistanceBetween(j.getValue().getLocation(), destination) / (1.4 * 60);
                                    tempRoute.addStep(publicTransportList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                            null,
                                            lineList.stream().filter(t -> t.getID() == 2).findFirst().get(),
                                            j.getValue(),
                                            new Station("hedef", destination),
                                            durationw,
                                            durationw * 1
                                    );

                                    resultMap.put((durations * 1) + (duration0 * 1) + (duration1 * 1) + (duration2 * 1) + (durationw * 1), tempRoute);
                                    tempRoute = null;
                                }
                            }
                        }
                    }
                }
            }
        }
        // Case 4 end

        return resultMap;
    }

    static TreeMap<Double, Station> findClosestStations(LatLng location, ArrayList<Station> stationList) {
        TreeMap<Double, Station> result = new TreeMap<>();
        for(Station i : stationList)
            result.put(SphericalUtil.computeDistanceBetween(location, i.getLocation()), i);
        return result;
    }
}
