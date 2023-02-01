package hu.sed.evaluator.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.task.exception.ExamValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExamValidator implements Task<Void> {

    ExamEvaluator evaluator;

    JsonMapper jsonMapper;

    RootItem rootItem;

    @Override
    public Void execute() {
        log.info("Executing validator..");

        try {
            String jsonString = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootItem);
            log.trace(jsonString);
            jsonMapper.readValue(jsonString, RootItem.class);
        } catch (JsonProcessingException e) {
            throw new ExamValidationException("Failed to serialize/deserialize exam json...", e);
        }

        Score score = evaluator.execute();

        if (score.getMaxScore().doubleValue() <= 0.0) {
            throw new ExamValidationException("No exam item was registered.");
        }

        if (score.getMaxScore().equals(score.getScore())) {
            log.info("Validation is successful..");
        } else {
            throw new ExamValidationException("Exam validation failed. One or more test has failed. Please check the validation result in the logs.");
        }

        return null;
    }
}