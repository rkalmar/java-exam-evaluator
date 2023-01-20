package hu.sed.evaluator.item.semantic;

import hu.sed.evaluator.item.ScorableItem;
import hu.sed.evaluator.item.ItemVisitor;
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
public class TestItem extends ScorableItem {

    private static final String ALL_TEST_METHOD = "all";

    String testClass;

    String[] testMethods;

    String description;

    @Override
    public <R> R accept(ItemVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
