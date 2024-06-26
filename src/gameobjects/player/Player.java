package gameobjects.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;

import javax.imageio.ImageIO;

import game.Game;
import game.GameKeyListener;
import game.GameObject;
import game.LocatedRectangle;
import game.Sprite;
import game.Vector2D;

public class Player implements GameObject, LocatedRectangle {
  private static final double ACCELERATION = 0.005;
  private static final double MAX_SPEED = 0.7;
  private static final double MAX_SPEED_Y = 1.8;
  private static final double GRAVITY = 0.0032; //Can not go above 0.004   
  private static final long INTERACTION_DURATION = 150;
 
  private static final Dimension SPRITE_FRAME_SIZE = new Dimension(50, 37);
  private static final int SPRITE_SCALE = 6;
  private static final int X_OFFSET = 96;

  private final GameKeyListener keyListener;
  private final PlayerKeymap keymap;
  private final Direction direction;
  private InteractionZone  interaction=null;

  private final Sprite idleSprite;
  private final Sprite runSprite;
  private final Sprite crouchSprite;
  private final Sprite attackSprite;
  private final Sprite airAttackSprite;

  private Sprite sprite;
  private boolean isSpriteLocked;
  private long activeSpriteTimer;

  private Point position;
  private double speed;
  private Vector2D speed2D;
  
  private boolean levitate=false;
  private boolean isOnGround=true;
  private int jumpCounter=0;
  private long elapsedTime = 0L;	
  private long interactionTime = 0L;

  public Player(GameKeyListener keyListener, PlayerKeymap keymap) throws IOException {
    BufferedImage spritesheet = loadSpritesheet();

    this.idleSprite = new Sprite(
        spritesheet,
        SPRITE_FRAME_SIZE,
        SPRITE_SCALE,
        0,
        4,
        Duration.ofSeconds(1));
    this.runSprite = new Sprite(
        spritesheet,
        SPRITE_FRAME_SIZE,
        SPRITE_SCALE,
        8,
        6,
        Duration.ofSeconds(1));
    this.crouchSprite = new Sprite(
        spritesheet,
        SPRITE_FRAME_SIZE,
        SPRITE_SCALE,
        4,
        4,
        Duration.ofSeconds(1));
    this.attackSprite = new Sprite(
        spritesheet,
        SPRITE_FRAME_SIZE,
        SPRITE_SCALE,
        42,
        4,
        Duration.ofMillis(350));
    this.airAttackSprite = new Sprite(
        spritesheet,
        SPRITE_FRAME_SIZE,
        SPRITE_SCALE,
        97,
        4,
        Duration.ofMillis(450));

    this.sprite = this.idleSprite;
    this.isSpriteLocked = false;
    this.activeSpriteTimer = 0;

    this.keyListener = keyListener;
    this.keymap = keymap;
    this.direction = Direction.right();

    int floorHeight = 250;
    int spriteHeightOffset = 6;
    this.position = new Point(
        0,
        (int) (Game.getWindowBounds().getHeight()
            - floorHeight
            - this.sprite.getSize().getHeight()
            + spriteHeightOffset));
    this.speed = 0;
    this.speed2D=new Vector2D(0,0);
  }

  @Override
  public void draw(Graphics2D graphics2D) {
    // Flip the image by negative scaling it with
    // graphics2D.drawImage(image, x + width, y, -width, height, null)
    // Since the negative scale will move the image to left, its horizontal position
    // has to be compensated.
//    graphics2D.setColor(Color.green);
//    graphics2D.drawRect(
//        (int) this.position.getX(),
//        (int) this.position.getY(),
//        (int) this.sprite.getSize().getWidth(),
//        (int) this.sprite.getSize().getHeight());
//    
//    graphics2D.setColor(Color.red);
//    graphics2D.drawRect(
//    		getAddress().x,
//    		getAddress().y,
//    		getDimension().width,
//    		getDimension().height);

    graphics2D.drawImage(
        this.sprite.getFrame(),
        (int) (this.position.getX() + (this.direction.isLeft() ? this.sprite.getSize().getWidth() : 0)),
        (int) this.position.getY(),
        (int) (this.sprite.getSize().getWidth() * this.direction.getX()),
        (int) this.sprite.getSize().getHeight(),
        null);
    
    if(levitate) {
    	graphics2D.setColor(new Color(0,0,0,155));
        graphics2D.fillRect(this.getAddress().x,
        					this.getAddress().y+this.getDimension().height,
        					this.getDimension().width,
        					this.getDimension().height/15);
        		
    }
  }
  
  public boolean vacantSpace(LocatedRectangle gameObject) {
		boolean anyIntersection = false ;
		anyIntersection = anyIntersection || this.intersects (gameObject);
		return !anyIntersection;
	}
  
