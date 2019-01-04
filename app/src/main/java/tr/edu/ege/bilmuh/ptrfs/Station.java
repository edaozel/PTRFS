package tr.edu.ege.bilmuh.ptrfs;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Station implements Serializable {
    private int ID;
    private String name;
    private transient LatLng location;

    public Station() {
        setID(-1);
        setLocation(null);
        setName(null);
    }

    public Station(int ID, String name, LatLng location) {
        setID(ID);
        setName(name);
        setLocation(location);
    }

    public Station(String name, LatLng location) {
        this();
        setName(name);
        setLocation(location);
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeDouble(location.latitude);
        out.writeDouble(location.longitude);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        location = new LatLng(in.readDouble(), in.readDouble());
    }
}