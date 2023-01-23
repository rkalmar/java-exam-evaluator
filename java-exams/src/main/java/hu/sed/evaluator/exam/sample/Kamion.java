package hu.sed.evaluator.exam.sample;

public class Kamion extends Jarmu {
    private final int loero;
    private double rakomany;
    private double osszTomeg;

    public Kamion(int gyartasiEv, double rakomany, double osszTomeg, int loero) {
        super(gyartasiEv, 0, 0);
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

    @Override
    public boolean muszaki() {
        if (loero > 350) {
            return true;
        } else {
            return super.muszaki();
        }
    }

    @Override
    public final double szennyezes(int faktor) {
        if (rakomany > 10000) {
            return super.szennyezes(2 * faktor);
        }
        return super.szennyezes(faktor);
    }

    @Override
    public void gyorsit(int sebesseg) throws TulNagySebesseg {
        if (rakomany > osszTomeg / 4) {
            throw new TulNagySebesseg(getSebesseg() + sebesseg);
        } else {
            setSebesseg(getSebesseg() + sebesseg);
        }
    }

    @Override
    public void lassit(int sebesseg) {
        if (rakomany > osszTomeg / 4) {
            setSebesseg(getSebesseg() - sebesseg);
        } else {
            System.out.print("Nem kell lassitania a kamionnak.");
        }
    }

    @Override
    public String toString() {
        return "Ennek a kamionnak a gyartasi eve " + getGyartasiEv() + ", sebessege " + getSebesseg() + ", a rakomanya " + rakomany + " kg es " + loero + " loeros.";
    }

    public double getRakomany() {
        return rakomany;
    }

    public void setRakomany(double rakomany) {
        if (rakomany < 0) {
            this.rakomany = 0;
        } else {
            this.rakomany = rakomany;
        }
    }

    public double getOsszTomeg() {
        return osszTomeg;
    }

    public int getLoero() {
        return loero;
    }
}
