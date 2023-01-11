package hu.sed.evaluator.task;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskArgument {

    TaskType taskType;

    String examPackage;

    String examItemOutputFile;
}
