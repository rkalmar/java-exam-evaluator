package hu.sed.evaluator.item.syntax;

import com.fasterxml.jackson.annotation.JsonTypeName;
import hu.sed.evaluator.item.ItemVisitor;
import hu.sed.evaluator.item.element.TypeDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonTypeName(value = "method")
@AllArgsConstructor
public class MethodItem extends ExecutableItem {

    TypeDefinition returnType;

    @Override
    public <R> R accept(ItemVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
