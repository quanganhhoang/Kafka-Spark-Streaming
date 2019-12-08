package model;

public class PostBody {
  private int time;
  private int liftId;

  public PostBody() {}

  public PostBody(int time, int liftId) {
    this.time = time;
    this.liftId = liftId;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getLiftId() {
    return liftId;
  }

  public void setLiftId(int liftId) {
    this.liftId = liftId;
  }

  @Override
  public String toString() {
    return "PostBody{" +
        "time=" + time +
        ", liftId=" + liftId +
        '}';
  }
}
