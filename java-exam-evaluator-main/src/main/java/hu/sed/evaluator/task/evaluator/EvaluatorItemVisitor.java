package hu.sed.evaluator.task.evaluator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.ItemVisitor;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.item.syntax.ConstructorItem;
import hu.sed.evaluator.item.syntax.FieldItem;
import hu.sed.evaluator.item.syntax.MethodItem;
import hu.sed.evaluator.item.syntax.TypeItem;
import hu.sed.evaluator.task.ScoredItem;
import hu.sed.evaluator.task.evaluator.semantic.TestEvaluator;
import hu.sed.evaluator.task.evaluator.syntax.ConstructorItemEvaluator;
import hu.sed.evaluator.task.evaluator.syntax.FieldItemEvaluator;
import hu.sed.evaluator.task.evaluator.syntax.MethodItemEvaluator;
import hu.sed.evaluator.task.evaluator.syntax.TypeItemEvaluator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EvaluatorItemVisitor implements ItemVisitor<ScoredItem<?>> {

    FieldItemEvaluator fieldItemEvaluator;
    MethodItemEvaluator methodItemEvaluator;
    ConstructorItemEvaluator constructorItemEvaluator;
    TypeItemEvaluator typeItemEvaluator;
    TestEvaluator testEvaluator;

    @Override
    public ScoredItem<?> visit(TestItem item) {
        return testEvaluator.evaluate(item);
    }

    @Override
    public ScoredItem<?> visit(TypeItem item) {
        return typeItemEvaluator.evaluate(item);
    }

    @Override
    public ScoredItem<?> visit(ConstructorItem item) {
        return constructorItemEvaluator.evaluate(item);
    }

    @Override
    public ScoredItem<?> visit(MethodItem item) {
        return methodItemEvaluator.evaluate(item);
    }

    @Override
    public ScoredItem<?> visit(FieldItem item) {
        return fieldItemEvaluator.evaluate(item);
    }
}
