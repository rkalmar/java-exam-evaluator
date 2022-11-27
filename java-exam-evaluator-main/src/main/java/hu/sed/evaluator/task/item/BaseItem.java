package hu.sed.evaluator.task.item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseItem implements Serializable {

    int points;
}