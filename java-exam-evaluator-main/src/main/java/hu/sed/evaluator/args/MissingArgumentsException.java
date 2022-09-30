package hu.sed.evaluator.args;

public class MissingArgumentsException extends Exception {

    public MissingArgumentsException(String message) {
        super(message);
    }

    public MissingArgumentsException(Throwable cause) {
        super(cause);
    }
}
