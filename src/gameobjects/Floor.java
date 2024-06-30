package gameobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import game.Game;
import game.GameObject;
import game.LocatedRectangle;
import game.Vector2D;

public class Floor implements GameObject, LocatedRectangle {
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

@Override
public Point getAddress() {
	// TODO Auto-generated method stub
	return position;
}

@Override
public Point getDirection() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Dimension getDimension() {
	// TODO Auto-generated method stub
	return size;
}

@Override
public void setAddress(Point location) {
	// TODO Auto-generated method stub
}

@Override
public void setDirection(Point direction) {
	// TODO Auto-generated method stub
	
}


@Override
public void setDimension(Dimension dimension) {
	// TODO Auto-generated method stub
	
}

@Override
public boolean vacantSpace(LocatedRectangle locatedRectangle) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public void collisionDirection(LocatedRectangle locatedRectangle) {
	// TODO Auto-generated method stub
	
}

@Override
public long getTime() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public long getTimer() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public void setTime(long time) {
	// TODO Auto-generated method stub
	
}

@Override
public void setSpeed(Vector2D speed) {
	// TODO Auto-generated method stub
	
}

@Override
public Vector2D getSpeed() {
	// TODO Auto-generated method stub
	return null;
}
}
