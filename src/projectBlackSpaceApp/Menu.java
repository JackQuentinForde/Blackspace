package projectBlackSpaceApp;

import static org.lwjgl.glfw.GLFW.*;

public class Menu {

    TTS tts;
    Listener listener;

    protected boolean running = false;
    protected int menuItem;

    public Menu(Listener inListener, TTS inTts) {

        tts = inTts;
        listener = inListener;
    }

    protected void init() {

        listener.init();
        menuItem = 0;
        running = true;
        tts.speak("Use the arrow keys and enter key to navigate the menu");
    }

    protected void run() {

        if (Input.isKeyDown(GLFW_KEY_UP)) {

            menuItem = 0;
            tts.speak("New Game");
        }

        if (Input.isKeyDown(GLFW_KEY_DOWN)) {

            menuItem = 1;
            tts.speak("Quit Game");
        }

        if (Input.isKeyDown(GLFW_KEY_ENTER)) {
            running = false;
        }
    }
}
