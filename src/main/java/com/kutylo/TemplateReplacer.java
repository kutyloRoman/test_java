package com.kutylo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemplateReplacer {

  private static final String placeholderRegex = "#\\{([a-zA-Z]+)}";
  private final Pattern regexPattern = Pattern.compile(placeholderRegex);

  public Map<String, List<Position>> findTagsInTemplate(String template) {
    Matcher matcher = regexPattern.matcher(template);
    Map<String, List<Position>> tags = new HashMap<>();
    while (matcher.find()) {
      Position position = new Position(matcher.start() + 2, matcher.end() - 1);
      String tag = matcher.group(1);
      if (tags.containsKey(tag)) {
        tags.get(tag).add(position);
      } else {
        tags.put(tag, new ArrayList<>(Arrays.asList(position)));
      }
    }
    return tags;
  }

  public String replaceTags(Map<String, String> inputTags, String template) {
    Map<String, List<Position>> templateTags = findTagsInTemplate(template);
    checkIfAllTagsIsPresent(inputTags, templateTags);
    return getReplacedTemplate(templateTags, inputTags, template);
  }

  private void checkIfAllTagsIsPresent(
      Map<String, String> inputTags, Map<String, List<Position>> templateTags) {
    if (isAllTagsPresentInTemplate(inputTags, templateTags))
      throw new PlaceholderInputException("Need to input all placeholders from template!");
  }

  private boolean isAllTagsPresentInTemplate(
      Map<String, String> inputTags, Map<String, List<Position>> templateTags) {
    return templateTags.keySet().stream().anyMatch(tag -> !inputTags.containsKey(tag));
  }

  private String getReplacedTemplate(
      Map<String, List<Position>> templateTags, Map<String, String> inputTags, String template) {
    StringBuilder stringBuilder = new StringBuilder();
    List<TagPosition> positionList = getSortedTemplateTags(templateTags);

    int previousPosition = 0;
    for (TagPosition tagPosition : positionList) {
      Position position = tagPosition.getPosition();
      String str = template.substring(previousPosition, position.getStart());
      stringBuilder.append(str);
      stringBuilder.append(inputTags.get(tagPosition.getTag()));
      previousPosition = position.getEnd();
    }
    stringBuilder.append(template.substring(previousPosition));

    return stringBuilder.toString();
  }

  private List<TagPosition> getSortedTemplateTags(Map<String, List<Position>> templateTags) {
    return templateTags.entrySet().stream()
        .flatMap(this::getTagPositionStream)
        .sorted(Comparator.comparingInt(a -> a.getPosition().getStart()))
        .collect(Collectors.toList());
  }

  private Stream<TagPosition> getTagPositionStream(
      Map.Entry<String, List<Position>> stringListEntry) {
    return stringListEntry.getValue().stream()
        .map(position -> new TagPosition(stringListEntry.getKey(), position));
  }
}
