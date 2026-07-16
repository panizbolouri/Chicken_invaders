package sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private static Clip backgroundClip;
    private static boolean musicEnabled = true;
    private static boolean shootEnabled = true;
    private static boolean hitEnabled = true;
    private static boolean endGameEnabled = true;

    public static void playMusic(String filePath) {
        playMusic(filePath, 0f);
    }

    public static void playMusic(String filePath, float volumeChange) {
        if (filePath.contains("music") && !musicEnabled) return;
        if ((filePath.contains("gameover") || filePath.contains("victory")) && !endGameEnabled) return;

        try {
            if (backgroundClip != null && backgroundClip.isRunning()) {
                backgroundClip.stop();
                backgroundClip.close();
            }
            File audioFile = new File(filePath);
            if (!audioFile.exists()) return;

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);

            if (backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                float newVolume = Math.min(volumeChange, gainControl.getMaximum());
                gainControl.setValue(newVolume);
            }

            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    public static void playSFX(String filePath) {
        playSFX(filePath, 0f);
    }

    public static void playSFX(String filePath, float volumeChange) {
        if (filePath.contains("shoot") && !shootEnabled) return;
        if ((filePath.contains("hit") || filePath.contains("chicken")) && !hitEnabled) return;
        if ((filePath.contains("gameover") || filePath.contains("victory")) && !endGameEnabled) return;

        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) return;

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float newVolume = Math.min(volumeChange, gainControl.getMaximum());
                gainControl.setValue(newVolume);
            }

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        if (!musicEnabled && backgroundClip != null) {
            stopMusic();
        }
    }

    public static void setShootEnabled(boolean enabled) { shootEnabled = enabled; }
    public static void setHitEnabled(boolean enabled) { hitEnabled = enabled; }
    public static void setEndGameEnabled(boolean enabled) { endGameEnabled = enabled; }

    public static boolean isMusicEnabled() { return musicEnabled; }
    public static boolean isShootEnabled() { return shootEnabled; }
    public static boolean isHitEnabled() { return hitEnabled; }
    public static boolean isEndGameEnabled() { return endGameEnabled; }
}