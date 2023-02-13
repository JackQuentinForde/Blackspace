package projectBlackSpaceApp;

import org.lwjgl.openal.AL10;

public class Listener {

    protected float posX;
    protected float posZ;
    protected float lastX;
    protected float lastZ;
    protected float[] listenerOri = {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f};

    protected double orientAngle;
    private double sinOrient;
    private double cosOrient;

    protected float moveSpeed = 6.0f;
    private float turnSpeed = 8.0f;

    protected boolean moveForward = false;
    protected boolean moveBackward = false;
    protected boolean moveRight = false;
    protected boolean moveLeft = false;
    protected boolean turnRight = false;
    protected boolean turnLeft = false;

    /**Create listener object and set properties**/
    protected void init() {

        AL10.alListener3f(AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
        AL10.alListener3f(AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
        /**Listener is looking into the screen (0,0,-1). The top of the listener's head is pointing up (0,1,0)**/
        AL10.alListenerfv(AL10.AL_ORIENTATION, new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f});
    }

    protected void setPosition(int x, int z) {

        posX = x;
        posZ = z;
        listenerOri = new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f};

        AL10.alListener3f(AL10.AL_POSITION, posX, 0.0f, posZ);
        AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
    }

    protected void update(float dt) {

        lastX = posX;
        lastZ = posZ;

        if (turnRight) {
            if (listenerOri[2]==-1.0f) {
                if (listenerOri[0]<4.0f) {
                    listenerOri[0] += turnSpeed * dt;
                }
                else {
                    listenerOri[2] = 1.0f;
                }
            }
            else {
                if (listenerOri[0]>=-4.0f) {
                    listenerOri[0] -= turnSpeed * dt;
                }
                else {
                    listenerOri[2] = -1.0f;
                }
            }
        }

        if (turnLeft) {
            if (listenerOri[2]==-1.0f) {
                if (listenerOri[0]>-4.0f) {
                    listenerOri[0] -= turnSpeed * dt;
                }
                else {
                    listenerOri[2] = 1.0f;
                }
            }
            else {
                if (listenerOri[0]<=4.0f) {
                    listenerOri[0] += turnSpeed * dt;
                }
                else {
                    listenerOri[2] = -1.0f;
                }
            }
        }

        orientAngle = Math.atan(listenerOri[2]/listenerOri[0]);
        sinOrient = Math.sin(orientAngle);
        cosOrient = Math.cos(orientAngle);

        if ((listenerOri[0] < 0 && listenerOri[2] < 0) || (listenerOri[0] < 0 && listenerOri[2] > 0)) {
            sinOrient *= -1;
            cosOrient *= -1;
        }

        if (moveForward) {
            posX += moveSpeed * cosOrient * dt;
            posZ += moveSpeed * sinOrient * dt;
        }

        if (moveBackward) {
            posX -= moveSpeed * cosOrient * dt;
            posZ -= moveSpeed * sinOrient * dt;
        }

        if (moveRight) {
            posZ += moveSpeed * cosOrient * dt;
            posX -= moveSpeed * sinOrient * dt;
        }

        if (moveLeft) {
            posZ -= moveSpeed * cosOrient * dt;
            posX += moveSpeed * sinOrient * dt;
        }

        AL10.alListener3f(AL10.AL_POSITION, posX, 0.0f, posZ);
        AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
    }

    protected void rebound() {
        posX = lastX;
        posZ = lastZ;

        AL10.alListener3f(AL10.AL_POSITION, posX, 0.0f, posZ);
        AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
    }
}

