import com.kutylo.PlaceholderValueInputUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceholderValueInputUtilsTest {

  @Test
  public void whenWriteToFileResultThenFileIsExisted(@TempDir Path tempDir) {
    Path output = tempDir.resolve("output.txt");

    PlaceholderValueInputUtils.writeToFile("test content", output.toString());
    assertAll(
        () -> assertTrue(Files.exists(output)),
        () -> assertLinesMatch(List.of("test content"), Files.readAllLines(output)));
  }
}
