package hu.sed.evaluator.task.item.syntax;

import hu.sed.evaluator.task.item.BaseItem;
import hu.sed.evaluator.task.item.element.Type;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MethodItem extends BaseItem {

    Type returnType;

    Type[] parameters;

    Type[] exceptions;

    boolean checkOverride;

    boolean checkExceptions;

    boolean constructor;
}
