package hu.sed.evaluator.task.evaluator.semantic;

import hu.sed.evaluator.task.ScoredItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoredSemanticItem extends ScoredItem<String> {
}
