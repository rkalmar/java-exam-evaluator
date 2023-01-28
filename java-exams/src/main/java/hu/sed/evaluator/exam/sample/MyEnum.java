package hu.sed.evaluator.exam.sample;

import hu.sed.evaluator.annotation.syntax.TypeCheck;

@TypeCheck(checkFields = true, checkMethods = true)
public enum MyEnum implements Interface {
    WHITE,
    BLACK
}
