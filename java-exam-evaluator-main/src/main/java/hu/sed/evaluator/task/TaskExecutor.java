package hu.sed.evaluator.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.argument.TaskType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TaskExecutor implements Task<Void, TaskArgument> {

    ExamValidator examValidator;

    ExamEvaluator evaluator;

    DocExporter docExporter;

    ExamItemLoader examItemLoader;

    ExamItemExporter examItemExporter;

    public Void execute(TaskArgument taskArgument) {
        RootItem rootItem = examItemLoader.execute(taskArgument);

        TaskType taskType = taskArgument.getTaskType();

        if (taskType == TaskType.EXAM_VALIDATOR) {
            examValidator.execute(rootItem);
        } else if (taskType == TaskType.EXPORT_EXAM_ITEMS) {
            examValidator.execute(rootItem);
            examItemExporter.execute(new ExamItemExporter.ExportParam() {
                @Override
                public RootItem getRootItem() {
                    return rootItem;
                }

                @Override
                public String getFileName() {
                    return taskArgument.getExamItemOutputFile();
                }
            });
        } else if (taskType == TaskType.EXPORT_DOC) {
            docExporter.execute(taskArgument);
        } else if (taskType == TaskType.EXAM_EVALUATOR) {
            evaluator.execute(rootItem);
        }

        return null;
    }


}