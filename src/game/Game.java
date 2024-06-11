package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.Player;
import player.PlayerKeymap;

public class Game extends JFrame implements Runnable {
  public static final Dimension WINDOW_SIZE = new Dimension(800, 800);

  private static final boolean SHOULD_PRINT_FPS = true;
  private static final int TARGET_FPS = 60;
  private static final double NANOSECONDS_PER_FRAME = Duration.ofSeconds(1).toNanos() / TARGET_FPS;

  private final GameKeyListener keyListener;
  private final List<GameObject> gameObjects;

  public Game() {
    super("Game Loop");
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
            Color.blue)));

    JPanel root = new JPanel();
    root.setLayout(null);
    root.setPreferredSize(WINDOW_SIZE);
    this.gameObjects.forEach(gameObject -> root.add(gameObject));

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.addKeyListener(this.keyListener);
    this.getContentPane().add(root);
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void run() {
    double loopTimeProgress = 0;
    int frameCount = 0;

    long previousLoopTime = System.nanoTime();
    long previousFrameTime = System.currentTimeMillis();

    long timer = System.currentTimeMillis();

    while (!this.keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)) {
      // This game loop is made so that the FPS is capped at TARGET_FPS. This is done
      // by updating loopTimeProgress at each loop iteration and if it's greater than
      // 1, that means it's time to update and render the game.
      long currentLoopTime = System.nanoTime();
      loopTimeProgress += (currentLoopTime - previousLoopTime) / NANOSECONDS_PER_FRAME;
      previousLoopTime = currentLoopTime;

      if (loopTimeProgress >= 1) {
        // Calculate the time since the last frame (deltaTime) and pass it to update so
        // that everything scales accordingly with the FPS.
        long currentFrameTime = System.currentTimeMillis();
        long deltaTime = currentFrameTime - previousFrameTime;
        previousFrameTime = currentFrameTime;

        this.update(deltaTime);
        this.repaint();

        frameCount++;
        loopTimeProgress--;
      }

      boolean hasOneSecondPassed = System.currentTimeMillis() - timer > Duration.ofSeconds(1).toMillis();
      if (hasOneSecondPassed) {
        if (SHOULD_PRINT_FPS) {
          System.out.println(String.format("FPS: %d", frameCount));
        }
        frameCount = 0;
        timer += Duration.ofSeconds(1).toMillis();
      }
    }

    this.exit();
  }

  private void update(double deltaTime) {
    this.gameObjects.forEach(gameObject -> gameObject.update(deltaTime));
  }

  private void exit() {
    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    System.out.println("Exit");
  }
}
