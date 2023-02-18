package test.hu.sed.evaluator.exception;

public class AssertionException extends RuntimeException {

    public AssertionException(String message) {
        super(message);
    }

    public AssertionException(AssertionError assertionError) {
        super(assertionError.getMessage());
    }
}
