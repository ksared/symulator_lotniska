package lotnisko;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class FlightEvent extends BasicSimEvent<Lotnisko, Object> {

    Samolot samolot;

    public FlightEvent(Lotnisko lotnisko, Samolot samolot, double delay) throws SimControlException {
        super(lotnisko, delay);
        this.samolot = samolot;
    }

    @Override
    protected void stateChange() throws SimControlException {
        getSimObj().flight(samolot);
    }

    @Override
    protected void onTermination() throws SimControlException {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onInterruption() throws SimControlException {
        // TODO Auto-generated method stub

    }
}
