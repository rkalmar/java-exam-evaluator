package hu.sed.evaluator.task;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class ExamValidator implements Task {

    @Override
    public void execute(TaskArgument argument) {
        log.debug("Execute validator");
    }
}