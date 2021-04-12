package com.kutylo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class Utils {
  private Utils() {
    throw new IllegalStateException("Utility class");
  }

  public static String getResourceFileAsString(String fileName) {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    File template = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
    if (template.exists()) {
      try {
        return Files.readString(template.toPath(), StandardCharsets.ISO_8859_1);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    throw new RuntimeException("File not exist!");
  }
}
