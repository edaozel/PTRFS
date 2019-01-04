package tr.edu.ege.bilmuh.ptrfs;

import java.io.Serializable;
import java.util.List;

public class PublicTransport implements Serializable {
    private int ID;
    private String name;
    private List<Line> lines;

    public PublicTransport() {
        setID(-1);
        setLines(null);
        setName(null);
    }

    public PublicTransport(String name, List<Line> lines, int ID) {
        setID(ID);
        setName(name);
        setLines(lines);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}