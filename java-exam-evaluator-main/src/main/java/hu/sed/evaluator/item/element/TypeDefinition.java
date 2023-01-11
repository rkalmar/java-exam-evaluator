package hu.sed.evaluator.item.element;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class TypeDefinition {

    String type;

    @Builder.Default
    TypeDefinition[] genericTypes = {};

    @Override
    public String toString() {
        return "Type{'" +
                type + '\'' +
                (getGenericTypes().length > 0 ? ", genericTypes=" + Arrays.toString(genericTypes) : "") +
                '}';
    }
}
