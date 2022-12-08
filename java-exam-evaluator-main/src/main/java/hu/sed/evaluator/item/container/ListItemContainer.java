package hu.sed.evaluator.item.container;

import hu.sed.evaluator.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListItemContainer implements ItemContainer, Item {

    String containerName;

    List<Item> items;
}
