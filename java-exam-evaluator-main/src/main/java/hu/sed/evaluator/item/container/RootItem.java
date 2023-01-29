package hu.sed.evaluator.item.container;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonTypeName(value = "root")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RootItem extends ListItemContainer {

    String createdBy;

    LocalDateTime creationTime;

}
