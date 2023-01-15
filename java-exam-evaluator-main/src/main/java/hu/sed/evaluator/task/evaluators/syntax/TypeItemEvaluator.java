package hu.sed.evaluator.task.evaluators.syntax;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.ReflectionUtils;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.TypeItem;
import hu.sed.evaluator.task.evaluators.ScoredItem;
import hu.sed.evaluator.task.evaluators.exception.NoSuchSyntaxItemException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static hu.sed.evaluator.task.evaluators.CheckedElement.EXISTANCE;
import static hu.sed.evaluator.task.evaluators.CheckedElement.INTERFACES;
import static hu.sed.evaluator.task.evaluators.CheckedElement.PARENT_CLASS;


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
    public void evaluate(ScoredItem scoredItem) throws NoSuchSyntaxItemException {
        TypeItem item = getItem(scoredItem);
        Class<?> clazz = getClass(item);
        scoredItem.successfulElement(EXISTANCE);

        checkModifiers(scoredItem, clazz.getModifiers());

        if (item.isCheckParentClazz()) {
            TypeDefinition actualParentClazz = itemFactory.createTypeDef(clazz.getGenericSuperclass());
            boolean checkResult = evaluatorService.checkType(actualParentClazz, item.getParentClazz());
            scoredItem.element(PARENT_CLASS, checkResult);
            if (!checkResult) {
                log.info("{} -> Parent class is incorrect. Actual value: {}, expected value: {}",
                        item.getIdentifier(), actualParentClazz, item.getParentClazz());
            }
        }

        if (item.isCheckInterfaces()) {
            TypeDefinition[] actualImplementedInterfaces = itemFactory.createTypeDefForImplementedInterfaces(clazz);
            boolean checkResult = evaluatorService.checkTypesInAnyOrder(actualImplementedInterfaces, item.getImplementedInterfaces());
            scoredItem.element(INTERFACES, checkResult);
            if (!checkResult) {
                log.info("{} -> Implemented interface list is incorrect. Actual value: {}, expected value: {}",
                        item.getIdentifier(), Arrays.toString(actualImplementedInterfaces), Arrays.toString(item.getImplementedInterfaces()));
            }
        }
    }

    private Class<?> getClass(TypeItem item) throws NoSuchSyntaxItemException {
        try {
            Class<?> classByName = ReflectionUtils.getClassByName(item.getName());
            if (item.isInterfce() != classByName.isInterface()) {
                throw new ClassNotFoundException();
            }
            return classByName;
        } catch (ClassNotFoundException e) {
            throw new NoSuchSyntaxItemException();
        }
    }
}
