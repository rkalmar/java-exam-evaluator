package hu.sed.evaluator.task;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.container.RootItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.util.Base64;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExamItemExporter implements Task<Void, ExamItemExporter.ExportParam> {

    JsonMapper jsonMapper;

    @SneakyThrows
    @Override
    public Void execute(ExportParam argument) {
        RootItem rootItem = argument.getRootItem();
        byte[] bytes = jsonMapper.writeValueAsBytes(rootItem);
        String encodedJson = Base64.getEncoder().encodeToString(bytes);
        try (FileWriter fileWriter = new FileWriter(argument.getFileName())) {
            fileWriter.write(encodedJson);
        }
        return null;
    }

    public interface ExportParam {

        RootItem getRootItem();

        String getFileName();
    }
}
