package de.bsautermeister.snegg.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.serializer.BinarySerializable;

public class MusicPlayer implements Updateable, BinarySerializable, Disposable {
    private final static float VOLUME_CHANGE_SPEED = 1.0f;

    private static MusicPlayer instance;

    private float currentVolume = 1.0f;
    private float targetVolume = 1.0f;
    private Music music;

    private MusicPlayer() {
    }

    public static MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return  instance;
    }

    public void setup(String filePath, float volume) {
        FileHandle fileHandle = Gdx.files.internal(filePath);
        music = Gdx.audio.newMusic(fileHandle);
        music.setLooping(true);
        setVolume(volume, true);
    }

    @Override
    public void update(float delta) {
        if (targetVolume != currentVolume) {
            float diff = targetVolume - currentVolume;

            if (diff > 0) {
                currentVolume += delta * VOLUME_CHANGE_SPEED;
                currentVolume = Math.min(targetVolume, currentVolume);
            } else {
                currentVolume -= delta * VOLUME_CHANGE_SPEED;
                currentVolume = Math.max(targetVolume, currentVolume);
            }
        }

        music.setVolume(currentVolume);
    }

    public void play() {
        music.play();
    }

    public void pause() {
        music.pause();
    }

    public void stop() {
        music.stop();
    }

    public void setVolume(float volume, boolean immediate) {
        targetVolume = volume;

        if (immediate) {
            currentVolume = volume;
        }
    }

    public float getVolume() {
        return currentVolume;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeFloat(music.getPosition());
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        music.setPosition(in.readFloat());
    }

    @Override
    public void dispose() {
        if (music != null) {
            music.dispose();
        }
    }
}
