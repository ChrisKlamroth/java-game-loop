package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JFrame;

import gameobjects.Floor;
import gameobjects.player.Player;
import gameobjects.player.PlayerKeymap;

public class Game extends JFrame implements Runnable {
  public static final int TARGET_FPS = 60;
  public static final boolean SHOULD_PRINT_FPS = false;

  /**
   * If a second screen is being used (ex. a second monitor), return the bounds
   * of that screen, otherwise return the bounds of the primary screen (ex.
   * the laptop screen).
   * 
   * @return The {@link Rectangle bounds} of the window where the frame is
   *         rendered.
   */
  public static Rectangle getWindowBounds() {
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] screens = graphicsEnvironment.getScreenDevices();
    int screenIndex = screens.length == 1 ? 0 : 1;

    return screens[screenIndex].getDefaultConfiguration().getBounds();
  }

  /**
   * If a single screen is being used (ex. the laptop screen), position the
   * frame on its top-left corner, otherwise position the frame on the top-left
   * corner of the second screen (ex. a second monitor).
   * 
   * @return The {@link Point position} where the frame should be rendered.
   */
  private static Point getWindowPosition() {
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] screens = graphicsEnvironment.getScreenDevices();

    if (screens.length == 1) {
      return new Point();
    }

    Rectangle windowBounds = screens[0].getDefaultConfiguration().getBounds();
    return new Point(
        (int) windowBounds.getWidth(),
        0);
  }

  private final GameLoop gameLoop;
  private final GameKeyListener keyListener;
  private final List<GameObject> gameObjects;

  public Game() throws IOException {
    super("Game Loop");
    this.gameLoop = new GameLoop(
        TARGET_FPS,
        SHOULD_PRINT_FPS,
        this::update,
        this::repaint,
        this::isRunning,
        this::onExit);

    this.keyListener = new GameKeyListener();

    this.gameObjects = new ArrayList<>();

    Floor floor = new Floor();
    Player player = new Player(
        this.keyListener,
        new PlayerKeymap(
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_SPACE),
        Color.blue);

    this.gameObjects.addAll(List.of(floor, player));

    this.setLocation(getWindowPosition());
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.addKeyListener(this.keyListener);
    this.add(new Root(this::draw));
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void run() {
    this.gameLoop.start();
  }

  private void update(long deltaTime) {
    this.gameObjects.forEach(gameObject -> gameObject.update(deltaTime));
  }

  private void draw(Graphics2D graphics2D) {
    this.gameObjects.forEach(gameObject -> gameObject.draw(graphics2D));
  }

  private boolean isRunning() {
    return !this.keyListener.isKeyPressed(KeyEvent.VK_ESCAPE);
  }

  private void onExit() {
    System.out.println("Exit");
    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  private class Root extends JComponent {
    private final Consumer<Graphics2D> draw;

    public Root(Consumer<Graphics2D> draw) {
      this.draw = draw;
      this.setPreferredSize(Game.getWindowBounds().getSize());
    }

    @Override
    public void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      Graphics2D graphics2D = (Graphics2D) graphics;
      this.draw.accept(graphics2D);
    }
  }
}
