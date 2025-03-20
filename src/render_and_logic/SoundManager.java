package render_and_logic;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SoundManager {

    private final HashMap<String, Clip> soundClips = new HashMap<>();

    // Load sound once into memory
    public void loadSound(String soundName, String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundClips.put(soundName, clip);
            System.out.println("Loaded sound: " + soundName);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Play sound by name
    public void playSound(String soundName) {
        stopSound(soundName);
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop(); // Stop if it's still playing
            }
            clip.setFramePosition(0); // Rewind to start
            clip.start();
        } else {
            System.out.println("Sound not found: " + soundName);
        }
    }

    public void loadAndPlaySound(String filePath) {
        loadSound(filePath, filePath);
        playSound(filePath);
    }

    // Optional: Stop sound
    public void stopSound(String soundName) {
        Clip clip = soundClips.get(soundName);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
