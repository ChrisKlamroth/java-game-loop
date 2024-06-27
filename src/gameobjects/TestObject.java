package gameobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import game.Game;
import game.GameObject;
import game.LocatedRectangle;

public class TestObject implements GameObject, LocatedRectangle {
	  private Dimension size;
	  private Point position;

	  public TestObject(Dimension size, Point position) {
	    this.size = size;
	    this.position=position;
	  }

	  public Dimension getSize() {
	    return this.size;
	  }

	  public Point getPosition() {
	    return this.position;
	  }

	  @Override
	  public void update(long deltaTime) {
		
	  }

	  @Override
	  public void draw(Graphics2D graphics2d) {
	    graphics2d.setColor(Color.red);
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
	public double getSpeed() {
		// TODO Auto-generated method stub
		return 0;
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
		
	}

	@Override
	public void setSpeed(double speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDimension(Dimension dimension) {
		// TODO Auto-generated method stub
		this.size=dimension;
	}

	public boolean vacantSpace(LocatedRectangle gameObject) {
		boolean anyIntersection = false ;
		anyIntersection = anyIntersection || this.intersects (gameObject);
		return !anyIntersection;
	}
  
  public void collisionDirection(LocatedRectangle gameObject) {
	  while(!this.vacantSpace(gameObject)&&
			(!(this.position.x+this.size.width>(int) Game.getWindowBounds().getWidth()||
			 !(this.position.x>0)))) {
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
  }
	}
