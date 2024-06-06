package game;

import javax.swing.JPanel;

public abstract class GameObject extends JPanel {
  public abstract void update(double deltaTime);
}
