package hu.sed.evaluator.item.element;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode
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
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < genericTypes.length; i++) {
            builder.append(genericTypes[i]);
            if (i != genericTypes.length - 1) {
                builder.append(", ");
            }
        }
        return type +
                (getGenericTypes().length > 0 ? "<" + builder + ">" : "");
    }
}
