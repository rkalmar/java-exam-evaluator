package hu.sed.evaluator.item;

import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.item.syntax.ConstructorItem;
import hu.sed.evaluator.item.syntax.FieldItem;
import hu.sed.evaluator.item.syntax.MethodItem;
import hu.sed.evaluator.item.syntax.TypeItem;

public interface ItemVisitor<R> {

    R visit(TestItem item);

    R visit(TypeItem item);

    R visit(ConstructorItem item);

    R visit(MethodItem item);

    R visit(FieldItem item);
}
