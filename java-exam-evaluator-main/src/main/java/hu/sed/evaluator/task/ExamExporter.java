package hu.sed.evaluator.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExamExporter implements Task<Void> {

    DocExporter docExporter;
    ExamItemExporter examItemExporter;

    @Override
    public Void execute() {
        docExporter.execute();
        examItemExporter.execute();
        return null;
    }
}
