package hu.sed.evaluator;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import hu.sed.evaluator.task.ExamEvaluator;
import hu.sed.evaluator.task.ExamItemCollector;
import hu.sed.evaluator.task.ExamValidator;
import hu.sed.evaluator.task.Task;
import hu.sed.evaluator.task.TaskArgument;
import hu.sed.evaluator.task.TaskType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rkalmar
 */
@Slf4j // TODO add config file for logger
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainModule extends AbstractModule {

    TaskArgument argument;

    @Provides
    @Singleton
    public Task getTask(Injector injector) {
        return switch (this.argument.getTaskType()) {
            case GENERATE_EXAM_ITEMS -> injector.getInstance(ExamItemCollector.class);
            case EXAM_VALIDATOR -> injector.getInstance(ExamValidator.class);
            case EXAM_EVALUATOR -> injector.getInstance(ExamEvaluator.class);
            default -> throw new IllegalArgumentException();
        };
    }

    public static void main(String[] args) {
        TaskArgument taskArgument = new TaskArgument(TaskType.GENERATE_EXAM_ITEMS, "hu.sed.evaluator.exam.y2020.zh2.task8.mysolution");
        Injector injector = Guice.createInjector(new MainModule(taskArgument));
        injector.getInstance(Task.class).execute(taskArgument);

        //        TaskArgument taskArgument;
//        try {
//            taskArgument = ArgumentsUtil.parseArguments(args);
//        } catch (MissingArgumentsException | IllegalArgumentException | InvalidArgumentException e) {
//            log.error("Argument error: {}", e.getMessage(), e);
//            return;
//        }
//
//        switch (taskArgument.getTaskType()) {
//            case UML_GENERATOR -> System.out.println();
//            case EXAM_EVALUATOR -> System.out.println();
//            case EXAM_VALIDATOR -> System.out.println();
//        }
//        log.error("Task arguments: {}", taskArgument);
    }
}
