package render_and_logic;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] args) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Chess Game");
        config.setWindowedMode(800, 800);

        MainGame game = new MainGame();
        new Lwjgl3Application(game, config);
    }
}


// TODO
// Starting screen?
// Ending screen
// Menu
// Online
// Promotion
