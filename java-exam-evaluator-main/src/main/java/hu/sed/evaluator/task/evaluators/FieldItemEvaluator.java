package hu.sed.evaluator.task.evaluators;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.ReflectionUtils;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.FieldItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;

import static hu.sed.evaluator.task.evaluators.CheckedElement.EXISTANCE;
import static hu.sed.evaluator.task.evaluators.CheckedElement.MODIFIERS;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FieldItemEvaluator implements Evaluator<FieldItem> {

    @Inject
    EvaluatorService evaluatorService;

    @Inject
    ItemFactory itemFactory;

    @Override
    public ScoredItem evaluate(FieldItem item) {
        ScoredItem scoredItem = ScoredItem.builder()
                .item(item)
                .build();
        try {
            Field field = ReflectionUtils.getFieldByName(item.getContainerClass(), item.getName());
            if (!checkType(field, item.getType())) {
                throw new NoSuchFieldException();
            }

            scoredItem.successfulElement(EXISTANCE);

            if (item.isCheckModifiers()) {
                scoredItem.element(MODIFIERS,
                        evaluatorService.checkModifiers(field.getModifiers(), item.getModifiers()));
            }

        } catch (ClassNotFoundException | NoSuchFieldException e) {
            scoredItem.unsuccessfulElement(EXISTANCE);
        }
        return scoredItem;
    }

    public boolean checkType(Field field, TypeDefinition expectedType) {
        TypeDefinition actualTypeDef = itemFactory.createTypeDef(field);
        return evaluatorService.checkType(actualTypeDef, expectedType);
    }
}
