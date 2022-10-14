package hu.sed.evaluator.exam.y2020.zh2.task8.mysolution;

public class TulNagySebesseg extends Exception {

    public TulNagySebesseg(int sebesseg) {
        super("Tullepted a megengedett sebesseghatart, " + sebesseg + " kilometer/oraval haladsz!");
    }
}
