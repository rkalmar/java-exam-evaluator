package hu.sed.evaluator.item.syntax;

import hu.sed.evaluator.item.ItemVisitor;
import hu.sed.evaluator.item.element.TypeDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class MethodItem extends ExecutableItem {

    TypeDefinition returnType;

    boolean checkOverrideAnnotation;

    @Override
    public <R> R accept(ItemVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
