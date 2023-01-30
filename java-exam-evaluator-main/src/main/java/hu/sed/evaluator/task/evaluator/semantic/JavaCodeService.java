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

import java.io.FileOutputStream;
import java.util.List;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class JavaCodeService extends ClassLoader implements Opcodes {

    @SneakyThrows
    public void addClass(TypeItem typeItem) {
//        if (typeItem.isInterfaze()) {
//
//        }
//        if (typeItem.isEnumeration()) {
//
//        }
//        Class<?> parentClass = null;
//        if (typeItem.getParentClazz() != null) {
//            parentClass = Class.forName(typeItem.getParentClazz().getType());
//            if (Modifier.isAbstract(parentClass.getModifiers())) {
//                ProxyFactory proxyFactory = new ProxyFactory();
//                proxyFactory.setSuperclass(parentClass);
//                proxyFactory.setFilter(method -> Modifier.isAbstract(method.getModifiers()));
//                MethodHandler handler = (self, method, proceed, args) -> {
//                    log.trace("Fake method called {}", method.getName());
//                    return null;
//                };
//                parentClass = proxyFactory.createClass();
//            }
//        }

        // https://github.com/miho/asm-playground/blob/master/ASMSample01/jars/asm-4.0/examples/helloworld/src/Helloworld.java
        ClassWriter classWriter = new ClassWriter(0);
        String[] parts = typeItem.getName().split("\\.");
        String className = parts[parts.length -1];
        String packageName = typeItem.getName().replace("." + className, "")
                .replace(".", "/");
        classWriter.visit(V1_1, ACC_PUBLIC, packageName + "/" + className, null, "java/lang/Object", null);

        byte[] code = classWriter.toByteArray();

        FileOutputStream fos = new FileOutputStream(className + ".class");
        fos.write(code);
        fos.close();

        JavaCodeService loader = new JavaCodeService();
        Class<?> exampleClass = loader.defineClass(typeItem.getName(), code, 0, code.length);
        System.out.println(exampleClass.getName());
    }

    public void addClasses(List<TypeItem> typeItems) {
        // must be ordered by it's dependencies
        typeItems.forEach(this::addClass);
    }
}
