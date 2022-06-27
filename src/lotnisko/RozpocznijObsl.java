package lotnisko;

import dissimlab.random.SimGenerator;
import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class RozpocznijObsl extends BasicSimEvent<Gate, Object> {

    SimGenerator sg = new SimGenerator();

    public RozpocznijObsl(Gate gate) throws SimControlException {
        super(gate);
    }

    @Override
    protected void stateChange() throws SimControlException {
        Passenger passenger = getSimObj().kolejka.peek();
        getSimObj().mvCzasOczekiwania.setValue(simTime() - passenger.czasPrzylotu);
        getSimObj().free = false;
        double endingTime = sg.exponential(getSimObj().a);
        new ZakonczObsl(getSimObj(), endingTime, passenger);
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}