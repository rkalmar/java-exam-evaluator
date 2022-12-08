package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.element.Type;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FieldItem extends BaseSyntaxItem {

    Type type;

    @Override
    public String toString() {
        return "FieldItem{" +
                ", type=" + type +
                ", " + super.toString() +
                '}';
    }
}
