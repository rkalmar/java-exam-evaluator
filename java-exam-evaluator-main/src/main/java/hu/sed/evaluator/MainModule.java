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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


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

//        loadClass();

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

//    @SneakyThrows
//    private static void loadClass() {
//        File file = new File("c:\\Users\\rkalmar\\" +
//                "Desktop\\szte\\diplomamunka\\java-exam-evaluator\\" +
//                "java-exam-test-solution\\target\\java-exam-test-solution-20230101-SNAPSHOT.jar");
//        JarFile jar = new JarFile(file);
//        URLClassLoader ucl = new URLClassLoader(new URL[]{file.toURI().toURL()});
//        Enumeration<JarEntry> entries = jar.entries();
//        while (entries.hasMoreElements()) {
//            JarEntry entry = entries.nextElement();
//
//            if (!entry.getName().endsWith(".class")) {
//                continue;
//            }
//
//            Class<?> clazz;
//            try {
//                clazz = ucl.loadClass(entry.getName().replace("/", ".").replace(".class", ""));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
//        jar.close();
//        ucl.close();
//    }

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
}
