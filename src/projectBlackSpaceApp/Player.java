package projectBlackSpaceApp;

import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Player {

    /**Class Reference Variables**/
    Listener listener;
    Sources sources;
    Sounds sounds;
    TTS tts;
    Maps maps;
    Registry registry;

    /**Sources**/
    private int head;
    private int hands;
    private int feet;

    /**Sounds**/
    protected int[] hardSteps = new int[8];
    protected int[] leafSteps = new int[8];
    protected int[] gravelSteps = new int[8];
    protected int[] waterSteps = new int[4];
    protected int[] metalSteps = new int[4];
    protected int[] surface;
    private int currentStep;
    private int lastStep;

    private int spawnSound;
    private int shotgun;
    private int reload;
    private int empty;
    private int bumpSound;
    private int hurtSound;
    private int deathSound;
    private int gaspSound;
    private int getKey;

    /**RGB Variables**/
    protected float redVal = 0.0f;
    protected float greenVal = 0.0f;
    protected float blueVal = 0.0f;

    /**Time Variables**/
    //Step Time
    private double nStepTime = 0;
    private double stepInterval = 0.35;
    private double lStepTime = 0;

    //Speak Time
    private double nSpeakTime = 0;
    private double speakInterval = 0.85;
    private double lSpeakTime = 0;

    //Shoot Time
    private double nShotTime = 0;
    private double shotInterval = 0.65;
    private double lShotTime = 0;

    /**Game Variables**/
    protected float bubble = 0.5f;
    protected int health;
    protected final int healthLim = 100;
    protected int ammo;
    protected final int ammoLim = 50;

    private float distX;
    private float distZ;
    private double nearDist;
    private final float maxDist = 15.0f;
    private final float shotOffset = 0.0085f;
    protected final int attkDmg = 25;
    protected int[] keysHeld;

    Random rn = new Random();

    public Player(Listener inListener, Sources inSources, Sounds inSounds, TTS inTTs, Maps inMaps, Registry inRegistry) {

        listener = inListener;
        sources = inSources;
        sounds = inSounds;
        tts = inTTs;
        maps = inMaps;
        registry = inRegistry;
    }

    protected void init() {

        head = sources.initSource();
        spawnSound = sounds.loadSound("player/spawn.wav");
        bumpSound = sounds.loadSound("player/hitWall.wav");
        hurtSound = sounds.loadSound("player/damage.wav");
        deathSound = sounds.loadSound("player/death.wav");
        gaspSound = sounds.loadSound("player/gasp.wav");
        getKey = sounds.loadSound("player/getKey.wav");

        hands = sources.initSource();
        shotgun = sounds.loadSound("player/shotgun.wav");
        reload = sounds.loadSound("player/reload.wav");
        empty = sounds.loadSound("player/empty.wav");

        feet = sources.initSource();
        for (int i=0; i<hardSteps.length; i++) {
            hardSteps[i] = sounds.loadSound("player/footsteps/hard" + i + ".wav");
            leafSteps[i] = sounds.loadSound("player/footsteps/leaves" + i + ".wav");
            gravelSteps[i] = sounds.loadSound("player/footsteps/gravel" + i + ".wav");
        }
        for (int i=0; i<waterSteps.length; i++) {
            waterSteps[i] = sounds.loadSound("player/footsteps/water" + i + ".wav");
            metalSteps[i] = sounds.loadSound("player/footsteps/metal" + i + ".wav");
        }
        surface = hardSteps;

        health = healthLim;
        ammo = 20;
        keysHeld = new int[3];
    }

    protected void update() {
        sources.setPosition(head, listener.posX, 0.0f, listener.posZ);
        sources.setPosition(hands, listener.posX, -4.0f, listener.posZ);
        sources.setPosition(feet, listener.posX, -8.0f, listener.posZ);
    }

    protected void spawn() {
        sources.playSound(head, spawnSound);
    }

    protected void takeStep() {

        nStepTime = glfwGetTime();

        if (nStepTime-lStepTime>=stepInterval) {

            currentStep = rn.nextInt(surface.length);
            if (currentStep == lastStep) {
                if (currentStep == surface.length-1) {
                    currentStep--;
                }
                else {
                    currentStep++;
                }
            }

            sources.playSound(feet, surface[currentStep]);
            lastStep = currentStep;
            lStepTime = nStepTime;
        }
    }

    protected void takeHit() {

        nStepTime = glfwGetTime();

        if (nStepTime-lStepTime>=stepInterval){
            sources.playSound(head, bumpSound);
            redVal += 0.25f;
            lStepTime = nStepTime;
        }
    }

    protected void shoot() {

        nShotTime = glfwGetTime();

        if (nShotTime-lShotTime>=shotInterval) {

            if (ammo>0) {
                sources.playSound(hands, shotgun);
                redVal += 0.5f;
                ammo--;

                if (registry.allEnemies.size() > 0) {
                    nearDist = -1;
                    if (maps.obstructions.size() > 0) {
                        checkHit(null, maps.obstructions, shotOffset);
                    }
                    for (Enemy enemy : registry.allEnemies) {
                        if (enemy.mapCoords.size() > 0) {
                            checkHit(enemy, enemy.mapCoords, enemy.bubble);
                        }
                    }
                }
            }
            else {
                sources.playSound(hands, empty);
            }
            lShotTime = nShotTime;
        }
    }

    protected void takeDmg(int dmg) {

        nSpeakTime = glfwGetTime();

        if (nSpeakTime-lSpeakTime>=speakInterval){
            health -= dmg;
            if (health>0) {
                sources.playSound(head, hurtSound);
                redVal += 0.5f;
            }
            else {
                sources.playSound(head, deathSound);
                tts.speak("You are dead");
                redVal += 1.0f;
            }
            lSpeakTime = nSpeakTime;
        }
    }

    private void checkHit(Enemy enemy, ArrayList<Float> entityList, float entityBubble) {

        int count = 0;
        for (int i=0; i<entityList.size(); i+=2) {

            float xPos = entityList.get(i);
            float zPos = entityList.get(i + 1);
            float playerX = listener.posX;
            float playerZ = listener.posZ;


            if (playerX > xPos) {
                distX = playerX - xPos;
            }
            else {
                distX = xPos - playerX;
            }

            if (playerZ > zPos) {
                distZ = playerZ - zPos;
            }
            else {
                distZ = zPos - playerZ;
            }

            double distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distZ, 2));

            if (distance <= maxDist) {

                double relativeAngle = Math.atan2(distZ, distX);
                double orientAngle = listener.orientAngle;
                float zDirection = listener.listenerOri[2];

                if (zPos > playerZ) {
                    if ((orientAngle > 0 && zDirection == -1.0f) || (orientAngle < 0 && zDirection == 1.0f)) {
                        orientAngle *= -1;
                    }
                } else {
                    if ((orientAngle < 0 && zDirection == -1.0f) || (orientAngle > 0 && zDirection == 1.0f)) {
                        orientAngle *= -1;
                    }
                }

                if (orientAngle <= relativeAngle + entityBubble && orientAngle >= relativeAngle - entityBubble) {

                    if (distance < nearDist || nearDist == -1) {
                        if (entityList == maps.obstructions) {
                            nearDist = distance;
                        }
                        else {
                            enemy.takeDmg(count, attkDmg, i);
                            nearDist = -1;
                        }
                    }
                }
            }
            count++;
        }
    }

    protected void checkKeys() {
        if (keysHeld[0] == 1 || keysHeld[1] == 1 || keysHeld[2] == 1) {
            String keys = "";
            if (keysHeld[0] == 1) {
                keys = "red key ";
            }
            if (keysHeld[1] == 1) {
                keys = keys + "blue key ";
            }
            if (keysHeld[2] == 1) {
                keys = keys + "yellow key ";
            }
            tts.speak("holding " + keys);
        }
        else {
            tts.speak("No keys held");
        }
    }

    protected void reload() {
        sources.playSound(hands, reload);
        greenVal += 0.5f;
    }

    protected void gasp() {
        sources.playSound(head, gaspSound);
        blueVal += 0.5f;
    }

    protected void getKey() {
        sources.playSound(head, getKey);
    }
}
