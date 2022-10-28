package hu.sed.evaluator.task.item;

import hu.sed.evaluator.task.item.syntax.TypeItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RootItem implements Item {

    List<TypeItem> classItems;
}
