package hu.sed.evaluator.task.evaluators.syntax;

import hu.sed.evaluator.item.syntax.BaseSyntaxItem;
import hu.sed.evaluator.task.evaluators.CheckedElement;
import hu.sed.evaluator.task.evaluators.Evaluator;
import hu.sed.evaluator.task.evaluators.ScoredItem;
import hu.sed.evaluator.task.evaluators.exception.NoSuchSyntaxItemException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Modifier;

import static hu.sed.evaluator.task.evaluators.CheckedElement.MODIFIERS;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class SyntaxItemEvaluator<T extends BaseSyntaxItem> implements Evaluator<T> {

    EvaluatorService evaluatorService;

    @Override
    public final ScoredItem evaluate(T item) {
        log.info("{} -> checking item.", item.getIdentifier());
        ScoredItem scoredItem = ScoredItem.builder()
                .item(item)
                .build();

        try {
            evaluate(scoredItem);
        } catch (NoSuchSyntaxItemException exception) {
            log.info("{} -> Item does not exist.", item.getIdentifier());
            scoredItem.unsuccessfulElement(CheckedElement.EXISTANCE);
        }

        return scoredItem;
    }

    public abstract void evaluate(ScoredItem scoredItem) throws NoSuchSyntaxItemException;

    protected void checkModifiers(ScoredItem scoredItem, int actualModifiers) {
        BaseSyntaxItem item = (BaseSyntaxItem) scoredItem.getItem();
        if (item.isCheckModifiers()) {
            boolean checkResult = evaluatorService.checkModifiers(actualModifiers, item.getModifiers());
            scoredItem.element(MODIFIERS, checkResult);
            if (!checkResult) {
                log.info("{} modifier mismatch. Actual value: {}, expected value: {}",
                        item.getIdentifier(), Modifier.toString(actualModifiers), item.getReadableModifiers());
            }
        }
    }

    protected T getItem(ScoredItem item) {
        //noinspection unchecked
        return (T) item.getItem();
    }
}
