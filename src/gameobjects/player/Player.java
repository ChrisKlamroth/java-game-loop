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

import javax.imageio.ImageIO;

import game.Game;
import game.GameKeyListener;
import game.GameObject;
import game.Sprite;

public class Player implements GameObject {
  private static final double ACCELERATION = 0.005;
  private static final double MAX_SPEED = 0.7;

  private static final Dimension SPRITE_FRAME_SIZE = new Dimension(50, 37);
  private static final int SPRITE_SCALE = 6;

  private final GameKeyListener keyListener;
  private final PlayerKeymap keymap;
  private final Direction direction;

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
  }

  @Override
  public void draw(Graphics2D graphics2D) {
    // Flip the image by negative scaling it with
    // graphics2D.drawImage(image, x + width, y, -width, height, null)
    // Since the negative scale will move the image to left, its horizontal position
    // has to be compensated.
    graphics2D.setColor(Color.blue);
    graphics2D.drawRect(
        (int) this.position.getX(),
        (int) this.position.getY(),
        (int) this.sprite.getSize().getWidth(),
        (int) this.sprite.getSize().getHeight());

    graphics2D.drawImage(
        this.sprite.getFrame(),
        (int) (this.position.getX() + (this.direction.isLeft() ? this.sprite.getSize().getWidth() : 0)),
        (int) this.position.getY(),
        (int) (this.sprite.getSize().getWidth() * this.direction.getX()),
        (int) this.sprite.getSize().getHeight(),
        null);
  }

  @Override
  public void update(long deltaTime) {
    this.sprite.update(deltaTime);

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
      }
    } else if (keyListener.isKeyPressed(this.keymap.getDown())) {
      if (!this.isSpriteLocked) {
        this.sprite = this.crouchSprite;
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
        this.speed = 0;
      }
    }

    if (this.isSpriteLocked && this.activeSpriteTimer >= this.sprite.getDuration().toMillis()) {
      this.activeSpriteTimer = 0;
      this.isSpriteLocked = false;
    }
    if (this.isSpriteLocked) {
      this.activeSpriteTimer += deltaTime;
    }
  }

  private void move(long deltaTime) {
    double speed = this.accelerate(deltaTime);
    double distance = this.calculateDistance(speed, deltaTime);
    this.position = this.translate(this.direction, distance);
    this.speed = speed;
  }

  /**
   * Formula to accelerate speed.
   * v2 = v1 + a * dt
   * 
   * @see https://www.youtube.com/watch?v=JOzoMkOmRrE&t=593s
   */
  private double accelerate(long deltaTime) {
    if (this.speed >= MAX_SPEED) {
      return MAX_SPEED;
    }
    return this.speed + ACCELERATION * deltaTime;
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

  /**
   * Formula to update distance.
   * d2 = d1 + (v1 + v2) * 0.5 * dt
   * 
   * @see https://www.youtube.com/watch?v=JOzoMkOmRrE&t=593s
   */
  private double calculateDistance(double speed, long deltaTime) {
    return (this.speed + speed) * 0.5 * deltaTime;
  }

  private Point translate(Direction direction, double distance) {
    return new Point(
        (int) (this.position.getX() + direction.getX() * distance),
        (int) (this.position.getY() + direction.getY() * distance));
  }

  private BufferedImage loadSpritesheet() throws IOException {
    String imagePathname = String.format("%1$sresources%1$splayer-spritesheet.png", File.separator);
    URL imageUrl = getClass().getResource(imagePathname);
    return ImageIO.read(imageUrl);
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
