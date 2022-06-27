package lotnisko;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class ZakonczObsl extends BasicSimEvent<Gate, Object> {

    Passenger passenger;

    public ZakonczObsl(Gate gate, double delay, Passenger passenger) throws SimControlException {
        super(gate, delay);
        this.passenger = passenger;
    }

    @Override
    protected void stateChange() throws SimControlException {
        getSimObj().kolejka.remove(passenger);
        getSimObj().mvDlugosc.setValue(getSimObj().kolejka.size()); 
        getSimObj().mvCzasZakonczenia.setValue(simTime() - passenger.czasPrzylotu);
        if(getSimObj().kolejka.size() > 0) {
            new RozpocznijObsl(getSimObj());
        } else {
            getSimObj().free = true;
        }
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}