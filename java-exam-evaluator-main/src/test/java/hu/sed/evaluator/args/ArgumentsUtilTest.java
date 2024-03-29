package hu.sed.evaluator.args;

import hu.sed.evaluator.task.argument.TaskArgument;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgumentsUtilTest {

    @SneakyThrows
    @Test
    public void testInvalidParam() {
        // GIVEN
        String[] args = {"-whatever"};
        // WHEN
        assertThrows(InvalidArgumentException.class, () -> ArgumentsUtil.parseArguments(args));
    }

    @SneakyThrows
    @Test
    public void testTaskMissing() {
        // GIVEN
        String[] args = {};
        // WHEN
        assertThrows(MissingArgumentsException.class, () -> ArgumentsUtil.parseArguments(args));
    }

    @SneakyThrows
    @Test
    public void testMissingTaskArgument() {
        // GIVEN
        String[] args = {"-task"};
        // WHEN
        MissingArgumentsException missingArgumentsException = assertThrows(MissingArgumentsException.class, () -> ArgumentsUtil.parseArguments(args));
        // THEN
        assertThat(missingArgumentsException.getMessage(), containsString("Missing argument for option: task"));
    }

    @SneakyThrows
    @Test
    public void testTaskValid() {
        // GIVEN
        String[] args = {"-task", "evaluate"};
        // WHEN
        MissingArgumentsException missingArgumentsException = assertThrows(MissingArgumentsException.class, () -> ArgumentsUtil.parseArguments(args));
        // THEN
        assertThat(missingArgumentsException.getMessage(), containsString("Following arguments are required"));
    }

    @SneakyThrows
    @Test
    public void testTaskInvalid() {
        // GIVEN
        String[] args = {"-task", "incorrect-param"};
        // WHEN
        InvalidArgumentException missingArgumentsException = assertThrows(InvalidArgumentException.class, () -> ArgumentsUtil.parseArguments(args));
        // THEN
        assertThat(missingArgumentsException.getMessage(), containsString("Invalid argument"));
    }

    @Test
    public void testTaskParamProvidedForValidateExam() {
        // GIVEN

        String[] args = {"--task", "validate", "-examPackage", "com.whatever"};
        //WHEN
        assertDoesNotThrow(() -> ArgumentsUtil.parseArguments(args));
    }

    @Test
    public void testTaskParamProvidedForExportExam() {
        // GIVEN
        String[] args = {"--task", "export", "-examPackage", "com.whatever", "-outputFolder", "~/"};
        //WHEN
        assertDoesNotThrow(() -> ArgumentsUtil.parseArguments(args));
    }

    @SneakyThrows
    @Test
    public void testTaskParamsProvided() {
        // GIVEN
        String[] args = {"--task", "evaluate", "-examItemFile", "~/examfile", "-outputFolder", "~/", "-solutionClassPath", "~/somewhere-over-the-rainbow"};
        //WHEN
        assertDoesNotThrow(() -> ArgumentsUtil.parseArguments(args));
        TaskArgument taskArgument = ArgumentsUtil.parseArguments(args);
        assertFalse(taskArgument.isEnableByteCodeManipulation());
    }

    @SneakyThrows
    @Test
    public void testEvaluateWithByteCodeManipulationProvided() {
        // GIVEN
        String[] args = {"--task", "evaluate", "-examItemFile", "~/examfile", "-outputFolder", "~/", "--enableByteCodeManipulation", "-solutionClassPath", "~/somewhere-over-the-rainbow"};
        //WHEN
        assertDoesNotThrow(() -> ArgumentsUtil.parseArguments(args));
        TaskArgument taskArgument = ArgumentsUtil.parseArguments(args);
        assertTrue(taskArgument.isEnableByteCodeManipulation());
    }
}
