package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.Player;
import player.PlayerKeymap;

public class Game extends JFrame implements Runnable {
  public static Dimension WINDOW_SIZE = new Dimension(800, 800);

  private static int ONE_SECOND_IN_MILLISECONDS = 1000;
  private static int ONE_SECOND_IN_NANOSECONDS = 1000000000;
  private static int TARGET_FPS = 60;
  private static double OPTIMAL_TIME = ONE_SECOND_IN_NANOSECONDS / TARGET_FPS;
  private static boolean SHOULD_PRINT_FPS = true;

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
    double deltaTime = 0;
    int frameCount = 0;
    long previousTime = System.nanoTime();
    long timer = System.currentTimeMillis();

    while (!this.keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)) {
      long currentTime = System.nanoTime();
      deltaTime += (currentTime - previousTime) / OPTIMAL_TIME;
      previousTime = currentTime;

      if (deltaTime >= 1) {
        this.update(deltaTime);
        this.repaint();
        frameCount++;
        deltaTime--;
      }

      boolean hasOneSecondPassed = System.currentTimeMillis() - timer > ONE_SECOND_IN_MILLISECONDS;
      if (hasOneSecondPassed) {
        if (SHOULD_PRINT_FPS) {
          System.out.println(String.format("FPS: %d", frameCount));
        }
        frameCount = 0;
        timer += ONE_SECOND_IN_MILLISECONDS;
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
