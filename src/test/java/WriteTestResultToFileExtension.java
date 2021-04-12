import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.Optional;

public class WriteTestResultToFileExtension implements TestWatcher {

  private String resultFormat = "%s: %s\n";

  private enum TestResultStatus {
    SUCCESSFUL,
    ABORTED,
    FAILED,
    DISABLED;
  }

  @Override
  public void testDisabled(ExtensionContext context, Optional<String> reason) {
    String resultToWrite =
        new Formatter()
            .format(resultFormat, context.getDisplayName(), TestResultStatus.DISABLED)
            .toString();
    writeToFile(resultToWrite);
  }

  @Override
  public void testSuccessful(ExtensionContext context) {
    String resultToWrite =
        new Formatter()
            .format(resultFormat, context.getDisplayName(), TestResultStatus.SUCCESSFUL)
            .toString();
    writeToFile(resultToWrite);
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    String resultToWrite =
        new Formatter()
            .format(resultFormat, context.getDisplayName(), TestResultStatus.FAILED)
            .toString();
    writeToFile(resultToWrite);
  }

  @Override
  public void testAborted(ExtensionContext context,  Throwable cause) {
    String resultToWrite =
        new Formatter()
            .format(resultFormat, context.getDisplayName(), TestResultStatus.ABORTED)
            .toString();
    writeToFile(resultToWrite);
  }

  private void writeToFile(String replacedTemplate) {
    String path = "D:/test-result.txt";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
      writer.write(replacedTemplate);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
