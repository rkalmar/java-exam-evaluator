package hu.sed.evaluator.task.evaluators.syntax;

import hu.sed.evaluator.item.syntax.ScorableSyntaxItem;
import hu.sed.evaluator.task.evaluators.Evaluator;
import hu.sed.evaluator.task.ScoredItem;
import hu.sed.evaluator.task.evaluators.exception.NoSuchSyntaxItemException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Modifier;

import static hu.sed.evaluator.task.evaluators.syntax.SyntaxElement.MODIFIERS;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class SyntaxItemEvaluator<T extends ScorableSyntaxItem> implements Evaluator<T, ScoredSyntaxItem> {

    EvaluatorService evaluatorService;

    @Override
    public final ScoredSyntaxItem evaluate(T item) {
        log.info("{} -> checking item.", item.getIdentifier());
        ScoredSyntaxItem scoredItem = ScoredSyntaxItem.builder()
                .item(item)
                .build();

        try {
            evaluate(scoredItem);
        } catch (NoSuchSyntaxItemException exception) {
            log.info("{} -> Item does not exist.", item.getIdentifier());
            scoredItem.unsuccessfulCheck(SyntaxElement.EXISTENCE);
        }

        return scoredItem;
    }

    public abstract void evaluate(ScoredSyntaxItem scoredItem) throws NoSuchSyntaxItemException;

    protected void checkModifiers(ScoredSyntaxItem scoredItem, int actualModifiers) {
        ScorableSyntaxItem item = (ScorableSyntaxItem) scoredItem.getItem();
        if (item.isCheckModifiers()) {
            boolean checkResult = evaluatorService.checkModifiers(actualModifiers, item.getModifiers());
            scoredItem.addCheck(MODIFIERS, checkResult);
            if (!checkResult) {
                log.info("{} modifier mismatch. Actual value: {}, expected value: {}",
                        item.getIdentifier(), Modifier.toString(actualModifiers), item.getReadableModifiers());
            }
        }
    }

    protected T getItem(ScoredItem<?> item) {
        //noinspection unchecked
        return (T) item.getItem();
    }
}
