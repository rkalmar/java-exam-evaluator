package hu.sed.evaluator.args;

import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.argument.TaskType;
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
    public static final String EVALUATE_TASK = "evaluateExam";
    public static final String VALIDATE_TASK = "validateExam";
    public static final String EXPORT_EXAM_TASK = "exportExam";

    public static final String EXAM_PACKAGE_ARG = "examPackage";
    public static final String OUTPUT_FOLDER_ARG = "outputFolder";
    public static final String EXAM_ITEM_FILE_ARG = "examItemFile";

    static final Map<String, Set<String>> ARG_DEPENDENCIES = new HashMap<>();

    static {
        ARG_DEPENDENCIES.put(VALIDATE_TASK, Set.of(EXAM_PACKAGE_ARG));
        ARG_DEPENDENCIES.put(EXPORT_EXAM_TASK, Set.of(EXAM_PACKAGE_ARG, OUTPUT_FOLDER_ARG));
        ARG_DEPENDENCIES.put(EVALUATE_TASK, Set.of(EXAM_ITEM_FILE_ARG, OUTPUT_FOLDER_ARG));
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

            return TaskArgument.builder()
                    .taskType(TaskType.taskTypeByArg(selectedTask))
                    .examPackage(commandLine.getOptionValue(EXAM_PACKAGE_ARG))
                    .examItemFile(commandLine.getOptionValue(EXAM_ITEM_FILE_ARG))
                    .outputFolder(commandLine.getOptionValue(OUTPUT_FOLDER_ARG))
                    .build();

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
        Option examClass = Option.builder(EXAM_PACKAGE_ARG)
                .desc("Exam classes root package.")
                .longOpt(EXAM_PACKAGE_ARG)
                .hasArgs()
                .required(false)
                .build();

        Option outputFolder = Option.builder(OUTPUT_FOLDER_ARG)
                .desc("Output files will be written to this folder.")
                .longOpt(OUTPUT_FOLDER_ARG)
                .hasArgs()
                .required(false)
                .build();

        Option examItemFile = Option.builder(EXAM_ITEM_FILE_ARG)
                .desc("Exam item for executing evaluation.")
                .longOpt(EXAM_ITEM_FILE_ARG)
                .hasArgs()
                .required(false)
                .build();

        return new Options()
                .addOption(taskType)
                .addOption(examClass)
                .addOption(outputFolder)
                .addOption(examItemFile);
    }

}
