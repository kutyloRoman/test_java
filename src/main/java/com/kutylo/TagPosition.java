package com.kutylo;

public class TagPosition {
  String tag;
  Position position;

  public TagPosition(String tag, Position position) {
    this.tag = tag;
    this.position = position;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }
}
