package hu.sed.evaluator.item.semantic;

import com.fasterxml.jackson.annotation.JsonTypeName;
import hu.sed.evaluator.item.ItemVisitor;
import hu.sed.evaluator.item.ScorableItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonTypeName(value = "test")
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

    @Override
    public String identifier() {
        return getClass().getSimpleName() + "-" + testClass + Arrays.toString(testMethods) + " - " + description;
    }
}
