package projectBlackSpaceApp;

import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class ShooterEnemy extends Enemy {

    Player player;

    private double angle;
    protected int hitChance;
    private final int hitCeil = 10;
    private final float shotOffset = 0.0085f;
    private double nearDist;

    protected int enemyShot;
    protected double shotInterval;
    protected double prevShotTime;

    Random rn = new Random();

    public ShooterEnemy(Sources inSources, Sounds inSounds, Maps inMaps, Registry inRegistry, Player inPlayer) {

        super(inSources, inSounds, inMaps, inRegistry);
        player = inPlayer;
    }

    @Override
    protected void update(float playerX, float playerZ, float dt) {

        for (int i=0; i<mapCoords.size(); i+=2) {

            float shooterX = mapCoords.get(i);
            float shooterZ = mapCoords.get(i + 1);

            if (shooterX > playerX) {
                distX = shooterX - playerX;
            }
            else {
                distX = playerX - shooterX;
            }

            if (shooterZ > playerZ) {
                distZ = shooterZ - playerZ;
            }
            else {
                distZ = playerZ - shooterZ;
            }

            double distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distZ, 2));

            if (distance <= maxDist) {

                speakInterval = minSpeakInterval + (maxSpeakInterval - minSpeakInterval) * rn.nextDouble();

                newTime = glfwGetTime();
                if (newTime-prevSpeakTime>=speakInterval) {
                    sources.playSound(enemySources.get(i), enemyIdle[rn.nextInt(enemyIdle.length)]);
                    prevSpeakTime = newTime;
                }

                angle = Math.atan2(distZ, distX);

                if ((playerZ > shooterZ)) {
                    angle *= -1;
                }

                nearDist = -1;
                if (maps.obstructions.size() > 0) {
                    checkHit(i, maps.obstructions, shotOffset);
                }

                if (((nearDist == -1) || (distance < nearDist)) && player.health > 0) {

                    if (newTime-prevShotTime>=shotInterval) {
                        sources.playSound(enemySources.get(i+1), enemyShot);
                        int isHit = rn.nextInt(hitCeil);
                        if (hitChance >= isHit) {
                            player.takeDmg(attkDmg);
                        }
                        prevShotTime = newTime;
                    }
                }
            }
        }
    }

    private void checkHit(int index, ArrayList<Float> entityList, float entityBubble) {

        for (int i=0; i<entityList.size(); i+=2) {

            float xPos = entityList.get(i);
            float zPos = entityList.get(i + 1);
            float shooterX = mapCoords.get(index);
            float shooterZ = mapCoords.get(index+1);

            if (shooterX > xPos) {
                distX = shooterX - xPos;
            }
            else {
                distX = xPos - shooterX;
            }

            if (shooterZ > zPos) {
                distZ = shooterZ - zPos;
            }
            else {
                distZ = zPos - shooterZ;
            }

            double obsDistance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distZ, 2));

            if (obsDistance <= maxDist) {

                double relativeAngle = Math.atan2(distZ, distX);

                if (zPos > shooterZ) {
                    relativeAngle *= -1;
                }

                if (angle <= relativeAngle + entityBubble && angle >= relativeAngle - entityBubble) {

                    if (obsDistance < nearDist || nearDist == -1) {
                        nearDist = obsDistance;
                    }
                }
            }
        }
    }
}
