package hu.sed.evaluator;

import hu.sed.evaluator.args.ArgumentsUtil;
import hu.sed.evaluator.args.InvalidArgumentException;
import hu.sed.evaluator.args.MissingArgumentsException;
import hu.sed.evaluator.task.ExamItemCollector;
import hu.sed.evaluator.task.TaskArgument;
import hu.sed.evaluator.task.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author rkalmar
 */
@Slf4j // TODO add config file for logger
public class Main {

    public static void main(String[] args) {
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

        TaskArgument taskArgument = new TaskArgument(TaskType.GENERATE_EXAM_ITEMS, "hu.sed.evaluator.exam.y2020.zh2.task8.mysolution");
        new ExamItemCollector().execute(taskArgument);
    }
}
