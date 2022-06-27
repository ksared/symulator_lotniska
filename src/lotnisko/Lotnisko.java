package lotnisko;

import dissimlab.monitors.MonitoredVar;
import dissimlab.monitors.Statistics;
import dissimlab.random.SimGenerator;
import dissimlab.simcore.BasicSimObj;
import dissimlab.simcore.SimControlException;
import dissimlab.simcore.SimManager;

import java.util.LinkedList;
import java.util.List;

public class Lotnisko extends BasicSimObj {
	List<Samolot> samoloty = new LinkedList<>();
	int inTheAir; // liczba samolotów w powietrzu
	int onTheGround; // liczba samolotów czekających na lotnisku
	boolean runwayFree; // dostępność pasa lądowania
	boolean runway2Free;
	int arrivalInterval; // okres pomiedzy kolejnymi przylotami
	int landingDuration; // czas trwania lądowania
	int departureInterval; // okres pomiędzy odlotami
	int flightInterval;
	PassengerQueue kolejkaPelna = new PassengerQueue();
	PassengerQueue kolejkaUpr = new PassengerQueue();
	Gate bramkaPelna;
	Gate bramkaUpr;

	MonitoredVar mvOnTheGround, mvInTheAir, mvRunwayFree, mvRunway2Free;
	MonitoredVar mvTOP, mvTOU, mvTKP, mvTKU, mvDKP, mvDKU;

	SimGenerator sg = new SimGenerator();

	public Lotnisko(int arrivalInterval, int landingDuration, int departureInterval, int flightInterval, int period, SimManager simMgr)
			throws SimControlException {
		this.inTheAir = 0;
		this.onTheGround = 0;
		this.runwayFree = true;
		this.runway2Free = true;
		this.arrivalInterval = arrivalInterval;
		this.landingDuration = landingDuration;
		this.departureInterval = departureInterval;
		this.flightInterval = flightInterval;
		new ArrivalEvent(this, 1.0);
		mvOnTheGround = new MonitoredVar(simMgr);
		mvInTheAir = new MonitoredVar(simMgr);
		mvRunwayFree = new MonitoredVar(simMgr);
		mvRunway2Free = new MonitoredVar(simMgr);
		mvRunwayFree.setValue(1);
		mvRunway2Free.setValue(1);
		mvInTheAir.setValue(0);
		mvOnTheGround.setValue(0);
		mvTOP = new MonitoredVar(simMgr);
		mvTOU = new MonitoredVar(simMgr);
		mvTKP = new MonitoredVar(simMgr);
		mvTKU = new MonitoredVar(simMgr);
		mvDKP = new MonitoredVar(simMgr);
		mvDKU = new MonitoredVar(simMgr);
		bramkaPelna = new Gate(kolejkaPelna.kolejka, mvDKP, mvTOP, mvTKP, 2);
		bramkaUpr = new Gate(kolejkaUpr.kolejka, mvDKU, mvTOU, mvTKU, 1);
	}

	public void arrival() throws SimControlException {
		Samolot newPlane = new Samolot();
		samoloty.add(newPlane);
		inTheAir++;
		mvInTheAir.setValue(inTheAir);
		System.out.println(
				simTime() + " - Przybył samolot. Nad lotniskiem aktualnie jest: " + inTheAir + " samolot(ów)");

		double arrivalTime = sg.exponential(arrivalInterval);
		new ArrivalEvent(this, arrivalTime);

		for(Samolot samolot : samoloty) {
			if(runwayFree && samolot.status == Samolot.Status.IN_THE_AIR) {
				runwayFree = false;
				mvRunwayFree.setValue(0);
				samolot.status = Samolot.Status.LANDING;
				double landingTime = 0.0;
				switch(samolot.planeType) {
					case PLANE1:
						landingTime = landingDuration;
						break;
					case PLANE2:
						landingTime = sg.chisquare(landingDuration);
						break;
					case PLANE3:
						landingTime = sg.geometric(0.5);
						break;
				}
				new LandingEvent(this, samolot, landingTime);
				System.out.println(simTime() + " - Zaplanowano lądowanie");
				break;
			}
		}
	}

