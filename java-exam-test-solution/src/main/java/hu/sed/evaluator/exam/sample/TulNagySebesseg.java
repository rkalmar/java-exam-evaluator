package hu.sed.evaluator.exam.sample;

public class TulNagySebesseg extends Exception {

    public TulNagySebesseg() {
    }

    public TulNagySebesseg(int sebesseg) {
        super("Tullepted a megengedett sebesseghatart, " + sebesseg + " kilometer/oraval haladsz!");
    }
}
