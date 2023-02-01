package hu.sed.evaluator.task.evaluator.semantic;

import com.google.inject.Inject;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.TypeItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ByteCodeManipulatorService implements ByteCodeManipulator {

    @Override
    public void addClass(TypeItem typeItem) {
        try {
            log.info("Injecting missing typeItem to classLoader: {}", typeItem.identifier());
            DynamicType.Unloaded<?> dynamicType;
            if (typeItem.isEnumeration()) {
                dynamicType = new ByteBuddy().makeEnumeration("NO_VALUE")
                        .name(typeItem.getName())
                        .modifiers(typeItem.getModifiers())
                        .make();

            } else {
                dynamicType = new ByteBuddy()
                        .subclass(Class.forName(typeItem.getParentClazz().getType()))
                        .modifiers(typeItem.getModifiers())
                        .name(typeItem.getName())
                        .make();
            }
            dynamicType.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded();
        } catch (Exception e) {
            log.warn("Failed to inject class to classLoader: {}", typeItem.getName());
        }
    }

    @Override
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
