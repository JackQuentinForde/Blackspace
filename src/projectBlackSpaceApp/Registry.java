package projectBlackSpaceApp;

import java.util.ArrayList;
import java.util.Collection;

public class Registry {

    protected Collection<Enemy> allEnemies = new ArrayList<>();

    protected void addEnemy(Enemy enemy) {

        allEnemies.add(enemy);
    }

    protected void clearAll() {

        allEnemies.clear();
    }
}
