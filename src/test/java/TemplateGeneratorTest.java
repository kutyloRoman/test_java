import com.kutylo.PlaceholderInputException;
import com.kutylo.TemplateParser;
import com.kutylo.TemplateReplacer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.suite.api.IncludeTags;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;

@IncludeTags("valid")
@EnabledForJreRange(min = JRE.JAVA_8, max = JRE.JAVA_13)
@ExtendWith(WriteTestResultToFileExtension.class)
@ExtendWith(MockitoExtension.class)
class TemplateGeneratorTest {

  @Mock private TemplateReplacer templateReplacer;

  private TemplateParser templateParser;

  @BeforeEach
  void setUp() {
    templateParser = Mockito.spy(new TemplateParser(templateReplacer));
  }

  @Test
  @ValidResultTest
  final void whenInputVariableThenReplaceValueInTemplate() {
    Map<String, String> userInputs = setUpUserInputs();
    final String expectedResponse =
        "To: #{Andriy}\r\n"
            + "Subject: #{Test template subject}\r\n"
            + "Text: Dear, #{Roman} #{Kutylo},\r\n"
            + "#{Test text for template generator with l1}";
    Mockito.when(templateReplacer.replaceTags(eq(userInputs), Mockito.anyString()))
        .thenReturn(expectedResponse);
    String actualResponse = templateParser.parseTemplate(userInputs);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }

  @Test
  final void whenValueIsNotInputThenExceptionThrow() {
    Map<String, String> userInputs = setUpUnsupportedUserInputs();
    Mockito.when(templateReplacer.replaceTags(eq(userInputs), Mockito.anyString()))
        .thenThrow(new PlaceholderInputException(""));
    Assertions.assertThrows(
        PlaceholderInputException.class, () -> templateParser.parseTemplate(userInputs));
  }

  @Test
  @ValidResultTest
  final void whenInputVariableNotExistInTemplateThenDoNotReplaceVariable() {
    Map<String, String> userInputs = setUpdUserInputsWithUnsupportedValues();
    final String expectedResponse =
        "To: #{Andriy}\r\n"
            + "Subject: #{Test template subject}\r\n"
            + "Text: Dear, #{Roman} #{Kutylo},\r\n"
            + "#{Test text for template generator with l1}";
    Mockito.when(templateReplacer.replaceTags(eq(userInputs), Mockito.anyString()))
        .thenReturn(expectedResponse);
    String actualResponse = templateParser.parseTemplate(userInputs);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }

  @Test
  @ValidResultTest
  final void whenInputVariableInLatin1ThenReplaceValueInTemplate() {
    Map<String, String> userInputs = setUpdUserInputsWithL1();
    final String expectedResponse =
        new String(
            "To: #{Andriy}\r\n"
                + "Subject: #{Test template subject}\r\n"
                + "Text: Dear, #{Roman} #{Kutylo},\r\n"
                + "#{Test text * / for template % generator with l1}");
    Mockito.when(templateReplacer.replaceTags(eq(userInputs), Mockito.anyString()))
        .thenReturn(expectedResponse);
    String actualResponse = templateParser.parseTemplate(userInputs);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }

  @ValidResultTest
  @ParameterizedTest
  @MethodSource("inputProvider")
  void whenInputVariablesThenReplacePlaceholdersWithValuesInTemplate(Map<String, String> input) {
    Mockito.when(templateReplacer.replaceTags(Mockito.anyMap(), Mockito.anyString()))
        .thenAnswer(invocationOnMock -> getCommonOutput());
    String actualResponse = templateParser.parseTemplate(input);
    Assertions.assertEquals(getCommonOutput(), actualResponse);
  }

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    Mockito.when(templateReplacer.replaceTags(eq(setUpUserInputs()), Mockito.anyString()))
        .thenAnswer(invocationOnMock -> getCommonOutput());
    Mockito.when(
            templateReplacer.replaceTags(eq(setUpUnsupportedUserInputs()), Mockito.anyString()))
        .thenThrow(new PlaceholderInputException(""));
    Mockito.when(
            templateReplacer.replaceTags(
                eq(setUpdUserInputsWithUnsupportedValues()), Mockito.anyString()))
        .thenAnswer(invocationOnMock -> getCommonOutput());
    return Arrays.asList(
        DynamicTest.dynamicTest(
            "whenInputVariableThenReplaceValueInTemplate",
            () ->
                Assertions.assertEquals(
                    getCommonOutput(), templateParser.parseTemplate(setUpUserInputs()))),
        DynamicTest.dynamicTest(
            " whenValueIsNotInputThenExceptionThrow",
            () ->
                Assertions.assertThrows(
                    RuntimeException.class,
                    () -> templateParser.parseTemplate(setUpUnsupportedUserInputs()))),
        DynamicTest.dynamicTest(
            "whenInputVariableNotExistInTemplateThenDoNotReplaceVariable",
            () ->
                Assertions.assertEquals(
                    getCommonOutput(),
                    templateParser.parseTemplate(setUpdUserInputsWithUnsupportedValues()))));
  }

  static Stream<Map<String, String>> inputProvider() {
    return Stream.of(setUpUserInputs(), setUpdUserInputsWithUnsupportedValues());
  }

  private String getCommonOutput() {
    return "To: #{Andriy}\r\n"
        + "Subject: #{Test template subject}\r\n"
        + "Text: Dear, #{Roman} #{Kutylo},\r\n"
        + "#{Test text for template generator with l1}";
  }

  private static Map<String, String> setUpUserInputs() {
    Map<String, String> userInputs = new HashMap<>();
    userInputs.put("to", "Andriy");
    userInputs.put("subject", "Test template subject");
    userInputs.put("name", "Roman");
    userInputs.put("surname", "Kutylo");
    userInputs.put("text", "Test text for template generator with l1");
    return userInputs;
  }

  private static Map<String, String> setUpUnsupportedUserInputs() {
    Map<String, String> userInputs = new HashMap<>();
    userInputs.put("to", "Andriy");
    userInputs.put("subject", "Test template subject");
    userInputs.put("name", "Roman");
    userInputs.put("surname", "Kutylo");
    return userInputs;
  }

  private static Map<String, String> setUpdUserInputsWithUnsupportedValues() {
    Map<String, String> userInputs = new HashMap<>();
    userInputs.put("to", "Andriy");
    userInputs.put("subject", "Test template subject");
    userInputs.put("name", "Roman");
    userInputs.put("surname", "Kutylo");
    userInputs.put("text", "Test text for template generator with l1");
    userInputs.put("unsupported", "unsupported value");
    return userInputs;
  }

  private static Map<String, String> setUpdUserInputsWithL1() {
    Map<String, String> userInputs = new HashMap<>();
    userInputs.put("to", "Andriy");
    userInputs.put("subject", "Test template subject");
    userInputs.put("name", "Roman");
    userInputs.put("surname", "Kutylo");
    userInputs.put("text", "Test text * / for template % generator with l1");
    return userInputs;
  }
}
