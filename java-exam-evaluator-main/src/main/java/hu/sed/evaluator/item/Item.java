package hu.sed.evaluator.item;

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
        @JsonSubTypes.Type(value = FieldItem.class, name = "field"),
        @JsonSubTypes.Type(value = MethodItem.class, name = "method"),
        @JsonSubTypes.Type(value = ConstructorItem.class, name = "constructor"),
        @JsonSubTypes.Type(value = TypeItem.class, name = "type"),
        @JsonSubTypes.Type(value = TestItem.class, name = "test"),
        @JsonSubTypes.Type(value = ListItemContainer.class, name = "container"),
        @JsonSubTypes.Type(value = RootItem.class, name = "root")}
)
public interface Item extends Serializable {

    <R> R accept(ItemVisitor<R> visitor);
}
