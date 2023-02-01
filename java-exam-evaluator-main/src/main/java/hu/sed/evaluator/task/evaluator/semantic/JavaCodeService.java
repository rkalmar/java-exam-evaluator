package hu.sed.evaluator.task.evaluator.semantic;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.TypeItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class JavaCodeService {

    public void addClass(TypeItem typeItem) {
        try {
            new ByteBuddy()
                    .subclass(Class.forName(typeItem.getParentClazz().getType()))
                    .name(typeItem.getName())
                    .make()
                    .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded();
        } catch (Exception e) {
            log.warn("Failed to inject class to classLoader: {}", typeItem.getName());
        }
    }

    public void addClasses(List<TypeItem> typeItems) {
        Map<TypeDefinition, List<TypeItem>> classesByParentClass
                = typeItems.stream().collect(Collectors.groupingBy(TypeItem::getParentClazz));

        do {
            Optional<TypeDefinition> availableClass = getAvailableClass(classesByParentClass.keySet());
            if (availableClass.isPresent()) {
                List<TypeItem> subTypes = classesByParentClass.get(availableClass.get());
                subTypes.forEach(this::addClass);
                classesByParentClass.remove(availableClass.get());
            } else {
                break;
            }
        }
        while (!classesByParentClass.isEmpty());

        if (!classesByParentClass.isEmpty()) {
            log.warn("Following types could not be injected: ");
            for (TypeDefinition typeDefinition : classesByParentClass.keySet()) {
                for (TypeItem typeItem : classesByParentClass.get(typeDefinition)) {
                    log.warn("Missing - {}, parentClass: {}", typeItem.getName(), typeItem.getParentClazz().getType());
                }
            }
        }
    }

    private Optional<TypeDefinition> getAvailableClass(Set<TypeDefinition> typeDefinitions) {
        return typeDefinitions.stream()
                .filter(typeDef -> {
                    try {
                        Class.forName(typeDef.getType());
                        return true;
                    } catch (ClassNotFoundException e) {
                        return false;
                    }
                }).findFirst();
    }
}
