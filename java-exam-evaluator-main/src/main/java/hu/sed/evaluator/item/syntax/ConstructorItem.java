package hu.sed.evaluator.item.syntax;

import com.fasterxml.jackson.annotation.JsonTypeName;
import hu.sed.evaluator.item.ItemVisitor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@JsonTypeName(value = "constructor")
@SuperBuilder
public class ConstructorItem extends ExecutableItem {

    @Override
    public <R> R accept(ItemVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
