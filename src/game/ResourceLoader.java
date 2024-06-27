package game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ResourceLoader {
  private static final String RESOURCE_FOLDER = "resources";

  public static BufferedImage loadPlayerSpritesheet() throws IOException {
    URL resource = getResource("player-spritesheet.png");
    BufferedImage playerSpritesheet = ImageIO.read(resource);

    System.out.println("Player spritesheet loaded");

    return playerSpritesheet;
  }

  private static URL getResource(String name) {
    return ResourceLoader.class.getResource("/%s/%s".formatted(
        RESOURCE_FOLDER,
        name));
  }
}
