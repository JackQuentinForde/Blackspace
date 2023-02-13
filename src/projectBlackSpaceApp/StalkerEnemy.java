package projectBlackSpaceApp;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class StalkerEnemy extends Enemy {

    protected float moveSpeed;
    protected float reach = 0.25f;

    protected int enemyStep;
    protected double stepInterval;
    protected double prevStepTime;

    protected boolean xCollision;
    protected boolean zCollision;

    private double cosAngle;
    private double sinAngle;

    public StalkerEnemy(Sources inSources, Sounds inSounds, Maps inMaps, Registry inRegistry) {

        super(inSources, inSounds, inMaps, inRegistry);
    }

    @Override
    protected void update(float playerX, float playerZ, float dt) {

        for (int i=0; i<mapCoords.size(); i+=2) {

            float xPos = mapCoords.get(i);
            float zPos = mapCoords.get(i + 1);

            if (playerX > xPos) {
                distX = playerX - xPos;
            }
            else {
                distX = xPos - playerX;
            }

            if (playerZ > zPos) {
                distZ = playerZ - zPos;
            }
            else {
                distZ = zPos - playerZ;
            }

            double distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distZ, 2));

            if (distance <= maxDist) {

                xCollision = false;
                zCollision = false;

                if (distance > reach) {

                    angle = Math.atan2(distZ, distX);
                    cosAngle = Math.cos(angle);
                    sinAngle = Math.sin(angle);

                    if (playerX < xPos) {
                        cosAngle *= -1;
                    }

                    if (playerZ < zPos) {
                        sinAngle *= -1;
                    }

                    float xTranspose = (float) (xPos + moveSpeed * cosAngle * dt);
                    float zTranspose = (float) (zPos + moveSpeed * sinAngle * dt);

                    if (maps.walls.size() > 0) {
                        for (int j = 0; j < maps.walls.size(); j+=2) {
                            if (!xCollision) {
                                if (onCollision(xTranspose, zPos, maps.walls.get(j), maps.walls.get(j + 1), reach)) {
                                    xCollision = true;
                                }
                            }
                            if (!zCollision) {
                                if (onCollision(xPos, zTranspose, maps.walls.get(j), maps.walls.get(j + 1), reach)) {
                                    zCollision = true;
                                }
                            }
                        }
                    }

                    if (maps.doors.size() > 0) {
                        for (int k = 0; k < maps.doors.size(); k+=2) {
                            if (!xCollision) {
                                if (onCollision(xTranspose, zPos, maps.doors.get(k), maps.doors.get(k + 1), reach)) {
                                    xCollision = true;
                                }
                            }
                            if (!zCollision) {
                                if (onCollision(xPos, zTranspose, maps.doors.get(k), maps.doors.get(k + 1), reach)) {
                                    zCollision = true;
                                }
                            }
                        }
                    }

                    if (!xCollision) {
                        mapCoords.set(i, xTranspose);
                    }
                    if (!zCollision) {
                        mapCoords.set(i + 1, zTranspose);
                    }
                    sources.setPosition(enemySources.get(i), mapCoords.get(i), 0.0f, mapCoords.get(i + 1));
                    sources.setPosition(enemySources.get(i+1), mapCoords.get(i), -8.0f, mapCoords.get(i + 1));
                }

                speakInterval = minSpeakInterval + (maxSpeakInterval - minSpeakInterval) * rn.nextDouble();

                newTime = glfwGetTime();
                if (newTime-prevSpeakTime>=speakInterval) {
                    sources.playSound(enemySources.get(i), enemyIdle[rn.nextInt(enemyIdle.length)]);
                    prevSpeakTime = newTime;
                }
                if (newTime-prevStepTime>=stepInterval) {
                    sources.playSound(enemySources.get(i+1), enemyStep);
                    prevStepTime = newTime;
                }
            }
        }
    }

    protected boolean onCollision(float x1, float y1, float x2, float y2, float Bubble) {

        if (x1 >= x2 - Bubble && x1 <= x2 + Bubble && y1 >= y2 - Bubble && y1 <= y2 + Bubble) {
            return true;
        } else {
            return false;
        }
    }
}
