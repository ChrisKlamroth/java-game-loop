package game;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;

public class Sprite {
  private final BufferedImage spritesheet;
  private final Dimension frameSize;
  private final double scale;
  private final int firstFrameIndex;
  private final int animationFrameCount;
  private final double timePerFrame;

  private long timeElapsed;
  private int frameIndex;
  private Offset offset;

  public Sprite(
      BufferedImage spritesheet,
      Dimension frameSize,
      double scale,
      int firstFrameIndex,
      int animationFrameCount,
      Duration animationDuration) throws IOException {
    this.spritesheet = spritesheet;
    this.frameSize = frameSize;
    this.scale = scale;

    this.firstFrameIndex = firstFrameIndex;
    this.animationFrameCount = animationFrameCount;
    this.timePerFrame = animationDuration.toMillis() / this.animationFrameCount;

    this.timeElapsed = 0;
    this.frameIndex = 0;
    this.offset = this.calculateOffset();
  }

  public BufferedImage getFrame() {
    return this.spritesheet.getSubimage(
        this.offset.getX(),
        this.offset.getY(),
        (int) this.frameSize.getWidth(),
        (int) this.frameSize.getHeight());
  }

  public Dimension getSize() {
    return new Dimension(
        (int) (this.frameSize.getWidth() * this.scale),
        (int) (this.frameSize.getHeight() * this.scale));
  }

  public void update(long deltaTime) {
    this.offset = this.calculateOffset();
    if (this.timeElapsed >= this.timePerFrame) {
      this.frameIndex = ++this.frameIndex % this.animationFrameCount;
      this.timeElapsed -= this.timePerFrame;
    }
    this.timeElapsed += deltaTime;
  }

  private Offset calculateOffset() {
    return new Offset(
        this.frameSize.getWidth() * ((this.firstFrameIndex + this.frameIndex) % this.getSpritesheetColumns()),
        this.frameSize.getHeight()
            * Math.floor((this.firstFrameIndex + this.frameIndex) / this.getSpritesheetColumns()));
  }

  private int getSpritesheetColumns() {
    return (int) (this.spritesheet.getWidth() / this.frameSize.getWidth());
  }
}

class Offset {
  private double x;
  private double y;

  public Offset(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return (int) this.x;
  }

  public int getY() {
    return (int) this.y;
  }

  @Override
  public String toString() {
    return "Offset[x=%f, y=%f]".formatted(this.x, this.y);
  }
}
