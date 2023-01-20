package hu.sed.evaluator.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.container.RootItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExamValidator implements Task<Void, RootItem> {

    ExamItemCollector examItemCollector;

    ExamEvaluator evaluator;

    JsonMapper jsonMapper;

    @Override
    public Void execute(RootItem rootItem) {
        log.debug("Execute validator");

        try {
            String s = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootItem);
            log.error(s);
        } catch (JsonProcessingException e) {
            // TODO
        }

        return null;
    }
}