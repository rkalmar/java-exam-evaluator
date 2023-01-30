package hu.sed.evaluator.task.evaluator.semantic;
import hu.sed.evaluator.item.ItemFactory;
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
//        TypeItem typeItem = itemFactory.createItem(Kamion.class.getAnnotation(TypeCheck.class), Kamion.class);
//        typeItem.setName(Kamion.class.getPackageName() + ".NewKamion");
//
//        javaCodeService.addClass(typeItem);
    }
}
