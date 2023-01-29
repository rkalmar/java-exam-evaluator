package hu.sed.evaluator.exam.sample;

import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import test.hu.sed.evaluator.exam.sample.SampleTulNagySebessegTests;

@TypeCheck(checkMethods = true)
@CustomTest(testClass = SampleTulNagySebessegTests.class, score = 2,
        description = "Check if there is no default constructor defined explicitly.")
public class TulNagySebesseg extends Exception {

    public TulNagySebesseg(int sebesseg) {
        super("Tullepted a megengedett sebesseghatart, " + sebesseg + " kilometer/oraval haladsz!");
    }
}
