package gameobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import game.Game;
import game.GameObject;

public class Enemy implements GameObject {
  private final Dimension size;
  private final Color color;
  private final Point position;

  public Enemy() {
    this.size = new Dimension(100, 100);
    this.color = Color.green;
    this.position = new Point(
        (int) ((Game.WINDOW_SIZE.getWidth() / 2) - (this.size.getWidth() / 2)),
        (int) ((Game.WINDOW_SIZE.getHeight() / 2) - (this.size.getHeight() / 2)));
  }

  @Override
  public void update(long deltaTime) {
    // TODO
  }

  @Override
  public void draw(Graphics2D graphics2D) {
    graphics2D.setColor(this.color);
    graphics2D.fillOval(
        (int) this.position.getX(),
        (int) this.position.getY(),
        (int) this.size.getWidth(),
        (int) this.size.getHeight());
  }
}
