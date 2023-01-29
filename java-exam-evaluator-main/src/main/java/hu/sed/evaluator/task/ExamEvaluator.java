package hu.sed.evaluator.task;

import static java.lang.System.lineSeparator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.ScorableItem;
import hu.sed.evaluator.item.container.ItemContainer;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.argument.TaskType;
import hu.sed.evaluator.task.evaluator.EvaluatorItemVisitor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExamEvaluator implements Task<Score> {

    EvaluatorItemVisitor evaluatorItemVisitor;

    RootItem rootItem;

    TaskArgument argument;

    @SneakyThrows
    @Override
    public Score execute() {
        log.info("Executing evaluator. Test was created by {} at {}. Container: {}", rootItem.getCreatedBy(), rootItem.getCreatedBy(), rootItem.getContainerName());
        List<ScoredItem<?>> scoredItems = new ArrayList<>();

        StopWatch watch = new StopWatch();
        watch.start();
        rootItem.getItems()
                .forEach(item -> evaluateItem(scoredItems, item));

        watch.stop();

        Score score = Score.builder()
                .scoredItems(scoredItems)
                .build();

        String message = resultMessage(score, watch.getTime());

        log.info("Evaluation result: {} ", message);

        if (argument.getTaskType() == TaskType.EXAM_EVALUATOR) {
            try (PrintWriter printWriter = new PrintWriter(
                    new FileWriter(argument.getOutputFolder() + File.separator + "exam_result"))) {
                printWriter.write(message);
            }
            try (PrintWriter printWriter = new PrintWriter(
                    new FileWriter(argument.getOutputFolder() + File.separator + "score"))) {
                printWriter.write(String.valueOf(score.getScore()));
            }
        }

        return score;
    }

    private String resultMessage(Score score, long evaluationTime) {
        StringBuilder message = new StringBuilder("Successful items:");
        message.append(lineSeparator());
        score.getScoredItems().stream()
                .filter(ScoredItem::isSuccessful)
                .map(this::printScoredItem)
                .forEach(message::append);
        message.append("------------------").append(lineSeparator());
        message.append("Unsuccessful items:")
                .append(lineSeparator());
        score.getScoredItems().stream()
                .filter(scoredItem -> !scoredItem.isSuccessful())
                .map(this::printScoredItem)
                .forEach(message::append);

        String percentage = String.format("%.0f", score.getPercentage() * 100) + "%";
        message.append("------------------")
                .append(lineSeparator())
                .append(String.format("Evaluation result %.2f/%.2f (%s)", score.getScore(), score.getMaxScore(), percentage))
                .append(lineSeparator())
                .append("Evaluation time: ").append(evaluationTime).append("ms");
        return message.toString();
    }

    private void evaluateItem(List<ScoredItem<?>> scoredItems, Item item) {
        if (item instanceof ScorableItem) {
            ScoredItem<?> scoredItem = item.accept(evaluatorItemVisitor);
            scoredItems.add(scoredItem);
        }
        if (item instanceof ItemContainer itemContainer) {
            itemContainer.getItems().forEach(childItem -> evaluateItem(scoredItems, childItem));
        }
    }

    private String printScoredItem(ScoredItem<?> scoredItem) {
        return String.format("\r %.2f/%.2f - %s%s%s",
                scoredItem.getScore(), scoredItem.getMaxScore(), scoredItem.identifier(),
                scoredItem.getUnsuccessfulChecks().isEmpty() ? "" : ", Failed checks: " + scoredItem.getUnsuccessfulChecks(), lineSeparator());
    }

}
