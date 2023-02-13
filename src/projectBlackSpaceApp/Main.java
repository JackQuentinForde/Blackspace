package projectBlackSpaceApp;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.*;
import org.lwjgl.openal.SOFTHRTF;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private long device;
    private long context;
    private IntBuffer contextBuf;

    private GLFWKeyCallback keyCallback;
    private GLFWErrorCallback errorCallback;

    private long window;
    private final int windowWidth = 800;
    private final int windowHeight = 600;

    private int gameState;
    private float redVal;
    private float greenVal;
    private float blueVal;

    Shaders shaders = new Shaders();
    Shapes shapes = new Shapes();
    TTS tts = new TTS();

    Listener listener = new Listener();
    Sources sources = new Sources();
    Sounds sounds = new Sounds();

    //Game States
    Menu menu = new Menu(listener, tts);
    Game game = new Game(listener, sources, sounds, tts);

    /***Initialise OpenAL**/
    private void init() {

        device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Error: failed to open the default device");
        }

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        contextBuf = (IntBuffer) BufferUtils.createIntBuffer(3).put(new int[]{SOFTHRTF.ALC_HRTF_SOFT, ALC_TRUE, 0}).flip();

        System.out.println("OpenALC10: " + deviceCaps.OpenALC10);
        System.out.println("OpenALC11: " + deviceCaps.OpenALC11);
        System.out.println("caps.ALC_EXT_EFX = " + deviceCaps.ALC_EXT_EFX);

        System.out.println("\nNum HRTF specifiers = 2: " + (alcGetInteger(device, SOFTHRTF.ALC_NUM_HRTF_SPECIFIERS_SOFT) == 2));

        context = alcCreateContext(device, contextBuf);
        alcMakeContextCurrent(context);

        System.out.println("Sample rate: " + alcGetString(device, SOFTHRTF.ALC_HRTF_SPECIFIER_SOFT));
        int response = alcGetInteger(device, SOFTHRTF.ALC_HRTF_STATUS_SOFT);
        System.out.println("HRTF enabled: " + (response == SOFTHRTF.ALC_HRTF_ENABLED_SOFT || response == SOFTHRTF.ALC_HRTF_REQUIRED_SOFT));
        System.out.println("HRTF state: 0x" + Integer.toHexString(response));

        AL.createCapabilities(deviceCaps);
        AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

        run();
        try {
            glfwDestroyWindow(window);
            keyCallback.free();
        } finally {
            glfwTerminate();
            errorCallback.free();
        }
    }

    /**Intialise OpenGL**/
    private void run() {

        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        if (glfwInit() != true) {
            throw new IllegalStateException("Error: GLFW failed to initialise");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        window = glfwCreateWindow(windowWidth, windowHeight, "Blackspace", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Error: Failed to create window");
        }

        glfwSetKeyCallback(window, keyCallback = new Input());
        glfwSetWindowPos(window, (vidMode.width() - windowWidth) / 2, (vidMode.height() - windowHeight) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();
        System.out.println("\nOpenGL version: " + glGetString(GL_VERSION));

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        shaders.init();
        shapes.init();
        gameState = 1;

        /**Activity Loop**/
        while (glfwWindowShouldClose(window) != true) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            switch (gameState) {

                case 0:
                    glfwSetWindowShouldClose(window, true);
                    break;

                case 1:
                    if (!menu.running) {
                        menu.init();
                    }
                    else {
                        menu.run();

                        if (!menu.running) {

                            if (menu.menuItem == 0) {
                                gameState = 2;
                            }
                            else {
                                gameState = 0;
                            }
                        }
                    }
                    break;

                case 2:
                    if (!game.running) {
                        game.init();
                    }
                    else {
                        game.run();
                        redVal = game.redVal;
                        greenVal = game.greenVal;
                        blueVal = game.blueVal;

                        if (!game.running) {
                            gameState = 1;
                        }
                    }
                    break;
            }

            shapes.bindArray();
            glUniform3f(glGetUniformLocation(shaders.shaderProgram, "colourMod"), redVal, greenVal, blueVal);
            shaders.useProgram();

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        terminate();
    }

    private void terminate() {
        shapes.deleteResources();
        shaders.deleteProgram();
        tts.stop();
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public static void main(String[] args) {
        new Main().init();
    }
}

