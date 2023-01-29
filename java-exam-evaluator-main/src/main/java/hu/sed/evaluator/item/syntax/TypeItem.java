package hu.sed.evaluator.item.syntax;

import com.fasterxml.jackson.annotation.JsonTypeName;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.ItemVisitor;
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
@JsonTypeName(value = "type")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeItem extends ScorableSyntaxItem implements ItemContainer {

    List<? extends Item> items;

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

    boolean interfaze;

    boolean enumeration;

    @Override
    public <R> R accept(ItemVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
