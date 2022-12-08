package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.BaseItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.beans.Transient;
import java.lang.reflect.Modifier;

@Data
@SuperBuilder
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
        return " points=" + getPoints() +
                ", name=" + name +
                ", modifiers=" + getReadableModifiers() +
                ", checkModifiers=" + checkModifiers;
    }
}
