package projectBlackSpaceApp;

public class Trolls extends StalkerEnemy {


    public Trolls(Sources inSources, Sounds inSounds, Maps inMaps, Registry inRegistry) {

        super(inSources, inSounds, inMaps, inRegistry);

        mapCoords = maps.trolls;

        bubble = 1.0f;
        maxHealth = 200;
        attkDmg = 20;
        moveSpeed = 1.7f;

        minSpeakInterval = 2.0;
        maxSpeakInterval = 2.5;
        stepInterval = 0.6f;
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
        enemyIdle[0] = sounds.loadSound("enemies/troll/T_IDLE1.wav");
        enemyIdle[1] = sounds.loadSound("enemies/troll/T_IDLE2.wav");
        enemyPain[0] = sounds.loadSound("enemies/troll/T_PAIN1.wav");
        enemyPain[1] = sounds.loadSound("enemies/troll/T_PAIN2.wav");
        enemyStep = sounds.loadSound("enemies/troll/T_STEP.wav");
        enemyDeath = sounds.loadSound("enemies/troll/T_DEATH.wav");

        xCollision = false;
        zCollision = false;
        newTime = 0;
        prevSpeakTime = 0;
        prevStepTime = 0;
    }
}
