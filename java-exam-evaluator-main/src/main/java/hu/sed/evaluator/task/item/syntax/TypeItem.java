package hu.sed.evaluator.task.item.syntax;

import hu.sed.evaluator.task.item.BaseSyntaxItem;
import hu.sed.evaluator.task.item.Item;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeItem extends BaseSyntaxItem {

    List<Item> childItems;

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
}
