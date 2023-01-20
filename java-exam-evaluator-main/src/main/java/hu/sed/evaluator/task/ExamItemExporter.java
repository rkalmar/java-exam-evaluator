package hu.sed.evaluator.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.container.RootItem;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.Writer;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExamItemExporter implements Task<Void, ExamItemExporter.ExportParam> {

    @Override
    public Void execute(ExportParam argument) {
        return null;
    }

    public interface ExportParam {

        RootItem getRootItem();

        Writer getWriter();
    }
}
