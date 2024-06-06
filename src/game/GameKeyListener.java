package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener {
  private static int ASCII_CHARACTER_SET_LENGTH = 128;

  private final boolean[] keysPressed;

  public GameKeyListener() {
    this.keysPressed = new boolean[ASCII_CHARACTER_SET_LENGTH];
  }

  public boolean isKeyPressed(int keyCode) {
    assertKeyCode(keyCode);
    return this.keysPressed[keyCode];
  }

  public boolean isNothingPressed() {
    for (boolean isKeyPressed : this.keysPressed) {
      if (isKeyPressed) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void keyPressed(KeyEvent event) {
    this.keysPressed[event.getKeyCode()] = true;
  }

  @Override
  public void keyReleased(KeyEvent event) {
    this.keysPressed[event.getKeyCode()] = false;
  }

  @Override
  public void keyTyped(KeyEvent event) {
  }

  private void assertKeyCode(int keyCode) {
    if (keyCode < 0 || keyCode >= ASCII_CHARACTER_SET_LENGTH) {
      String errorMessage = String.format("Invalid key code '%d'", keyCode);
      throw new IllegalArgumentException(errorMessage);
    }
  }
}
