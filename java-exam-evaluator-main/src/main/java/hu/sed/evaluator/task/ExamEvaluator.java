package hu.sed.evaluator.task;

import static java.lang.System.lineSeparator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.container.ItemContainer;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.item.syntax.ScorableSyntaxItem;
import hu.sed.evaluator.item.syntax.TypeItem;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.argument.TaskType;
import hu.sed.evaluator.task.evaluator.EvaluatorItemVisitor;
import hu.sed.evaluator.task.evaluator.semantic.JavaCodeService;
import hu.sed.evaluator.task.evaluator.syntax.ScoredSyntaxItem;
import hu.sed.evaluator.task.evaluator.syntax.SyntaxElement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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

    JavaCodeService javaCodeService;

    @Override
    public Score execute() {
        log.info("Executing evaluator. Test was created by {} at {}. Container: {}",
                rootItem.getCreatedBy(), rootItem.getCreationTime(), rootItem.getContainerName());
        List<ScoredItem<?>> scoredItems = new ArrayList<>();

        StopWatch watch = new StopWatch();
        watch.start();
        // execute syntax items first
        rootItem.getItems()
                .forEach(item -> evaluateSyntaxItems(scoredItems, item));

        injectMissingClasses(scoredItems);

        // execute semantic items
        rootItem.getItems()
                .forEach(item -> evaluateSemanticItems(scoredItems, item));

        watch.stop();

        Score score = Score.builder()
                .scoredItems(scoredItems)
                .build();

        String message = resultMessage(score, watch.getTime());

        log.info("Evaluation result: {} ", message);
        exportResult(score, message);

        return score;
    }

    @SneakyThrows
    private void exportResult(Score score, String message) {
        if (argument.getTaskType() == TaskType.EXAM_EVALUATOR) {
            try (PrintWriter printWriter = new PrintWriter(
                    new FileWriter(argument.getOutputFolder() + File.separator + "exam_result"))) {
                printWriter.write(message);
            }
            try (PrintWriter printWriter = new PrintWriter(
                    new FileWriter(argument.getOutputFolder() + File.separator + "score"))) {
                printWriter.write(roundedScore(score));
            }
        }
    }

    private void injectMissingClasses(List<ScoredItem<?>> scoredItems) {
        // inject missing classes to classloader to make semantic test classes properly loadable
        List<TypeItem> typeItems = scoredItems.stream()
                .filter(scoredItem -> scoredItem.getItem() instanceof TypeItem typeItem &&
                        StringUtils.isEmpty(typeItem.getContainerClass()))
                .filter(scoredItem -> !((ScoredSyntaxItem) scoredItem).itemExists())
                .map(scoredItem -> (TypeItem) scoredItem.getItem())
                .filter(item -> !item.isInterfaze())
                .toList();
        typeItems.forEach(item -> log.info("Missing typeItems {}", item.identifier()));
        javaCodeService.addClasses(typeItems);
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
                .append(String.format("Evaluation result %s/%.2f (%s)", roundedScore(score), score.getMaxScore(), percentage))
                .append(lineSeparator())
                .append("Evaluation time: ").append(evaluationTime).append("ms");
        return message.toString();
    }

    private void evaluateSemanticItems(List<ScoredItem<?>> scoredItems, Item item) {
        if (item instanceof TestItem) {
            ScoredItem<?> scoredItem = item.accept(evaluatorItemVisitor);
            scoredItems.add(scoredItem);
        }
        if (item instanceof ItemContainer itemContainer) {
            itemContainer.getItems().forEach(childItem -> evaluateSemanticItems(scoredItems, childItem));
        }
    }

    private void evaluateSyntaxItems(List<ScoredItem<?>> scoredItems, Item item) {
        evaluateSyntaxItems(scoredItems, item, false);
    }

    private void evaluateSyntaxItems(List<ScoredItem<?>> scoredItems, Item item, boolean parentItemMissingParam) {
        boolean parentItemMissing = parentItemMissingParam;
        if (item instanceof ScorableSyntaxItem scorableSyntaxItem) {
            if (parentItemMissing) {
                log.info("Parent item is missing. Skip test for item: {}", scorableSyntaxItem.identifier());
                ScoredSyntaxItem scoredSyntaxItem = ScoredSyntaxItem.builder()
                        .item(scorableSyntaxItem)
                        .build();
                scoredSyntaxItem.unsuccessfulCheck(SyntaxElement.EXISTENCE);
                scoredItems.add(scoredSyntaxItem);
            } else {
                ScoredItem<?> scoredItem = scorableSyntaxItem.accept(evaluatorItemVisitor);
                scoredItems.add(scoredItem);
                parentItemMissing = !((ScoredSyntaxItem) scoredItem).itemExists();
            }
        }
        if (item instanceof ItemContainer itemContainer) {
            for (Item childItem : itemContainer.getItems()) {
                evaluateSyntaxItems(scoredItems, childItem, parentItemMissing);
            }
        }
    }

    private String printScoredItem(ScoredItem<?> scoredItem) {
        return String.format("\r %.2f/%.2f - %s%s%s",
                scoredItem.getScore(), scoredItem.getMaxScore(), scoredItem.identifier(),
                scoredItem.getUnsuccessfulChecks().isEmpty() ? "" : ", Failed checks: " + scoredItem.getUnsuccessfulChecks(), lineSeparator());
    }

    private String roundedScore(Score score) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(score.getScore());
    }

}
