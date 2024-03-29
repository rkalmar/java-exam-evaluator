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
import hu.sed.evaluator.args.ArgumentsUtil;
import hu.sed.evaluator.args.InvalidArgumentException;
import hu.sed.evaluator.args.MissingArgumentsException;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.task.ExamEvaluator;
import hu.sed.evaluator.task.ExamExporter;
import hu.sed.evaluator.task.ExamItemLoader;
import hu.sed.evaluator.task.ExamValidator;
import hu.sed.evaluator.task.Task;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.argument.TaskType;
import hu.sed.evaluator.task.evaluator.classloader.ApplicationClassLoaderProvider;
import hu.sed.evaluator.task.evaluator.classloader.ClassLoaderProvider;
import hu.sed.evaluator.task.evaluator.classloader.SolutionClassLoaderProvider;
import hu.sed.evaluator.task.evaluator.semantic.ByteCodeManipulator;
import hu.sed.evaluator.task.evaluator.semantic.ByteCodeManipulatorService;
import hu.sed.evaluator.task.evaluator.semantic.NoopByteCodeManipulator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @author rkalmar
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainModule extends AbstractModule {

    TaskArgument taskArgument;

    public static void main(String[] args) throws InvalidArgumentException, MissingArgumentsException {
        TaskArgument taskArgument = ArgumentsUtil.parseArguments(args);

        if (StringUtils.isNotBlank(taskArgument.getOutputFolder())) {
            checkFileExistence(taskArgument.getOutputFolder());
        }
        if (StringUtils.isNotBlank(taskArgument.getExamItemFile())) {
            checkFileExistence(taskArgument.getOutputFolder());
        }

        log.info("Initializing.. arguments: {}", taskArgument);
        Injector injector = Guice.createInjector(new MainModule(taskArgument));
        injector.getInstance(Task.class).execute();
    }

    private static void checkFileExistence(String file) throws InvalidArgumentException {
        if (StringUtils.isNotEmpty(file)) {
            Path path = Paths.get(file);
            if (!Files.exists(path)) {
                throw new InvalidArgumentException("Does not exists: " + file);
            }
        }
    }

    @Provides
    @Singleton
    @SuppressWarnings("rawtypes")
    public Task getTask(Injector injector) {
        return switch (taskArgument.getTaskType()) {
            case EXAM_VALIDATOR -> injector.getInstance(ExamValidator.class);
            case EXPORT_EXAM -> injector.getInstance(ExamExporter.class);
            case EXAM_EVALUATOR -> injector.getInstance(ExamEvaluator.class);
            default -> throw new IllegalArgumentException(taskArgument.getTaskType().name());
        };
    }

    @Provides
    @Singleton
    public RootItem getRootExamItem(ExamItemLoader examItemLoader) {
        return examItemLoader.execute();
    }

    @Provides
    @Singleton
    public TaskArgument getTaskArgument() {
        return taskArgument;
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

    @Provides
    @Singleton
    public ByteCodeManipulator getByteCodeManipulator() {
        if (this.taskArgument.isEnableByteCodeManipulation()) {
            return new ByteCodeManipulatorService();
        }
        return new NoopByteCodeManipulator();
    }

    @Provides
    @Singleton
    public ClassLoaderProvider examClassLoader() {
        return this.taskArgument.getTaskType() == TaskType.EXAM_EVALUATOR ?
                new SolutionClassLoaderProvider(taskArgument.getSolutionClassPath()) : new ApplicationClassLoaderProvider();
    }
}
