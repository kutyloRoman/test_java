package com.kutylo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PlaceholderValueInputUtils {

  public static Map<String, String> readInputFromConsole() {
    Scanner in = new Scanner(System.in);
    Map<String, String> inputTags = new HashMap<>();
    String placeholder = "";
    do {
      System.out.println("Input placeholder");
      placeholder =
          new String(in.nextLine().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
      System.out.println("Input value");
      String value =
          new String(in.nextLine().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
      inputTags.put(placeholder, value);
    } while (!placeholder.equalsIgnoreCase("stop"));
    return inputTags;
  }

  public static Map<String, String> readInputFromFile(String path) {
    Map<String, String> inputTags = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
      String currentLine;
      while ((currentLine = reader.readLine()) != null) {
        String placeholder = currentLine.substring(0, currentLine.indexOf(" "));
        String value = currentLine.substring(currentLine.indexOf(" ") + 1);
        inputTags.put(placeholder, value);
      }
      return inputTags;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Collections.emptyMap();
  }

  public static void writeToFile(String replacedTemplate, String path) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      writer.write(replacedTemplate);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
