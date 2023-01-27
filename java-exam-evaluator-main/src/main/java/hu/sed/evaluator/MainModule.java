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
import hu.sed.evaluator.task.TaskExecutor;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.argument.TaskType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


/**
 * @author rkalmar
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainModule extends AbstractModule {

    public static void main(String[] args) {
        TaskArgument taskArgument = TaskArgument.builder()
                .taskType(TaskType.EXAM_EVALUATOR)
                .examPackage("hu.sed.evaluator.exam.sample")
                .examItemOutputFile("c:\\Users\\rkalmar\\Desktop\\szte\\diplomamunka\\examfile")
                .examItemFile("c:\\Users\\rkalmar\\Desktop\\szte\\diplomamunka\\examfile")
                .examDocOutputFolder("c:\\Users\\rkalmar\\Desktop\\szte\\diplomamunka")
                .build();
        log.info("Initializing.. arguments: {}", taskArgument);
        Injector injector = Guice.createInjector(new MainModule());
        injector.getInstance(TaskExecutor.class).execute(taskArgument);
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
