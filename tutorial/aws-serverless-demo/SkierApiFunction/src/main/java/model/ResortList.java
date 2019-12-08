package model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement // there is no built-in for JSON
public class ResortList {
  private List<Resort> resorts;

  public ResortList() {}

  public ResortList(List<Resort> resorts) {
    this.resorts = resorts;
  }

  public List<Resort> getResorts() {
    return resorts;
  }

  public void setResorts(List<Resort> resorts) {
    this.resorts = resorts;
  }

  @Override
  public String toString() {
    return "ResortList{" +
        "resorts=" + resorts +
        '}';
  }
}
