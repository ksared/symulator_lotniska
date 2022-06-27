package lotnisko;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class ArrivalEvent extends BasicSimEvent<Lotnisko, Object> {

    public ArrivalEvent(Lotnisko lotnisko, double delay) throws SimControlException {
        super(lotnisko, delay);
    }

    @Override
    protected void stateChange() throws SimControlException {
        getSimObj().arrival();
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
