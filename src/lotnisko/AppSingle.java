package lotnisko;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import dissimlab.monitors.Diagram;
import dissimlab.monitors.Diagram.DiagramType;
import dissimlab.monitors.Statistics;
import dissimlab.simcore.SimControlEvent;
import dissimlab.simcore.SimControlException;
import dissimlab.simcore.SimManager;
import dissimlab.simcore.SimParameters.SimControlStatus;

public class AppSingle {
	
	public static void main(String[] args) {
		try{
			SimManager simMgr = new SimManager();
			
			//Lotnisko lotnisko = new Lotnisko(2, 3, 4, 1, simMgr);
			Lotnisko lotnisko = new Lotnisko(6, 5, 2, 1, 1, simMgr);
			
			// Dwa sposoby planowanego końca symulacji
			//model.setEndSimTime(20);
			// lub
			SimControlEvent stopEvent = new SimControlEvent(50.0, SimControlStatus.STOPSIMULATION);

			// Badanie czasu trwania eksperymentu - początek
			long czst = new Date().getTime();
			// Uruchomienie symulacji metodą "start"
			simMgr.startSimulation();
			// Pomiar czasu trwania eksperymentu
			czst = new Date().getTime() - czst;
			
			Diagram d1 = new Diagram(DiagramType.TIME_FUNCTION, "R-inTheAir G-onTheGround B-runwayFree M-runway2Free");
			d1.add(lotnisko.mvInTheAir, java.awt.Color.RED, "Samoloty w powietrzu");
			d1.add(lotnisko.mvOnTheGround, java.awt.Color.GREEN, "Samoloty na ziemi");
			d1.add(lotnisko.mvRunwayFree, java.awt.Color.BLUE, "Zwolnienie pasa nr 1");
			d1.add(lotnisko.mvRunway2Free, Color.MAGENTA, "Zwolnienie pasa nr 2");
			d1.show();

			Diagram d2 = new Diagram(DiagramType.HISTOGRAM, "Histogram dla długości kolejki pełnej");
			d2.add(lotnisko.mvDKP, Color.RED);
			d2.show();

			Diagram d3 = new Diagram(DiagramType.HISTOGRAM, "Histogram dla długości kolejki uproszczonej");
			d3.add(lotnisko.mvDKU, Color.RED);
			d3.show();

			Diagram d4 = new Diagram(DiagramType.TIME_FUNCTION, "Długość kolejek w czasie R-Pełna B-Uproszczona");
			d4.add(lotnisko.mvDKP, Color.RED, "Kolejka pełna");
			d4.add(lotnisko.mvDKU, Color.BLUE, "Kolejka uproszczona");
			d4.show();

			double gamma = 0.05;
			double wynik;
			wynik = Statistics.min(lotnisko.mvTOP);
			System.out.println("Wartość minimalna czasu oczekiwania na obsługę pęłną: " + wynik);
			wynik = Statistics.min(lotnisko.mvTOU);
			System.out.println("Wartość minimalna czasu oczekiwania na obsługę uproszczoną: " + wynik);
			wynik = Statistics.min(lotnisko.mvTKP);
			System.out.println("Wartość minimalna czasu do zakończenia obsługi pełnej: " + wynik);
			wynik = Statistics.min(lotnisko.mvTKU);
			System.out.println("Wartość minimalna czasu do zakończenia obsługi uproszczonej: " + wynik);
			wynik = Statistics.max(lotnisko.mvTOP);
			System.out.println("Wartość maksymalna czasu oczekiwania na obsługę pęłną: " + wynik);
			wynik = Statistics.max(lotnisko.mvTOU);
			System.out.println("Wartość maksymalna czasu oczekiwania na obsługę uproszczoną: " + wynik);
			wynik = Statistics.max(lotnisko.mvTKP);
			System.out.println("Wartość maksymalna czasu do zakończenia obsługi pełnej: " + wynik);
			wynik = Statistics.max(lotnisko.mvTKU);
			System.out.println("Wartość maksymalna czasu do zakończenia obsługi uproszczonej: " + wynik);
			wynik = Statistics.arithmeticMean(lotnisko.mvTOP);
			System.out.println("Wartość średnia czasu oczekiwania na obsługę pełną: " + wynik);
			wynik = Statistics.arithmeticMean(lotnisko.mvTOU);
			System.out.println("Wartość średnia czasu oczekiwania na obsługę uproszczoną: " + wynik);
			wynik = Statistics.arithmeticMean(lotnisko.mvTKP);
			System.out.println("Wartość średnia czasu do zakończenia obsługi pełnej: " + wynik);
			wynik = Statistics.arithmeticMean(lotnisko.mvTKU);
			System.out.println("Wartość średnia czasu do zakończenia obsługi uproszczonej: " + wynik);
			wynik = Statistics.variance(lotnisko.mvTOP);
			System.out.println("Wartość wariancji czasu oczekiwania na obsługę pełną: " + wynik);
			wynik = Statistics.variance(lotnisko.mvTOU);
			System.out.println("Wartość wariancji czasu oczekiwania na obsługę uproszczoną: " + wynik);
			wynik = Statistics.variance(lotnisko.mvTKP);
			System.out.println("Wartość wariancji czasu do zakończenia obsługi pełnej: " + wynik);
			wynik = Statistics.variance(lotnisko.mvTKU);
			System.out.println("Wartość wariancji czasu do zakończenia obsługi uproszczonej: " + wynik);
			double[] wynik2;
			wynik2 = Statistics.intervalEstimationOfEX(lotnisko.mvTOP, gamma);
			System.out.println("przedział ufności czasu oczekiwania na obsługę pełną: " + Arrays.toString(wynik2));
			wynik2 = Statistics.intervalEstimationOfEX(lotnisko.mvTOU, gamma);
			System.out.println("przedział ufności czasu oczekiwania na obsługę uproszczoną: " + Arrays.toString(wynik2));
			wynik2 = Statistics.intervalEstimationOfEX(lotnisko.mvTKP, gamma);
			System.out.println("przedział ufności czasu do zakończenia obsługi pełnej: " + Arrays.toString(wynik2));
			wynik2 = Statistics.intervalEstimationOfEX(lotnisko.mvTKU, gamma);
			System.out.println("przedział ufności czasu do zakończenia obsługi uproszczonej: " + Arrays.toString(wynik2));





			
		}
		catch (SimControlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
