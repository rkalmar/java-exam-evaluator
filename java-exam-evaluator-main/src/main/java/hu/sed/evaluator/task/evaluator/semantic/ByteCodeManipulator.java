package hu.sed.evaluator.task.evaluator.semantic;

import hu.sed.evaluator.item.syntax.TypeItem;

import java.util.List;

public interface ByteCodeManipulator {

    void addClass(TypeItem typeItem);

    void addClasses(List<TypeItem> typeItems);
}
