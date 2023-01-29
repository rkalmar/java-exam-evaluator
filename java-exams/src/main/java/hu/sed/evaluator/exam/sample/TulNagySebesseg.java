package hu.sed.evaluator.exam.sample;

import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import test.hu.sed.evaluator.exam.sample.SampleAutoTests;

@TypeCheck(checkMethods = true)
@CustomTest(testClass = SampleAutoTests.class, method = "checkDefaultConstructor",
        description = "Check if there is no default constructor defined explicitly.")
public class TulNagySebesseg extends Exception {

    public TulNagySebesseg(int sebesseg) {
        super("Tullepted a megengedett sebesseghatart, " + sebesseg + " kilometer/oraval haladsz!");
    }
}
