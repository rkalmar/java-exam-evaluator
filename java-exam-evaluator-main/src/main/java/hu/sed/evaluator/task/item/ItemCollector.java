package hu.sed.evaluator.task.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ItemCollector {

    ItemFactory itemFactory; // TODO dependency injection

    public RootItem collectItems(String packagePrefix) {
        Set<String> all = new Reflections(packagePrefix)
                .getAll(Scanners.SubTypes).stream()
                // filter for non-inner classes only
                .filter(pckg -> pckg.startsWith(packagePrefix) && !pckg.contains("$"))
                .collect(Collectors.toSet());

        RootItem.RootItemBuilder rootItemBuilder = RootItem.builder();

        all.forEach(clazz -> {
            try {
                Class.forName(clazz);// TODO
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        return rootItemBuilder.build();
    }
}
