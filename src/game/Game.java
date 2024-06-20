package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JFrame;

import gameobjects.Enemy;
import gameobjects.player.Player;
import gameobjects.player.PlayerKeymap;

public class Game extends JFrame implements Runnable {
  public static final int TARGET_FPS = 60;
  public static final boolean SHOULD_PRINT_FPS = true;
  public static final Dimension WINDOW_SIZE = new Dimension(1000, 1000);

  private final GameLoop gameLoop;
  private final GameKeyListener keyListener;
  private final List<GameObject> gameObjects;

  public Game() {
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
    this.gameObjects.addAll(List.of(
        new Player(
            this.keyListener,
            new PlayerKeymap(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A),
            new Point(),
            Color.red),
        new Player(
            this.keyListener,
            new PlayerKeymap(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT),
            new Point((int) WINDOW_SIZE.getWidth() - 100, 0),
            Color.blue),
        new Enemy()));

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
      this.setPreferredSize(WINDOW_SIZE);
    }

    @Override
    public void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      Graphics2D graphics2D = (Graphics2D) graphics;
      this.draw.accept(graphics2D);
    }
  }
}
