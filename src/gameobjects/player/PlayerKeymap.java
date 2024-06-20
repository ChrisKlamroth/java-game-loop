package gameobjects.player;

public class PlayerKeymap {
  private final int up;
  private final int down;
  private final int right;
  private final int left;

  public PlayerKeymap(
      int up,
      int down,
      int right,
      int left) {
    this.up = up;
    this.down = down;
    this.right = right;
    this.left = left;
  }

  public int getUp() {
    return this.up;
  }

  public int getDown() {
    return this.down;
  }

  public int getRight() {
    return this.right;
  }

  public int getLeft() {
    return this.left;
  }
}