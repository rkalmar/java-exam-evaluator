package hu.sed.evaluator.task;

import hu.sed.evaluator.task.argument.TaskArgument;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UmlGenerator implements Task {

    @Override
    public void execute(TaskArgument argument) {
        log.debug("Execute Uml generator");
    }
}
