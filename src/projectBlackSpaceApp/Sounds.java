package projectBlackSpaceApp;

import org.lwjgl.openal.AL10;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

public class Sounds {

    /**Store buffer object ID's in this array list**/
    private ArrayList<Integer> buffers = new ArrayList<>();

    /**Load wav file into buffer memory**/
    protected int loadSound(String file) {

        int data = AL10.alGenBuffers(); //Create new buffer object
        buffers.add(data); //Store buffer ID in array list
        InputStream filePath = getClass().getResourceAsStream("/res/sound/" + file);
        WaveData wavFile = WaveData.create(new BufferedInputStream(filePath)); //Retrieve wav file
        AL10.alBufferData(data, wavFile.format, wavFile.data, wavFile.samplerate); //Store wav file data and properties in buffer object
        wavFile.dispose(); //Discard wav file from stored memory

        return data; //Return buffer ID
    }

    protected void clearAll() {
        for (int buffer : buffers) {
            AL10.alDeleteBuffers(buffer); //Delete all existing buffers
        }
    }
}

