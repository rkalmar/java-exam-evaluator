package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.element.TypeDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class MethodItem extends ExecutableItem {

    TypeDefinition returnType;

    boolean checkOverrideAnnotation;
}
