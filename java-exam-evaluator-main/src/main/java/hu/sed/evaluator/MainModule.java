package hu.sed.evaluator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import hu.sed.evaluator.task.ExamEvaluator;
import hu.sed.evaluator.task.ExamItemCollector;
import hu.sed.evaluator.task.ExamValidator;
import hu.sed.evaluator.task.Task;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.argument.TaskType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import static com.google.inject.matcher.Matchers.annotatedWith;

/**
 * @author rkalmar
 */
@Slf4j // TODO add config file for logger
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainModule extends AbstractModule {

    TaskArgument argument;

    public static void main(String[] args) {
        TaskArgument taskArgument = TaskArgument.builder()
                .taskType(TaskType.GENERATE_EXAM_ITEMS)
                .examPackage("hu.sed.evaluator.exam.y2020.zh2.task8.mysolution")
                .build();
        Injector injector = Guice.createInjector(new MainModule(taskArgument));
        injector.getInstance(Task.class).execute(taskArgument);
    }

    @Provides
    @Singleton
    public Task getTask(Injector injector) {
        return switch (this.argument.getTaskType()) {
            case GENERATE_EXAM_ITEMS -> injector.getInstance(ExamItemCollector.class);
            case EXAM_VALIDATOR -> injector.getInstance(ExamValidator.class);
            case EXAM_EVALUATOR -> injector.getInstance(ExamEvaluator.class);
            default -> throw new IllegalArgumentException();
        };
    }

    @Provides
    @Singleton
    public JsonMapper getObjectMapper() {
        return JsonMapper.builder()
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
                .build();
    }
}
