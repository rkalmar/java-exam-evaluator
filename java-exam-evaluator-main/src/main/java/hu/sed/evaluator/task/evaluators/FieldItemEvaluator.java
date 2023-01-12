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
        ScoredItem.ScoredItemBuilder scoredItemBuilder = ScoredItem.builder()
                .item(item);
        try {
            Field field = ReflectionUtils.getFieldByName(item.getContainerClass(), item.getName());
            if (!checkType(field, item.getType())) {
                throw new NoSuchFieldException();
            }

            scoredItemBuilder.correctElement(CheckedElement.EXISTANCE);

            if (item.isCheckModifiers()) {
                if (evaluatorService.checkModifiers(field.getModifiers(), item.getModifiers())) {
                    scoredItemBuilder.correctElement(CheckedElement.MODIFIERS);
                } else {
                    scoredItemBuilder.incorrectElement(CheckedElement.MODIFIERS);
                }
            }
            return scoredItemBuilder.build();

        } catch (ClassNotFoundException | NoSuchFieldException e) {
            return ScoredItem.builder()
                    .incorrectElement(CheckedElement.EXISTANCE)
                    .item(item)
                    .build();
        }
    }

    public boolean checkType(Field field, TypeDefinition expectedType) {
        TypeDefinition actualTypeDef = itemFactory.createTypeDef(field);
        return evaluatorService.checkType(actualTypeDef, expectedType);
    }
}
