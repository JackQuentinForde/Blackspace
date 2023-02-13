package projectBlackSpaceApp;

import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;

public class TTS {

    private MaryInterface marytts;
    private AudioPlayer ap;

    public TTS()  {

        //try {
            //marytts = new LocalMaryInterface();
            //ap = new AudioPlayer();
        //}
        //catch (MaryConfigurationException e) {
            //e.printStackTrace();
        //}
    }

    protected void stop() {

        //ap.cancel();
    }

    protected void speak(String input)  {

        //try {
            //AudioInputStream audio = marytts.generateAudio(input);
            //stop();
            //ap = new AudioPlayer();
            //ap.setAudio(audio);
            //ap.start();
        //}
        //catch (SynthesisException e) {
            //System.err.println("Error: Could not speak text");
       // }
    }
}