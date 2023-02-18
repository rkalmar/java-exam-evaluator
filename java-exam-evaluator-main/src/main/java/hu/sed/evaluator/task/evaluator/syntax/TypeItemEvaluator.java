package hu.sed.evaluator.task.evaluator.syntax;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.TypeItem;
import hu.sed.evaluator.task.ReflectionUtils;
import hu.sed.evaluator.task.evaluator.exception.NoSuchSyntaxItemException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;


@Slf4j
@Singleton
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class TypeItemEvaluator extends SyntaxItemEvaluator<TypeItem> {

    ItemFactory itemFactory;

    @Inject
    public TypeItemEvaluator(EvaluatorService evaluatorService, ItemFactory itemFactory) {
        super(evaluatorService);
        this.itemFactory = itemFactory;
    }

    @Override
    public void evaluate(ScoredSyntaxItem scoredItem) throws NoSuchSyntaxItemException {
        TypeItem item = getItem(scoredItem);
        Class<?> clazz = getClass(item);
        scoredItem.successfulCheck(SyntaxElement.EXISTENCE);

        checkModifiers(scoredItem, clazz.getModifiers());

        if (!item.isInterfaze()) {
            TypeDefinition actualParentClazz = itemFactory.createTypeDef(clazz.getGenericSuperclass());
            boolean checkResult = evaluatorService.checkType(actualParentClazz, item.getParentClazz());
            scoredItem.addCheck(SyntaxElement.PARENT_CLASS, checkResult);
            if (!checkResult) {
                log.info("{} -> Parent class is incorrect. Actual value: {}, expected value: {}",
                        item.identifier(), actualParentClazz, item.getParentClazz());
            }
        }

        if (item.isCheckInterfaces()) {
            TypeDefinition[] actualImplementedInterfaces = itemFactory.createTypeDefForImplementedInterfaces(clazz);
            boolean checkResult = evaluatorService.checkTypesInAnyOrder(actualImplementedInterfaces, item.getImplementedInterfaces());
            scoredItem.addCheck(SyntaxElement.INTERFACES, checkResult);
            if (!checkResult) {
                log.info("{} -> Implemented interface list is incorrect. Actual value: {}, expected value: {}",
                        item.identifier(), Arrays.toString(actualImplementedInterfaces), Arrays.toString(item.getImplementedInterfaces()));
            }
        }
    }

    private Class<?> getClass(TypeItem item) throws NoSuchSyntaxItemException {
        try {
            Class<?> classByName = ReflectionUtils.getClassByName(item.getName());
            if (item.isInterfaze() != classByName.isInterface()) {
                throw new ClassNotFoundException();
            }
            if (item.isEnumeration() != classByName.isEnum()) {
                throw new ClassNotFoundException();
            }
            return classByName;
        } catch (ClassNotFoundException e) {
            throw new NoSuchSyntaxItemException();
        }
    }
}
