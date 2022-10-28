package hu.sed.evaluator.task.executor;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskArgument {
    TaskType taskType;
    List<Class<?>> examClasses;
    List<Class<?>> solutionClasses;
}
