package game;

import java.awt.Color;
import java.awt.Dimension;
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
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JFrame;

import gameobjects.Floor;
import gameobjects.Manual;
import gameobjects.Platform;
import gameobjects.TestObject;
import gameobjects.player.InteractionZone;
import gameobjects.player.Player;
import gameobjects.player.PlayerKeymap;

public class Game extends JFrame implements Runnable {
  public static final int TARGET_FPS = 60;
  public static final boolean SHOULD_PRINT_FPS = false;
  
  private Player player;
  private InteractionZone interaction;

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
  private final List<LocatedRectangle> interactionZones;
  private final List<LocatedRectangle> objectsWithHitbox;

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
    this.interactionZones = new ArrayList<>();
    this.objectsWithHitbox = new ArrayList<>();

    Manual manual = new Manual();
    Floor floor = new Floor();
    Platform platform1 = new Platform(
    		new Point(
    		 (int) Game.getWindowBounds().getWidth()/2-(int) Game.getWindowBounds().getWidth()/12,
    		 (int) Game.getWindowBounds().getHeight()/2),
    		new Dimension(
    		 (int) Game.getWindowBounds().getWidth()/6,
    		(int) Game.getWindowBounds().getHeight()/20));
    Platform platform2 = new Platform(
    		new Point(
    		 100,
			 (int) Game.getWindowBounds().getHeight()/4),
			new Dimension(
			 (int) Game.getWindowBounds().getWidth()/6,
			 (int) Game.getWindowBounds().getHeight()/20));
    Platform platform3 = new Platform(
    		new Point(
    		 (int) Game.getWindowBounds().getWidth()-(int) Game.getWindowBounds().getWidth()/6-100,
			 (int) Game.getWindowBounds().getHeight()/4),
			new Dimension(
			 (int) Game.getWindowBounds().getWidth()/6,
			 (int) Game.getWindowBounds().getHeight()/20));
    TestObject testObject= new TestObject(new Dimension(100, 100), new Point(700, 200));
    player = new Player(
        this.keyListener,
        new PlayerKeymap(
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_C,
            KeyEvent.VK_SPACE));

    this.gameObjects.addAll(List.of(manual, floor, player, testObject, platform1,platform2,platform3));
    this.objectsWithHitbox.addAll(List.of(player, testObject, floor, platform1,platform2,platform3));

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
	interactionManager();
	  
    this.gameObjects.forEach(gameObject -> gameObject.update(deltaTime));
    
    for(LocatedRectangle part: objectsWithHitbox) {
    	if(!objectsWithHitbox.get(1).vacantSpace(part)&&!(objectsWithHitbox.get(1)==part)) {
        	objectsWithHitbox.get(1).collisionDirection(part);
    	}
    }
    
    for(LocatedRectangle part: objectsWithHitbox) {
    	if(!objectsWithHitbox.get(0).vacantSpace(part)&&!(objectsWithHitbox.get(0)==part)) {
        	objectsWithHitbox.get(0).collisionDirection(part);
    	}
    }
    
    
  }

  private void draw(Graphics2D graphics2D) {
    this.gameObjects.forEach(gameObject -> gameObject.draw(graphics2D));
  }
  
  private void interactionManager() {
	  if(!(player.getInteraction()==null)&&!(interactionZones.contains(player.getInteraction()))) {
		  this.gameObjects.add(player.getInteraction());
		  this.interactionZones.add(player.getInteraction());
		  //System.out.println(interactionZones.size());
	  }
	  for(LocatedRectangle i: interactionZones) {
		  if(!(objectsWithHitbox.get(1).vacantSpace(i))){
			  System.out.println("hit");
			  objectsWithHitbox.get(1).setSpeed(new Vector2D(10,0));
		  }
		  
		  if(!(i==null)&&(new Date().getTime()-i.getTime()>i.getTimer())) {
			  gameObjects.remove(i);
			  interactionZones.remove(i);
//			  System.out.println(interactionZones.size());
			  break;
		  }
	  }
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
