package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.element.TypeDefinition;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MethodItem extends BaseSyntaxItem {

    TypeDefinition returnType;

    TypeDefinition[] parameters;

    TypeDefinition[] exceptions;

    boolean checkOverride;

    boolean checkExceptions;
}
