package hu.sed.evaluator.exam.sample;

import hu.sed.evaluator.annotation.syntax.TypeCheck;

@TypeCheck
public class TulNagySebesseg extends Exception {

    public TulNagySebesseg(int sebesseg) {
        super("Tullepted a megengedett sebesseghatart, " + sebesseg + " kilometer/oraval haladsz!");
    }
}
