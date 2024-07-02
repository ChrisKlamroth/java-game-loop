package game;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameobjects.player.Player;

public class Background implements GameObject, LocatedRectangle {
	private Vector2D speed2DFront;
	private Vector2D speed2DMiddle;
	private Vector2D speed2DBack;
	private Dimension size;
	private Point positionFront;
	private Point positionMiddle;
	private Point positionBack;
	private BufferedImage layer1;
	private BufferedImage layer2;
	private BufferedImage layer3;
	private Player player;
	private int offsetY;
	
	
	public Background(Point position, Dimension size, int floorHeight, Player player) throws IOException {
		this.size=size;
		this.positionFront=position;
		this.positionMiddle=position;
		this.positionBack=position;
		this.offsetY=floorHeight;
		this.player=player;
		layer1=ImageIO.read(new File(".//resources//background_layer_1.png"));
		layer2=ImageIO.read(new File(".//resources//background_layer_2.png"));
		layer3=ImageIO.read(new File(".//resources//background_layer_3.png"));
		this.speed2DBack=new Vector2D(0,0);
		}

	@Override
	public void update(long deltaTime) {
		// TODO Auto-generated method stub
//		Vector2D speedPlayer = player.getSpeed();
//		
//		if(speedPlayer.VectorX()>0.1)
//			this.positionFront=new Point(positionFront.x+player.getDirection().x*(int)speed2DBack.VectorX(), positionFront.y+(int)speed2DBack.VectorY());
//	
//		this.speed2DBack=new Vector2D(speedPlayer.VectorX()*deltaTime,0);
//		this.positionPlayer=new Point(position.x+(int)speed2DBack.VectorX(), position.y+(int)speed2DBack.VectorY());
		

	}

	@Override
	public void draw(Graphics2D graphics2d) {
		// TODO Auto-generated method stub
		
		graphics2d.drawImage(layer1, positionBack.x, positionBack.y, size.width, size.height-offsetY, null);
		graphics2d.drawImage(layer2, positionMiddle.x, positionMiddle.y, size.width, size.height-offsetY, null);
		graphics2d.drawImage(layer3, positionFront.x, positionFront.y, size.width, size.height-offsetY, null);
		
	}

	@Override
	public Point getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2D getSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getDimension() {
		// TODO Auto-generated method stub
		return null;
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
	public void setAddress(Point location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDirection(Point direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSpeed(Vector2D speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDimension(Dimension dimension) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTime(long time) {
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
}
