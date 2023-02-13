package projectBlackSpaceApp;

import org.lwjgl.openal.AL10;

import java.util.ArrayList;

public class Sources {

    private ArrayList<Integer> buffers = new ArrayList<>();
    private int sourceId;

    protected int initSource() {
        sourceId = AL10.alGenSources(); //Create new source object
        buffers.add(sourceId);
        AL10.alSourcef(sourceId, AL10.AL_GAIN, 1.0f);
        AL10.alSourcef(sourceId, AL10.AL_PITCH, 1.0f);
        AL10.alSource3f(sourceId, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 1.0f); //Degree at which gain decreases after reference distance
        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 0.0f); //Point at which gain begins to decrease
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 20.0f); //Maximum distance after which sound will no longer be attenuated
        return sourceId;
    }

    protected void setPosition(int source, float x, float y, float z) {
        AL10.alSource3f(source, AL10.AL_POSITION, x, y, z);
    }

    protected void setLooping(int source, boolean loop) {
        AL10.alSourcei(source, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    protected void setPitch(int source, float pitch) {
        AL10.alSourcef(source, AL10.AL_PITCH, pitch);
    }

    protected void playSound(int source, int buffer) {
        AL10.alSourceStop(source);
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer); //Associate source with buffer (sound file)
        AL10.alSourcePlay(source);
    }

    protected void stopSound(int source) {
        AL10.alSourceStop(source);
    }

    protected void deleteSource(int source) {
        AL10.alDeleteBuffers(source);
    }

    protected void clearAll() {
        for (int buffer : buffers) {
            AL10.alSourceStop(buffer);
            AL10.alDeleteBuffers(buffer); //Delete all existing buffers
        }
    }
}
