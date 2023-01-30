package hu.sed.evaluator.task.evaluator.syntax;

import hu.sed.evaluator.task.ScoredItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoredSyntaxItem extends ScoredItem<SyntaxElement> {

    public boolean itemExists() {
        return !getUnsuccessfulChecks().contains(SyntaxElement.EXISTENCE);
    }
}
