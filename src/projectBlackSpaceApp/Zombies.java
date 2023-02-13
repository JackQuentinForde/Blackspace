package projectBlackSpaceApp;

public class Zombies extends StalkerEnemy {


    public Zombies(Sources inSources, Sounds inSounds, Maps inMaps, Registry inRegistry) {

        super(inSources, inSounds, inMaps, inRegistry);

        mapCoords = maps.zombies;

        bubble = 0.5f;
        maxHealth = 100;
        attkDmg = 10;
        moveSpeed = 2.0f;

        minSpeakInterval = 1.5;
        maxSpeakInterval = 2.0;
        stepInterval = 0.5f;
    }

    protected void init() {

        registry.addEnemy(this);
        int count = 0;
        for (int i=0; i<mapCoords.size(); i+=2) {
            enemySources.add(i, sources.initSource());
            enemySources.add(i+1, sources.initSource());
            hpVals.add(count, maxHealth);
            sources.setPosition(enemySources.get(i), mapCoords.get(i), 0.0f, mapCoords.get(i+1));
            sources.setPosition(enemySources.get(i+1), mapCoords.get(i), -8.0f, mapCoords.get(i+1));
            count++;
        }
        enemyIdle[0] = sounds.loadSound("enemies/zombie/Z_IDLE1.wav");
        enemyIdle[1] = sounds.loadSound("enemies/zombie/Z_IDLE2.wav");
        enemyPain[0] = sounds.loadSound("enemies/zombie/Z_PAIN1.wav");
        enemyPain[1] = sounds.loadSound("enemies/zombie/Z_PAIN2.wav");
        enemyStep = sounds.loadSound("enemies/zombie/Z_STEP.wav");
        enemyDeath = sounds.loadSound("enemies/zombie/Z_DEATH.wav");

        xCollision = false;
        zCollision = false;
        newTime = 0;
        prevSpeakTime = 0;
        prevStepTime = 0;
    }
}
