package hu.sed.evaluator.task.evaluators.semantic;

import hu.sed.evaluator.task.evaluators.ScoredItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoredSemanticItem extends ScoredItem<String> {

}
