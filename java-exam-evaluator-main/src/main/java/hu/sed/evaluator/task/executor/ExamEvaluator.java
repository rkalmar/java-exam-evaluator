package hu.sed.evaluator.task.executor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExamEvaluator implements Task {

    @Override
    public void execute(TaskArgument argument) {
        log.debug("Execute evaluator");
    }
}
