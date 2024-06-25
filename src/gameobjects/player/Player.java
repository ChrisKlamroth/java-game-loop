package gameobjects.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import javax.imageio.ImageIO;

import game.Game;
import game.GameKeyListener;
import game.GameObject;
import game.Sprite;

public class Player implements GameObject {
  private static final double ACCELERATION = 0.005;
  private static final double MAX_SPEED = 0.75;

  private final GameKeyListener keyListener;
  private final PlayerKeymap keymap;
  private final Color color;
  private final Direction direction;

  private Sprite sprite;
  private final Sprite idle;
  private final Sprite run;
  private final Sprite attack;

  private Dimension size;
  private Point position;
  private double speed;

  public Player(
      GameKeyListener keyListener,
      PlayerKeymap keymap,
      Color color) throws IOException {
    BufferedImage spritesheet = loadSpritesheet();

    this.idle = new Sprite(
        spritesheet,
        new Dimension(50, 37),
        6,
        0,
        4,
        Duration.ofSeconds(1));

    this.run = new Sprite(
        spritesheet,
        new Dimension(50, 37),
        6,
        8,
        6,
        Duration.ofSeconds(1));

    this.attack = new Sprite(
        spritesheet,
        new Dimension(50, 37),
        6,
        42,
        4,
        Duration.ofMillis(350));

    this.sprite = this.idle;

    this.keyListener = keyListener;
    this.keymap = keymap;
    this.color = color;
    this.direction = new Direction(1, 0);

    this.size = new Dimension(100, 100);
    this.position = new Point(
        0,
        (int) (Game.getWindowBounds().getHeight() - 250 - this.sprite.getSize().getHeight() + 6));
    this.speed = 0;
  }

  @Override
  public void draw(Graphics2D graphics2D) {
    // graphics2D.setColor(Color.red);
    // graphics2D.drawRect(
    // (int) this.position.getX(),
    // (int) this.position.getY(),
    // (int) this.sprite.getSize().getWidth(),
    // (int) this.sprite.getSize().getHeight());

    graphics2D.drawImage(
        this.sprite.getFrame(),
        (int) (this.position.getX() + (this.direction.getX() == -1 ? this.sprite.getSize().getWidth() : 0)),
        (int) this.position.getY(),
        (int) (this.sprite.getSize().getWidth() * this.direction.getX()),
        (int) this.sprite.getSize().getHeight(),
        null);
  }

  @Override
  public void update(long deltaTime) {
    this.sprite.update(deltaTime);

    if (keyListener.isNothingPressed()) {
      this.sprite = this.idle;
      this.speed = 0;
      return;
    }

    if (keyListener.isKeyPressed(this.keymap.getRight())) {
      this.sprite = this.run;
      this.direction.setRight();
    } else if (keyListener.isKeyPressed(this.keymap.getLeft())) {
      this.sprite = this.run;
      this.direction.setLeft();
    } else if (keyListener.isKeyPressed(this.keymap.getAttack())) {
      this.sprite = this.attack;
    }
    // else {
    // this.direction.resetX();
    // }

    if (keyListener.isKeyPressed(this.keymap.getRight())
        || keyListener.isKeyPressed(this.keymap.getLeft())) {
      double speed = this.accelerate(deltaTime);
      double distance = this.calculateDistance(speed, deltaTime);
      this.position = this.translate(this.direction, distance);
      this.speed = speed;
    }
  }

  /**
   * Formula to accelerate speed.
   * v2 = v1 + a * dt
   * 
   * @see https://www.youtube.com/watch?v=JOzoMkOmRrE&t=593s
   */
  private double accelerate(long deltaTime) {
    if (this.speed >= MAX_SPEED) {
      return MAX_SPEED;
    }
    return this.speed + ACCELERATION * deltaTime;
  }

  /**
   * Formula to decelerate speed.
   * v2 = v1 - a * dt
   */
  private double decelerate(long deltaTime) {
    if (this.speed <= 0) {
      return 0;
    }
    return this.speed - ACCELERATION * deltaTime;
  }

  /**
   * Formula to update distance.
   * d2 = d1 + (v1 + v2) * 0.5 * dt
   * 
   * @see https://www.youtube.com/watch?v=JOzoMkOmRrE&t=593s
   */
  private double calculateDistance(double speed, long deltaTime) {
    return (this.speed + speed) * 0.5 * deltaTime;
  }

  private Point translate(Direction direction, double distance) {
    return new Point(
        (int) (this.position.getX() + direction.getX() * distance),
        (int) (this.position.getY() + direction.getY() * distance));
  }

  private BufferedImage loadSpritesheet() throws IOException {
    String pathname = "/Users/christophklamroth/Developer/Repositories/java-game-loop/src/assets/player-spritesheet.png";
    return ImageIO.read(new File(pathname));
  }
}

class Direction {
  private int x;
  private int y;

  public Direction(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Direction() {
    this(0, 0);
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public void setRight() {
    this.x = 1;
  }

  public void setLeft() {
    this.x = -1;
  }

  public void setUp() {
    this.y = -1;
  }

  public void setDown() {
    this.y = 1;
  }

  public void reset() {
    this.resetX();
    this.resetY();
  }

  public void resetX() {
    this.x = 0;
  }

  public void resetY() {
    this.y = 0;
  }
}