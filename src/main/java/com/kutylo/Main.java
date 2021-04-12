package com.kutylo;

import java.util.HashMap;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    Map<String, String> userInputs = new HashMap<>();
    if (args.length == 2) {
      userInputs = PlaceholderValueInputUtils.readInputFromFile(args[0]);
    } else {
      userInputs = PlaceholderValueInputUtils.readInputFromConsole();
    }
    TemplateParser templateParser = new TemplateParser();
    String parsedMail = templateParser.parseTemplate(userInputs);
  }
}
