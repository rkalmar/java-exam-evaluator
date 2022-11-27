package hu.sed.evaluator.task.item;

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

    int points;

    int modifiers;

    boolean checkModifiers;

    String name;

    @Transient
    public String getReadableModifiers() {
        return Modifier.toString(modifiers);
    }

    @Override
    public String toString() {
        return " points=" + points +
                ", name=" + name +
                ", modifiers=" + getReadableModifiers() +
                ", checkModifiers=" + checkModifiers;
    }
}
