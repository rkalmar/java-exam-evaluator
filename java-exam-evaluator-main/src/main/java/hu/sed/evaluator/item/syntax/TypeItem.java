package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.container.ItemContainer;
import hu.sed.evaluator.item.element.TypeDefinition;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeItem extends BaseSyntaxItem implements ItemContainer {

    List<Item> items;

    boolean checkParentClazz;

    /**
     * Fully qualified name of class
     */
    TypeDefinition parentClazz;

    boolean checkInterfaces;

    /**
     * Array of fully qualified name of interfaces
     */
    TypeDefinition[] implementedInterfaces;

    boolean interfce;

    @Override
    public String getIdentifier() {
        return this.getName();
    }
}
