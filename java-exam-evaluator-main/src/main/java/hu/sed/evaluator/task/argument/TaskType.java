package hu.sed.evaluator.task.argument;

import hu.sed.evaluator.args.ArgumentsUtil;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum TaskType {
    EXAM_VALIDATOR(ArgumentsUtil.VALIDATE_TASK),
    EXPORT_EXAM(ArgumentsUtil.EXPORT_TASK),
    EXAM_EVALUATOR(ArgumentsUtil.EVALUATE_TASK);

    String argName;

    public static TaskType taskTypeByArg(String argName) {
        Optional<TaskType> taskType = Arrays.stream(TaskType.values())
                .filter(task -> task.argName.equals(argName))
                .findFirst();

        return taskType.orElseThrow();
    }
}
