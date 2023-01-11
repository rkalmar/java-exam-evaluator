package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.BaseItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.beans.Transient;
import java.lang.reflect.Modifier;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseSyntaxItem extends BaseItem {

    String containerClass;

    int modifiers;

    boolean checkModifiers;

    String name;

    @Transient
    public String getReadableModifiers() {
        return Modifier.toString(modifiers);
    }

    @Override
    public String toString() {
        return " score=" + this.getScore() +
                ", name=" + name +
                ", modifiers=" + getReadableModifiers() +
                ", checkModifiers=" + checkModifiers;
    }
}
