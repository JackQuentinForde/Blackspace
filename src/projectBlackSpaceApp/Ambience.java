package projectBlackSpaceApp;

import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Ambience {

    /**Class Reference Variables**/
    Listener listener;
    Sources sources;
    Sounds sounds;
    TTS tts;
    Maps maps;

    /**Sources**/
    private int door;
    private int alarmSource;
    private ArrayList<Integer> beepSources = new ArrayList<>();
    private ArrayList<Integer> airSources = new ArrayList<>();
    private ArrayList<Integer> buzzSources = new ArrayList<>();
    private ArrayList<Integer> compSources = new ArrayList<>();
    private ArrayList<Integer> draftSources = new ArrayList<>();
    private ArrayList<Integer> errorSources = new ArrayList<>();
    private ArrayList<Integer> fireSources = new ArrayList<>();
    private ArrayList<Integer> humSources = new ArrayList<>();
    private ArrayList<Integer> machSources = new ArrayList<>();
    private ArrayList<Integer> plumSources = new ArrayList<>();
    private ArrayList<Integer> swampSources = new ArrayList<>();
    private ArrayList<Integer> thundSources = new ArrayList<>();
    private ArrayList<Integer> vacuumSources = new ArrayList<>();
    private ArrayList<Integer> waterSources = new ArrayList<>();

    /**Sounds**/
    private int dOpen;
    private int dClose;
    private int tryKey;
    private int useKey;
    private int doorBeep;
    private int airSound;
    private int buzzSound;
    private int compSound;
    private int draftSound;
    private int errorSound;
    private int fireSound;
    private int humSound;
    private int machSound;
    private int plumSound;
    private int swampSound;
    private int thundSound;
    private int vacSound;
    private int waterSound;
    private int alarmSound;
    private int beatLevel;

    /**Time Variables**/
    private double nDoorTime = 0;
    private double doorInterval = 1;
    private double lDoorTime = 0;

    private double nBeepTime = 0;
    private double beepInterval = 1.5;
    private double lBeepTime = 0;

    protected boolean doorOpen = false;

    Random rn = new Random();

    public Ambience(Listener inListener, Sources inSources, Sounds inSounds, TTS inTTS, Maps inMaps) {

        listener = inListener;
        sources = inSources;
        sounds = inSounds;
        tts = inTTS;
        maps = inMaps;
    }

    protected void initSources() {

        if (maps.doors.size()>0) {
            door = sources.initSource();
            dOpen = sounds.loadSound("world/doors/open.wav");
            dClose = sounds.loadSound("world/doors/close.wav");
        }

        if (maps.redDoors.size() > 0 || maps.blueDoors.size() > 0 || maps.yellowDoors.size() > 0) {
            tryKey = sounds.loadSound("world/doors/keyFail.wav");
            useKey = sounds.loadSound("world/doors/keyUse.wav");
        }

        if (maps.doorBeeps.size() > 0) {
            int count = 0;
            for (int i=0; i<maps.doorBeeps.size(); i+=2) {
                beepSources.add(count, sources.initSource());
                float pitch = rn.nextFloat() * (2.0f - 1.0f) + 1.0f;
                sources.setPitch(beepSources.get(count), pitch);
                sources.setPosition(beepSources.get(count), maps.doorBeeps.get(i), 0.0f, maps.doorBeeps.get(i+1));
                count++;
            }
            doorBeep = sounds.loadSound("world/doors/doorBeep.wav");
        }

        if (maps.airSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.airSpawns.size(); i+=2) {
                airSources.add(count, sources.initSource());
                sources.setPosition(airSources.get(count), maps.airSpawns.get(i), 8.0f, maps.airSpawns.get(i+1));
                count++;
            }
            airSound = sounds.loadSound("world/ambience/AIRDUCT.wav");
        }

        if (maps.buzzSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.buzzSpawns.size(); i+=2) {
                buzzSources.add(count, sources.initSource());
                sources.setPosition(buzzSources.get(count), maps.buzzSpawns.get(i), 8.0f, maps.buzzSpawns.get(i+1));
                count++;
            }
            buzzSound = sounds.loadSound("world/ambience/BUZZ.wav");
        }

        if (maps.compSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.compSpawns.size(); i+=2) {
                compSources.add(count, sources.initSource());
                sources.setPosition(compSources.get(count), maps.compSpawns.get(i), 0.0f, maps.compSpawns.get(i+1));
                count++;
            }
            compSound = sounds.loadSound("world/ambience/COMP.wav");
        }

        if (maps.draftSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.draftSpawns.size(); i+=2) {
                draftSources.add(count, sources.initSource());
                sources.setPosition(draftSources.get(count), maps.draftSpawns.get(i), 10.0f, maps.draftSpawns.get(i+1));
                count++;
            }
            draftSound = sounds.loadSound("world/ambience/DRAFT.wav");
        }

        if (maps.errorSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.errorSpawns.size(); i+=2) {
                errorSources.add(count, sources.initSource());
                sources.setPosition(errorSources.get(count), maps.errorSpawns.get(i), 0.0f, maps.errorSpawns.get(i+1));
                count++;
            }
            errorSound = sounds.loadSound("world/ambience/ERROR.wav");
        }

        if (maps.fireSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.fireSpawns.size(); i+=2) {
                fireSources.add(count, sources.initSource());
                sources.setPosition(fireSources.get(count), maps.fireSpawns.get(i), 0.0f, maps.fireSpawns.get(i+1));
                count++;
            }
            fireSound = sounds.loadSound("world/ambience/FIRE.wav");
        }

        if (maps.humSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.humSpawns.size(); i+=2) {
                humSources.add(count, sources.initSource());
                sources.setPosition(humSources.get(count), maps.humSpawns.get(i), 10.0f, maps.humSpawns.get(i+1));
                count++;
            }
            humSound = sounds.loadSound("world/ambience/HUM.wav");
        }

        if (maps.machSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.machSpawns.size(); i+=2) {
                machSources.add(count, sources.initSource());
                sources.setPosition(machSources.get(count), maps.machSpawns.get(i), -8.0f, maps.machSpawns.get(i+1));
                count++;
            }
            machSound = sounds.loadSound("world/ambience/MACHINE.wav");
        }

        if (maps.plumSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.plumSpawns.size(); i+=2) {
                plumSources.add(count, sources.initSource());
                sources.setPosition(plumSources.get(count), maps.plumSpawns.get(i), 0.0f, maps.plumSpawns.get(i+1));
                count++;
            }
            plumSound = sounds.loadSound("world/ambience/PLUMBING.wav");
        }

        if (maps.swampSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.swampSpawns.size(); i+=2) {
                swampSources.add(count, sources.initSource());
                sources.setPosition(swampSources.get(count), maps.swampSpawns.get(i), 0.0f, maps.swampSpawns.get(i+1));
                count++;
            }
            swampSound = sounds.loadSound("world/ambience/SWAMP.wav");
        }

        if (maps.thundSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.thundSpawns.size(); i+=2) {
                thundSources.add(count, sources.initSource());
                sources.setPosition(thundSources.get(count), maps.thundSpawns.get(i), 8.0f, maps.thundSpawns.get(i+1));
                count++;
            }
            thundSound = sounds.loadSound("world/ambience/THUNDER.wav");
        }

        if (maps.vacSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.vacSpawns.size(); i+=2) {
                vacuumSources.add(count, sources.initSource());
                sources.setPosition(vacuumSources.get(count), maps.vacSpawns.get(i), 8.0f, maps.vacSpawns.get(i+1));
                count++;
            }
            vacSound = sounds.loadSound("world/ambience/VACUUM.wav");
        }

        if (maps.waterSpawns.size()>0) {
            int count = 0;
            for (int i=0; i<maps.waterSpawns.size(); i+=2) {
                waterSources.add(count, sources.initSource());
                sources.setPosition(waterSources.get(count), maps.waterSpawns.get(i), -10.0f, maps.waterSpawns.get(i+1));
                count++;
            }
            waterSound = sounds.loadSound("world/ambience/WATER.wav");
        }

        alarmSource = sources.initSource();
        sources.setPosition(alarmSource, maps.finishPos[0], 8.0f, maps.finishPos[1]);
        alarmSound = sounds.loadSound("world/ambience/ALARM.wav");
        beatLevel = sounds.loadSound("world/doors/beatLevel.wav");
    }

    protected void findDoors() {

        if (beepSources.size() > 0) {

            nBeepTime = glfwGetTime();
            if (nBeepTime - lBeepTime >= beepInterval) {
                for (int i = 0; i < beepSources.size(); i++) {
                    sources.playSound(beepSources.get(i), doorBeep);
                    lBeepTime = nBeepTime;
                }
            }
        }
    }

    protected void playSounds() {

        if (airSources.size()>0) {
            for (int i = 0; i < airSources.size(); i++) {
                sources.setLooping(airSources.get(i), true);
                sources.playSound(airSources.get(i), airSound);
            }
        }

        if (buzzSources.size()>0) {
            for (int i = 0; i < buzzSources.size(); i++) {
                sources.setLooping(buzzSources.get(i), true);
                sources.playSound(buzzSources.get(i), buzzSound);
            }
        }

        if (compSources.size()>0) {
            for (int i = 0; i < compSources.size(); i++) {
                sources.setLooping(compSources.get(i), true);
                sources.playSound(compSources.get(i), compSound);
            }
        }

        if (draftSources.size()>0) {
            for (int i = 0; i < draftSources.size(); i++) {
                sources.setLooping(draftSources.get(i), true);
                sources.playSound(draftSources.get(i), draftSound);
            }
        }

        if (errorSources.size()>0) {
            for (int i = 0; i < errorSources.size(); i++) {
                sources.setLooping(errorSources.get(i), true);
                sources.playSound(errorSources.get(i), errorSound);
            }
        }

        if (fireSources.size()>0) {
            for (int i = 0; i < fireSources.size(); i++) {
                sources.setLooping(fireSources.get(i), true);
                sources.playSound(fireSources.get(i), fireSound);
            }
        }

        if (humSources.size()>0) {
            for (int i = 0; i < humSources.size(); i++) {
                sources.setLooping(humSources.get(i), true);
                sources.playSound(humSources.get(i), humSound);
            }
        }

        if (machSources.size()>0) {
            for (int i = 0; i < machSources.size(); i++) {
                sources.setLooping(machSources.get(i), true);
                sources.playSound(machSources.get(i), machSound);
            }
        }

        if (plumSources.size()>0) {
            for (int i = 0; i < plumSources.size(); i++) {
                sources.setLooping(plumSources.get(i), true);
                sources.playSound(plumSources.get(i), plumSound);
            }
        }

        if (swampSources.size()>0) {
            for (int i = 0; i < swampSources.size(); i++) {
                sources.setLooping(swampSources.get(i), true);
                sources.playSound(swampSources.get(i), swampSound);
            }
        }

        if (thundSources.size()>0) {
            for (int i = 0; i < thundSources.size(); i++) {
                sources.setLooping(thundSources.get(i), true);
                sources.playSound(thundSources.get(i), thundSound);
            }
        }

        if (vacuumSources.size()>0) {
            for (int i = 0; i < vacuumSources.size(); i++) {
                sources.setLooping(vacuumSources.get(i), true);
                sources.playSound(vacuumSources.get(i), vacSound);
            }
        }

        if (waterSources.size()>0) {
            for (int i = 0; i < waterSources.size(); i++) {
                sources.setLooping(waterSources.get(i), true);
                sources.playSound(waterSources.get(i), waterSound);
            }
        }

        sources.setLooping(alarmSource, true);
        sources.playSound(alarmSource, alarmSound);
    }

    protected void updateDoor() {
        sources.setPosition(door, listener.posX, 4.0f, listener.posZ);
    }

    protected void openDoor(int doorType) {

        nDoorTime = glfwGetTime();

        if (nDoorTime - lDoorTime >= doorInterval) {
            if (doorType == 0) {
                sources.playSound(door, dOpen);
            }
            else {
                sources.playSound(door, useKey);
            }
            lDoorTime = nDoorTime;
            doorOpen = true;
        }
    }

    protected void closeDoor() {

        nDoorTime = glfwGetTime();

        if (nDoorTime - lDoorTime >= doorInterval) {
            sources.playSound(door, dClose);
            lDoorTime = nDoorTime;
            doorOpen = false;
        }
    }

    protected void doorLocked(int type) {

        nDoorTime = glfwGetTime();

        if (nDoorTime - lDoorTime >= doorInterval) {
            updateDoor();
            sources.playSound(door, tryKey);
            lDoorTime = nDoorTime;
            switch (type) {
                case 1:
                    tts.speak("You need the red key");
                    break;
                case 2:
                    tts.speak("You need the blue key");
                    break;
                case 3:
                    tts.speak("You need the yellow key");
                    break;
            }
        }
    }

    protected void finishLevel() {
        sources.setLooping(alarmSource, false);
        sources.playSound(alarmSource, beatLevel);
        tts.speak("Level completed");
    }

    protected void clearAll() {

        airSources.clear();
        buzzSources.clear();
        compSources.clear();
        draftSources.clear();
        errorSources.clear();
        fireSources.clear();
        humSources.clear();
        machSources.clear();
        plumSources.clear();
        swampSources.clear();
        thundSources.clear();
        vacuumSources.clear();
        waterSources.clear();
    }
}
