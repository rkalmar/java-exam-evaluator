package hu.sed.evaluator.task;

import java.util.List;

public record TaskArgument(TaskType taskType, List<Class<?>> examClasses,
                           List<Class<?>> solutionClasses) {
}
