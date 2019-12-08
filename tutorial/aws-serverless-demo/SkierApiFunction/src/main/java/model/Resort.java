package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement // there is no built-in for JSON
public class Resort {
  private String id;
  private String name;

  public Resort() {}

  public Resort(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Resort{" +
        "name='" + name + '\'' +
        ", id='" + id + '\'' +
        '}';
  }
}
