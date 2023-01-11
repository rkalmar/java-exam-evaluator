package hu.sed.evaluator.item.semantic;

import hu.sed.evaluator.item.BaseItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestItem extends BaseItem {

    private static final String ALL_TEST_METHOD = "all";

    String testClass;

    String[] testMethods;

    String description;
}
