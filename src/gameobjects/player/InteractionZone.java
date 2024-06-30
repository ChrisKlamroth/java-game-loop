package gameobjects.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Date;

import game.GameObject;
import game.LocatedRectangle;
import game.Vector2D;

public class InteractionZone implements LocatedRectangle, GameObject{
	private Dimension size;
	private Point position;
	private long duration;
	private Point direction;
	private long time;
	
	public InteractionZone(Dimension size, Point position, long duration) {
		this.intializeTime();
		this.size=size;
		this.position=position;
		this.duration=duration;
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
		return this.size;
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
	public void setDimension(Dimension dimension) {
		// TODO Auto-generated method stub
		this.size=size;
	}

	@Override
	public boolean vacantSpace(LocatedRectangle locatedRectangle) {
		boolean anyIntersection = false ;
		anyIntersection = anyIntersection || this.intersects (locatedRectangle);
		return !anyIntersection;
	}

	@Override
	public void collisionDirection(LocatedRectangle locatedRectangle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(long deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D graphics2d) {
		// TODO Auto-generated method stub
		 graphics2d.setColor(Color.red);
		    graphics2d.fillRect(
		        (int) this.position.getX(),
		        (int) this.position.getY(),
		        (int) this.size.getWidth(),
		        (int) this.size.getHeight());
	}

	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return this.time;
	}

	@Override
	public long getTimer() {
		// TODO Auto-generated method stub
		return this.duration;
	}

	@Override
	public void setTime(long time) {
		// TODO Auto-generated method stub
		this.time=time;
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
