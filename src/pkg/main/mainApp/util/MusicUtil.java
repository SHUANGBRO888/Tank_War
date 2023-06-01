package pkg.main.mainApp.util;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;

public class MusicUtil {
    private static AudioClip start;
    private static AudioClip bomb;
    static {
        try {
            start = Applet.newAudioClip(new File("music/start.wav").toURL());
            bomb = Applet.newAudioClip(new File("music/bomb.wav").toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playStart() {
        start.play(); 
    }

    public static void playBomb() {
        bomb.play(); 
    }
}
