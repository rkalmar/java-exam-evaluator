package hu.sed.evaluator.exam.sample;

public class Auto extends Jarmu {

    private final String marka;

    private boolean muszakis;

    public Auto(int gyartasiEv, int sebesseg, double fogyasztas, String marka) {
        super(gyartasiEv, sebesseg, fogyasztas);
        this.muszakis = false;
        this.marka = marka;
    }

    @Override
    public boolean muszaki() {
        if (!muszakis) {
            this.muszakis = super.muszaki();
            return super.muszaki();
        }
        return true;
    }

    @Override
    public void gyorsit(int sebesseg) throws TulNagySebesseg {
        if (!muszakis && getSebesseg() + sebesseg > 50) {
            throw new TulNagySebesseg(getSebesseg() + sebesseg);
        } else if (muszakis && getSebesseg() + sebesseg > 130) {
            throw new TulNagySebesseg(getSebesseg() + sebesseg);
        } else {
            setSebesseg(getSebesseg() + sebesseg);
        }
    }

    @Override
    public void lassit(int sebesseg) {
        setSebesseg(getSebesseg() - sebesseg);
    }

    public void fogyasztastModosit() {
        if (getSebesseg() > 120) {
            setFogyasztas(getFogyasztas() * 1.2);
        } else if (getSebesseg() < 50) {
            setFogyasztas(getFogyasztas() * 0.7);
        }
    }

    public boolean isMuszakis() {
        return muszakis;
    }

    public boolean getMuszakis() {
        return muszakis;
    }

    public String getMarka() {
        return marka;
    }

    @Override
    public String toString() {
        return "Az auto markaja " + marka + ", gyartasi eve " + getGyartasiEv() + ", fogyasztasa " +
                getFogyasztas() + ", sebessege pedig " + getSebesseg() + " kilometer/ora es " +
                (muszakis ? "van muszakija" : "nincs muszakija") + ".";
    }
}