  public void collisionDirection(LocatedRectangle gameObject) {
	  while(!this.vacantSpace(gameObject)) {
		  if(this.rightOf(gameObject, -15 )) {
			  this.position.x+=1;
		  }
		  if(this.leftOf(gameObject, -15)) {
			  this.position.x-=1;
		  }
		  if(this.above(gameObject, -30)) {
			  this.position.y-=1;
			  this.speed2D.setVectorY(0);
			  isOnGround=true;
			  jumpCounter=2;
		  }
		  if(this.below(gameObject, -30)) {
			  this.position.y+=1;
			  this.speed2D.setVectorY(0);
		  }
	  }
  }
  
  public void levitate() {
	  if(!levitate) {
		  levitate=true;
	  }
  }

  @Override
  public void update(long deltaTime) {
    this.sprite.update(deltaTime);
    levitate=false;

    // Warp to the other side of the window when player goes out of bounds.
    if (this.position.getX() > Game.getWindowBounds().getSize().getWidth() - 95) {
      this.position = new Point(
          (int) -this.sprite.getSize().getWidth() + 95,
          (int) this.position.getY());
    } else if (this.position.getX() < -this.sprite.getSize().getWidth() + 95) {
      this.position = new Point(
          (int) Game.getWindowBounds().getSize().getWidth() - 95,
          (int) this.position.getY());
    }

    // TODO: find a better way to lock other sprite animations when a blockable
    // sprite (ex. attacking) is running.

    if (keyListener.isKeyPressed(this.keymap.getAttack())
        && keyListener.isKeyPressed(this.keymap.getDown())) {
      if (!this.isSpriteLocked) {
        this.sprite = this.airAttackSprite;
        this.isSpriteLocked = true;
      }
    } else if (keyListener.isKeyPressed(this.keymap.getAttack())) {
      if (!this.isSpriteLocked) {
        this.sprite = this.attackSprite;
        this.isSpriteLocked = true;
        if(direction.getX()<0) {
        	interaction=new InteractionZone(new Dimension(X_OFFSET,this.sprite.getSize().height),
					position,
					this.sprite.getDuration().toMillis());
        }
        else {
        	interaction=new InteractionZone(new Dimension(X_OFFSET,this.sprite.getSize().height),
					new Point(position.x+X_OFFSET*2+12,position.y),
					this.sprite.getDuration().toMillis());
        }
        
        
      }
    } else if (keyListener.isKeyPressed(this.keymap.getDown())) {
      if (!this.isSpriteLocked) {
        this.sprite = this.crouchSprite;
        this.speed2D.setVectorY(0);
        levitate();
        isOnGround=true;
        jumpCounter=2;
      }
    } else if (keyListener.isKeyPressed(this.keymap.getRight())) {
      if (!this.isSpriteLocked) {
        this.direction.setRight();
        this.sprite = this.runSprite;
        this.move(deltaTime);
      }
    } else if (keyListener.isKeyPressed(this.keymap.getLeft())) {
      if (!this.isSpriteLocked) {
        this.direction.setLeft();
        this.sprite = this.runSprite;
        this.move(deltaTime);
      }
    } else {
      if (!this.isSpriteLocked) {
        this.sprite = this.idleSprite;
      }
     speed2D.setVectorX(0);
     this.move(deltaTime, new Direction(0,0));
    }
    
    if (keyListener.isKeyPressed(this.keymap.getPseudoJump())&&(jumpCounter==2)) {
    	speed2D.setVectorY(-1.1);
    	isOnGround=false;
    	jumpCounter=1;
    	elapsedTime = (new Date()).getTime();
    }
    else if (keyListener.isKeyPressed(this.keymap.getPseudoJump())&&(jumpCounter==1)&&(new Date()).getTime()-elapsedTime>175) {
        speed2D.setVectorY(-1.1);
        isOnGround=false;
       	jumpCounter=0; 
       	elapsedTime=0;
    }   

    if (this.isSpriteLocked && this.activeSpriteTimer >= this.sprite.getDuration().toMillis()) {
      this.activeSpriteTimer = 0;
      this.isSpriteLocked = false;
    }
    if (this.isSpriteLocked) {
      this.activeSpriteTimer += deltaTime;
    }
    
    if (!(interaction==null)&&new Date().getTime()-interaction.getTime()>interaction.getTimer()) {
    	interaction=null;
    }
  } 
  
  public InteractionZone getInteraction() {
	  if (!(interaction==null)) {
		  return interaction;
	  }
	  else{
		  return null;
	  }
  }
  
  @Override
  public Point getAddress() {
  	// TODO Auto-generated method stub
  	return new Point(position.x+X_OFFSET, position.y+35);
  }

  @Override
  public Point getDirection() {
  	// TODO Auto-generated method stub
  	return null;
  }

  @Override
  public Vector2D getSpeed() {
  	// TODO Auto-generated method stub
  	return this.speed2D;
  }

