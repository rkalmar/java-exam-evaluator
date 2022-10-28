package hu.sed.evaluator.task.executor;

import hu.sed.evaluator.args.ArgumentsUtil;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum TaskType {
    EXAM_EVALUATOR(ArgumentsUtil.EVALUATE_ARG),
    EXAM_VALIDATOR(ArgumentsUtil.VALIDATE_ARG),
    UML_GENERATOR(ArgumentsUtil.UML_ARG);

    String argName;

    public static TaskType taskTypeByArg(String argName) {
        Optional<TaskType> taskType = Arrays.stream(TaskType.values())
                .filter(task -> task.argName.equals(argName))
                .findFirst();

        return taskType.orElseThrow();
    }
}
