package hu.sed.evaluator.task.evaluators.syntax;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.task.ReflectionUtils;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.FieldItem;
import hu.sed.evaluator.task.evaluators.exception.NoSuchSyntaxItemException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

import static hu.sed.evaluator.task.evaluators.syntax.SyntaxElement.EXISTENCE;

@Slf4j
@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FieldItemEvaluator extends SyntaxItemEvaluator<FieldItem> {

    ItemFactory itemFactory;

    @Inject
    public FieldItemEvaluator(EvaluatorService evaluatorService, ItemFactory itemFactory) {
        super(evaluatorService);
        this.itemFactory = itemFactory;
    }

    @Override
    public void evaluate(ScoredSyntaxItem scoredItem) throws NoSuchSyntaxItemException {
        FieldItem item = getItem(scoredItem);
        Field field;
        try {
            field = ReflectionUtils.getFieldByName(item.getContainerClass(), item.getName());
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new NoSuchSyntaxItemException();
        }
        if (!checkType(field, item.getType())) {
            throw new NoSuchSyntaxItemException();
        }
        scoredItem.successfulCheck(EXISTENCE);

        checkModifiers(scoredItem, field.getModifiers());
    }

    public boolean checkType(Field field, TypeDefinition expectedType) {
        TypeDefinition actualTypeDef = itemFactory.createTypeDef(field);
        return evaluatorService.checkType(actualTypeDef, expectedType);
    }
}
