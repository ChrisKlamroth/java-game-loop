package gameobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Game;
import game.GameObject;
import game.LocatedRectangle;
import game.Vector2D;

public class Floor implements GameObject, LocatedRectangle {
  private final Dimension size;
  private final Point position;
  private BufferedImage texture;
  private BufferedImage textureDirt;

  public Floor() throws IOException {
    this.size = new Dimension(
        (int) Game.getWindowBounds().getWidth()+50,
        225);
    this.position = new Point(
        -25,
        (int) (Game.getWindowBounds().getHeight() - this.size.getHeight()));
    
    texture=ImageIO.read(new File(".//resources//dirt_orange.png"));
    textureDirt=ImageIO.read(new File(".//resources//deep_ground.png"));
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

	  int repetitions=8;
	  int offset=0;
	  for(int i=0; i<repetitions;i++) {
		  graphics2d.drawImage(
			    	texture,
			        (int) this.position.getX()+i*(int) this.size.getWidth()/repetitions+offset,
			        (int) this.position.getY(),
			        (int) this.size.getWidth()/repetitions+1,
			        (int) this.size.getHeight()/2,
			        null);
		  graphics2d.drawImage(
			    	textureDirt,
			        (int) this.position.getX()+i*(int) this.size.getWidth()/repetitions+offset,
			        (int) this.position.getY()+(int) this.size.getHeight()/2,
			        (int) this.size.getWidth()/repetitions+1,
			        2*(int) this.size.getHeight()/2,
			        null);
	  }
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
