package gameobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

import game.GameObject;

public class Manual implements GameObject {
  private final Dimension size;
  private final Point position;

  public Manual() {
    this.size = new Dimension(279, 178);
    this.position = new Point(20, 20);
  }

  @Override
  public void update(long deltaTime) {
  }

  @Override
  public void draw(Graphics2D graphics2d) {
    graphics2d.setColor(Color.black);

    graphics2d.setFont(new Font("Arial", Font.BOLD, 20));
    graphics2d.drawString("Controls", 28, 43);

    graphics2d.setFont(new Font("Arial", Font.PLAIN, 16));
    graphics2d.drawString("Move right: [Right Arrow]", 28, 67);
    graphics2d.drawString("Move left: [Left Arrow]", 28, 87);
    graphics2d.drawString("Crouch: [Down Arrow]", 28, 107);
    graphics2d.drawString("Attack: [C]", 28, 127);
    graphics2d.drawString("Air Attack: [Down Arrow] + [Space]", 28, 147);
    graphics2d.drawString("Jump: [Space]", 28, 167);
    graphics2d.drawString("Levitate: [Down Arrow]", 28, 187);

    graphics2d.drawRect(
        (int) this.position.getX(),
        (int) this.position.getY(),
        (int) this.size.getWidth(),
        (int) this.size.getHeight());
  }
}
