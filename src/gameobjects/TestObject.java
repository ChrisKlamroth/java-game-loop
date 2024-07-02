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

public class TestObject implements GameObject, LocatedRectangle {
	  private Dimension size;
	  private Point position;
	  private Vector2D speed2D;
	  private Point direction;
	  private BufferedImage texture;
	  
	  public TestObject(Dimension size, Point position) throws IOException {
	    this.size = size;
	    this.position=position;
	    this.speed2D=new Vector2D(0,0);
	    this.texture=ImageIO.read(new File(".//resources//cube.png"));
	  }

	  public Dimension getSize() {
	    return this.size;
	  }

	  public Point getPosition() {
	    return this.position;
	  }

	  @Override
	  public void update(long deltaTime) {
		  if(this.position.x>(int) Game.getWindowBounds().getWidth()) {
			  this.position.x=-this.size.width;
		  }
		  else if (this.position.x<0-this.size.width) {
			  this.position.x=(int) Game.getWindowBounds().getWidth();
		  }
		this.position=new Point(position.x+(int)speed2D.VectorX(), position.y+(int)speed2D.VectorY());
		
		
	  }

	  @Override
	  public void draw(Graphics2D graphics2d) {
		  graphics2d.drawImage(
				  this.texture,
	        (int) this.position.getX(),
	        (int) this.position.getY(),
	        (int) this.size.getWidth(),
	        (int) this.size.getHeight(),
	        null);
	  }

	@Override
	public Point getAddress() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Point getDirection() {
		// TODO Auto-generated method stub
		return direction;
	}

	@Override
	public Vector2D getSpeed() {
		// TODO Auto-generated method stub
		return speed2D;
	}

	@Override
	public Dimension getDimension() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public void setAddress(Point location) {
		// TODO Auto-generated method stub
		this.position=location;
	}

	@Override
	public void setDirection(Point direction) {
		// TODO Auto-generated method stub
		this.direction=direction;
	}


	@Override
	public void setDimension(Dimension dimension) {
		// TODO Auto-generated method stub
		this.size=dimension;
	}

	public boolean vacantSpace(LocatedRectangle gameObject) {
		boolean anyIntersection = false ;
		anyIntersection = anyIntersection || this.intersects (gameObject);
		//this.speed2D=new Vector2D(0,0); //random find: push outside of sword range;
		return !anyIntersection;
	}
  
  public void collisionDirection(LocatedRectangle gameObject) {
//	  while(!this.vacantSpace(gameObject)&&
//			(!(this.position.x+this.size.width>(int) Game.getWindowBounds().getWidth()||
//			 !(this.position.x>0)))) {
	  while(!this.vacantSpace(gameObject)) {
		  if(this.rightOf(gameObject, -30)) {
			  this.position.x+=1;
		  }
		  if(this.leftOf(gameObject, -30)) {
			  this.position.x-=1;
		  }
		  if(this.above(gameObject, -30)) {
			  this.position.y-=1;
		  }
		  if(this.below(gameObject, -30)) {
			  this.position.y+=1;
		  }
	  }
	  this.speed2D=new Vector2D(0,0);
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
	this.speed2D=speed;
}
}
