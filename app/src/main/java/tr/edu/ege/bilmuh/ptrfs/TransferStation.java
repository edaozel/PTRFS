package tr.edu.ege.bilmuh.ptrfs;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransferStation extends Station  implements Serializable {
    private List<PublicTransport> publicTransports;

    private List<Line> lines;

    public TransferStation(int ID, String name, LatLng location) {
        super(ID, name, location);
        this.setPublicTransports(new ArrayList<PublicTransport>());
        this.setLines(new ArrayList<Line>());
    }

    public List<PublicTransport> getPublicTransports() {
        return publicTransports;
    }

    private void setPublicTransports(List<PublicTransport> publicTransports) {
        this.publicTransports = publicTransports;
    }

    public void addPublicTransport(PublicTransport publicTransport) {
        if(!this.publicTransports.contains(publicTransport))
            this.publicTransports.add(publicTransport);
    }

    private void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public void addLine(Line line) {
        if(!this.lines.contains(line))
            this.lines.add(line);
    }
}
