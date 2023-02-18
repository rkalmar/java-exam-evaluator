package hu.sed.evaluator.task.argument;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskArgument {

    TaskType taskType;

    // required for all the taskType
    String examPackage;

    // only for export
    String outputFolder;

    // only for evaluation
    String examItemFile;

    // only for evaluation
    String solutionClassPath;

    boolean enableByteCodeManipulation;
}
