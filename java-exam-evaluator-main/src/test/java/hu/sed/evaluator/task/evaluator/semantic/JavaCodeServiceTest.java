package hu.sed.evaluator.task.evaluator.semantic;

import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.TypeItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class JavaCodeServiceTest {

    JavaCodeService javaCodeService;

    ItemFactory itemFactory;

    @BeforeEach
    public void setup() {
        javaCodeService = new JavaCodeService();
        itemFactory = new ItemFactory();
    }

    @Test
    void loadNewClass() {
        // GIVEN
        TypeItem typeItem = itemFactory.createItem(SubClass.class.getAnnotation(TypeCheck.class), SubClass.class);
        String className = SubClass.class.getPackageName() + ".NewSubClass";
        typeItem.setName(className);

        // WHEN
        javaCodeService.addClass(typeItem);

        // THEN
        assertDoesNotThrow(() -> Class.forName(className));
    }

    @Test
    void loadDependentClasses() {
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

    public abstract class BaseClass {
    }

    @TypeCheck
    public class SubClass extends BaseClass {

    }
}
