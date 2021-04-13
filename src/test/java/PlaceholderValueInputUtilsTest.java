import com.kutylo.PlaceholderValueInputUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceholderValueInputUtilsTest {

  @Test
  void whenWriteToFileResultThenFileIsExisted(@TempDir Path tempDir) {
    Path output = tempDir.resolve("output.txt");

    PlaceholderValueInputUtils.writeToFile("test content", output.toString());
    assertAll(
        () -> assertTrue(Files.exists(output)),
        () -> assertLinesMatch(List.of("test content"), Files.readAllLines(output)));
  }

  @Test
  void whenReadFromFileThenMapWithInputValues(){
    try(MockedStatic<PlaceholderValueInputUtils> mock = Mockito.mockStatic(PlaceholderValueInputUtils.class)){
        mock.when(()->PlaceholderValueInputUtils.readInputFromFile(Mockito.anyString()))
            .thenAnswer(invocationOnMock -> Collections.emptyMap());
      assertEquals(0, PlaceholderValueInputUtils.readInputFromFile("D:/test.txt").size());
    }

  }
}
