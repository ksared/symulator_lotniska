package smo;
/**
 * @author Dariusz Pierzchala
 * 
 * Description: Aktywno�� gniazda obs�ugi. Realizuje obs�ug� przez losowy czas obiekt�w - zg�osze�.
 */

import dissimlab.random.SimGenerator;
import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class RozpocznijObslugeBis extends BasicSimEvent<SmoBis, Zgloszenie>
{
    private SmoBis smoParent;
    private SimGenerator generator;

    public RozpocznijObslugeBis(SmoBis parent, double delay) throws SimControlException
    {
    	super(parent, delay);
    	generator = new SimGenerator();
        this.smoParent = parent;
    }

    public RozpocznijObslugeBis(SmoBis parent) throws SimControlException
    {
    	super(parent);
    	generator = new SimGenerator();
        this.smoParent = parent;
    }
    
	@Override
	protected void onInterruption() throws SimControlException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTermination() throws SimControlException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stateChange() throws SimControlException {
        if (smoParent.liczbaZgl() > 0)
        {
        	// Zablokuj gniazdo
        	smoParent.setWolne(false);
        	// Pobierz zg�oszenie
        	Zgloszenie zgl = smoParent.usun();
        	//Otwarcie semafora blokuj�cego gniazdo 1-sze     	
        	if (smoParent.liczbaZgl() == smoParent.getMaxDlKolejki() - 1) {
        		try {
                    System.out.println(simTime()+": SMO2- otwarcie semafora - zwolnienie: " + smoParent.getSemafor().readFirstBlocked().toString());					
				} catch (Exception e) {
				}
        		smoParent.getSemafor().open();
        	}
        	// Wygeneruj czas obs�ugi
        	double czasObslugi = generator.normal(10.0, 1.0);
            // Zapami�taj dane monitorowane
            smoParent.MVczasy_obslugi.setValue(czasObslugi);
            smoParent.MVczasy_oczekiwania.setValue(simTime()
                    - zgl.getCzasOdniesienia());
            System.out.println(simTime()+": SMO2-Pocz�tek obs�ugi zgl. nr: " + zgl.getTenNr());
        	// Zaplanuj koniec obs�ugi
        	smoParent.zakonczObsluge = new ZakonczObslugeBis(smoParent, czasObslugi, zgl);        	
        }
		
	}
}