  @Override
  public Dimension getDimension() {
  	// TODO Auto-generated method stub
  	return new Dimension(this.sprite.getSize().width -X_OFFSET*2,this.sprite.getSize().height-41);
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
  public void setSpeed(Vector2D speed) {
  	// TODO Auto-generated method stub
  	this.speed2D=speed;
  }

  @Override
  public void setDimension(Dimension dimension) {
  	// TODO Auto-generated method stub
  	
  }

  private void move(long deltaTime) {
    Vector2D speed2D=this.accelerate2D(deltaTime);
    Vector2D distance2D = this.calculateDistance2D(speed2D, deltaTime);
    this.position = this.translate2D(this.direction, distance2D);
    this.speed2D=speed2D;
  }
  
  private void move(long deltaTime, Direction direction) {
	Vector2D speed2D=this.accelerate2D(deltaTime);
	Vector2D distance2D = this.calculateDistance2D(speed2D, deltaTime);
	this.position = this.translate2D(direction, distance2D);
	this.speed2D.setVectorY(speed2D.VectorY());
  }

  /**
   * Formula to accelerate speed.
   * v2 = v1 + a * dt
   * 
   * @see https://www.youtube.com/watch?v=JOzoMkOmRrE&t=593s
   */
  
  private Vector2D accelerate2D(long deltaTime) {
	  Vector2D speed= speed2D;
	  if ((this.speed2D.VectorX() >= MAX_SPEED)||(this.speed2D.VectorY() >= MAX_SPEED_Y)) {
		  if ((this.speed2D.VectorX() >= MAX_SPEED)&&(this.speed2D.VectorY() >= MAX_SPEED_Y)) {
			  speed.setVector2D(MAX_SPEED, MAX_SPEED_Y);
		  }
		  else if ((this.speed2D.VectorX() >= MAX_SPEED)) {
		      speed.setVector2D(MAX_SPEED,this.speed2D.VectorY() + GRAVITY * deltaTime);
		      //System.out.println("MAX_SPEED + " + speed2D.VectorY());
		  }
		  else if ((this.speed2D.VectorY() >= MAX_SPEED_Y)) {
			  speed.setVector2D(this.speed2D.VectorX() + ACCELERATION * deltaTime, MAX_SPEED_Y);
			  //System.out.println(speed2D.VectorX() +" + MAX_SPEED_Y");
		  }
	  }
	  else {
	    speed.setVector2D(this.speed2D.VectorX() + ACCELERATION * deltaTime,
	    		this.speed2D.VectorY() + GRAVITY * deltaTime);
	    //System.out.println(speed.VectorX()+" + " + speed.VectorY());
	  }
	    return speed;
	  }

  /**
   * Formula to decelerate speed.
   * v2 = v1 - a * dt
   */
  private double decelerate(long deltaTime) {
    if (this.speed <= 0) {
      return 0;
    }
    return this.speed - ACCELERATION * deltaTime;
  }
  
  private Vector2D decelerate2D(long deltaTime) {
	    return new Vector2D(0,
	    		this.speed2D.VectorY() + GRAVITY * deltaTime);
	  }

  /**
   * Formula to update distance.
   * d2 = d1 + (v1 + v2) * 0.5 * dt
   * 
   * @see https://www.youtube.com/watch?v=JOzoMkOmRrE&t=593s
   */
  
  private Vector2D calculateDistance2D(Vector2D speed, long deltaTime) {
	    return new Vector2D((this.speed2D.VectorX() + speed.VectorX()) * 0.5 * deltaTime,
	    					(this.speed2D.VectorY() + speed.VectorY()) * 0.5 * deltaTime);
	  }
  
  private Point translate2D(Direction direction, Vector2D distance) {
	    return new Point(
	        (int) (this.position.getX() + direction.getX() * distance.VectorX()),
	        (int) (this.position.getY() + 1 * distance.VectorY()));
	  }

  private BufferedImage loadSpritesheet() throws IOException {
//    String imagePathname = String.format("%1$sresources%1$splayer-spritesheet.png", File.separator);
//    URL imageUrl = getClass().getResource(imagePathname);
//    return ImageIO.read(imageUrl);
    File file=new File(".//resources//player-spritesheet.png");
    return ImageIO.read(file);
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

}

class Direction {
  private int x;
  private int y;

  public static Direction right() {
    return new Direction(1, 0);
  }

  public Direction(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Direction() {
    this(0, 0);
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public void setRight() {
    this.x = 1;
  }

  public void setLeft() {
    this.x = -1;
  }

  public void setUp() {
    this.y = -1;
  }

  public void setDown() {
    this.y = 1;
  }

  public void reset() {
    this.resetX();
    this.resetY();
  }

  public void resetX() {
    this.x = 0;
  }

  public void resetY() {
    this.y = 0;
  }

  public boolean isLeft() {
    return this.x == -1;
  }
}
