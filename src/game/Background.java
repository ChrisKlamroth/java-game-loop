package game;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background implements GameObject {
	private Dimension size;
	private Point position;
	private BufferedImage layer1;
	private BufferedImage layer2;
	private BufferedImage layer3;
	private int offsetY;
	
	public Background(Point position, Dimension size, int floorHeight) throws IOException {
		this.size=size;
		this.position=position;
		this.offsetY=floorHeight;
		layer1=ImageIO.read(new File(".//resources//background_layer_1.png"));
		layer2=ImageIO.read(new File(".//resources//background_layer_2.png"));
		layer3=ImageIO.read(new File(".//resources//background_layer_3.png"));
	}

	@Override
	public void update(long deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D graphics2d) {
		// TODO Auto-generated method stub
		
		graphics2d.drawImage(layer1, position.x, position.y, size.width, size.height-offsetY, null);
		graphics2d.drawImage(layer2, position.x, position.y, size.width, size.height-offsetY, null);
		graphics2d.drawImage(layer3, position.x, position.y, size.width, size.height-offsetY, null);
		
	}
}
