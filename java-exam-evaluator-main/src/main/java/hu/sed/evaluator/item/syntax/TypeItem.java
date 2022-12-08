package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.container.ItemContainer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeItem extends BaseSyntaxItem implements ItemContainer {

    List<Item> items;

    boolean checkParentClazz;

    /**
     * Fully qualified name of class
     */
    String parentClazz;

    boolean checkInterfaces;

    /**
     * Array of fully qualified name of interfaces
     */
    String[] implementedInterfaces;

    boolean checkMethods;
}
