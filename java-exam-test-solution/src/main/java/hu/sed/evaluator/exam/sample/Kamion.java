package hu.sed.evaluator.exam.sample;

public class Kamion {

    private final int gyartasiEv;
    private int sebesseg;
    private double fogyasztas;
    private final int loero;
    private double rakomany;
    private double osszTomeg;

    public Kamion(int gyartasiEv, double rakomany, double osszTomeg, int loero) {
        this.gyartasiEv = gyartasiEv;
        this.rakomany = rakomany;
        this.osszTomeg = osszTomeg;
        this.loero = loero;

        if (rakomany < 0) {
            this.rakomany = 0;
        }
        if (osszTomeg < 0) {
            this.osszTomeg = 0;
        }

    }

    public boolean muszaki() {
        if (loero > 350) {
            return true;
        } else {
            return false;
        }
    }

    public final double szennyezes(int faktorParam) {
        int faktor = 2 * faktorParam;
        if (rakomany > 10000) {
            if (faktor <= 1) {
                return sebesseg / fogyasztas;
            } else {
                return faktor * (sebesseg / fogyasztas);
            }
        }
        if (faktorParam <= 1) {
            return sebesseg / fogyasztas;
        } else {
            return faktorParam * (sebesseg / fogyasztas);
        }
    }

    public void gyorsit(int sebesseg) throws TulNagySebesseg {
        int ujSebesseg = this.sebesseg + sebesseg;
        if (rakomany > osszTomeg / 4) {
            throw new TulNagySebesseg(ujSebesseg);
        } else {
            this.sebesseg = ujSebesseg;
        }
    }

    public int getGyartasiEv() {
        return gyartasiEv;
    }

    public int getSebesseg() {
        return sebesseg;
    }

    public double getFogyasztas() {
        return fogyasztas;
    }

    public int getLoero() {
        return loero;
    }

    public double getRakomany() {
        return rakomany;
    }

    public double getOsszTomeg() {
        return osszTomeg;
    }
}
