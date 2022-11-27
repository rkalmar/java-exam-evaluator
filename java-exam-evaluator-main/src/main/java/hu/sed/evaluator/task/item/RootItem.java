package hu.sed.evaluator.task.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RootItem implements Item {

    @Singular
    List<BaseItem> items;
}
