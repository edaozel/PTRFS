package tr.edu.ege.bilmuh.ptrfs;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

public class Line implements Serializable {
    private int ID;
    private String name;
    private List<Station> stations;
    private LinkedHashMap<Station, Integer> timetable;
    private List<LocalTime> runningTimes;
    private PublicTransport publicTransport;

    public Line() {
        setID(-1);
        setName(null);
        setStations(null);
        setRunningTimes(null);
        setRunningTimes(null);
        setTimetable(null);
        setPublicTransport(null);
    }

    public Line(int ID, String name, List<Station> stations, LinkedHashMap<Station, Integer> timetable, PublicTransport publicTransport) {
        setID(ID);
        setName(name);
        setStations(stations);
        setTimetable(timetable);
        setPublicTransport(publicTransport);
    }

    public int getNearestTime(LocalTime now) {
        if(this.getRunningTimes().size() == 3) {
            return now.getMinute() % this.getRunningTimes().get(1).getMinute();
        } else {
            LocalTime tempTime = this.getRunningTimes().stream()
                                                             .filter(t -> now.isBefore(t))
                                                             .findFirst().get();
            return (int) MINUTES.between(now, tempTime);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = (List<Station>) ((ArrayList<Station>) stations).clone();
    }

    public LinkedHashMap<Station, Integer> getTimetable() {
        return timetable;
    }

    public void setTimetable(LinkedHashMap<Station, Integer> timetable) {
        this.timetable = timetable;
    }

    public List<LocalTime> getRunningTimes() {
        return runningTimes;
    }

    public void setRunningTimes(List<LocalTime> runningTimes) {
        this.runningTimes = runningTimes;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public PublicTransport getPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(PublicTransport publicTransport) {
        this.publicTransport = publicTransport;
    }

    public boolean containsStations(Station station1, Station station2) {
        return stations.contains(station1) && stations.contains(station2);
    }
}