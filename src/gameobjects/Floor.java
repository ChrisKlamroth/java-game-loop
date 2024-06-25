package gameobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import game.Game;
import game.GameObject;

public class Floor implements GameObject {
  private final Dimension size;
  private final Point position;

  public Floor() {
    this.size = new Dimension(
        (int) Game.getWindowBounds().getWidth(),
        250);
    this.position = new Point(
        0,
        (int) (Game.getWindowBounds().getHeight() - this.size.getHeight()));
  }

  public Dimension getSize() {
    return this.size;
  }

  public Point getPosition() {
    return this.position;
  }

  @Override
  public void update(long deltaTime) {
    //
  }

  @Override
  public void draw(Graphics2D graphics2d) {
    graphics2d.setColor(Color.lightGray);
    graphics2d.fillRect(
        (int) this.position.getX(),
        (int) this.position.getY(),
        (int) this.size.getWidth(),
        (int) this.size.getHeight());
  }
}