	public void landing(Samolot samolot) throws SimControlException {
		inTheAir--;
		mvInTheAir.setValue(inTheAir);
		onTheGround++;
		mvOnTheGround.setValue(onTheGround);
		System.out.println(simTime() + " - Wylądował samolot. Na płycie altualnie jest: " + onTheGround
				+ " a w powietrzu " + inTheAir + " samolot(ów)");
		samolot.status = Samolot.Status.ON_THE_GROUND;

		gettingOff(samolot);

		double departureTime = departureInterval;
		new DepartureEvent(this, samolot, departureTime);

		if (inTheAir > 0) {
			for(Samolot samolot2 : samoloty) {
				if(samolot2.status == Samolot.Status.IN_THE_AIR) {
					samolot2.status = Samolot.Status.LANDING;
					double landingTime = 0.0;
					switch(samolot.planeType) {
						case PLANE1:
							landingTime = landingDuration;
							break;
						case PLANE2:
							landingTime = sg.chisquare(landingDuration);
							break;
						case PLANE3:
							landingTime = sg.geometric(0.5);
							break;
					}
					new LandingEvent(this, samolot2, landingTime);
					System.out.println(simTime() + " - Zaplanowano lądowanie");
					break;
				}
			}
		} else {
			runwayFree = true;
			mvRunwayFree.setValue(1);
			System.out.println(simTime() + " - Zwolniono pas lądowania");
		}
	}

	public void gettingOff(Samolot samolot) throws SimControlException {
		Passenger passenger;
		for (int i = 1; i <= samolot.liczbaOsobP; i++) {
			passenger = new Passenger();
			kolejkaPelna.kolejka.add(passenger);
			if (bramkaPelna.free) {
				new RozpocznijObsl(bramkaPelna);
			}
		}
		mvDKP.setValue(kolejkaPelna.kolejka.size());
		for (int i = 1; i <= samolot.liczbaOsobU; i++) {
			passenger = new Passenger();
			kolejkaUpr.kolejka.add(passenger);
			if (bramkaUpr.free) {
				new RozpocznijObsl(bramkaUpr);
			}
		}
		mvDKU.setValue(kolejkaUpr.kolejka.size());
	}

	public void departure(Samolot samolot) throws SimControlException {
		samolot.status = Samolot.Status.DEPARTURE;
		if(runway2Free) {
			runway2Free = false;
			mvRunway2Free.setValue(0);
			System.out.println(
					simTime() + " - Samolot zajął pas do odlotów. Na płycie altualnie jest: " + onTheGround + " samolot(ów)");
			samolot.status = Samolot.Status.FLIGHT;

			double flightTime = flightInterval;
			new FlightEvent(this, samolot, flightTime);
		}
	}

	public void flight(Samolot samolot) throws SimControlException {
		onTheGround--;
		mvOnTheGround.setValue(onTheGround);
		samoloty.remove(samolot);
		if (onTheGround > 0) {
			boolean noneFound = true;
			for (Samolot samolot2 : samoloty) {
				if (samolot2.status == Samolot.Status.DEPARTURE) {
					noneFound = false;
					System.out.println(
							simTime() + " - Samolot zajął pas do odlotów. Na płycie altualnie jest: " + onTheGround + " samolot(ów)");
					samolot2.status = Samolot.Status.FLIGHT;

					double flightTime = flightInterval;
					new FlightEvent(this, samolot2, flightTime);
					break;
				}
			}
			if (noneFound) {
				runway2Free = true;
				mvRunway2Free.setValue(1);
				System.out.println(simTime() + " - Zwolniono pas odlotów");
			}
		} else {
			runway2Free = true;
			mvRunway2Free.setValue(1);
			System.out.println(simTime() + " - Zwolniono pas odlotów");
		}
	}
}
