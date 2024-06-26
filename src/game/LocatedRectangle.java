package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

public interface LocatedRectangle {
	public Point getAddress();
	public Point getDirection();
	public double getSpeed();
	public Dimension getDimension();
	public void setAddress(Point location);
	public void setDirection(Point direction);
	public void setSpeed(double speed);
	public void setDimension(Dimension dimension);

	public default boolean intersects(LocatedRectangle other) {
		return !doesNotIntersect(other, 0);
	}	
	
	public default boolean intersects(LocatedRectangle other, int margin) {
		return !doesNotIntersect(other, margin);
	}
	
	private boolean doesNotIntersect(LocatedRectangle other, int margin) {
		return 	leftOf(other, margin) || rightOf(other, margin) || 
				above(other, margin) || below(other, margin);
	}
	
	public default boolean leftOf(LocatedRectangle other, int margin){
		return this.getAddress().x + this.getDimension().width + margin < other.getAddress().x;
	}
	
	public default boolean rightOf(LocatedRectangle other, int margin){
		return this.getAddress().x > other.getAddress().x + other.getDimension().width + margin;
	}
	
	public default boolean above(LocatedRectangle other, int margin) {
		return this.getAddress().y + this.getDimension().height + margin <= other.getAddress().y;
	}
	
	public default boolean below(LocatedRectangle other, int margin) {
		return this.getAddress().y >= other.getAddress().y + other.getDimension().height + margin;
	}
}
