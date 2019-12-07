package model;

public class SkierPostPathParam {
  private int resortId;
  private String seasonId;
  private int dayId;
  private int skierId;

  public SkierPostPathParam() {}

  public SkierPostPathParam(int resortId, String seasonId, int dayId, int skierId) {
    this.resortId = resortId;
    this.seasonId = seasonId;
    this.dayId = dayId;
    this.skierId = skierId;
  }

  public int getResortId() {
    return resortId;
  }

  public void setResortId(int resortId) {
    this.resortId = resortId;
  }

  public String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(String seasonId) {
    this.seasonId = seasonId;
  }

  public int getDayId() {
    return dayId;
  }

  public void setDayId(int dayId) {
    this.dayId = dayId;
  }

  public int getSkierId() {
    return skierId;
  }

  public void setSkierId(int skierId) {
    this.skierId = skierId;
  }

  @Override
  public String toString() {
    return "SkierPostPathParam{" +
        "resortId=" + resortId +
        ", seasonId=" + seasonId +
        ", dayId=" + dayId +
        ", skierId=" + skierId +
        '}';
  }
}