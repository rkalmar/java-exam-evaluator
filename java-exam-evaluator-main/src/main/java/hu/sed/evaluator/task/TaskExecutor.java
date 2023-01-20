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

import static hu.sed.evaluator.task.argument.TaskType.EXAM_EVALUATOR;
import static hu.sed.evaluator.task.argument.TaskType.EXAM_VALIDATOR;
import static hu.sed.evaluator.task.argument.TaskType.EXPORT_EXAM_ITEMS;
import static hu.sed.evaluator.task.argument.TaskType.EXPORT_DOC;

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

        if (taskType == EXAM_VALIDATOR) {
            examValidator.execute(rootItem);
        } else if (taskType == EXPORT_EXAM_ITEMS) {
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
        } else if (taskType == EXPORT_DOC) {
            docExporter.execute(taskArgument);
        } else if (taskType == EXAM_EVALUATOR) {
            evaluator.execute(rootItem);
        }

        return null;
    }


}
