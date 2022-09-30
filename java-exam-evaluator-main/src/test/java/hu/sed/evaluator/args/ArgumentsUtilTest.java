package hu.sed.evaluator.args;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        String[] args = {"-task", "evaluateExam"};
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

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"validateExam", "generateUml"})
    public void testTaskParamsProvided(String task) {
        // GIVEN
        String[] args = {"--task", task, "-examPackage", "com.whatever"};
        //WHEN
        assertDoesNotThrow(() -> ArgumentsUtil.parseArguments(args));
    }

    @Test
    public void testTaskParamsProvided() {
        // GIVEN
        String[] args = {"--task", "evaluateExam", "-examPackage", "hu.whatever.exam", "-solutionPackage", "hu.whatever.solution"};
        //WHEN
        assertDoesNotThrow(() -> ArgumentsUtil.parseArguments(args));
    }
}
