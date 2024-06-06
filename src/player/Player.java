package player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import game.Game;
import game.GameKeyListener;
import game.GameObject;

public class Player extends GameObject {
  private static final double ACCELERATION = 0.25;
  private static final double MAX_SPEED = 10;

  private final GameKeyListener keyListener;
  private final PlayerKeymap keymap;
  private final Color color;

  private double speed;

  public Player(
      GameKeyListener keyListener,
      PlayerKeymap keymap,
      Point initialLocation,
      Color color) {
    this.keyListener = keyListener;
    this.keymap = keymap;
    this.color = color;
    this.speed = 0;

    Dimension size = new Dimension(100, 100);
    this.setBounds(new Rectangle(initialLocation, size));
  }

  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D graphics2D = (Graphics2D) graphics;
    graphics2D.setColor(this.color);
    graphics2D.fillRect(
        0,
        0,
        (int) this.getSize().getWidth(),
        (int) this.getSize().getHeight());
  }

  @Override
  public void update(double deltaTime) {
    if (keyListener.isNothingPressed()) {
      this.speed = 0;
      return;
    }

    // TODO: find a cleaner way to do the movement

    double speed = this.calculateSpeed(deltaTime);
    double distance = this.calculateDistance(speed, deltaTime);

    if (keyListener.isKeyPressed(this.keymap.getRight())) {
      Point location = this.translateX(distance);
      if (this.isOutOfBounds(location)) {
        this.setLocation(
            (int) (Game.WINDOW_SIZE.getWidth() - this.getSize().getWidth()),
            (int) location.getY());
      } else {
        this.setLocation(location);
      }
      this.speed = speed;
    }
    if (keyListener.isKeyPressed(this.keymap.getLeft())) {
      Point location = this.translateX(-distance);
      if (this.isOutOfBounds(location)) {
        this.setLocation(0, (int) location.getY());
      } else {
        this.setLocation(location);
      }
      this.speed = speed;
    }
    if (keyListener.isKeyPressed(this.keymap.getDown())) {
      Point location = this.translateY(distance);
      if (this.isOutOfBounds(location)) {
        this.setLocation(
            (int) location.getX(),
            (int) (Game.WINDOW_SIZE.getHeight() - this.getSize().getHeight()));
      } else {
        this.setLocation(location);
      }
      this.speed = speed;
    }
    if (keyListener.isKeyPressed(this.keymap.getUp())) {
      Point location = this.translateY(-distance);
      if (this.isOutOfBounds(location)) {
        this.setLocation((int) location.getX(), 0);
      } else {
        this.setLocation(location);
      }
      this.speed = speed;
    }
  }

  /**
   * Formula to update speed.
   * v2 = v1 + a * dt
   * 
   * @see https://www.youtube.com/watch?v=JOzoMkOmRrE&t=593s
   */
  private double calculateSpeed(double deltaTime) {
    if (this.speed >= MAX_SPEED) {
      return MAX_SPEED;
    }
    return this.speed + ACCELERATION * deltaTime;
  }

  /**
   * Formula to update distance.
   * d2 = d1 + (v1 + v2) * 0.5 * dt
   * 
   * @see https://www.youtube.com/watch?v=JOzoMkOmRrE&t=593s
   */
  private double calculateDistance(double speed, double deltaTime) {
    return (this.speed + speed) * 0.5 * deltaTime;
  }

  /**
   * Returns a {@link Point} whose x value is moved by the given
   * <code>distance</code>.
   */
  private Point translateX(double distance) {
    return new Point(
        (int) (this.getLocation().getX() + distance),
        this.getY());
  }

  /**
   * Returns a {@link Point} whose y value is moved by the given
   * <code>distance</code>.
   */
  public Point translateY(double distance) {
    return new Point(
        this.getX(),
        (int) (this.getLocation().getY() + distance));
  }

  /**
   * Checks whether the given <code>location</code> is out of the window bounds.
   */
  private boolean isOutOfBounds(Point location) {
    return location.getX() < 0
        || location.getY() < 0
        || location.getX() + this.getSize().getWidth() > Game.WINDOW_SIZE.getWidth()
        || location.getY() + this.getSize().getHeight() > Game.WINDOW_SIZE.getHeight();
  }
}
