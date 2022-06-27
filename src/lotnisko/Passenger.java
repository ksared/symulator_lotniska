package lotnisko;

import dissimlab.simcore.BasicSimObj;

public class Passenger extends BasicSimObj {
    double czasPrzylotu;

    public Passenger() {
        czasPrzylotu = simTime();
    }
}
