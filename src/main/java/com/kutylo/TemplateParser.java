package com.kutylo;

import java.util.Map;

public class TemplateParser {

  private final TemplateReplacer templateReplacer = new TemplateReplacer();
  private final String template = Utils.getResourceFileAsString("template/template.txt");

  public String parseTemplate(Map<String, String> userInputs) {
    String parsedTemplate = templateReplacer.replaceTags(userInputs, template);
    System.out.println("Result:\n" + parsedTemplate);
    return parsedTemplate;
  }
}
