package gameobjects.player;

public class PlayerKeymap {
  private final int up;
  private final int down;
  private final int right;
  private final int left;
  private final int attack;
  private final int pseudoJump;

  public PlayerKeymap(
      int up,
      int down,
      int right,
      int left,
      int attack,
      int pseudoJump) {
    this.up = up;
    this.down = down;
    this.right = right;
    this.left = left;
    this.attack = attack;
    this.pseudoJump=pseudoJump;
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
  
  public int getPseudoJump() {
	    return this.pseudoJump;
	  }
}