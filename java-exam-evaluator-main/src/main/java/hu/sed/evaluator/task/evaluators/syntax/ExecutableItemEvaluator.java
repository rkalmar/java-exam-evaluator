package hu.sed.evaluator.task.evaluators.syntax;

import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.ExecutableItem;
import hu.sed.evaluator.task.evaluators.CheckedElement;
import hu.sed.evaluator.task.evaluators.ScoredItem;
import hu.sed.evaluator.task.evaluators.exception.NoSuchSyntaxItemException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import java.util.Arrays;

@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class ExecutableItemEvaluator<T extends Executable, E extends ExecutableItem> extends SyntaxItemEvaluator<E> {

    ItemFactory itemFactory;

    public ExecutableItemEvaluator(EvaluatorService evaluatorService, ItemFactory itemFactory) {
        super(evaluatorService);
        this.itemFactory = itemFactory;
    }

    @Override
    public final void evaluate(ScoredItem scoredItem) throws NoSuchSyntaxItemException {
        E item = getItem(scoredItem);
        T executable;
        try {
            executable = findExecutableElement(item);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new NoSuchSyntaxItemException();
        }
        scoredItem.successfulElement(CheckedElement.EXISTANCE);

        checkModifiers(scoredItem, executable.getModifiers());

        checkExceptions(scoredItem, executable.getGenericExceptionTypes());

        evaluate(executable, scoredItem);
    }

    protected void evaluate(T executable, ScoredItem scoredItem) {
    }

    protected abstract T findExecutableElement(E item) throws ClassNotFoundException, NoSuchMethodException;

    protected void checkExceptions(ScoredItem scoredItem, Type[] actualExceptionsTypes) {
        E item = getItem(scoredItem);
        if (item.isCheckExceptions()) {
            TypeDefinition[] actualExceptions = itemFactory.buildParameterizedTypeFromList(actualExceptionsTypes);
            TypeDefinition[] exceptedExceptions = item.getExceptions();
            boolean checkResult = evaluatorService.checkTypesInAnyOrder(actualExceptions, exceptedExceptions);
            scoredItem.element(CheckedElement.EXCEPTIONS, checkResult);

            if (!checkResult) {
                log.info("{} defined exception mismatch. Actual value: {}, expected value: {}",
                        item.getIdentifier(), Arrays.toString(actualExceptions), Arrays.toString(exceptedExceptions));
            }
        }
    }
}
