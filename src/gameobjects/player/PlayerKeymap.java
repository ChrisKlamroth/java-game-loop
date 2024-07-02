package gameobjects.player;

public class PlayerKeymap {
  private final int up;
  private final int down;
  private final int right;
  private final int left;
  private final int attack;
  private final int jump;
  private final int dash;

  public PlayerKeymap(
      int up,
      int down,
      int right,
      int left,
      int attack,
      int jump,
      int dash) {
    this.up = up;
    this.down = down;
    this.right = right;
    this.left = left;
    this.attack = attack;
    this.jump=jump;
	this.dash = dash;
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

  public int getAttack() {
    return this.attack;
  }
  
  public int getJump() {
	    return this.jump;
	  }
  public int getDash() {
	    return this.dash;
	  }
}