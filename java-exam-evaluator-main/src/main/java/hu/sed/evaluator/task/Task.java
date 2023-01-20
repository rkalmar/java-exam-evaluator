package hu.sed.evaluator.task;


public interface Task<R, T> {

    R execute(T argument);
}
