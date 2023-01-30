package hu.sed.evaluator.task.evaluator.semantic;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.TypeItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JavaCodeServiceTest {

    JavaCodeService javaCodeService;

    ItemFactory itemFactory;

    @BeforeEach
    public void setup() {
        javaCodeService = new JavaCodeService();
        itemFactory = new ItemFactory();
    }

    @Test
    void name() {
        // WHEN
        TypeItem typeItem = itemFactory.createItem(Akarmi.class.getAnnotation(TypeCheck.class), Akarmi.class);
        typeItem.setName("hu.sed.evaluator.generated.NewClass");

        javaCodeService.addClass(typeItem);
    }

    @TypeCheck
    public static class Akarmi {

    }
}
