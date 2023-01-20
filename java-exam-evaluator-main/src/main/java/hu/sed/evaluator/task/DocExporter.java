package hu.sed.evaluator.task;

import hu.sed.evaluator.task.argument.TaskArgument;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocExporter implements Task<Void, TaskArgument> {

    @Override
    public Void execute(TaskArgument argument) {
        log.debug("Execute Uml generator");
        return null;
    }
}
