package hu.sed.evaluator.task.evaluator.syntax;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.MethodItem;
import hu.sed.evaluator.task.ReflectionUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

@Slf4j
@Singleton
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class MethodItemEvaluator extends ExecutableItemEvaluator<Method, MethodItem> {

    @Inject
    public MethodItemEvaluator(EvaluatorService evaluatorService, ItemFactory itemFactory) {
        super(evaluatorService, itemFactory);
    }

    @Override
    protected Method findExecutableElement(MethodItem item) throws ClassNotFoundException, NoSuchMethodException {
        List<Method> methodsByName = ReflectionUtils.getMethodsByName(item.getContainerClass(), item.getName());
        for (Method method : methodsByName) {
            if (evaluatorService.checkType(itemFactory.createReturnTypeDef(method), item.getReturnType()) &&
                    evaluatorService.checkTypesInOrder(itemFactory.buildParameterizedTypeFromList(method.getGenericParameterTypes()), item.getParameters())) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }
}
