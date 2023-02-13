package projectBlackSpaceApp;

public class Orcs extends ShooterEnemy {

    public Orcs(Sources inSources, Sounds inSounds, Maps inMaps, Registry inRegistry, Player inPlayer) {

        super(inSources, inSounds, inMaps, inRegistry, inPlayer);

        mapCoords = maps.orcs;

        bubble = 0.5f;
        maxHealth = 150;
        attkDmg = 13;
        hitChance = 6;

        minSpeakInterval = 1.0;
        maxSpeakInterval = 1.5;
        shotInterval = 1.6f;
    }

    protected void init() {

        registry.addEnemy(this);
        int count = 0;
        for (int i=0; i<mapCoords.size(); i+=2) {
            enemySources.add(i, sources.initSource());
            enemySources.add(i+1, sources.initSource());
            hpVals.add(count, maxHealth);
            sources.setPosition(enemySources.get(i), mapCoords.get(i), 0.0f, mapCoords.get(i+1));
            sources.setPosition(enemySources.get(i+1), mapCoords.get(i), -4.0f, mapCoords.get(i+1));
            count++;
        }
        enemyIdle[0] = sounds.loadSound("enemies/orc/O_IDLE1.wav");
        enemyIdle[1] = sounds.loadSound("enemies/orc/O_IDLE2.wav");
        enemyPain[0] = sounds.loadSound("enemies/orc/O_PAIN1.wav");
        enemyPain[1] = sounds.loadSound("enemies/orc/O_PAIN2.wav");
        enemyShot = sounds.loadSound("enemies/orc/O_SHOT.wav");
        enemyDeath = sounds.loadSound("enemies/orc/O_DEATH.wav");

        newTime = 0;
        prevSpeakTime = 0;
        prevShotTime = 0.5f;
    }
}