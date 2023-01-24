package hu.sed.evaluator.item;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import hu.sed.evaluator.item.container.ListItemContainer;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.item.syntax.ConstructorItem;
import hu.sed.evaluator.item.syntax.FieldItem;
import hu.sed.evaluator.item.syntax.MethodItem;
import hu.sed.evaluator.item.syntax.TypeItem;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "itemType")
@JsonSubTypes({
        @Type(value = FieldItem.class, name = "field"),
        @Type(value = MethodItem.class, name = "method"),
        @Type(value = ConstructorItem.class, name = "constructor"),
        @Type(value = TypeItem.class, name = "type"),
        @Type(value = TestItem.class, name = "test"),
        @Type(value = ListItemContainer.class, name = "container"),
        @Type(value = RootItem.class, name = "root")}
    )
public interface Item extends Serializable {

    <R> R accept(ItemVisitor<R> visitor);
}
