package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.ItemVisitor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ConstructorItem extends ExecutableItem {

    @Override
    public <R> R accept(ItemVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
