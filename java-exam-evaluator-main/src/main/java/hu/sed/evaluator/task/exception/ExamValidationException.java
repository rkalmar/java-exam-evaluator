package hu.sed.evaluator.task.exception;

public class ExamValidationException extends RuntimeException {

    public ExamValidationException(String message) {
        super(message);
    }

    public ExamValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
