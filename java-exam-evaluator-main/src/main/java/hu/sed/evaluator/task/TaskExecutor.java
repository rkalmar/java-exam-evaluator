package hu.sed.evaluator.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.argument.TaskType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.Writer;

import static hu.sed.evaluator.task.argument.TaskType.*;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TaskExecutor implements Task<Void, TaskArgument> {

    ExamValidator examValidator;

    ExamEvaluator evaluator;

    UmlGenerator umlGenerator;

    ExamItemLoader examItemLoader;

    ExamItemExporter examItemExporter;

    public Void execute(TaskArgument taskArgument) {
        RootItem rootItem = examItemLoader.execute(taskArgument);

        TaskType taskType = taskArgument.getTaskType();

        switch (taskType) {
            case EXAM_VALIDATOR:
            case UML_GENERATOR:
            case EXPORT_EXAM_ITEMS:
            case EXAM_EVALUATOR:
        }
        if (taskType == EXAM_VALIDATOR) {
            examValidator.execute(rootItem);
        } else if (taskType == EXPORT_EXAM_ITEMS) {
            examValidator.execute(rootItem);
            examItemExporter.execute(new ExamItemExporter.ExportParam() {
                @Override
                public RootItem getRootItem() {
                    return rootItem;
                }

                @SneakyThrows
                @Override
                public Writer getWriter() {
                    return new FileWriter(taskArgument.getExamItemOutputFile());
                }
            });
        } else if (taskType == UML_GENERATOR) {
            umlGenerator.execute(taskArgument);
        } else if(taskType == EXAM_EVALUATOR) {
            evaluator.execute(rootItem);
        }

        return null;
    }


}
