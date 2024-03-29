package hu.sed.evaluator.item.container;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.ItemVisitor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@JsonTypeName(value = "container")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListItemContainer implements ItemContainer, Item {

    String containerName;

    List<? extends Item> items;

    @JsonIgnore
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    @Override
    public <R> R accept(ItemVisitor<R> visitor) {
        throw new UnsupportedOperationException();
    }
}
