package projectBlackSpaceApp;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    protected boolean running;
    private boolean levelComplete;

    /**Game Variables**/
    private boolean isHit;
    private boolean nearDoor;
    private int doorType;

    /**Time Variables**/
    private double newTime;
    private double delta;
    private double prevTime;

    /**RGB Variables**/
    protected float redVal = 0.0f;
    protected float greenVal = 0.0f;
    protected float blueVal = 0.0f;

    /**Class Reference Variables**/
    TTS tts;
    Listener listener;
    Sources sources;
    Sounds sounds;
    Maps maps = new Maps();
    Player player;
    Items items;
    Ambience ambience;
    Registry registry;
    Zombies zombies;
    Demons demons;
    Trolls trolls;
    Ghosts ghosts;
    Soldiers soldiers;
    Orcs orcs;
    Elites elites;

    public Game(Listener inListener, Sources inSources, Sounds inSounds, TTS inTts) {

        tts = inTts;
        listener = inListener;
        sources = inSources;
        sounds = inSounds;

        ambience = new Ambience(listener, sources, sounds, tts, maps);
        registry = new Registry();
        player = new Player(listener, sources, sounds, tts, maps, registry);
        zombies = new Zombies(sources, sounds, maps, registry);
        demons = new Demons(sources, sounds, maps, registry);
        trolls = new Trolls(sources, sounds, maps, registry);
        ghosts = new Ghosts(sources, sounds, maps, registry);
        soldiers = new Soldiers(sources, sounds, maps, registry, player);
        orcs = new Orcs(sources, sounds, maps, registry, player);
        elites = new Elites(sources, sounds, maps, registry, player);
        items = new Items(listener, sources, sounds, maps, player);
    }

    protected void init() {

        tts.stop();
        levelComplete = false;
        running = true;
        maps.loadMap("M1.txt");

        listener.setPosition(maps.playerPos[0], maps.playerPos[1]);

        player.init();
        player.update();

        if (maps.zombies.size()>0) {
            zombies.init();
        }
        if (maps.demons.size()>0) {
            demons.init();
        }
        if (maps.trolls.size()>0) {
            trolls.init();
        }
        if (maps.ghosts.size()>0) {
            ghosts.init();
        }
        if (maps.soldiers.size()>0) {
            soldiers.init();
        }
        if (maps.orcs.size()>0) {
            orcs.init();
        }
        if (maps.elites.size()>0) {
            elites.init();
        }

        items.initSources();
        items.playSounds();

        ambience.initSources();
        ambience.playSounds();
        prevTime = glfwGetTime();
        player.spawn();
    }

    protected void run() {

        //Reset booleans
        isHit = false;
        nearDoor = false;

        /**Check Collisions**/
        //Walls
        if (maps.walls.size() > 0) {
            for (int i = 0; i < maps.walls.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.walls.get(i), maps.walls.get(i + 1), player.bubble)) {
                    isHit = true;
                }
            }
        }

        //Doors
        if (maps.doors.size() > 0) {
            for (int i = 0; i < maps.doors.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.doors.get(i), maps.doors.get(i + 1), player.bubble)) {
                    doorType = 0;
                    nearDoor = true;
                }
            }
        }

        if (maps.redDoors.size() > 0) {
            for (int i = 0; i < maps.redDoors.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.redDoors.get(i), maps.redDoors.get(i + 1), player.bubble)) {
                    doorType = 1;
                    if (player.keysHeld[0] == 1) {
                        nearDoor = true;
                        i = maps.redDoors.size();
                    }
                    else {
                        isHit = true;
                        ambience.doorLocked(doorType);
                    }
                }
            }
        }

        if (maps.blueDoors.size() > 0) {
            for (int i = 0; i < maps.blueDoors.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.blueDoors.get(i), maps.blueDoors.get(i + 1), player.bubble)) {
                    doorType = 2;
                    if (player.keysHeld[1] == 1) {
                        nearDoor = true;
                        i = maps.blueDoors.size();
                    }
                    else {
                        isHit = true;
                        ambience.doorLocked(doorType);
                    }
                }
            }
        }

        if (maps.yellowDoors.size() > 0) {
            for (int i = 0; i < maps.yellowDoors.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.yellowDoors.get(i), maps.yellowDoors.get(i + 1), player.bubble)) {
                    doorType = 3;
                    if (player.keysHeld[2] == 1) {
                        nearDoor = true;
                        i = maps.yellowDoors.size();
                    }
                    else {
                        isHit = true;
                        ambience.doorLocked(doorType);
                    }
                }
            }
        }

        //Surfaces
        if (maps.hard.size() > 0) {
            for (int i = 0; i < maps.hard.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.hard.get(i), maps.hard.get(i + 1), player.bubble)) {
                    player.surface = player.hardSteps;
                }
            }
        }
        if (maps.leaves.size() > 0) {
            for (int i = 0; i < maps.leaves.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.leaves.get(i), maps.leaves.get(i + 1), player.bubble)) {
                    player.surface = player.leafSteps;
                }
            }
        }
        if (maps.gravel.size() > 0) {
            for (int i = 0; i < maps.gravel.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.gravel.get(i), maps.gravel.get(i + 1), player.bubble)) {
                    player.surface = player.gravelSteps;
                }
            }
        }
        if (maps.water.size() > 0) {
            for (int i = 0; i < maps.water.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.water.get(i), maps.water.get(i + 1), player.bubble)) {
                    player.surface = player.waterSteps;
                }
            }
        }
        if (maps.metal.size() > 0) {
            for (int i = 0; i < maps.metal.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.metal.get(i), maps.metal.get(i + 1), player.bubble)) {
                    player.surface = player.metalSteps;
                }
            }
        }

        //Items
        if (maps.healthSpawns.size() > 0 && player.health < player.healthLim) {
            int count = 0;
            for (int i = 0; i < maps.healthSpawns.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.healthSpawns.get(i), maps.healthSpawns.get(i + 1), items.bubble)) {
                    maps.healthSpawns.remove(maps.healthSpawns.get(i + 1));
                    maps.healthSpawns.remove(maps.healthSpawns.get(i));
                    items.giveHealth(count);
                    player.gasp();
                    checkHealth();
                }
                count++;
            }
        }

        if (maps.ammoSpawns.size() > 0 && player.ammo < player.ammoLim) {
            int count = 0;
            for (int i = 0; i < maps.ammoSpawns.size(); i += 2) {
                if (onCollision(listener.posX, listener.posZ, maps.ammoSpawns.get(i), maps.ammoSpawns.get(i + 1), items.bubble)) {
                    maps.ammoSpawns.remove(maps.ammoSpawns.get(i + 1));
                    maps.ammoSpawns.remove(maps.ammoSpawns.get(i));
                    items.giveAmmo(count);
                    player.reload();
                    checkAmmo();
                }
                count++;
            }
        }

        if (maps.redKey.length == 2) {
            if (onCollision(listener.posX, listener.posZ, maps.redKey[0], maps.redKey[1], items.bubble)) {
                maps.redKey = new int[1];
                items.giveKeys(0);
                tts.speak("red key");
            }

        }

        if (maps.blueKey.length == 2) {
            if (onCollision(listener.posX, listener.posZ, maps.blueKey[0], maps.blueKey[1], items.bubble)) {
                maps.blueKey = new int[1];
                items.giveKeys(1);
                tts.speak("blue key");
            }

        }

        if (maps.yellowKey.length == 2) {
            if (onCollision(listener.posX, listener.posZ, maps.yellowKey[0], maps.yellowKey[1], items.bubble)) {
                maps.yellowKey = new int[1];
                items.giveKeys(2);
                tts.speak("yellow key");
            }

        }

        //Enemies
        for (Enemy enemy : registry.allEnemies) {
            if (enemy.mapCoords.size() > 0 && player.health > 0) {
                for (int i = 0; i < enemy.mapCoords.size(); i += 2) {
                    if (onCollision(listener.posX, listener.posZ, enemy.mapCoords.get(i), enemy.mapCoords.get(i + 1), enemy.bubble)) {
                        player.takeDmg(enemy.attkDmg);
                    }
                }
            }
        }

        for (Enemy enemy : registry.allEnemies) {
            if (enemy.mapCoords.size() > 0) {
                enemy.update(listener.posX, listener.posZ, (float) delta);
            }
        }

        if (player.health>0 && !levelComplete) {

            if (onCollision(listener.posX, listener.posZ, maps.finishPos[0], maps.finishPos[1], player.bubble)) {
                levelComplete = true;
                ambience.finishLevel();
            }

            /**Input Processing**/
            if (Input.isKeyDown(GLFW_KEY_W) || Input.isKeyDown(GLFW_KEY_UP)) {
                listener.moveForward = true;
            }
            else {
                listener.moveForward = false;
            }

            if (Input.isKeyDown(GLFW_KEY_S) || Input.isKeyDown(GLFW_KEY_DOWN)) {
                listener.moveBackward = true;
            }
            else {
                listener.moveBackward = false;
            }

            if (Input.isKeyDown(GLFW_KEY_D)) {
                listener.moveRight = true;
            }
            else {
                listener.moveRight = false;
            }

            if (Input.isKeyDown(GLFW_KEY_A)) {
                listener.moveLeft = true;
            }
            else {
                listener.moveLeft = false;
            }

            if (Input.isKeyDown(GLFW_KEY_RIGHT)) {
                listener.turnRight = true;
            }
            else {
                listener.turnRight = false;
            }

            if (Input.isKeyDown(GLFW_KEY_LEFT)) {
                listener.turnLeft = true;
            }
            else {
                listener.turnLeft = false;
            }

            if (Input.isKeyDown(GLFW_KEY_SPACE) || Input.isKeyDown(GLFW_KEY_LEFT_CONTROL)) {
                player.shoot();
            }

            if (Input.isKeyDown(GLFW_KEY_E)) {
                checkAmmo();
            }

            if (Input.isKeyDown(GLFW_KEY_Q)) {
                checkHealth();
            }

            if (Input.isKeyDown(GLFW_KEY_F)) {
                player.checkKeys();
            }

            if (Input.isKeyDown(GLFW_KEY_C)) {
                ambience.findDoors();
            }

            newTime = glfwGetTime();
            delta = newTime - prevTime;
            prevTime = newTime;

            if (!isHit) {
                listener.update((float) delta);
            }
            else {
                listener.rebound();
            }

            player.update();

            if (!isHit && (listener.moveForward || listener.moveBackward || listener.moveRight || listener.moveLeft)) {

                player.takeStep();
            }
            else if (isHit) {

                player.takeHit();
            }

            if (nearDoor) {

                ambience.updateDoor();

                if (!ambience.doorOpen) {

                    ambience.openDoor(doorType);

                    switch (doorType) {
                        case 1:
                            tts.speak("Used red key");
                            break;
                        case 2:
                            tts.speak("Used blue key");
                            break;
                        case 3:
                            tts.speak("Used yellow key");
                            break;
                    }
                }
            }
            else {

                if (ambience.doorOpen) {

                    ambience.closeDoor();
                }
            }
        }

        if (Input.isKeyDown(GLFW_KEY_ESCAPE)) {
            running = false;
            cleanUp();
        }

        redVal = player.redVal;
        greenVal = player.greenVal;
        blueVal = player.blueVal;

        if (player.redVal>0) {
            player.redVal -= 0.06f;
        }

        if (player.greenVal>0) {
            player.greenVal -= 0.03f;
        }

        if (player.blueVal>0) {
            player.blueVal -= 0.03f;
        }
    }

    private void checkAmmo() {

        if (player.ammo == player.ammoLim) {
            tts.speak("Ammo full");
        }
        else {
            tts.speak("Ammo " + player.ammo);
        }
    }

    private void checkHealth() {

        tts.speak("Health " + player.health + "%");
    }

    private boolean onCollision(float x1, float y1, float x2, float y2, float bubble) {

        if (x1 >= x2 - bubble && x1 <= x2 + bubble && y1 >= y2 - bubble && y1 <= y2 + bubble) {
            return true;
        } else {
            return false;
        }
    }

    private void cleanUp() {

        maps.clearAll();
        ambience.clearAll();
        items.clearAll();
        zombies.clearAll();
        registry.clearAll();
        sounds.clearAll();
        sources.clearAll();
    }
}
