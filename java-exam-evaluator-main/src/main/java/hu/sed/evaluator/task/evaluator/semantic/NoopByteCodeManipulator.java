package hu.sed.evaluator.task.evaluator.semantic;

import hu.sed.evaluator.item.syntax.TypeItem;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class NoopByteCodeManipulator implements ByteCodeManipulator {

    @Override
    public void addClass(TypeItem typeItem) {
        log.info("Type code manipulation is not enabled. Item is skipped {}", typeItem.identifier());
    }

    @Override
    public void addClasses(List<TypeItem> typeItems) {
        log.info("Type code manipulation is not enabled. Following items are skipped: ");
        typeItems.forEach(typeItem -> log.info("- {}", typeItem.identifier()));
    }
}
