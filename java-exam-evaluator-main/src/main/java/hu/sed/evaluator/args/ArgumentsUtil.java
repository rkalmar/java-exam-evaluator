package hu.sed.evaluator.args;

import hu.sed.evaluator.task.TaskArgument;
import hu.sed.evaluator.task.TaskType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@UtilityClass
public class ArgumentsUtil {

    public static final String TASK_ARG = "task";
    public static final String EVALUATE_ARG = "evaluateExam";
    public static final String VALIDATE_ARG = "validateExam";
    public static final String UML_ARG = "generateUml";
    public static final String EXAM_CLASS_ARG = "examPackage";
    public static final String SOLUTION_CLASS_ARG = "solutionPackage";

    static final Map<String, Set<String>> ARG_DEPENDENCIES = new HashMap<>();

    static {
        ARG_DEPENDENCIES.put(EVALUATE_ARG, Set.of(EXAM_CLASS_ARG, SOLUTION_CLASS_ARG));
        ARG_DEPENDENCIES.put(VALIDATE_ARG, Set.of(EXAM_CLASS_ARG));
        ARG_DEPENDENCIES.put(UML_ARG, Set.of(EXAM_CLASS_ARG));
    }

    public static TaskArgument parseArguments(String[] args) throws MissingArgumentsException, InvalidArgumentException {
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(createOptions(), args);

            Set<String> tasks = ARG_DEPENDENCIES.keySet();
            if (!commandLine.hasOption(TASK_ARG) && !tasks.contains(commandLine.getOptionValue(TASK_ARG))) {
                throw new MissingArgumentsException(String.format("Exactly one of the main arguments should be set. \"%s\" \"%s\"", TASK_ARG, tasks));
            }
            String selectedTask = commandLine.getOptionValue(TASK_ARG);
            if (!tasks.contains(selectedTask)) {
                throw new InvalidArgumentException(String.format("Invalid argument for \"%s\" param: \"%s\"", TASK_ARG, selectedTask));
            }

            log.info("Selected task {}", selectedTask);
            Set<String> subArgs = ARG_DEPENDENCIES.get(selectedTask);
            boolean hasAllDependencies = subArgs.stream().allMatch(commandLine::hasOption);
            if (!hasAllDependencies) {
                throw new MissingArgumentsException(String.format("Following arguments are required for task %s: %s", selectedTask, subArgs));
            }

            //return new TaskArgument(TaskType.taskTypeByArg(VALIDATE_ARG)); // TODO packages
            return null;

        } catch (UnrecognizedOptionException e) {
            throw new InvalidArgumentException(e.getMessage());
        } catch (ParseException e) {
            log.error("Failed to parse arguments.", e);
            throw new MissingArgumentsException(e);
        }
    }

    private static Options createOptions() {
        Option taskType = Option.builder(TASK_ARG)
                .desc("Executes selected task.")
                .longOpt(TASK_ARG)
                .hasArgs()
                .required(false)
                .build();
        Option examClass = Option.builder(EXAM_CLASS_ARG)
                .desc("Exam classes root package.")
                .longOpt(EXAM_CLASS_ARG)
                .hasArgs()
                .required(false)
                .build();
        Option solutionClass = Option.builder(SOLUTION_CLASS_ARG)
                .desc("Solution classes root package.")
                .longOpt(SOLUTION_CLASS_ARG)
                .hasArgs()
                .required(false)
                .build();
        return new Options()
                .addOption(taskType)
                .addOption(examClass)
                .addOption(solutionClass);
    }

}
