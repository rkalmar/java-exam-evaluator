package hu.sed.evaluator;

import hu.sed.evaluator.args.ArgumentsUtil;
import hu.sed.evaluator.args.InvalidArgumentException;
import hu.sed.evaluator.args.MissingArgumentsException;
import hu.sed.evaluator.task.executor.TaskArgument;
import lombok.extern.slf4j.Slf4j;


/**
 * @author rkalmar
 */
@Slf4j // TODO add config file for logger
public class Main {

    public static void main(String[] args) {
        TaskArgument taskArgument;
        try {
            taskArgument = ArgumentsUtil.parseArguments(args);
        } catch (MissingArgumentsException | IllegalArgumentException | InvalidArgumentException e) {
            log.error("Argument error: {}", e.getMessage(), e);
            return;
        }

        log.error("Task arguments: {}", taskArgument);
    }
}
