package hu.sed.evaluator.exam.y2020.zh2.task8.mysolution;

import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.annotation.syntax.SkipCheck;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.exam.y2020.zh2.task8.mytests.Tests;
import hu.sed.evaluator.exam.y2020.zh2.task8.mytests.Tests2;

@CustomTest(testClass = Tests2.class, method = {"myThirdTest", "myFourthTest"}, maxPoint = 5)
@TypeCheck(checkMethods = true,
        checkFields = true,
        maxPoint = 15)
@CustomTest(testClass = Tests2.class, method = {"xx", "yy"}, maxPoint = 8)
public class Auto extends Jarmu {

    @FieldCheck
    private final String marka;

    @FieldCheck(maxPoint = 2)
    private boolean muszakis;

    @SkipCheck
    @CustomTest(description = "Check Akarmi", testClass = Tests2.class, method = {"xx", "yy"}, maxPoint = 8)
    private String whatever;


    public Auto(int gyartasiEv, int sebesseg, double fogyasztas, String marka) {
        super(gyartasiEv, sebesseg, fogyasztas);
        this.muszakis = false;
        this.marka = marka;
    }

    @MethodCheck
    @Override
    public boolean muszaki() {
        if (!muszakis) {
            this.muszakis = super.muszaki();
            return super.muszaki();
        }
        return true;
    }

    @CustomTest(testClass = Tests.class, method = {"myFirstTest", "mySecondTest"}, maxPoint = 5)
    @CustomTest(testClass = Tests2.class, method = {"myThirdTest", "myFourthTest"}, maxPoint = 8)
    @Override
    public void gyorsit(int sebesseg) throws TulNagySebesseg {
        if (!muszakis && getSebesseg() + sebesseg > 50) {
            throw new TulNagySebesseg(getSebesseg() + sebesseg);
        } else if (muszakis && getSebesseg() + sebesseg > 120) {
            throw new TulNagySebesseg(getSebesseg() + sebesseg);
        } else {
            setSebesseg(getSebesseg() + sebesseg);
        }
    }

    @Override
    public void lassit(int sebesseg) {
        if (getSebesseg() - sebesseg >= 0) {
            setSebesseg(getSebesseg() - sebesseg);
        } else {
            setSebesseg(0);
        }
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

    @SkipCheck
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
