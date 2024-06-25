import java.io.IOException;

import game.Game;

public class App {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.run();
    }
}
