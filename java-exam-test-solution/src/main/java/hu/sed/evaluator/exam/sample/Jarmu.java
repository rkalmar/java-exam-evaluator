package hu.sed.evaluator.exam.sample;

public abstract class Jarmu {
    private final int gyartasiEv;
    private int sebesseg;
    private double fogyasztas;

    public Jarmu(int gyartasiEv, int sebesseg, double fogyasztas) {
        if (gyartasiEv <= 1950) {
            this.gyartasiEv = 2000;
        } else {
            this.gyartasiEv = gyartasiEv;
        }
        if (sebesseg < 0) {
            this.sebesseg = 0;
        } else {
            this.sebesseg = sebesseg;
        }
        if (fogyasztas < 0) {
            this.fogyasztas = 0;
        } else {
            this.fogyasztas = fogyasztas;
        }
    }

    public static Jarmu minFogyasztas(Jarmu[] jarmuvek) {
        if (jarmuvek == null || jarmuvek.length == 0) {
            return null;
        }

        Jarmu min = jarmuvek[0];
        for (Jarmu j : jarmuvek) {
            if (j.gyartasiEv >= 2005 && j.fogyasztas < min.fogyasztas) {
                min = j;
            }
        }
        if (min.fogyasztas >= 10) {
            return null;
        }
        return min;
    }

    public boolean muszaki() {
        if (gyartasiEv >= 2000) {
            return true;
        } else {
            return gyartasiEv >= 1985 && fogyasztas < 8;
        }
    }

    public double szennyezes(int faktor) {
        if (faktor <= 1) {
            return sebesseg / fogyasztas;
        } else {
            return faktor * (sebesseg / fogyasztas);
        }
    }

    public abstract void gyorsit(int sebesseg) throws TulNagySebesseg;

    public abstract void lassit(int sebesseg);

    @Override
    public String toString() {
        return "A jarmu gyartasi eve " + gyartasiEv + ", sebessege " + sebesseg + " kilometer/ora, fogyasztasa pedig " + fogyasztas + " liter 100 kilometeren.";
    }

    public int getGyartasiEv() {
        return gyartasiEv;
    }

    public int getSebesseg() {
        return sebesseg;
    }

    public void setSebesseg(int sebesseg) {
        if (sebesseg < 0) {
            throw new IllegalArgumentException("A sebesseg nem lehet negativ!");
        } else {
            this.sebesseg = sebesseg;

        }
    }

    public double getFogyasztas() {
        return fogyasztas;
    }

    public void setFogyasztas(double fogyasztas) {
        this.fogyasztas = fogyasztas;
        if (fogyasztas < 0) {
            this.fogyasztas = 0;
        }
    }
}
