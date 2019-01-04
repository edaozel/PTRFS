package tr.edu.ege.bilmuh.ptrfs;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Route implements Serializable {
    private List<Step> stepList;

    public Route() {
        this.stepList = new ArrayList<>();
    }

    public void addStep(PublicTransport usedPT,
                        PublicTransport secondPT,
                        Line usedLine,
                        Station fromStation,
                        Station toStation,
                        double totalDuration,
                        double totalUnpleasantness) {
        this.stepList.add(new Step(usedPT, secondPT, usedLine, fromStation, toStation, Math.round(totalDuration), totalUnpleasantness));
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public double getTotalDuration() {
        double output = 0;
        for(Step i : getStepList()) {
            output += i.totalDuration;
        }
        return Math.round(output);
    }

    public String printAllSteps() {
        StringBuilder output = new StringBuilder();
        for(Step i : getStepList()) {
            if (i.getSecondPT() != null) {
                output.append("Aktarma");
            } else {
                output.append(i.getUsedPT().getName());
            }
            output.append(" → ");
        }

        return output.toString().substring(0, output.toString().length() - 3);
    }

    public List<String> printDetailedSteps(List<Station> stationList) {
        ArrayList<String> stepTextList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(Step i : getStepList()) {
            if(i.getUsedPT().getID() == 2) {
                if (i.fromStation == null && i.toStation == null) {
                    sb.append("Baslangic noktanizdan hedef noktaniza yuruyun. Tahmini yuruyus suresi ");
                    sb.append((int) i.totalDuration).append(" dakika.");
                } else if (stationList.stream().anyMatch(t -> t.getID() == i.getFromStation().getID())
                        && !stationList.stream().anyMatch(t -> t.getID() == i.getToStation().getID())) {
                    sb.append("→ ");
                    sb.append(i.fromStation.getName()).append(" istasyonundan ");
                    sb.append(i.toStation.getName()).append(" noktaniza yuruyun. ");
                    sb.append("Tahmini yuruyus suresi ").append((int) i.totalDuration).append(" dakika.");
                } else if (!stationList.stream().anyMatch(t -> t.getID() == i.getFromStation().getID())
                        && stationList.stream().anyMatch(t -> t.getID() == i.getToStation().getID())) {
                    sb.append("→ ");
                    sb.append(i.fromStation.getName()).append(" noktanizdan ");
                    sb.append(i.toStation.getName()).append(" istasyonuna yuruyun. ");
                    sb.append("Tahmini yuruyus suresi ").append((int) i.totalDuration).append(" dakika.");
                } else {
                    sb.append("→ ");
                    sb.append(i.fromStation.getName()).append(" istasyonundan ");
                    sb.append(i.toStation.getName()).append(" istasyonuna yuruyun. ");
                    sb.append("Tahmini yuruyus suresi ").append((int) i.totalDuration).append(" dakika.");
                }
            } else {
                if (i.getSecondPT() != null) {
                    sb.append("→ ");
                    sb.append(i.fromStation.getName()).append(" istasyonunda ");
                    sb.append(i.usedPT.getName()).append(" toplu tasima aracindan ");
                    sb.append(i.secondPT.getName()).append(" toplu tasima aracina aktarma yapin.");
                } else {
                    LocalTime tempTime = LocalTime.now().plusMinutes(i.getUsedLine().getNearestTime(LocalTime.now()));
                    sb.append("→ ").append(tempTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                    sb.append(" saatinde ").append(i.fromStation.getName()).append(" istasyonundan ");
                    sb.append(i.getUsedPT().getName()).append(" toplu tasima aracina binin. ");
                    sb.append(i.toStation.getName()).append(" istasyonunda inin. Yolculuk suresi ");
                    sb.append((int) i.totalDuration).append(" dakika.");
                }
            }
            stepTextList.add(sb.toString());
            sb.delete(0, sb.length());
        }
        return stepTextList;
    }

    public class Step implements Serializable {
        private PublicTransport usedPT;
        private PublicTransport secondPT;
        private Line usedLine;
        private Station fromStation;
        private Station toStation;
        private double totalDuration;
        private double totalUnpleasantness;

        public Step(PublicTransport usedPT, PublicTransport secondPT, Line usedLine, Station fromStation, Station toStation, double totalDuration, double totalUnpleasantness) {
            this.usedPT = usedPT;
            this.secondPT = secondPT;
            this.usedLine = usedLine;
            this.fromStation = fromStation;
            this.toStation = toStation;
            this.totalDuration = totalDuration;
            this.totalUnpleasantness = totalUnpleasantness;
        }

        public PublicTransport getUsedPT() {
            return usedPT;
        }

        public void setUsedPT(PublicTransport usedPT) {
            this.usedPT = usedPT;
        }

        public PublicTransport getSecondPT() {
            return secondPT;
        }

        public void setSecondPT(PublicTransport secondPT) {
            this.secondPT = secondPT;
        }

        public Line getUsedLine() {
            return usedLine;
        }

        public void setUsedLine(Line usedLine) {
            this.usedLine = usedLine;
        }

        public Station getFromStation() {
            return fromStation;
        }

        public void setFromStation(Station fromStation) {
            this.fromStation = fromStation;
        }

        public Station getToStation() {
            return toStation;
        }

        public void setToStation(Station toStation) {
            this.toStation = toStation;
        }

        public double getTotalDuration() {
            return totalDuration;
        }

        public void setTotalDuration(double totalDuration) {
            this.totalDuration = totalDuration;
        }

        public double getTotalUnpleasantness() {
            return totalUnpleasantness;
        }

        public void setTotalUnpleasantness(double totalUnpleasantness) {
            this.totalUnpleasantness = totalUnpleasantness;
        }
    }
}
