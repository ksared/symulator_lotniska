package lotnisko;

import dissimlab.random.SimGenerator;
import dissimlab.simcore.BasicSimObj;

public class Samolot extends BasicSimObj {
    public PlaneType planeType;
    public Status status;

    public int a, b, c, d;
    public int liczbaOsobP;
    public int liczbaOsobU;

    SimGenerator sg = new SimGenerator();

    public Samolot() {
        status = Status.IN_THE_AIR;
        randPlaneType();
        liczbaOsobP = (int) sg.uniform(a, b);
        liczbaOsobU = (int) sg.uniform(c, d);
    }

    private void randPlaneType() {
        int tmp = sg.nextInt(3);
        switch(tmp) {
            case 0:
                this.a = 10;
                this.b = 30;
                this.c = 12;
                this.d = 40;
                planeType = PlaneType.PLANE1;
                return;
            case 1:
                this.a = 5;
                this.b = 15;
                this.c = 10;
                this.d = 20;
                planeType = PlaneType.PLANE2;
                return;
            case 2:
                this.a = 4;
                this.b = 8;
                this.c = 3;
                this.d = 12;
                planeType = PlaneType.PLANE3;
                return;
        }
    }

    public enum PlaneType {
        PLANE1,
        PLANE2,
        PLANE3
    }

    public enum Status {
        IN_THE_AIR,
        LANDING,
        ON_THE_GROUND,
        DEPARTURE,
        FLIGHT,
    }
}
