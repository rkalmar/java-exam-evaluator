package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.element.TypeDefinition;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ExecutableItem extends ScorableSyntaxItem {

    TypeDefinition[] parameters;

    TypeDefinition[] exceptions;

    boolean checkExceptions;

    @Override
    public String identifier() {
        return super.identifier() + Arrays.toString(parameters);
    }
}
