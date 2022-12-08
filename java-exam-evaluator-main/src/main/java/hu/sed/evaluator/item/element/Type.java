package hu.sed.evaluator.item.element;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;

@Data
@Builder
public final class Type {

    String type;

    @Builder.Default
    Type[] genericTypes = {};

    @Override
    public String toString() {
        return "Type{'" +
                type + '\'' +
                (getGenericTypes().length > 0 ? ", genericTypes=" + Arrays.toString(genericTypes) : "") +
                '}';
    }
}
