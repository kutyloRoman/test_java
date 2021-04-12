import com.kutylo.TemplateParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TemplateGeneratorTest {

  private TemplateParser templateParser = new TemplateParser();

  @Test
  public final void whenInputVariableThenReplaceValueInTemplate() {
    Map<String, String> userInputs = setUpUserInputs();
    final String expectedResponse =
        new String(
            "To: #{Andriy}\r\n"
                + "Subject: #{Test template subject}\r\n"
                + "Text: Dear, #{Roman} #{Kutylo},\r\n"
                + "#{Test text for template generator with l1}");
    String actualResponse = templateParser.parseTemplate(userInputs);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public final void whenValueIsNotInputThenExceptionThrow() {
    Map<String, String> userInputs = setUpUnsupportedUserInputs();
    Assertions.assertThrows(
        RuntimeException.class, () -> templateParser.parseTemplate(userInputs));
  }

  @Test
  public final void whenInputVariableNotExistInTemplateThenDoNotReplaceVariable() {
    Map<String, String> userInputs = setUpdUserInputsWithUnsupportedValues();
    final String expectedResponse =
        new String(
            "To: #{Andriy}\r\n"
                + "Subject: #{Test template subject}\r\n"
                + "Text: Dear, #{Roman} #{Kutylo},\r\n"
                + "#{Test text for template generator with l1}");
    String actualResponse = templateParser.parseTemplate(userInputs);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public final void whenInputVariableInLatin1ThenReplaceValueInTemplate() {
    Map<String, String> userInputs = setUpdUserInputsWithL1();
    final String expectedResponse =
        new String(
            "To: #{Andriy}\r\n"
                + "Subject: #{Test template subject}\r\n"
                + "Text: Dear, #{Roman} #{Kutylo},\r\n"
                + "#{Test text * / for template % generator with l1}");
    String actualResponse = templateParser.parseTemplate(userInputs);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }

  private Map<String, String> setUpUserInputs() {
    Map<String, String> userInputs = new HashMap<>();
    userInputs.put("to", "Andriy");
    userInputs.put("subject", "Test template subject");
    userInputs.put("name", "Roman");
    userInputs.put("surname", "Kutylo");
    userInputs.put("text", "Test text for template generator with l1");
    return userInputs;
  }

  private Map<String, String> setUpUnsupportedUserInputs() {
    Map<String, String> userInputs = new HashMap<>();
    userInputs.put("to", "Andriy");
    userInputs.put("subject", "Test template subject");
    userInputs.put("name", "Roman");
    userInputs.put("surname", "Kutylo");
    return userInputs;
  }

  private Map<String, String> setUpdUserInputsWithUnsupportedValues() {
    Map<String, String> userInputs = new HashMap<>();
    userInputs.put("to", "Andriy");
    userInputs.put("subject", "Test template subject");
    userInputs.put("name", "Roman");
    userInputs.put("surname", "Kutylo");
    userInputs.put("text", "Test text for template generator with l1");
    userInputs.put("unsupported", "unsupported value");
    return userInputs;
  }

  private Map<String, String> setUpdUserInputsWithL1() {
    Map<String, String> userInputs = new HashMap<>();
    userInputs.put("to", "Andriy");
    userInputs.put("subject", "Test template subject");
    userInputs.put("name", "Roman");
    userInputs.put("surname", "Kutylo");
    userInputs.put("text", "Test text * / for template % generator with l1");
    return userInputs;
  }
}
