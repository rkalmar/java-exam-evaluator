package hu.sed.evaluator.task;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.task.argument.TaskArgument;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.util.Base64;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class ExamItemExporter implements Task<Void> {

    static final String FILE_NAME = "examfile";

    JsonMapper jsonMapper;

    TaskArgument argument;

    RootItem rootItem;

    ExamValidator examValidator;

    @SneakyThrows
    @Override
    public Void execute() {
        examValidator.execute();

        byte[] bytes = jsonMapper.writeValueAsBytes(rootItem);
        String encodedJson = Base64.getEncoder().encodeToString(bytes);
        try (FileWriter fileWriter = new FileWriter(argument.getOutputFolder() + File.separator + FILE_NAME)) {
            fileWriter.write(encodedJson);
        }

        // TODO export the same what we are exporting in case of examEvaluator
        return null;
    }
}
