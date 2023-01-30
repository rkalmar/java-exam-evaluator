package hu.sed.evaluator.task.evaluator.semantic;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.syntax.TypeItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.util.List;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class JavaCodeService implements Opcodes {

    @SneakyThrows
    public void addClass(TypeItem typeItem) {
        // https://github.com/miho/asm-playground/blob/master/ASMSample01/jars/asm-4.0/examples/helloworld/src/Helloworld.java
        ClassWriter classWriter = new ClassWriter(0);
        String[] parts = typeItem.getName().split("\\.");
        String className = parts[parts.length - 1];
        String packageName = typeItem.getName().replace("." + className, "")
                .replace(".", "/");
        String parentClassName = typeItem.getParentClazz().getType().replace(".", "/");

        classWriter.visit(V1_1, ACC_PUBLIC, packageName + "/" + className, null,
                parentClassName, null);

        byte[] code = classWriter.toByteArray();
        this.defineClass(typeItem.getName(), code);
    }

    public void addClasses(List<TypeItem> typeItems) {
        // TODO must be ordered by it's dependencies
        typeItems.forEach(this::addClass);
    }

    private void defineClass(String className, byte[] code) {
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class<?> cls = Class.forName("java.lang.ClassLoader");
            java.lang.reflect.Method method = cls.getDeclaredMethod("defineClass",
                    String.class, byte[].class, int.class, int.class);

            // requires: --add-opens java.base/java.lang=ALL-UNNAMED
            method.setAccessible(true);
            try {
                Object[] args =
                        new Object[]{className, code, 0, code.length};
                method.invoke(loader, args);
            } finally {
                method.setAccessible(false);
            }
        } catch (Exception e) {
            log.error("Could not register class.. {}", className);
        }
    }
}
