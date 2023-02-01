package hu.sed.evaluator.task.evaluator.semantic;

import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.TypeItem;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class JavaCodeServiceTest {

    ByteCodeManipulatorService javaCodeService;

    ItemFactory itemFactory;

    @BeforeEach
    public void setup() {
        javaCodeService = new ByteCodeManipulatorService();
        itemFactory = new ItemFactory();
    }

    @SneakyThrows
    @Test
    void addNewClass() {
        // GIVEN
        TypeItem typeItem = itemFactory.createItem(SubClass.class.getAnnotation(TypeCheck.class), SubClass.class);
        String className = SubClass.class.getPackageName() + ".NewSubClass";
        typeItem.setName(className);

        // WHEN
        javaCodeService.addClass(typeItem);

        // THEN
        assertDoesNotThrow(() -> Class.forName(className));
        assertThat(Class.forName(className).getModifiers()).isEqualTo(typeItem.getModifiers());
    }


    @SneakyThrows
    @Test
    void addNewEnum() {
        // GIVEN
        TypeItem typeItem = itemFactory.createItem(MyEnum.class.getAnnotation(TypeCheck.class), MyEnum.class);
        String enumName = MyEnum.class.getPackageName() + ".NewEnum";
        typeItem.setName(enumName);

        // WHEN
        javaCodeService.addClass(typeItem);

        // THEN
        assertDoesNotThrow(() -> Class.forName(enumName));
        Class<?> aClass = Class.forName(enumName);
        assertThat(aClass.isEnum()).isTrue();
    }

    @SneakyThrows
    @Test
    void addAbstractClass() {
        // GIVEN
        TypeItem typeItem = itemFactory.createItem(BaseClass.class.getAnnotation(TypeCheck.class), BaseClass.class);
        String className = BaseClass.class.getPackageName() + ".NewAbstractClass";
        typeItem.setName(className);

        // WHEN
        javaCodeService.addClass(typeItem);

        // THEN
        assertDoesNotThrow(() -> Class.forName(className));
        Class<?> newClass = Class.forName(className);
        assertThat(Modifier.isAbstract(newClass.getModifiers())).isTrue();
    }

    @Test
    void addDependentClasses() {
        // GIVEN
        TypeItem missingItem = itemFactory.createItem(SubClass.class.getAnnotation(TypeCheck.class), SubClass.class);
        String missingItemClassName = BaseClass.class.getPackageName() + ".NewSubClass";
        missingItem.setName(missingItemClassName);
        missingItem.setContainerClass(null);

        TypeItem subItem = itemFactory.createItem(SubClass.class.getAnnotation(TypeCheck.class), SubClass.class);
        String missingSubItemClassName = BaseClass.class.getPackageName() + ".NewSubSubClass";
        subItem.setName(missingSubItemClassName);
        subItem.setParentClazz(TypeDefinition.builder()
                .genericTypes(new TypeDefinition[0])
                .type(missingItemClassName)
                .build());
        subItem.setContainerClass(null);

        // WHEN
        javaCodeService.addClasses(Arrays.asList(subItem, missingItem));

        // THEN
        assertDoesNotThrow(() -> Class.forName(missingItemClassName));
        assertDoesNotThrow(() -> Class.forName(missingSubItemClassName));
    }

    @TypeCheck
    public abstract class BaseClass {
    }

    @TypeCheck
    public class SubClass extends BaseClass {

    }

    @TypeCheck
    public enum MyEnum {
    }
}
