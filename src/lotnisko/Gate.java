package lotnisko;

import dissimlab.monitors.MonitoredVar;
import dissimlab.simcore.BasicSimObj;
import dissimlab.simcore.SimControlException;

import java.util.Queue;

public class Gate extends BasicSimObj {
    public boolean free = true;
    public Queue<Passenger> kolejka;
    public MonitoredVar mvDlugosc, mvCzasOczekiwania, mvCzasZakonczenia;
    public double a;

    public Gate(Queue<Passenger> queue, MonitoredVar mvDlugosc, MonitoredVar mvCzasOczekiwania, MonitoredVar mvCzasZakonczenia, double a) {
        this.kolejka = queue;
        this.mvDlugosc = mvDlugosc;
        this.mvCzasOczekiwania = mvCzasOczekiwania;
        this.mvCzasZakonczenia = mvCzasZakonczenia;
        this.a = a;
    }
}
