package smo;
/**
 * @author Dariusz Pierzchala
 * 
 * Description: Aktywno�� gniazda obs�ugi. Realizuje obs�ug� przez losowy czas obiekt�w - zg�osze�.
 */

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class ZakonczObslugeBis extends BasicSimEvent<SmoBis, Zgloszenie>
{
    private SmoBis smoParent;

    public ZakonczObslugeBis(SmoBis parent, double delay, Zgloszenie zgl) throws SimControlException
    {
    	super(parent, delay, zgl);
        this.smoParent = parent;
    }

	@Override
	protected void onInterruption() throws SimControlException {
        System.out.println(simTime()+": !Przerwanie obs�ugi przy zgl. nr: " + transitionParams.getTenNr());			
	}

	@Override
	protected void onTermination() throws SimControlException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stateChange() throws SimControlException {
		// Odblokuj gniazdo
		smoParent.setWolne(true);
        System.out.println(simTime()+": SMO2-Koniec obs�ugi zgl. nr: " + transitionParams.getTenNr());
		// Zaplanuj dalsza obs�uge
        if (smoParent.liczbaZgl() > 0)
        {
        	smoParent.rozpocznijObsluge = new RozpocznijObslugeBis(smoParent);        	
        }		
	}
}