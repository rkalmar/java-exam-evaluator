package hu.sed.evaluator.item.container;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RootItem extends ListItemContainer {

    String createdBy;

    LocalDateTime creationTime;

    @Override
    public String getContainerName() {
        return "root";
    }
}
