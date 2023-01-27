package hu.sed.evaluator.task;

import com.google.inject.Inject;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.doc.uml.UmlRepresentation;
import hu.sed.evaluator.task.doc.uml.UmlUtility;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DocExporter implements Task<Void, TaskArgument> {

    @Override
    public Void execute(TaskArgument argument) {
        log.debug("Executing document generator..");

        UmlRepresentation umlRepresentation = UmlUtility.createUmlRepresentation(argument.getExamPackage());

        SourceStringReader reader = new SourceStringReader(umlRepresentation.represent());

        exportUmlToPng(reader, argument.getExamDocOutputFolder());
        exportUmlToSvg(reader, argument.getExamDocOutputFolder());
        return null;
    }

    @SneakyThrows
    private void exportUmlToSvg(SourceStringReader reader, String outputFolder) {
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            reader.outputImage(os, new FileFormatOption(FileFormat.SVG));

            // The XML is stored into svg
            final String svg = os.toString(StandardCharsets.UTF_8);
            try (OutputStream outputStream = new FileOutputStream(outputFolder + File.separator + "uml.svg")) {
                outputStream.write(svg.getBytes());
            }
        }
    }

    @SneakyThrows
    private void exportUmlToPng(SourceStringReader reader, String outputFolder) {
        try (OutputStream outputStream = new FileOutputStream(outputFolder + File.separator + "uml.png")) {
            reader.outputImage(outputStream);
        }
    }
}
