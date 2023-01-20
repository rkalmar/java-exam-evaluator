package hu.sed.evaluator.item.syntax;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.sed.evaluator.item.ScorableItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Modifier;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ScorableSyntaxItem extends ScorableItem {

    String containerClass;

    int modifiers;

    boolean checkModifiers;

    String name;

    @JsonIgnore
    public String getReadableModifiers() {
        return Modifier.toString(modifiers);
    }

    @Override
    public String identifier() {
        return getClass().getSimpleName() + "." + (StringUtils.isNotBlank(getContainerClass()) ? getContainerClass() + "." : "") + getName();
    }

}
