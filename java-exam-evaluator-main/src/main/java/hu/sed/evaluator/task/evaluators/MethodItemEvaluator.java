package hu.sed.evaluator.task.evaluators;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.ReflectionUtils;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.MethodItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.util.List;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MethodItemEvaluator implements Evaluator<MethodItem> {

    @Inject
    EvaluatorService evaluatorService;

    @Inject
    ItemFactory itemFactory;

    @Override
    public ScoredItem evaluate(MethodItem item) {
        ScoredItem scoredItem = ScoredItem.builder()
                .item(item)
                .build();
        try {
            Method method = findMethod(item);
            scoredItem.successfulElement(CheckedElement.EXISTANCE);

            if (item.isCheckModifiers()) {
                scoredItem.element(CheckedElement.MODIFIERS,
                        evaluatorService.checkModifiers(method.getModifiers(), item.getModifiers())
                );
            }

            if (item.isCheckExceptions()) {
                scoredItem.element(CheckedElement.EXCEPTIONS,
                        evaluatorService.checkTypes(
                                itemFactory.buildParameterizedTypeFromList(method.getGenericExceptionTypes()), item.getExceptions())
                );
            }

            if (item.isCheckOverride()) {
                // TODO
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            scoredItem.unsuccessfulElement(CheckedElement.EXISTANCE);
        }
        return scoredItem;
    }

    private Method findMethod(MethodItem item) throws ClassNotFoundException, NoSuchMethodException {
        List<Method> methodsByName = ReflectionUtils.getMethodsByName(item.getContainerClass(), item.getName());
        for (Method method : methodsByName) {
            if (evaluatorService.checkType(itemFactory.createReturnTypeDef(method), item.getReturnType())
                    && evaluatorService.checkTypes(itemFactory.buildParameterizedTypeFromList(method.getGenericParameterTypes()), item.getParameters())) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }
}
