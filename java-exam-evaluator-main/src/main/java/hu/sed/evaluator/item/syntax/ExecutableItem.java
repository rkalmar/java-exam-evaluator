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
public abstract class ExecutableItem extends ScorableSyntaxItem {

    TypeDefinition[] parameters;

    TypeDefinition[] exceptions;

    boolean checkExceptions;

    @Override
    public String identifier() {
        StringBuilder params = new StringBuilder();
        params.append('(');
        for (int i = 0; i < parameters.length; i++) {
            params.append(parameters[i]);
            if (parameters.length - 1 != i) {
                params.append(", ");
            }
        }
        params.append(")");

        return super.identifier() + params;
    }
}
