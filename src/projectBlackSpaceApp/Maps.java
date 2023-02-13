package projectBlackSpaceApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Maps {

    private char[][] map;
    private int lineNum = 0;

    /**Actor Coordinate Arrays**/
    //Player
    protected int[] playerPos = new int[2];
    protected int[] finishPos = new int[2];

    //Keys
    protected int[] redKey = new int[1];
    protected int[] blueKey = new int[1];
    protected int[] yellowKey = new int[1];

    //Walls & Doors
    protected ArrayList<Float> walls = new ArrayList<>();
    protected ArrayList<Float> doors = new ArrayList<>();
    protected ArrayList<Float> redDoors = new ArrayList<>();
    protected ArrayList<Float> blueDoors = new ArrayList<>();
    protected ArrayList<Float> yellowDoors = new ArrayList<>();
    protected ArrayList<Integer> doorBeeps = new ArrayList<>();
    protected ArrayList<Float> obstructions = new ArrayList<>();

    //Surfaces
    protected ArrayList<Integer> hard = new ArrayList<>();
    protected ArrayList<Integer> leaves = new ArrayList<>();
    protected ArrayList<Integer> gravel = new ArrayList<>();
    protected ArrayList<Integer> water = new ArrayList<>();
    protected ArrayList<Integer> metal = new ArrayList<>();

    //Enemies
    protected ArrayList<Float> zombies = new ArrayList<>();
    protected ArrayList<Float> demons = new ArrayList<>();
    protected ArrayList<Float> trolls = new ArrayList<>();
    protected ArrayList<Float> ghosts = new ArrayList<>();

    protected ArrayList<Float> soldiers = new ArrayList<>();
    protected ArrayList<Float> orcs = new ArrayList<>();
    protected ArrayList<Float> elites = new ArrayList<>();

    //Items
    protected ArrayList<Integer> healthSpawns = new ArrayList<>();
    protected ArrayList<Integer> ammoSpawns = new ArrayList<>();

    //Ambience
    protected ArrayList<Integer> airSpawns = new ArrayList<>();
    protected ArrayList<Integer> buzzSpawns = new ArrayList<>();
    protected ArrayList<Integer> compSpawns = new ArrayList<>();
    protected ArrayList<Integer> draftSpawns = new ArrayList<>();
    protected ArrayList<Integer> errorSpawns = new ArrayList<>();
    protected ArrayList<Integer> fireSpawns = new ArrayList<>();
    protected ArrayList<Integer> humSpawns = new ArrayList<>();
    protected ArrayList<Integer> machSpawns = new ArrayList<>();
    protected ArrayList<Integer> plumSpawns = new ArrayList<>();
    protected ArrayList<Integer> swampSpawns = new ArrayList<>();
    protected ArrayList<Integer> thundSpawns = new ArrayList<>();
    protected ArrayList<Integer> vacSpawns = new ArrayList<>();
    protected ArrayList<Integer> waterSpawns = new ArrayList<>();

    protected float wallBubble = 0.05f;

    protected void loadMap(String mapName) {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream mapPath = loader.getResourceAsStream("dat/maps/" + mapName);
        lineNum = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(mapPath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                for (int i=0; i<line.length(); i++) {
                    if (lineNum==0) {
                        map = new char[Integer.valueOf(parts[0])][Integer.valueOf(parts[1])];
                    }
                    else {
                        map[lineNum][i] = (line.charAt(i));
                    }
                    if (map[lineNum][i] == '|' || map[lineNum][i]=='-' || map[lineNum][i]=='/' || map[lineNum][i]=='\\') {
                        for (float j=0.0f; j<1.0f; j+=wallBubble) {
                            for (float k=0.0f; k<1.0f; k+=wallBubble) {
                                walls.add((float) lineNum + j);
                                walls.add((float) -i - k);
                            }
                        }
                    }
                    else if (map[lineNum][i]==':' || map[lineNum][i]=='=') {
                        for (float j=0.0f; j<1.0f; j+=wallBubble) {
                            for (float k=0.0f; k<1.0f; k+=wallBubble) {
                                doors.add((float) lineNum + j);
                                doors.add((float) -i - k);
                            }
                        }
                        if (map[lineNum][i]=='=') {
                            doorBeeps.add(lineNum);
                            doorBeeps.add(-i);
                        }
                    }
                    else if (map[lineNum][i]=='1' || map[lineNum][i]=='!') {
                        for (float j=0.0f; j<1.0f; j+=wallBubble) {
                            for (float k=0.0f; k<1.0f; k+=wallBubble) {
                                redDoors.add((float) lineNum + j);
                                redDoors.add((float) -i - k);
                            }
                        }
                        if (map[lineNum][i]=='!') {
                            doorBeeps.add(lineNum);
                            doorBeeps.add(-i);
                        }
                    }
                    else if (map[lineNum][i]=='2' || map[lineNum][i]=='%') {
                        for (float j=0.0f; j<1.0f; j+=wallBubble) {
                            for (float k=0.0f; k<1.0f; k+=wallBubble) {
                                blueDoors.add((float) lineNum + j);
                                blueDoors.add((float) -i - k);
                            }
                        }
                        if (map[lineNum][i]=='%') {
                            doorBeeps.add(lineNum);
                            doorBeeps.add(-i);
                        }
                    }
                    else if (map[lineNum][i]=='3' || map[lineNum][i]=='&') {
                        for (float j=0.0f; j<1.0f; j+=wallBubble) {
                            for (float k=0.0f; k<1.0f; k+=wallBubble) {
                                yellowDoors.add((float) lineNum + j);
                                yellowDoors.add((float) -i - k);
                            }
                        }
                        if (map[lineNum][i]=='&') {
                            doorBeeps.add(lineNum);
                            doorBeeps.add(-i);
                        }
                    }
                    else if (map[lineNum][i]=='h') {
                        hard.add(lineNum);
                        hard.add(-i);
                    }
                    else if (map[lineNum][i]=='l') {
                        leaves.add(lineNum);
                        leaves.add(-i);
                    }
                    else if (map[lineNum][i]=='G') {
                        gravel.add(lineNum);
                        gravel.add(-i);
                    }
                    else if (map[lineNum][i]=='w') {
                        water.add(lineNum);
                        water.add(-i);
                    }
                    else if (map[lineNum][i]=='m') {
                        metal.add(lineNum);
                        metal.add(-i);
                    }
                    else if (map[lineNum][i]=='+') {
                        healthSpawns.add(lineNum);
                        healthSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='#') {
                        ammoSpawns.add(lineNum);
                        ammoSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='A') {
                        airSpawns.add(lineNum);
                        airSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='B') {
                        buzzSpawns.add(lineNum);
                        buzzSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='C') {
                        compSpawns.add(lineNum);
                        compSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='D') {
                        draftSpawns.add(lineNum);
                        draftSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='E') {
                        errorSpawns.add(lineNum);
                        errorSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='F') {
                        fireSpawns.add(lineNum);
                        fireSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='H') {
                        humSpawns.add(lineNum);
                        humSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='M') {
                        machSpawns.add(lineNum);
                        machSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='P') {
                        plumSpawns.add(lineNum);
                        plumSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='S') {
                        swampSpawns.add(lineNum);
                        swampSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='T') {
                        thundSpawns.add(lineNum);
                        thundSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='V') {
                        vacSpawns.add(lineNum);
                        vacSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='W') {
                        waterSpawns.add(lineNum);
                        waterSpawns.add(-i);
                    }
                    else if (map[lineNum][i]=='z') {
                        zombies.add((float)lineNum);
                        zombies.add((float)-i);
                    }
                    else if (map[lineNum][i]=='d') {
                        demons.add((float)lineNum);
                        demons.add((float)-i);
                    }
                    else if (map[lineNum][i]=='t') {
                        trolls.add((float)lineNum);
                        trolls.add((float)-i);
                    }
                    else if (map[lineNum][i]=='g') {
                        ghosts.add((float)lineNum);
                        ghosts.add((float)-i);
                    }
                    else if (map[lineNum][i]=='s') {
                        soldiers.add((float)lineNum);
                        soldiers.add((float)-i);
                    }
                    else if (map[lineNum][i]=='o') {
                        orcs.add((float)lineNum);
                        orcs.add((float)-i);
                    }
                    else if (map[lineNum][i]=='e') {
                        elites.add((float)lineNum);
                        elites.add((float)-i);
                    }
                    else if (map[lineNum][i]=='r') {
                        redKey = new int[2];
                        redKey[0] = lineNum;
                        redKey[1] = -i;
                    }
                    else if (map[lineNum][i]=='b') {
                        blueKey = new int[2];
                        blueKey[0] = lineNum;
                        blueKey[1] = -i;
                    }
                    else if (map[lineNum][i]=='y') {
                        yellowKey = new int[2];
                        yellowKey[0] = lineNum;
                        yellowKey[1] = -i;
                    }
                    else if (map[lineNum][i]=='@') {
                        playerPos[0] = lineNum;
                        playerPos[1] = -i;
                    }
                    else if (map[lineNum][i]=='$') {
                        finishPos[0] = lineNum;
                        finishPos[1] = -i;
                    }
                }
                lineNum++;
            }
            obstructions.addAll(walls);
            obstructions.addAll(doors);
            obstructions.addAll(redDoors);
            obstructions.addAll(blueDoors);
            obstructions.addAll(yellowDoors);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void clearEntity(ArrayList entityList, int index) {

        entityList.remove(index + 1);
        entityList.remove(index);
    }

    protected void clearAll() {

        walls.clear();
        doors.clear();
        redDoors.clear();
        blueDoors.clear();
        yellowDoors.clear();
        doorBeeps.clear();
        obstructions.clear();

        hard.clear();
        leaves.clear();
        gravel.clear();
        water.clear();
        metal.clear();

        zombies.clear();
        demons.clear();
        trolls.clear();
        ghosts.clear();
        soldiers.clear();
        orcs.clear();
        elites.clear();

        healthSpawns.clear();
        ammoSpawns.clear();

        airSpawns.clear();
        buzzSpawns.clear();
        compSpawns.clear();
        draftSpawns.clear();
        errorSpawns.clear();
        fireSpawns.clear();
        humSpawns.clear();
        machSpawns.clear();
        plumSpawns.clear();
        swampSpawns.clear();
        thundSpawns.clear();
        vacSpawns.clear();
        waterSpawns.clear();
    }
}
