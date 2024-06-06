import game.Game;

public class App {
    public static void main(String[] args) {
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.run();
    }
}
