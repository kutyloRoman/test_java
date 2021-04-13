package com.kutylo;

import java.util.Map;

public class TemplateParser {

  private TemplateReplacer templateReplacer;
  private final String template = Utils.getResourceFileAsString("template/template.txt");

//  public TemplateParser() {
//    this.templateReplacer = new TemplateReplacer();
//  }

  public TemplateParser(TemplateReplacer templateReplacer) {
    this.templateReplacer = templateReplacer;
  }

  public String parseTemplate(Map<String, String> userInputs) {
    String parsedTemplate = templateReplacer.replaceTags(userInputs, template);
    System.out.println("Result:\n" + parsedTemplate);
    return parsedTemplate;
  }
}
