package gameobjects.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import game.Game;
import game.GameKeyListener;
import game.GameObject;

public class Player implements GameObject {
  private static final double ACCELERATION = 0.001;
  private static final double MAX_SPEED = 1;

  private final GameKeyListener keyListener;
  private final PlayerKeymap keymap;
  private final Color color;
  private final Direction direction;

  private Dimension size;
  private Point position;
  private double speed;

  public Player(
      GameKeyListener keyListener,
      PlayerKeymap keymap,
      Point initialPosition,
      Color color) {
    this.keyListener = keyListener;
    this.keymap = keymap;

    this.color = color;
    this.direction = new Direction();

    this.position = initialPosition;
    this.speed = 0;
    this.size = new Dimension(100, 100);
  }

  @Override
  public void draw(Graphics2D graphics2D) {
    graphics2D.setColor(this.color);
    graphics2D.fillRect(
        (int) this.position.getX(),
        (int) this.position.getY(),
        (int) this.size.getWidth(),
        (int) this.size.getHeight());
  }

  @Override
  public void update(long deltaTime) {
    if (keyListener.isNothingPressed()) {
      double speed = this.decelerate(deltaTime);
      double distance = this.calculateDistance(speed, deltaTime);
      this.position = this.translate(this.direction, distance);
      this.speed = speed;
      if (this.speed == 0) {
        this.direction.reset();
      }
      return;
    }

    if (keyListener.isKeyPressed(this.keymap.getRight())) {
      this.direction.setRight();
    } else if (keyListener.isKeyPressed(this.keymap.getLeft())) {
      this.direction.setLeft();
    } else {
      this.direction.resetX();
    }

    if (keyListener.isKeyPressed(this.keymap.getDown())) {
      this.direction.setDown();
    } else if (keyListener.isKeyPressed(this.keymap.getUp())) {
      this.direction.setUp();
    } else {
      this.direction.resetY();
    }

    double speed = this.accelerate(deltaTime);
    double distance = this.calculateDistance(speed, deltaTime);
    this.position = this.translate(this.direction, distance);
    this.speed = speed;
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
}

class Direction {
  private int x;
  private int y;

  public Direction() {
    this.x = 0;
    this.y = 0;
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