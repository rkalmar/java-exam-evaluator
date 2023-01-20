package hu.sed.evaluator.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.argument.TaskType;
import hu.sed.evaluator.task.exception.TaskExecutionException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExamItemLoader implements Task<RootItem, TaskArgument> {

    ExamItemCollector examItemCollector;

    @Override
    public RootItem execute(TaskArgument argument) {
        try {
            RootItem rootItem;
            if (argument.getTaskType() == TaskType.EXAM_EVALUATOR) {
                rootItem = null; // TODO load from file
            } else {
                rootItem = examItemCollector.execute(argument);
            }
            return rootItem;
        } catch (Exception e) {
            log.error("Failed to load task items..", e);
            throw new TaskExecutionException(e);
        }
    }
}
