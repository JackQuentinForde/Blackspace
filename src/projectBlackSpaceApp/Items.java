package projectBlackSpaceApp;

import java.util.ArrayList;

public class Items {

    /**Class Reference Variables**/
    Listener listener;
    Sources sources;
    Sounds sounds;
    Maps maps;
    Player player;

    /**Game Variables**/
    private final int healthIncr = 15;
    private final int ammoIncr = 10;
    protected final int bubble = 1;

    /**Sources**/
    private ArrayList<Integer> healthSources = new ArrayList<>();
    private ArrayList<Integer> ammoSources = new ArrayList<>();
    private ArrayList<Integer> keySources = new ArrayList<>();

    /**Sounds**/
    private int healthItem;
    private int ammoItem;
    private int keyItem;

    public Items(Listener inListener, Sources inSources, Sounds inSounds, Maps inMaps, Player inPlayer) {

        listener = inListener;
        sources = inSources;
        sounds = inSounds;
        maps = inMaps;
        player = inPlayer;
    }

    protected void initSources() {

        if (maps.healthSpawns.size() > 0) {
            int count = 0;
            for (int i = 0; i < maps.healthSpawns.size(); i += 2) {
                healthSources.add(count, sources.initSource());
                sources.setPosition(healthSources.get(count), maps.healthSpawns.get(i), -8.0f, maps.healthSpawns.get(i + 1));
                count++;
            }
            healthItem = sounds.loadSound("world/items/healthPickup.wav");
        }

        if (maps.ammoSpawns.size() > 0) {
            int count = 0;
            for (int i = 0; i < maps.ammoSpawns.size(); i += 2) {
                ammoSources.add(count, sources.initSource());
                sources.setPosition(ammoSources.get(count), maps.ammoSpawns.get(i), -8.0f, maps.ammoSpawns.get(i + 1));
                count++;
            }
            ammoItem = sounds.loadSound("world/items/ammoPickup.wav");
        }

        if (maps.redKey.length == 2) {
            keySources.add(keySources.size(), sources.initSource());
            sources.setPosition(keySources.get(keySources.size()-1), maps.redKey[0], -8.0f, maps.redKey[1]);
        }

        if (maps.blueKey.length == 2) {
            keySources.add(keySources.size(), sources.initSource());
            sources.setPosition(keySources.get(keySources.size()-1), maps.blueKey[0], -8.0f, maps.blueKey[1]);
        }

        if (maps.yellowKey.length == 2) {
            keySources.add(keySources.size(), sources.initSource());
            sources.setPosition(keySources.get(keySources.size()-1), maps.yellowKey[0], -8.0f, maps.yellowKey[1]);
        }

        if (maps.redKey.length == 2 || maps.blueKey.length == 2 || maps.yellowKey.length == 2) {
            keyItem = sounds.loadSound("world/items/keyPickup.wav");
        }
    }



    protected void playSounds() {

        if (healthSources.size()>0) {
            for (int i = 0; i < healthSources.size(); i++) {
                sources.setLooping(healthSources.get(i), true);
                sources.playSound(healthSources.get(i), healthItem);
            }
        }

        if (ammoSources.size()>0) {
            for (int i = 0; i < ammoSources.size(); i++) {
                sources.setLooping(ammoSources.get(i), true);
                sources.playSound(ammoSources.get(i), ammoItem);
            }
        }

        if (maps.redKey.length == 2 || maps.blueKey.length == 2 || maps.yellowKey.length == 2) {
            for (int i=0; i<keySources.size(); i++) {
                sources.setLooping(keySources.get(i), true);
                sources.playSound(keySources.get(i), keyItem);
            }
        }
    }

    protected void giveHealth(int index) {


        if (player.health + healthIncr > player.healthLim) {
            player.health = player.healthLim;
        }
        else {
            player.health += healthIncr;
        }
        sources.stopSound(healthSources.get(index));
        sources.deleteSource(healthSources.get(index));
        healthSources.remove(index);
    }

    protected void giveAmmo(int index) {

        if (player.ammo + ammoIncr > player.ammoLim) {
            player.ammo = player.ammoLim;
        }
        else {
            player.ammo += ammoIncr;
        }
        sources.stopSound(ammoSources.get(index));
        sources.deleteSource(ammoSources.get(index));
        ammoSources.remove(index);
    }

    protected void giveKeys(int index) {

        player.keysHeld[index] = 1;
        player.getKey();
        sources.stopSound(keySources.get(index));
        sources.deleteSource(keySources.get(index));
        keySources.remove(index);
    }

    protected void clearAll() {

        healthSources.clear();
        ammoSources.clear();
        keySources.clear();
    }
}
