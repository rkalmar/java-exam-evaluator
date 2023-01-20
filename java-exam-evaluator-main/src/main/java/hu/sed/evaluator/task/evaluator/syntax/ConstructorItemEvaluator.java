package hu.sed.evaluator.task.evaluator.syntax;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.task.ReflectionUtils;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.ConstructorItem;

import java.lang.reflect.Constructor;
import java.util.List;

@Singleton
public class ConstructorItemEvaluator extends ExecutableItemEvaluator<Constructor<?>, ConstructorItem> {

    @Inject
    public ConstructorItemEvaluator(EvaluatorService evaluatorService, ItemFactory itemFactory) {
        super(evaluatorService, itemFactory);
    }

    @Override
    protected Constructor<?> findExecutableElement(ConstructorItem item) throws ClassNotFoundException, NoSuchMethodException {
        List<Constructor<?>> constructors = ReflectionUtils.getConstructorsByName(item.getContainerClass());
        for (Constructor<?> constructor : constructors) {
            if (evaluatorService.checkTypesInOrder(
                    itemFactory.buildParameterizedTypeFromList(constructor.getGenericParameterTypes()), item.getParameters())) {
                return constructor;
            }
        }
        throw new NoSuchMethodException();
    }
}
