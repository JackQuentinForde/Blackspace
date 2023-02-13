package projectBlackSpaceApp;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {

    /**Class Reference Variables**/
    Sources sources;
    Sounds sounds;
    Maps maps;
    Registry registry;

    /**Sources**/
    protected ArrayList<Float> mapCoords = new ArrayList<>();
    protected ArrayList<Integer> enemySources = new ArrayList<>();

    /**Sounds**/
    protected int[] enemyIdle = new int[2];
    protected int[] enemyPain = new int[2];
    protected int enemyDeath;

    /**Time Variables**/
    protected double newTime;
    protected double speakInterval;
    protected double minSpeakInterval;
    protected double maxSpeakInterval;
    protected double prevSpeakTime;

    /**Game Variables**/
    protected ArrayList<Integer> hpVals = new ArrayList<>();
    protected float bubble;
    protected int maxHealth;
    protected int attkDmg;

    protected float maxDist = 15.0f;
    protected float distX;
    protected float distZ;
    protected double angle;

    protected Random rn = new Random();

    public Enemy(Sources inSources, Sounds inSounds, Maps inMaps, Registry inRegistry) {

        sources = inSources;
        sounds = inSounds;
        maps = inMaps;
        registry = inRegistry;
    }

    protected void update(float playerX, float playerZ, float dt) {}

    protected void takeDmg(int index, int dmg, int coord) {

        hpVals.set(index, hpVals.get(index) - dmg);

        if (hpVals.get(index) > 0) {
            sources.playSound(enemySources.get(coord), enemyPain[rn.nextInt(enemyPain.length)]);
            prevSpeakTime = newTime;
        }
        else {
            death(index, coord);
        }
    }

    private void death(int index, int coord) {
        sources.playSound(enemySources.get(coord), enemyDeath);
        hpVals.remove(index);
        maps.clearEntity(mapCoords, coord);
        sources.deleteSource(enemySources.get(coord+1));
        sources.deleteSource(enemySources.get(coord));
        enemySources.remove(coord+1);
        enemySources.remove(coord);
    }

    protected void clearAll() {

        enemySources.clear();
        hpVals.clear();
    }
}
